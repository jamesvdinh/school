## Storage Units
A type $T$ that has $|T|$ distinct values needs at least $log_2|T|$ bits of storage
- ASCII characters have 127 values -> 7-bit (8 bits used)
- 32-bit integers have $2^{32}$ values

![[Screenshot 2025-09-30 at 2.36.02 PM.png]]

## Common File Categories
**Record per line**: newline-delimited rows of uniform, symbol-delimited data
- csv, tsv files

**Dictionaries/Objects**: explicit key:value pairs, may be nested
- object-per-line: ex. newline-delimited rows of JSON, XML, etc.
- complex object: entire dataset is one full-nested JSON, XML, or YAML object'

**Unions**: mixture of rows from k distinct schemas
- tagged unions: each row  has an ID or name identifying its schema; often tag is in first col
- untagged unions: schema for row must be classified by its content

**Natural Language (prose)**: natural language intended for human consumption

**Other**: long tail of file formats
- Ex. SQL dumps with suffix .db, .sql

### Plain text encodings
Traditionally 2 encodings of roman-alphabet characters: **EBCDIC** and **ASCII**
- additionally, **Unicode**, **UTF-8**

## Types
In dataframes, post a transpose, a **type induction** operators is used to infer a type from a column of values
- **Feature types** by data scientists
- **Storage data types** by system or data engineers

Coercion
- DataFrame: convert to object (mixed types) if all else fails
- SQL and other DBMSes: coerce to attribute type in schema, or error

### Techniques
> H = {bool, char, int16, int32, int64, float32, double64, string}.
1. "Hard Rules": Occam's Razor ("simple is best")
	- For types H from most-to-least restrictive
	- choose first one that matches *all* values in c
2. Classification rules: machine learning
	- Supervised learning
	- predict data types of c, where model is trained on existing data -> classify data as type in H
3. Minimum Description Length (MDL): compromise of the two above
	- compute simple statistical heuristic that accounts for "penalty" of encoding data that doesn't fit into a given type

### MDL
**Minimum Description Length (MDL)**: a simple statistical heuristic that accounts for "penalty" of encoding data that doesn't fit into a given type
- like the "hard" rules, prefers simplicity

Define: len(v) as the *bit-length* for encoding a value v in some default type

![[Screenshot 2025-09-30 at 3.21.05 PM.png]]![[Screenshot 2025-09-30 at 3.21.40 PM.png]]
