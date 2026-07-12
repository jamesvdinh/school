## Overview
Most likely topics for Valency (Claude):
- Hash maps / arrays / strings
- Basic recursion or BFS/DFS (e.g., tree traversal, graph)
- Two pointers, sliding window
- Simple parsing / data transformation

```ad-important
Don't over-prepare hard dynamic programming; focus on cleanly solving easy/medium problems with **good** communication
```

## Asymptotics

| Operation                    | Time       | Space             |
| ---------------------------- | ---------- | ----------------- |
| sorted() & .sort()           | O(N log N) | O(N) for sorted() |
| [list_a] + [list_b]          | O(N + M)   | O(N + M)          |
| list()                       | O(N)       | O(N)              |
| .values(), .keys(), .items() | O(1)       | O(1)              |
|                              |            |                   |

### Arrays
Array (and string) slices are *inclusive* at the start and *non-inclusive* at the end

**Methods**
```python
# Indexing, O(n)
items = ['a', 'b', 'c']
idx = items.index('b')
print(idx)  # 1

# Find Counts, O(n)
nums = [1, 2, 2, 3]
count = nums.count(2)
print(count)  # 2
```

**Unicode**
```python
>> ord('a')  # 97
>> ord('b')  # 98

>> chr(97)  # a
>> chr(98)  # b

>> "123".isdigit()  # True
```
common practice to subtract unicode value by `ord('a')` to get index value of letter
### Linked Lists
**Scenario**: Create a linked list and add a sequence to it
```ad-example
~~~py
def create_new_list() -> ListNode:
	head = ListNode(0)
	tail = head # point to dummy node
	
	# append an element
	num = np.random(1, 3)
	tail.next = ListNode(num)
	tail = tail.next
	
	return head.next
~~~
```

Pointers to a ListNode `head` is a *shallow copy*
- can access elements in `head`
- does not directly modify `head`

```python
def reverseList(head: ListNode) -> ListNode:
	tail = head
	while 
```
### Queue
**Deque**: double-ended queue
```python
from collections import deque

char_deque = deque()

char_deque.appendleft('a')
char_deque.appendleft('b')
char_deque.appendleft('c')

reversed_str = "".join(char_deque)
print(reverser_str)  # cba
```

### Regex
```python
import re

pat = r"[0-9]"
match = re.search(pat, "a-s24b")
print(match)  # [2, 4]
```

## Patterns & Algorithms
| **Pattern**           | **Time** | **Space**    |
| --------------------- | -------- | ------------ |
| HashMap lookup/insert | O(1) avg | O(n)         |
| Sliding window        | O(n)     | O(1) or O(k) |
| Prefix sum (build)    | O(n)     | O(n)         |
| Prefix sum (query)    | O(1)     | --           |
| Two pointers          | O(n)     | O(1)         |
| Binary search         | O(log n) | O(1)         |
| Stack push/pop        | O(1)     | O(n)         |
### Two Pointers
**Floyd's Tortoise and Hare Algorithm**
hitches on the linked list strategy of mapping indices to values

**Problem**: finding duplicate in unsorted array
*Phase 1*: use slow, fast pointers to find a meeting point; slow by 1, fast by 2
*Phase 2*: run cycle again, but only iterate each by 1
![[Screenshot 2026-06-15 at 11.59.03 AM.png|475]]

**Window size**: `r - l + 1`

### Hash tables
**Deleting**
```python
d = {"apple": 1, "banana": 2}
del d["apple"]
val = d.pop("blueberry", 0) # safe fallback
```

**defaultdict**: best for initializing values without setting them first
```python
from collections import defaultdict

lookup = defaultdict(list)
lookup["words"].append("apple")
# no if statement needed, creates list instantly

del lookup["words"]
print("words" in lookup) # False

print(d["words"]) # True, since we reinitialized it
```


**Counter**: best used for counting frequencies of items, accepts iterable arg
```python
from collections import Counter

text = "mommy"
counts = Counter(text)
# {'m': 3, 'o': 1, 'y': 1}
```
- doesn't throw a `KeyError` when you index a nonexistent key, returns 0 instead

- Average O(1) lookup/insert, O(n) space
- Worst case O(n) time due to collisions (rarely matters in interviews but good to mention)
- When asked for tradeoffs: "I'm trading space for time here — the hashmap gives me O(1) lookups at the cost of O(n) extra memory"

#### Hash sets
**Remove Items**
`set.remove(x)`: removes a specific item
- raises error if missing
`set.discard(x)`: removes a specific item
- safe/no errors

## Tips
**for loops**:
- each iter looks at next item and assigns i to it, so incrementing i to *skip* an iteration does not work

**checking Null**:
```python
if not num -> True
# num can be 0, [], "", None

if num is None -> True
# num can only be None
```

- **for loops** are *faster* than **while** loops (in Python)