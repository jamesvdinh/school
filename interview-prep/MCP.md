## Basics
**MCP**: Model Context Protocol
**FastAPI**: Anthropic's official SDK for building MCP servers

### Primitives

| **Feature**   | **Description**                                                                                                                                                                 | **Examples**                                          |
| ------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- | ----------------------------------------------------- |
| **Tools**     | Functions that the LLM can *actively call* and decide when to use based on the prompt. Tools can write to DBs, call external APIs, modify files, etc. (controlled by **model**) | search flights, send messages, create calendar events |
| **Resources** | Passive data sources that provide *read-only* access to information for context such as file contents, DB schemas, or API docs (controlled by **app**)                          | retrieve files, access knowledge DBs, read calendars  |
| **Prompts**   | Pre-built instruction *templates* that tell model to work with specific tools and resources (controlled by **user**)                                                            | plan a vacation, summarize meetings, draft an email   |

## Medical Imaging MCP Project
**Structure**
- define a `SemanticScholarClient` class that defines all the methods of requesting from *SemanticScholar API*
	- this gives a structured object to invoke queries from
- define the *MCP server* with all tools
	- Define the tool description and args in a docstring
	- FastAPI then registers them with the server

Setup `SemanticScholarClient` class with API endpoints
```python
import requests

class SemanticScholarClient:
	API_URL = "https://api.semanticscholar.org/graph/v1"
	timeout = 10
	
	def __init__(self, api_key: str = None):
		self.session = requests.Session()
		if api_key:
			self.session.headers["x-api-key"] = api_key
			
	def search_papers(query: str, fields: list[str] = None, limit: int = 10):
		params = {
			"query": query,
			"limit": limit,
		}
		if fields:
			params["fields"] = ",".join(fields)
		
		res = requests.get(f"{self.API_URL}/paper/search", params=params, timeout=self.timeout)
		res.raise_for_status()
		return res.json()
		
	...
```

Setup MCP, load config
```python
from mcp.server.fastmcp import FastMCP

from ..config import load_config

config = load_config
client = SemanticScholarChlient(config)

@asynccontextmanager
async def lifespan(app: Any):
	yield
	await client.aclose()
	
mcp = FastMCP(
	"medical-imaging-research",
	lifespan=lifespan,
)
```

```python
@mcp.tool()
async def serach_papers(query: str, limit: int = 10) -> str:
	"""
	Search Semantic Scholar for research papers matching a free-text query.
	
	Returns title, authors, year, venue, citation count, and paper ID for each result. Use the paper ID with get_paper_details, ..., for deeper exploration.
	
	Args:
		query: Free-text search query (e.g. "transformer segmentation MRI 2023")
		limit: Number of results to return (1-100, default 10)
	"""
	data = await client.search_papers(query=query, limit=limit)
	return _format_paper_list(data)
```

## Run locally
Opens up the MCP Inspector
```sh
uv run mcp dev main.py
```

Use in Claude
```json
{
  "mcpServers": {
    "medical-imaging-research": {
      "command": "uv",
      "args": [
        "run",
        "--directory", // needed to avoid free tier
        "/Users/james/Downloads/projects/medical-imaging-ml-research-mcp",
        "main.py"
      ],
      "env": {
        "SEMANTIC_SCHOLAR_API_KEY": "<your api key>"
      }
    }
  }
}
```
