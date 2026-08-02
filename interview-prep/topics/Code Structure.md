Use a mental model: three layers, one rule

**API layer**: speaks HTTP and parses the request, calls business logic, and turns the result (or an error) into a status code and JSON
- contains 0 rules

**Business logic layer**: where the decisions live -- validation, computation, policy. It should not *know* anything about HTTP or *how* data is stored. This is purely for code logic and is the heart of the app
- **Business logic**: static rules that don't change when changing other parts of the app -- if I removed the frontend, then business logic should still be able to run as a plain script
	- *validation of domain invariants*: amount can't be zero, transaction date can't be future
	- *computations* and derivations: running balance, tax owed, reconciliation
	- *decisions and policies*: auto-categorizing by rule, flagging for human review
	- *orchestration*: reconciliation loop: match transactions, resolve when possible, then route the rest to human

**Data layer**: the repository that stores and fetches data. Swapping from a hardcoded dict to Postgres should not require a business logic code change
- knows nothing about rules

> Dependency flow: API -> business logic -> data
- data never reaches into business rules, business logic never touches HTTP
- this allows you to *test* the business logic with *no HTTP* and *no database*

```python
from dataclasses import dataclass
from typing import Optional

# ---------- Domain model ----------
@dataclass
class Transaction:
    id: str
    amount_cents: int          # signed integer cents; negative = outflow
    description: str
    category: Optional[str] = None
    needs_review: bool = False


# ---------- Data layer: stores & fetches, no rules ----------
class TransactionRepository:
    def __init__(self):
        self._store: dict[str, Transaction] = {}

    def exists(self, txn_id: str) -> bool:
        return txn_id in self._store

    def save(self, txn: Transaction) -> Transaction:
        self._store[txn.id] = txn
        return txn

    def list_by_category(self, category: str) -> list[Transaction]:
        return [t for t in self._store.values() if t.category == category]


# ---------- Business logic: the rules & decisions ----------
class DuplicateTransactionError(Exception): pass
class InvalidTransactionError(Exception): pass

class CategorizationService:
    RULES = {"starbucks": "Meals", "uber": "Travel", "aws": "Software"}

    def __init__(self, repo: TransactionRepository):
        self._repo = repo                      # depends on data layer via the repo

    def categorize(self, description: str) -> tuple[Optional[str], bool]:
        """Pure rule application -> (category, needs_review)."""
        desc = description.lower()
        for keyword, category in self.RULES.items():
            if keyword in desc:
                return category, False
        return None, True                       # no match -> flag for a human

    def record_transaction(self, txn_id, amount_cents, description) -> Transaction:
        if not txn_id:
            raise InvalidTransactionError("id is required")
        if amount_cents == 0:                   # domain invariant
            raise InvalidTransactionError("amount cannot be zero")
        if self._repo.exists(txn_id):           # idempotency: retry must not double-record
            raise DuplicateTransactionError(txn_id)
        category, needs_review = self.categorize(description)
        txn = Transaction(txn_id, amount_cents, description, category, needs_review)
        return self._repo.save(txn)


# ---------- API layer: HTTP in, HTTP out, thin ----------
def handle_create_transaction(body: dict, service: CategorizationService):
    required = {"id", "amount_cents", "description"}
    if not required.issubset(body):
        return 400, {"error": "missing required fields"}     # shape check, not a domain rule
    try:
        txn = service.record_transaction(
            body["id"], body["amount_cents"], body["description"]
        )
    except InvalidTransactionError as e:
        return 422, {"error": str(e)}
    except DuplicateTransactionError:
        return 409, {"error": "transaction already exists"}
    return 201, {"id": txn.id, "category": txn.category, "needs_review": txn.needs_review}
```
## Class structure for a Reconciliation Service
**Dependency Injection**: how layers connect
Pass the repo into the service rather than the service constructing its own
- `CategorizationService(TransactionRepository())`

When you need to migrate to a DB like Postgres, write a `PostgresTransactionRepository` with the same methods and inject that instead. The service never changes

```ad-important
Keep each service *single-purpose* and *inject* dependencies
```

