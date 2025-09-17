# BSTs and 23, 234 trees, LLRB trees

## BSTs

### spindly tree

- worst case BST (1 children per parent)

### bushy tree

- best case BST (0-2 children per parent)

### asymptotics

- worst case: $\theta(N)$
- best case: $\theta(log(N))$
- $\theta(log N)$ height if constructed via random inserts

> $\theta$ shows worst or best case '$=$' runtime  
> $O$ shows worst case or less runtime

## B tree (2-3 trees)

> L=3: also called 2-3-4 trees, or 2-4 trees  
> -> pass left middle item up
>
> L=2: also called 2-3 trees  
> -> pass middle item up

- L (level) indicates how many children each node can have
  - Ex. 2-3 trees can at most 3 children

## Rotation

- `RotateRight(n)`: sets the **left** child as the parent of n
- `RotateLeft(n)`: sets the **right** child as the parent of n

## Left-Leaning Red Black BST

- are **Normal** BSTs
- have correspondence to 2-3-4 trees
- for nodes that have 2 items, split w/ a "red" link
  - right item is parent of the red sub-tree
  - left item is left child of right (glued by the "red" link)
- all red links must be left-leaning, or only have left children
  - if there is a right-leaning, rotate the red subtree right at the parent

> all root-to-null nodes have the same # of black links

NOTE: each red-link represents a SINGLE node.

1. when inserting -> use red link
2. right leaning "3-node" -> left leaning violation -> rotate left

![alt text](/img/image-1.png)

3. two consecutive left links -> incorrect 4 node violation -> rotate right
4. nodes w/ 2 red children -> temporary 4 node -> color flip (split operation)

![alt text](/img/image.png)

### asymptotics

- LLRB has height O(log N)
- `contains()` is trivially O(log N)
- `insert()` is O(log N)
