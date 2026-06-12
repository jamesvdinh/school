## Overview
Most likely topics for Valency (Claude):
- Hash maps / arrays / strings
- Basic recursion or BFS/DFS (e.g., tree traversal, graph)
- Two pointers, sliding window
- Simple parsing / data transformation

```ad-important
Don't over-prepare hard dynamic programming; focus on cleanly solving easy/medium problems with **good** communication
```

### Arrays
Array (and string) slices are *inclusive* at the start and *non-inclusive* at the end

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