**Service Architecture**: how services talk
In-process
- services compose by holding references to each other (private fields) and calling their methods
- A `ReconciliationService` might depend on a `MatchingService` and `LedgerService`, similar to how `CategorizationService` depends on the repo (`TransactionRepository`).
**Distributed**: in a real system, services might be separate layers talking over REST or gRPC for synchronous calls, or by a message queue/events for asynchronous work.
- when a transaction is recorded, emit a `transaction.created` event that the reconciliation service consumes
- when to split: keep it one process until there's a real reason (independent scaling, team boundaries) to pay the cost of a network hop

```ad-tip
title: Interview tip

Start by clarifying the **domain rules**: "so a transaction gets categorized by these rules, and anything unmatched goes to a human for review -- is that right?"

Then, *name* the layers before coding anything: "I'll structure this in three layers -- a repository for **storage**, a service for the **business rules**, and a **thin API handler**. This lets the business logic be testable on its own"

Start by modeling the data, build the thin data layer quickly, then slow down for the business logic, narrating each rule as a decision -- "zero amount is invalid, so I'll reject it here... a repeat ID is an idempotent retry, so I'll raise an exception". Add the **API layer** last and narrate the status-code mapping -- duplicate maps to `409`, invalid input to `422`.
```

## Clean separation of concerns (data layer, business logic, API layer)
**Problem**: Create an invoice and purchase service that creates invoices based on multiple line items, and applies payments to those invoices.

**Data Modeling**
```python
from dataclasses import dataclass

@dataclass
class LineItem:
	description: str
	quantity: int
	unit: str
	rate_cents: int

@dataclass
class Invoice:
	id: int
	...
	line_items: list[LineItem]
	
@dataclass
class Payment:
	id: int
	invoice_id: int
	...
	amount_cents: int
```

**Data Layer**
```python
class InvoiceRepository():
	def __init__(self):
		self._store: dict[id, Invoice] = {}
	
	def get(self, inv_id) -> Invoice | None: pass
	def save(self, inv: Invoice) -> Invoice: pass
	def exists(self, inv_id) -> bool: pass
	def list_items(self) -> list[Invoice]: pass
	
class PaymentRepository():
	def __init__(self):
		self._store: dict[id, Payment] = {}
	
	def get(self, pmt_id) -> Payment | None: pass
	def save(self, pmt: Payment) -> Payment: pass
	def exists(self, pmt_id) -> bool: pass
	def list_items(self) -> list[Payment]: pass
```

**Business Logic Layer**
```python
class InvoiceService:
	def __init__(self, repo: InvoiceRepository): ...
	def create_invoice(self, ...) -> dict: pass
	def calculate_total(self, invoice_id -> int: pass

class PaymentService:
	def __init__(self, repo: PaymentRepository, inv_service: InvoiceService):
		...
		self._credits: dict[str, int] = {}
	
	def apply_payment(self, ...) -> dict: pass
	def get_invoice_summary(self, ...) -> {"total", "paid", "remaining_balance", "status"}: pass
```
Key design choices:
- inject the `InvoiceService` into `PaymentService` in order to calculate the paid balance on the invoice based on payment history
- define a `_credits` variable to store all overpayments and client emails
- define a `get_invoice_summary` method on `PaymentService` in order to calculate *inferred* data (total, paid, remaining balance, and status) of the invoice at *compute time*
- front load each method with all validation checks to ensure: **argument validation**, **domain invariance**, and **idempotency**

**API Layer**
One function for each API function: `GET`, `POST`, etc.
```python
from dataclasses import fields

def handle_create_invoice(body: dict, service: Invoice Service):
	required_fields = {f.name for f in fields(Invoice)}
	if not required_fields.issubset(body.keys()):
		return 400, {"error": "missing required fields"}
	try:
		inv = ...
	except InvalidInvoiceError as e:
		return 422, {"error": str(e)}
	except DuplicateInvoiceError:
		return 409
	except InvoiceNotFoundError:
		return 404
	except Exception as e:
		return 500
	
	return 201, {"id": inv.id, "recipient_name": inv.recipient_name}

def handle_apply_payment(body: dict, service: PaymentService)
```
Key design choices:
- define max 3 custom `Exception`s
		- `ValidationError` -> **422**
		- `DuplicateError` -> **409**
		- `NotFoundError` -> **404**
