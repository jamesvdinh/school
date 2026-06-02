## Leetcode-style questions
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

## Model Context Protocol (MCP)
**Drawbacks**


## Python + FastAPI
FastAPI is async by default -> fast & efficient

Basic setup
```sh
pip install fastapi
pip install uvicorn
```

Run the server
```sh
uvicorn main:app --reload
```
- the `--reload` flag sets hot reload for the app on code changes

`main.py`

```python
from fastapi import FastAPI

app = FastAPI()
items = []

@app.get("/")  # root
def root():
	return {"Hello": "World"}
	

# routes
@app.post("/items")
def create_item(item: str):
	items.append(item)
	return items
	
	
@app.get("/items")
def list_items(limit: int = 10):
	return items[:limit]
```

test the `/items` POST
```sh
curl -X POST -H "Content-Type: application/json" 'http://127.0.0.1:8000/items?item=apple'
```
```sh
curl -X POST -H "Content-Type: application/json" -d '{"text":"apple"}' 'http://127.0.0.1:8000/items'
```

```ad-important
Note: every time you make a change in the code, the server will restart, resetting any variables
```

### Error handling
Use **HTTPException**
```python
from fastapi import HTTPException

@app.get("/items/{item_id}")
def get_item(item_id: int) -> str:
	if item_id < len(items):
		return items[item_id]
	else:
		raise HTTPException(status_code=404, detail=f"Item {item_id} not found")
```

### Pydantic Types
```python
from pydantic import BaseModel

class Task(BaseModel):
	text: str = None
	is_done: bool = False
```

### Response Models
Helpful when you want the frontend client (e.g. React) to interact with FastAPI because you have a defined response structure to rely on

```python
@app.get("/items", response_model=list[Item])
...

@app.get("/items/{item_id}", response_model=Item)
...
```
