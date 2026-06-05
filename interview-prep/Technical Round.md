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

## Python + FastAPI
### Virtual Environments (venv)
```sh
python -m venv venv
source venv/bin/activate
```
**to install packages**
```sh
python -m pip install <package>
```
### Setup
FastAPI is async by default -> fast & efficient

Basic setup
```sh
pip install fastapi uvicorn
```

Run the server
```sh
uvicorn main:app --reload
```
- the `--reload` flag sets hot reload for the app on code changes

`main.py`

```python
from fastapi import FastAPI, HTTPException

app = FastAPI()
items = []

@app.get("/")  # root
def root():
	return {"Hello World"}
	

# routes
@app.post("/items")
def create_item(item: str):
	items.append(item)
	return items
	
	
@app.get("/items")
def list_items(limit: int = 10):
	return items[:limit]
	
@app.get("/items/{id}")
def get_item(id: int):
	if id < 0 or id > len(items) - 1:
		raise HTTPException(status_code=400, detail="ID not found in items")
	item = items[id]
	return item
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

**Class Structure**
```python
import requests

class MyClass:
	API_URL = "..."
	def __init__(self, api_key: str = None):
		self.session = requests.Session()
		if api_key:
			self.session.headers["x-api-key"] = api_key
```

**With API Key**
```python
import os
from dotenv import load_dotenv

load_dotenv()
API_KEY = os.getenv("API_KEY")  # in .env
client = MyClass(API_KEY)
```
### Reading a file
In general:
```python
import json

with open("./data/papers.json") as f:
	papers = json.load(f)
```
Note: use `with open` for massive datasets

Use `asynccontextmanager` for explicit lifespan, easier test, and easier to swap data source
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
	item = next((i for i in items if i["item_id"] == id), None)
	if item is None:
		raise HTTPException(status_code=404, detail=f"Item {item_id} not found")
	return item
```

**Status 400**: Bad Request
- client sent something invalid
- invalid query params
**Status 404**: Page not found
- resource doesn't exist (e.g. id not in list)
**Status 500**: Internal Server Error
- developer bug -> reserve for unexpected crashes
**Status 502**: Bad Gateway Error
- server receives invalid response
- bad api query params
### Pydantic Types
```python
from pydantic import BaseModel

class Item(BaseModel):
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

**adding new fields to a `dict`**
```python
old_dict = {
	"name": "Alice",
	"age": 20
}

new_dict = {
	**old_dict,
	"email": "alice@example.com"
}
```

## Filtering
By keyword
```python
def get_papers(title: str = "", limit: int = 10):
	filtered []
	for paper in papers:
		if title and title.lower() not in paper["title"]:
			continue
		filtered.append(paper)
	return filtered[:limit]
```

By semantic meaning
```python
import torch
import torch.nn.functional as F

def filter_semantic(query: str = "", k: int = 20):
	q_emb = embed_text(query)
	q = torch.tensor(q_emb)  # or any tensorizor
	scores = []
	for paper in papers:
		paper_id = paper["id"]
		p_emb = embeddings["embeddings"][paper_id]
		p = torch.tensor(p_emb)
		sim = F.cosine_similarity(q.unsqueeze(0), p.unsqueese(0)).item()
		scores.append({"id": paper_id, "score": sim})
	
	scores.sort(key=lambda x: x["score"], reverse=True)
	k_nearest = scores[:k]  # gets closest paper ids
	...
```
**torch.tensor**
- used to move vectors from CPU to GPU
- used to work with pytorch functions

**Cosine Similarity**: a metric used to measure how similar two vectors are, regardless of size
- measures angle between the vectors
- outputs score between -1 and 1
	- 1: highly similar
	- 0: orthogonal/unrelated
	- -1: highly not similar
$$Similarity(A,B) = cos(\theta) = \dfrac{A*B}{||A||*||B||}$$
**Embedding**: a way to translate real-world data (words, sentences, images) into a list of numbers that a computer can understand
- captures meaning and context, transforming data into *vectors* in multi-dimensional geometric space
- similar meanings sit close to one another
- "King" & "Queen" similar

```ad-info
title: Which strategy is best?
Best to use a hybrid (keyword + semantic) model for matching queries to paper fields
```
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

useEffect(() => {
	const search = async () => {
		try {
			const res = await fetch(`${API_URL}/papers`);
			if (!res.ok) throw new Error(`HTTP ${res.status}`);
			const data = await res.json();
			setItems(data);
		} catch (err) {
			console.error(err);
		}
	}
}, []);
```

```ad-important
Always wrap fetch requests in an *event handler* or **`useEffect`** hook with an empty dependency

**Also**: leave the trailing `/` off of URLs!
```

