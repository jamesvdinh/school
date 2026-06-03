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
In general:
```python
import json

with open("./data/papers.json") as f:
	papers = json.load(f)
```
Note: use `with open` for massive datasets

Use `asynccontextmanager` for explicit lifestyle, easier test, and easier to swap data source
```python
import json
from pathlib import Path
from contextlib import asynccontextmanager

@asynccontextmanager
async def lifespan(app: FastAPI):
	app.state.papers = json.loads(Path("./data/papers.json").read_text())
	yield
	
app = FastAPI(lifespan=lifespan)
```
Note: the `yield` signals when the app can start handling requests
- before `yield`: startup
- after `yield`: shutdown

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
- client sent something invalid
- invalid query params
**Status 404**: Page not found
- resource doesn't exist (e.g. id not in list)
**Status 500**: Internal Server Error
- developer bug -> reserve for unexpected crashes
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

**At scale** -> use *cursor-based* pagination
- instead of an offset, use an opaque cursor pointing to last seen ID
```sh
GET /papers?limit=20&cursor=eyJpZCI6InBhcGVyXzAwNTAifQ==
```

### Resolving CORS issues
```python
from fastapi.middleware.cors import CORSMiddleware

app.add_middleware(
    CORSMiddleware,
    allow_origins=["http://localhost:5173"],
    allow_methods=["*"],
    allow_headers=["*"],
)
```
- `allow_origins`: include your local dev React server
## React, Node.js

### Setup
using **create-react-app**
```sh
npx create-react-app .
```

using **Vite** (faster)
```sh
npm create vite@latest . -- --template react-ts
```

using **Next.js** (for SEO, SSR, and for JS backend/frontend)
```sh
npx create-next-app@latest . --typescript
```

### Fetching from API
use **fetch** for lightweight load, modern usage
```typescript
const [items, setItems] = useState([]);

const API_URL = "https://api.example.com/v1";

const search = async () => {
	try {
		const res = fetch(`${API_URL}/papers`);
		if (!res.ok) throw new Error(`HTTP ${res.status}`);
		const data = await res.json();
		setItems(data);
	} catch (err) {
		console.error(err);
	}
}
```

```ad-important
Always wrap fetch requests in an *event handler* or **`useEffect`** hook with an empty dependency
```

**General React logic loop**:
1. user does something (click, types, selects)
2. state updates (**`useState`**)
3. side effects run (**`useEffect`**, onClick)
4. response comes back and *updates* more state
5. react *re-renders* based on new state

### Install Tailwind
1. Install
```sh
npm i tailwindcss @tailwindcss/vite
```

2. Add plugin to `vite.config.ts`
```typescript
export default defineConfig({
	plugins: [react(), tailwindcss()],
})
```

3. Add the import to `index.css`
```css
@import "tailwindcss";

.other-class{
	...
}
```

### Components
**Inputs**
```ts
<input type="text" onChange={(e) => setQuery(e.target.value)} />

<button onClick={search}>Search Papers</button>
```

**Mapping a list**
```ts
<div className="flex flex-col">
	{results?.data.length > 0 ? (
		results?.data.map((paper) => (
			<div key={paper.id}>{paper.title}</div>
		))
	) : (
		<span>No papers found.</span>
	)}
</div>
```

```ad-important
**#1** bug when data is not displaying -> check if *nested in schema*!!!

also, make sure to check for null object with `res?.data`
```

**Passing in props to a child component**
```ts
interface ChildProps {
	mode: string;
	setMode: React.Dispatch<React.SetStateAction<string>>;
}

function Child({ value, setValue }: ChildProps) {
	return ...
}

function Parent() {
	const [value, setValue] = useState<string>("");
	
	return(
		<Child value={value} setValue={setValue} />
	)
}
```

### Type vs Interface
```ts
interface User { name: string; }
interface User { age: number; } // User now has name and age

type User { name: string; }
type User { age: number; } // Error: duplicate def
```

**Type**
- much more powerful
- can represent primitives, unions (`string | number`), intersections, tuples, and mapped types

**Interface**
- can only describe object shapes and functions/classes
- cannot represent unions or primirives directly