## Relational Algebra

**Table/Relation**: collection of tuples w/ predefined collection of attributes

- relation = schema + instance
- columns can be reordered; relation stays the same

**Schema**: the structure, format, or scaffolding defining a relation

- similar to interface in TS

**Instance**: a relation with values "filled in", a specific instantiation

**Database schema**: the structure, format, or scaffolding defining a collection of relations

**Relational Algebra**: theory of operations that help us transform relations (operands)

- Notation: $R(B_1, B_2, ..., B_m)$

### Primitive RA operations

Unary operators: $S = f(R)$
- selection, projection, renaming

#### Selection
Cross out rows
Operation: filter out rows, keep only rows that satisfy some condition C
![[Screenshot 2025-09-18 at 2.44.58 PM.png]]
Changes metadata (schema): No
SQL syntax: `WHERE`
#### Projection
Cross out columns
Operation: keep only columns $A_1, ..., A_n$
![[Screenshot 2025-09-18 at 2.45.07 PM.png]]
Changes metadata (schema): Yes
SQL syntax: `SELECT`
#### Renaming
Rename attributes and/or relation name
Operation: change metadata
![[Screenshot 2025-09-18 at 2.45.55 PM.png]]
Changes metadata (schema): Yes
SQL Syntax: `AS`
#### Cartesian Product (cross product)
Operation: associate each tuple on *Left* with *Right*
![[Screenshot 2025-09-18 at 2.49.55 PM.png]]
Notes: combines each item in $S$ (each id) with every item (each id) in $T$
#### Union
Operation: all rows in $R_1$ **or** $R_2$
![[Screenshot 2025-09-18 at 2.52.14 PM.png]]
Notes: all relations must have same schema
#### Difference
Operation: all rows in $R_1$ and **not in** $R_2$
![[Screenshot 2025-09-18 at 2.52.50 PM.png]]
Notes: all relations must have same schema

## Joins

### Theta Join (and Equi join)
$$R_1 \bowtie_\theta R_2$$
Operation: join 2 relations such that rows satisfy condition $\theta$
- a cross product of $R_1$ and $R_2$, followed by selection on $\theta$

Notes:
- call this **equi join** if $\theta$ is an equality condition

RA syntax: $R_1 \bowtie_\theta R_2 = \sigma_\theta (R_1 x R_2)$

### Natural Join
$$R_1 \bowtie R_2$$
Operation: join 2 relations such that rows are matched on shared attributes, no duplicate attribute renaming
1. cross product
2. match tuples based on values of shared attributes
3. rename $R_1.A_i$ back to $A_i$ for each $A_i=B_j$ (condense attributes)
4. drop $R_2.B_j$
![[Screenshot 2025-09-18 at 3.00.49 PM.png]]
RA Syntax:
![[Screenshot 2025-09-18 at 3.02.49 PM.png]]
