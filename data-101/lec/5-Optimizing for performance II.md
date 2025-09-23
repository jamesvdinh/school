## Joins
### Nested Loop Joins
Simplest approach to joins

```txt
for tuple in R
  for tuple in S
    if tuples in pages match:
      add_to_output
```

![[Screenshot 2025-09-23 at 2.35.45 PM.png]]
Each page in R `JOINS` with every page in S and sends to output if there is a tuple match

### Sort Merge Join
Phase 1: Sort
1. Sort portions of R on join attr; write out sorted runs of pages
2. Sort portions of S on join attr; write out sorted runs of pages

Phase 2: Merge
1. Merge and match tuples across "runs" of R and S by walking down runs in sorted order

Benefit: output is sorted!

![[Screenshot 2025-09-23 at 2.50.41 PM.png]]

### Hash Join
Phase 1
- Hash R into buckets b1, b2, ... based on join attr
- Hash S into buckets b1, ,b2, ... based on join attr

Phase 2
- Read all tuples hashed to form b1 from both R and S at the same time, perform join, repeat
![[Screenshot 2025-09-23 at 2.58.03 PM.png]]

Join/cross-product is the ONLY operator that "multiplies" table size -> **grows multiplicatively**
