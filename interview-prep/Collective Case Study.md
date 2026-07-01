**Title**: Collective Engineering Take-Home: Reconciliation

**Data**
- Transactions CSV
	- schema: date, amount
	- granularity: one row per transaction
	- multiple transactions can occur in one day
- Bank balances CSV
	- schema: date, balance
	- granularity: one row per day
	- index on day -> one balance per day

**Task**
- Problem: on *each* day, does the *sum* of transactions *up to* that date match the reported balance
- core reconciliation logic, clean and commented
- console/terminal report output
- build a simple React app that displays a clean table view and any reconciliation
	- shows expected vs actual balance per day
	- discrepancies highlighted in red, delta column

**Architecture**
- create CSVs for the two sample inputs
- data cleaning via pandas
- analysis table in terminal to show reconciliation dates and deltas
- transfer data and results to React app
	- React / TS