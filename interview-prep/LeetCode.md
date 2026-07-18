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

**enumerate()**
```python
for i, num in enumerate(nums):
	...
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

**Alphanumeric**
```python
s = "Hi, World!"
alpha_s = ""
for ch in s:
	if ch.isalnum():
		alpha_s += ch
print(alpha_s)
# "HiWorld"

import re
reg_s = re.sub(r'[^a-zA-Z0-9]', s)
print(alpha_s)

```

**Math operations**
```python
import math

print(math.ceil(4.2))  # 5
print(math.ceil(-4.2))  # -4

print(math.floor(4.2))  # 4
print(math.floor(-4.2))  # -5

print(11 // 3)  # 3
```
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
	while head:
		head.next = tail
		head = head.next
	
	return tail
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

**Convert set to list**
```python
set_nums = set()
nums = list(set_nums)
```
### Sliding Window
**Window size**: `r - l + 1`
```python
"""
A generic template for dynamic sliding window finding max window length
"""
def longest_window(nums, condition):
    i = 0
    max_length = 0
    result = None

    for j in range(len(nums)):
        # Expand the window
        # Add nums[j] to the current window logic

        # Shrink the window if the condition is violated
        while not condition():  
            # Shrink the window from the left
            # Remove nums[i] from the current window logic
            i += 1

        # Update the result if the current window is larger
		max_length = max(max_length, j - i + 1)
		# Add business logic to update result

    return result
```
- note: some problems allow you to use a simple if statement in place of the while loop

A trick for the window and *max length* problems is that we can use the problem parameters to our advantage. Since we only care about finding the *max* length of a subarray, we sometimes don't need to downsize the window using the **while** loop and instead just replace it with a simple **if** statement. This way, the next iteration just *shifts* the window by 1 to the right. Also, any tracker variable that tracks the maximum frequency of an item never needs to decrement unless there's a higher value.
### Hash tables
- Average O(1) lookup/insert, O(n) space
- Worst case O(n) time due to collisions (rarely matters in interviews but good to mention)
- When asked for tradeoffs: "I'm trading space for time here — the hashmap gives me O(1) lookups at the cost of O(n) extra memory"
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

counter1 = Counter(a=2, b=3)
counter2 = Counter(a=2, b=3)
counter2 = Counter(a=2, b=3, c = 0)
print(counter1 == counter2) # Returns True
print(counter1 == counter3) # Returns False
print(+c1 == +c3) # Returns True (strips out the zero count before comparing)
```
- doesn't throw a `KeyError` when you index a nonexistent key, returns 0 instead

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