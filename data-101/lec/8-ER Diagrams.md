## ER Diagram
Entity Relationship Diagram
- diagram to show interconnections between data

![[Screenshot 2025-10-21 at 2.26.43 PM.png]]
**Entity set** (rectangles)
- **Entities** are things, objects, etc
- **Entity sets** are sets of entities with common properties

**Attributes** (ovals) are atomic features
- connected to entity sets of relationships

**Relationships** (diamonds) between entity sets A & B is a *subset* of A x B
- ex. makes is a subset of Company x Product

![[Screenshot 2025-10-21 at 2.44.00 PM.png]]

**Multi-way Relationships**
- relationship b/w multiple entities

### Constraints
Bold arrow -> "no dangling references"
- ensure every product has a company

![[Screenshot 2025-10-21 at 3.10.19 PM.png]]

A **primary key** is a set of attributes that uniquely identify each record
- unique, non-null
- represent PKs by underlining the attribute
- every entity set is required to have a PK

## Normalization
Process of splitting relations into multiple to **minimize redundancy**
- can lead to overhead due to joins

### Functional Dependencies (FDs)
Constraints between two sets of attributes in a relation
- used to guide normalization
- captures "keys"

$$X \to Y$$

A relational instance satisfies this FD if for *every pair* of tuples $t1$ and $t2$
- if $t1.X = t2.X$, then $t1.Y = t2.Y$

$$AB \to C$$
A relational instance satisfies this FD if for *every pair* of tuples $t1$ and $t2$
- if $t1.A = t2.A$ **and** $t1.B = t2.B$, then $t1.Y = t2.Y$

Suppose we find the 2 FDs across the entire relational instance:
- SSN $\to$ Address
- SSN $\not\to$ Phone Number
- Address $\not\to$ Phone Number

causes us to normalize, or decompose/"factor out" SSN and Address into separate relations

|            |             |                |
| ---------- | ----------- | -------------- |
| Address    | SSN         | phone number   |
| 10 Green   | 123-456-789 | (201) 233-1456 |
| 10 Green   | 123-456-789 | (201) 123-3439 |
| 431 Purple | 987-654-321 | (145) 241-2131 |
| 431 Purple | 987-654-321 | (312) 123-1287 |
| …          | …           | …              |
Certain FDs are "special" and help us identify keys
- ex. primary key FD: movies(title, director, price, update, studio_name)
- title → director, price, update, studio_name