**Passing queries in the fetch URL**
```ts
const [query, setQuery] = useState({});
const search = async () => {
	const params = new URLSearchParams(
		Object.entries(query).filter(([, v]) => v !== "")
	);
	const res = await fetch(`${API_URL}/papers?${params}`);
	...
}

return  (
	<input type="text" onChange={(e) => setQuery({...query, title: e.target.value})} />
	...
)
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

| Attribute                       | Tailwind Equivalent |
| ------------------------------- | ------------------- |
| display: flex                   | flex                |
| flex-direction: column          | flex-col            |
| width/height: 100%              | w-full, h-full      |
| width: var(--spacing) * 12      | w-12                |
| width: auto, height: auto       | size-auto           |
| background-color: (white, 0.60) | bg-white/60         |
| z-index: 1                      | z-1                 |
| border: 4px solid               | border-4            |
| border-color: light gray        | border-gray-200     |
| animation: spin                 | animate-spin        |

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

**Loading Spinner**
```ts
type LoadingSpinnerProps = {
	status: boolean;
};

function LoadingSpinner({ status }: LoadingSpinnerProps) {
	return (
		<div className={`fixed inset-0 flex justify-center items-center bg-white/60 backdrop-blur-sm z-50 {!status ? "hidden" : ""}`}>
			<div className="w-12 h-12 rounded-full border-4 border-gray-200 animate-spin" />
		</div>
	);
}
```
Then, in `App.tsx`, create a new useState
```ts
const [loading, setLoading] = useState(false);
const search = async () => {
	setLoading(true);
	// fetch data...
	setResults(data);
	setLoading(false);
};
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

### `setTimeout` and `setInterval`
**`setTimeout(callback, delay, ...args)`**
Runs after $\dfrac{delay}{1000}$ seconds
```ts
const timeoutId = setTimeout((name) => {
	console.log(`Hello, ${name}`);
}, 2000, "Alice"); // "Alice" passed as an arg
// console: "Hello, Alice"
```

**`setInterval(callback, delay, ...args`**
Runs every $\dfrac{delay}{1000}$ seconds
```ts
const intervalId = setInterval(() => {
	console.log("Tick");
}, 1000);
```

**clearing timers**
```ts
clearTimeout(timeoutId);
clearInterval(intervalId);
```

**Real app use**
```ts
useEffect(() => {
	const timeoutId = setTimeout(() => {
		console.log(`Hi, ${name}`);
	}, 1000) // after 1 sec... console: "Hi, {name}"
	
	// cleanup function
	return () => {
		clearTimout(timeoutId);
	};
}, [name]);
```

```ad-important
The timeout delay represnts the *minimum* time to wait, not the guaranteed time until the callback is run.
~~~ts
console.log(1);
setTimeout(() => console.log(2), 0);
console.log(3);
// 1, 3, 2
~~~
even with a 0 ms delay, the timeout must wait for the **synchronous** script to finish
```

**Recursive `setTimeout` vs `setInterval`**
```
|----------|----------|
    ^      ^   ^
  200ms  500ms 700ms
```

`setInterval` *includes* execution time of the callback fxn
- if fxn takes **200ms** to run and delay is **500ms**, then the next fxn execution happens at the end of that delay (**500ms** from start)
- executions can stack without break

recursive `setTimout` *excludes* execution time
- if fxn takes **700ms** to run and delay is **500ms**, then the next fxn execution happens at **1200ms** from start
- guaranteed fixed gap between execution end and next execution start
- safer for heavy 

### Race Conditions
Use `useRef` and `AbortController` for handling multiple requests in a short time span
```ts
import {useRef} from "react";

const search = async () => {
	abortRef.current?.abort();
	const controller = new AbortController();
	abortRef.current = controller;
	
	setLoading(true);
	try {
		const res = await fetch(`${API_URL}/papers`, {
			signal: controller.signal,
		});
		...
	} catch (err) {
		if (err.name === "AbortError") return;
		setLoading(false);
		throw err;
	}
}
```
1. *abort* any current requests
2. *set* the new request as controller's signal
3. *maintain* the loading state of the new request in the `catch` by returning on an abort error

### Debounce
Delays execution until a period of inactivity passes. If the function is called again before the delay expires, the timer resets
- perfect for use in *search-as-you-type* apps
```ts
useEffect(() => {
	const id = setTimeout() => {
		setPage(1);
		search(1)
	}, 300);
	return () => clearTimeout(id); // cancel if query/mode changes before 300ms
}, [query, mode])
```
### Caching
Stores prior results by their request key so repeat fetches skip fetching entirely. Only recommended if data updates are infrequent.

```ts
const cacheRef = useRef<Map<string, PapersResponse>>(new Map());

const search = async () => {
	...
	const cacheKey = paperParams.toString();
	if (cacheRef.current.has(cacheKey)) {
		setResults(cacheRef.current.get(cacheKey)!);
		return;
	}
	
	try {
		...
		cacheRef.current.set(cacheKey, data);
	}
}
```

```ad-info
If data updates are frequent, skip caching or add a TTL (store { data, timestamp } and invalidate after N minutes)
```

