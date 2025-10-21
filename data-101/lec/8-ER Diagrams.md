## ER Diagram
Entity Relationship Diagram
- diagram to show interconnections between data

![[Screenshot 2025-10-21 at 2.26.43 PM.png]]
**Entity set** (rectangles)
- **Entities** are things, objects, etc
- **Entity** sets are sets of entities with common properties

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