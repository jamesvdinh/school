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