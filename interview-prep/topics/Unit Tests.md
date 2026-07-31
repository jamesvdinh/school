## Collective Interview
After implementing a function, write two or three `assert` statements or a small `pytest`/`unittest` block that covers the "happy path" plus an edge case -> signals prod maturity

**Example code block**
```python
from dataclass import dataclass

@dataclass
class Transaction:
	id: str
	amount_cents: int  # signed integer cents; negative = outflow
	
def running_balance(transactions, init_cents = 0):
	"""Return the running balance of these transactions as [(transaction_id, balance_after_cents), ...]"""
	balance = init_cents
	result = []
	for t in transaction:
		balance += t.amount_cents
		result.append((t.id, balance))
	return result
```

**`assert`** -- recommended lowest effort, high readability
```python
transactions = [Transaction("a", 1000), Transaction("b", -300)]

# average case
assert running_balance(transactions) == [("a", 1000), ("b", 700)]
# empty case
assert running_balance(transactions) == []
# edge case
assert running_balance(transactions, init_cents=-100) == [("a", 900), ("b", 600)]

print("All tests passed")
```

**`unittest`** -- stdlib, always available
```python
import unittest

class TestRunningBalance(unittest.TestCase):
    def test_basic(self):
        txns = [Transaction("a", 1000), Transaction("b", -300)]
        self.assertEqual(running_balance(txns), [("a", 1000), ("b", 700)])

    def test_empty(self):
        self.assertEqual(running_balance([]), [])

    def test_opening_balance(self):
        txns = [Transaction("a", 1000)]
        self.assertEqual(running_balance(txns, opening_cents=500), [("a", 1500)])

unittest.main()

# In CoderPad, use this form so it doesn't try to parse command-line args:
unittest.main(argv=[""], exit=False)
```

**`pytest`** -- cleanest syntax, if enabled
Uses plain functions named `test_*` using bare `assert`, and **pytest** *auto-discovers* them
```python
def test_running_balance_basic():
    txns = [Transaction("a", 1000), Transaction("b", -300)]
    assert running_balance(txns) == [("a", 1000), ("b", 700)]

def test_running_balance_empty():
    assert running_balance([]) == []
```
