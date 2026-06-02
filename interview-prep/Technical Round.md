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

### Fetching from a URL
```python
import requests

API_URL = ""
try:
	res = requests.get(API_URL, timeout=5)
	res.raise_for_status()  # checks for HTTP errors
	data = res.json()
	
except Exception as e:
	print(f"An unexpected error occured: {e}")
```
### Reading a file
```python
import json

with open("./data/papers.json") as f:
	papers = json.load(f)
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

**Status 400**: Bad Request
- invalid arguments
**Status 404**: Page not found
- item not found in items
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

### Pagination
**Offset**: gets the correct index for the start of the page
```python
def paginate(items: list, page: int = 1, page_size: int = 10) -> dict:
	total = len(items)
	total_pages = math.ceil(total / page_size) if total else 1
	if page < 1 or page > total_pages:
		raise HTTPException(status_code=400, detail=f"Page must be between 1 and {total_pages}")
	offset = (page - 1) * page_size
	return {
	"data": items[offset:offset + page_size],
	"pagination":
		{
			"page": page,
			"page_size": page_size,
			"total": total,
			"total_pages": total_pages,
			"has_next": page < total_pages,
			"has_prev": page > 1
		}
	}
```
