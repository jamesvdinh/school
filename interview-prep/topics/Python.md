## Data Imports
```python
import json
import pandas as pd

# Read a JSON
with open("papers.json") as f:
	papers = json.load(f)
	
# Read a CSV
df = pd.read_csv("./data/transactions.csv")
```

## Pandas
**Casting**
```python
df["date"] = pd.to_datetime(df["date"])
```

**Indexing**
```python
df = df.set_index("id") # "id" column
```

**Group by / aggregation**
```python
grouped_df = df.groupby("date").sum()

# Sort values
grouped_df.sort_values(by="amount", ascending=False, inplace=True)

# Get running sum of amount
df["running_total"] = df["amount"].cumsum()
```

**Handling missing entries**
```python
grouped_df = df.groupby("date").sum()

# Forward Fill
ffill_df = grouped_df.ffill()

# Backward Fill
bbfill_df = grouped_df.bfill()

# Fill na
na_df = grouped_df.fillna(0) # 0 as default value

# Fill in missing days
days_df = grouped_df.asfreq('D').fillna(0)
```

**Merge**
```python
combined_df = df1.merge(df2, how="left", on="id")

combined_df = df1.merge(df2, left_on="id", right_on="df_id")

combined_df = df1.merge(df2, left_index=True, right_index=True)
```

## Classes
### Dataclasses
```python
from dataclasses import dataclass
from typing import Optional

@dataclass
class Invoice:
	id: int
	recipient_name: str
	recipient_email: str
	amount_cents: int
	category: Optional(str) = "General"
```

- access a `@dataclass` type like how you access fields on an instance, not a dict
```python
  inv = Invoice(1, "John", "john@email.com", 250)
  print(inv.recipient_name) # "John"
```
