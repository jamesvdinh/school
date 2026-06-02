# Valency Technical Interview Cheat Sheet

A reference for entry-level full-stack interviews at AI-infrastructure companies, calibrated to Valency Bond's stack (MCP servers, retrieval over research papers, AI assistant integration).

---

## Table of Contents

1. [HTTP & REST APIs](#1-http--rest-apis)
2. [Async Programming](#2-async-programming)
3. [Databases: Relational vs. Vector](#3-databases-relational-vs-vector)
4. [Embeddings & Semantic Search](#4-embeddings--semantic-search)
5. [MCP (Model Context Protocol)](#5-mcp-model-context-protocol)
6. [RAG (Retrieval-Augmented Generation)](#6-rag-retrieval-augmented-generation)
7. [LLM API Patterns](#7-llm-api-patterns)
8. [Git & GitHub](#8-git--github)
9. [Coding Patterns for the Screen](#9-coding-patterns-for-the-screen)
10. [Behavioral Hooks for Technical Answers](#10-behavioral-hooks-for-technical-answers)

---

## 1. HTTP & REST APIs

### Core concepts

**Request/response cycle:** client sends a request (method, URL, headers, optional body), server responds (status code, headers, body). Stateless by default — each request stands alone unless you add cookies, tokens, or session storage.

**HTTP methods (the ones that matter):**

| Method | Purpose | Idempotent? | Safe? |
|---|---|---|---|
| `GET` | Read a resource | Yes | Yes |
| `POST` | Create a resource (or "do something") | No | No |
| `PUT` | Replace a resource entirely | Yes | No |
| `PATCH` | Partially update a resource | No (often) | No |
| `DELETE` | Remove a resource | Yes | No |

*Idempotent* = calling it N times has the same effect as calling it once. *Safe* = doesn't change server state.

**Status codes (memorize these ranges):**

- `2xx` — success (`200 OK`, `201 Created`, `204 No Content`)
- `3xx` — redirects (`301 Moved Permanently`, `304 Not Modified`)
- `4xx` — client error (`400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `429 Too Many Requests`)
- `5xx` — server error (`500 Internal Server Error`, `502 Bad Gateway`, `503 Service Unavailable`, `504 Gateway Timeout`)

**Common headers:**

- `Content-Type: application/json` — what the body is
- `Authorization: Bearer <token>` — auth (most APIs)
- `Accept: application/json` — what you want back
- `User-Agent: ...` — who's calling
- `X-RateLimit-Remaining` — common rate-limit header
- `Retry-After: 30` — server says wait N seconds

### REST conventions

```
GET    /papers           → list papers
GET    /papers/123       → get paper 123
POST   /papers           → create a paper (body has fields)
PUT    /papers/123       → replace paper 123
PATCH  /papers/123       → partially update paper 123
DELETE /papers/123       → delete paper 123
```

**Nested resources:**
```
GET    /papers/123/citations  → citations of paper 123
POST   /papers/123/comments   → add a comment to paper 123
```

### Pagination

Three common patterns:

**Offset/limit** (simple but slow at scale):
```
GET /papers?limit=20&offset=40
```

**Cursor-based** (scales better, what most APIs use):
```
GET /papers?limit=20&cursor=eyJpZCI6MTAwfQ
```
The cursor is opaque (often base64-encoded), points to a position in the result set.

**Page-based** (human-friendly):
```
GET /papers?page=3&per_page=20
```

### Rate limiting

How servers protect themselves. Common strategies:

- **Token bucket** — you get N tokens; each request costs 1; bucket refills at rate R
- **Fixed window** — N requests per minute, resets at the top of each minute
- **Sliding window** — N requests in the last 60s

What to do as a client:
1. Read the `X-RateLimit-*` headers
2. Back off when you hit `429 Too Many Requests`
3. Respect `Retry-After`
4. Implement **exponential backoff** with jitter: wait 1s, 2s, 4s, 8s + random jitter, give up after N retries

### REST vs. alternatives

| Approach | Pros | Cons |
|---|---|---|
| **REST** | Universal, cacheable, simple | Over/under-fetching, multiple round trips |
| **GraphQL** | Client picks fields, one endpoint | Complex server, harder caching, N+1 query problem |
| **gRPC** | Fast (binary), typed, streaming | Browser support weak, harder to debug |
| **MCP** | Designed for LLM tool use, typed | Newer ecosystem, smaller community |
```ad-info
title: Remote Procedure Call (RPC)
An action in which a computer program causes a procedure to execute in a different address space of the current process.
```
### Interview-ready talking point

> "REST is great for resource-shaped data, but when I built the Darena MCP server, REST wasn't the right abstraction — the LLM needed to invoke *actions* with typed arguments, not just CRUD resources. MCP fills that gap by giving you a typed RPC layer designed for LLM tool calls."

---

## 2. Async Programming

### Why async matters

A typical web request spends most of its time *waiting* — for the DB, an external API, a file read. Synchronous code blocks the whole thread during that wait. Async lets one thread juggle many waiting operations.

For an MCP server hitting Semantic Scholar + an embedding API + a database for every query, async isn't optional — it's the difference between handling 10 concurrent users and 1000.

### Python: `async`/`await`

```python
import asyncio
import httpx

async def fetch_paper(paper_id: str) -> dict:
    async with httpx.AsyncClient() as client:
        response = await client.get(f"https://api.semanticscholar.org/graph/v1/paper/{paper_id}")
        return response.json()

async def fetch_many(paper_ids: list[str]) -> list[dict]:
    # Run all fetches concurrently
    tasks = [fetch_paper(pid) for pid in paper_ids]
    return await asyncio.gather(*tasks)

# Run from sync code:
papers = asyncio.run(fetch_many(["123", "456", "789"]))
```

**Key rules:**

- `async def` defines a coroutine. Calling it returns a coroutine object, *not the result*.
- `await` pauses until the awaited thing resolves. Only usable inside `async def`.
- `asyncio.gather(*coros)` runs coroutines concurrently and waits for all.
- `asyncio.create_task(coro)` schedules a coroutine to run in the background.
- Don't call sync blocking code (like `time.sleep`, `requests.get`) inside async — it blocks the whole event loop. Use `asyncio.sleep`, `httpx`, `aiofiles`, etc.

**Concurrency vs. parallelism:** Python's async is *concurrent*, not parallel — one thread, one event loop, switching between tasks at `await` points. For CPU-bound work, use `multiprocessing` or threads instead.

### JavaScript: Promises & async/await

```javascript
// Promise-based
function fetchPaper(id) {
  return fetch(`/api/papers/${id}`).then(res => res.json());
}

// async/await (preferred — same Promises under the hood)
async function fetchPaper(id) {
  const res = await fetch(`/api/papers/${id}`);
  return res.json();
}

// Concurrent fetches
async function fetchMany(ids) {
  return Promise.all(ids.map(fetchPaper));
}

// Don't accidentally serialize:
// ❌ BAD - sequential, slow
for (const id of ids) {
  const paper = await fetchPaper(id);  // waits for each one
  results.push(paper);
}

// ✅ GOOD - concurrent
const results = await Promise.all(ids.map(fetchPaper));
```

**Promise states:** `pending` → `fulfilled` (resolved with value) or `rejected` (with error).

**Useful Promise helpers:**

- `Promise.all([...])` — wait for all; rejects if any reject
- `Promise.allSettled([...])` — wait for all; returns array of `{status, value/reason}`
- `Promise.race([...])` — first to settle wins (great for timeouts)
- `Promise.any([...])` — first to fulfill wins; rejects only if all reject

### Common async pitfalls

| Pitfall | Symptom | Fix |
|---|---|---|
| Forgetting `await` | Function returns a coroutine/Promise, not value | Add `await` |
| Sequential when you want concurrent | Slow | Use `gather`/`Promise.all` |
| Blocking the event loop | Whole server stalls | Move CPU work to threads/processes |
| Unhandled promise rejection | Silent failure | Wrap in try/catch or `.catch()` |
| Async in a sync context | "Coroutine was never awaited" warning | Use `asyncio.run` at boundary |

### Interview talking point

> "When I was building the MCP server, every tool call hits the DB and often an external API. Doing those sequentially would have made even simple queries painful. I structured the tool handlers async and used `asyncio.gather` to fan out parallel calls when a tool needed multiple data sources — like fetching a paper's metadata and its citation graph concurrently."

---

## 3. Databases: Relational vs. Vector

### Relational (PostgreSQL, MySQL, SQLite)

Store structured data in tables with rows and columns. Use SQL to query. Strong consistency, transactions (ACID), schemas enforce structure.

**Core SQL you must know:**

```sql
-- SELECT with filtering, sorting, limiting
SELECT title, year, citation_count
FROM papers
WHERE year >= 2023 AND citation_count > 100
ORDER BY citation_count DESC
LIMIT 20;

-- JOIN
SELECT p.title, a.name
FROM papers p
JOIN paper_authors pa ON pa.paper_id = p.id
JOIN authors a ON a.id = pa.author_id
WHERE p.year = 2024;

-- GROUP BY with aggregation
SELECT year, COUNT(*) as paper_count, AVG(citation_count) as avg_citations
FROM papers
GROUP BY year
ORDER BY year DESC;

-- INSERT / UPDATE / DELETE
INSERT INTO papers (title, year) VALUES ('My Paper', 2025) RETURNING id;
UPDATE papers SET citation_count = 50 WHERE id = 123;
DELETE FROM papers WHERE year < 2000;
```

**Join types (memorize):**

- `INNER JOIN` — only rows with matches in both tables
- `LEFT JOIN` — all rows from left, matching from right (NULL if no match)
- `RIGHT JOIN` — all rows from right, matching from left
- `FULL OUTER JOIN` — all rows from both, NULL where no match

**Indexes:**

Indexes make `WHERE` and `ORDER BY` fast at the cost of slower writes and more storage.

```sql
CREATE INDEX idx_papers_year ON papers(year);
CREATE INDEX idx_papers_year_citations ON papers(year, citation_count);  -- composite
```

Rules of thumb:
- Index columns you filter or join on frequently
- Composite index `(a, b)` helps queries on `a` or `(a, b)`, NOT just `b`
- Don't index everything — writes get slow, storage balloons
- Use `EXPLAIN` to see if your index is actually being used

**ACID:**

- **Atomicity** — transactions are all-or-nothing
- **Consistency** — DB moves from one valid state to another
- **Isolation** — concurrent transactions don't see each other's partial state
- **Durability** — committed data survives crashes

### NoSQL (MongoDB, DynamoDB, Redis)

Different data models — document, key-value, wide-column, graph. Generally trade consistency for scalability and flexibility.

| Type | Examples | Use case |
|---|---|---|
| Document | MongoDB | Flexible schemas, nested data |
| Key-value | Redis, DynamoDB | Caching, sessions, fast lookups |
| Wide-column | Cassandra | Time series, huge writes |
| Graph | Neo4j | Citation graphs, social networks |

### Vector databases

Store high-dimensional vectors (embeddings) and query by *similarity* rather than equality. Essential for semantic search and RAG.

**Options:**

| Tool | Pros | Cons |
|---|---|---|
| **pgvector** (Postgres extension) | Same DB as your relational data; transactions; simple ops | Slower than dedicated vector DBs at >10M vectors |
| **Pinecone** | Hosted, fast, easy | $$, vendor lock-in |
| **Weaviate** | Open source, schema-aware, hybrid search built-in | Heavier to operate |
| **Qdrant** | Open source, fast, great filtering | Newer ecosystem |
| **FAISS** (Meta) | In-process, very fast | No persistence, no API server (it's a library) |
| **Chroma** | Dead simple for prototypes | Less mature for production |

**For a startup of Valency's size, my bet is pgvector** — they likely keep relational metadata and vectors in one Postgres instance. Worth being able to discuss.

### When to use what

| Scenario | Use |
|---|---|
| Paper metadata, authors, citations | Relational (Postgres) |
| Semantic search over abstracts | Vector (pgvector) |
| Caching query results | Key-value (Redis) |
| Citation graph traversal | Relational (recursive CTE) or graph DB |
| User sessions | Key-value or relational |

### Interview talking point

> "For the MCP server I'm building, I'm using Postgres with pgvector. The relational side handles paper metadata, authors, and citation edges. The vector side stores embeddings of titles and abstracts. Keeping them in one DB means I can do hybrid queries — semantic similarity filtered by year, venue, or citation count — with a single SQL statement, instead of orchestrating two systems."

---

## 4. Embeddings & Semantic Search

### What an embedding is

A function `text → vector` (typically 384–3072 floats) where semantically similar text produces vectors that are close in the vector space. Generated by a neural network trained on huge text corpora.

```
"transformer for medical imaging"     → [0.12, -0.43, 0.88, ...]
"attention model for radiology"       → [0.15, -0.40, 0.85, ...]  (close)
"recipe for chocolate cake"           → [-0.62, 0.91, 0.04, ...]  (far)
```

### How similarity is measured

**Cosine similarity** — measures the *angle* between two vectors, not their magnitude.

```
cos_sim(a, b) = (a · b) / (||a|| * ||b||)
              = sum(a_i * b_i) / (sqrt(sum(a_i^2)) * sqrt(sum(b_i^2)))
```

Range: `-1` (opposite) to `1` (identical). For normalized embeddings (length 1), cosine similarity equals the dot product, which is faster to compute.

**Other distance metrics:**

- **Euclidean (L2)** — straight-line distance. Sensitive to magnitude.
- **Dot product** — `a · b`. Used for normalized vectors.
- **Manhattan (L1)** — sum of absolute differences. Rare for embeddings.

Most embedding models produce normalized vectors, so cosine ≈ dot product.

### Embedding models

| Model | Provider | Dim | Notes |
|---|---|---|---|
| `text-embedding-3-small` | OpenAI | 1536 (configurable down) | Cheap, fast, good baseline |
| `text-embedding-3-large` | OpenAI | 3072 | Higher quality, more $ |
| `voyage-3` | Voyage AI | 1024 | Strong on retrieval benchmarks |
| `all-MiniLM-L6-v2` | Sentence Transformers (open) | 384 | Tiny, fast, runs locally |
| `bge-large-en` | BAAI (open) | 1024 | Open, competitive quality |

### Semantic search pipeline

1. **Index time** — for each document, generate an embedding, store `(id, text, embedding)` in vector DB.
2. **Query time** — embed the query, find K nearest vectors, return their documents.

```sql
-- pgvector example
SELECT id, title, abstract,
       1 - (embedding <=> $1) AS similarity
FROM papers
ORDER BY embedding <=> $1   -- <=> is cosine distance
LIMIT 10;
```

### Why combine semantic + keyword

Semantic search is great for paraphrases ("attention models" ≈ "transformer architectures") but **bad at exact matches** — author names, specific dataset names, acronyms.

**Keyword search (BM25)** is the opposite — perfect for exact terms, blind to paraphrases.

**Hybrid search** combines both, typically:

1. Run both BM25 and semantic search, get top-K from each
2. **Reciprocal Rank Fusion (RRF)** to merge:

```
score(doc) = sum over methods m of 1 / (k + rank_m(doc))
```

3. Optionally **rerank** the top results with a cross-encoder (e.g., `cross-encoder/ms-marco-MiniLM-L-6-v2`) — slower but much more accurate, so you only run it on the top 50–100.

### Chunking (for long documents)

Embeddings degrade for long text. For papers, common approach:

- Embed title + abstract as one chunk
- Optionally embed each section separately
- Optionally embed every ~500-token sliding window with overlap

For Valency's domain, you'd likely embed at the paper level (title + abstract) and possibly at the section level for deeper retrieval.

### Interview talking point

> "Pure semantic search has a known weakness — it's bad at exact-term matching. For research papers, that's a real problem because users often query by author name or specific dataset. So I'd combine BM25 for keyword matching with embedding-based semantic search, fuse the results with RRF, and rerank the top 50 with a cross-encoder. Most production retrieval systems end up looking something like this."

---

## 5. MCP (Model Context Protocol)

This is your strongest area — make sure you can articulate it cleanly.

### What MCP is

An open protocol from Anthropic that standardizes how AI assistants connect to external data sources and tools. Think of it as "USB-C for LLMs" — a typed, language-agnostic way for an LLM client (Claude Desktop, Cursor, etc.) to discover and invoke capabilities from any compliant server.

### Architecture

```
┌──────────────┐        MCP        ┌──────────────┐
│  MCP Client  │ ←─────────────────→ │  MCP Server  │
│ (Claude app, │   JSON-RPC over    │  (your app)  │
│  Cursor,     │   stdio or SSE     │              │
│  agent)      │                    │              │
└──────────────┘                    └──────────────┘
                                            │
                                            ▼
                                    ┌──────────────┐
                                    │ Your DB,     │
                                    │ external API,│
                                    │ filesystem   │
                                    └──────────────┘
```

### Core primitives

MCP servers expose three kinds of capabilities:

| Primitive | What it is | Example |
|---|---|---|
| **Tools** | Functions the LLM can call | `search_papers(query, year_range)` |
| **Resources** | Read-only data the LLM can fetch | `paper://12345` returning paper text |
| **Prompts** | Reusable prompt templates the user can invoke | `/literature_review {topic}` |

### Transports

- **stdio** — server runs as a subprocess, communicates over stdin/stdout. Common for local servers (Claude Desktop runs MCP servers this way).
- **HTTP + SSE (Server-Sent Events)** — server runs as a network service. Common for remote/shared servers.
- **Streamable HTTP** — newer transport for remote servers, replacing the older SSE-based one.

### Handshake (high level)

1. **Initialize** — client sends `initialize` with its capabilities and protocol version; server responds with its capabilities.
2. **List capabilities** — client calls `tools/list`, `resources/list`, `prompts/list` to discover what's available.
3. **Invoke** — client calls `tools/call` with a tool name + arguments. Server runs the tool and returns results.
4. **Notifications** — server can push notifications (e.g., resource changed) to the client.

Under the hood it's JSON-RPC 2.0, so messages look like:

```json
{
  "jsonrpc": "2.0",
  "id": 1,
  "method": "tools/call",
  "params": {
    "name": "search_papers",
    "arguments": {"query": "transformer for radiology", "limit": 10}
  }
}
```

### Tool definition (Python SDK example)

```python
from mcp.server.fastmcp import FastMCP

mcp = FastMCP("radris")

@mcp.tool()
async def search_papers(query: str, year_min: int = 2020, limit: int = 10) -> list[dict]:
    """Search medical imaging research papers by semantic relevance.

    Args:
        query: Natural language search query
        year_min: Earliest publication year to include
        limit: Maximum number of results (1-50)
    """
    results = await retrieval.hybrid_search(query, year_min=year_min, limit=limit)
    return [{"title": r.title, "year": r.year, "abstract": r.abstract, "url": r.url} for r in results]

if __name__ == "__main__":
    mcp.run()
```

The docstring becomes the tool description the LLM sees — **write it well**, because it's what the model uses to decide when to call the tool.

### MCP vs. alternatives

| Approach | Pros | Cons |
|---|---|---|
| **MCP** | Standard across clients, typed, designed for LLMs | Newer; smaller ecosystem |
| **Plain function calling** (OpenAI/Anthropic tool use) | Built into the LLM API, simple | Tied to one vendor, no separation between model and tools |
| **LangChain tools** | Big library | Heavy abstraction, framework lock-in |
| **OpenAPI/Swagger + agent** | Reuses existing REST APIs | LLM has to figure out semantics from OpenAPI, less precise |

### Interview talking point

> "MCP's value is decoupling: the same server can be called from Claude Desktop, Cursor, or any agent that speaks the protocol — without rewriting it for each client. At Darena, we built our server tightly coupled to one chat agent. If I rebuilt it today, MCP would let me expose the same EHR tooling to any LLM-based interface the hospital wanted to use."

---

## 6. RAG (Retrieval-Augmented Generation)

### The pattern

LLMs hallucinate when asked about specifics they don't know. RAG grounds them by retrieving relevant context from your data and stuffing it into the prompt before generation.

```
User query
   │
   ▼
[Retriever]  ──> top-K relevant chunks
   │
   ▼
[Prompt assembly]  ──>  "Using these sources, answer: <query>
                         Sources:
                         1. <chunk 1>
                         2. <chunk 2>
                         ..."
   │
   ▼
[LLM]  ──> grounded answer with citations
```

### Components

**1. Chunking** — split documents into pieces that fit the model's context and produce good embeddings.

Strategies:
- **Fixed-size** — every N tokens. Simple, can split mid-sentence.
- **Recursive** — split on paragraph → sentence → token boundaries.
- **Semantic** — split where topic shifts (uses embeddings to detect).
- **Document-aware** — chunk by section, page, etc.

Typical chunk size: 200–800 tokens with 10–20% overlap.

**2. Retrieval** — find relevant chunks. Typically hybrid search (semantic + keyword).

**3. Reranking** — cross-encoder reorders top-K for higher precision.

**4. Context assembly** — pack retrieved chunks into the prompt, often with source IDs for citation.

**5. Generation** — LLM answers, ideally citing sources.

### Common failure modes

- **Lost in the middle** — LLMs underweight context in the middle of long prompts. Mitigation: keep context short, put most important sources first.
- **Hallucinated citations** — model invents source IDs. Mitigation: post-process to verify cited IDs actually exist.
- **Retrieval miss** — the right document wasn't retrieved. Mitigation: better retriever, more candidates, hybrid search.
- **Context dilution** — too many retrieved chunks confuse the model. Mitigation: fewer, more relevant chunks.

### Evaluation

- **Retrieval metrics:** precision@K, recall@K, MRR (mean reciprocal rank), nDCG.
- **Generation metrics:** faithfulness (does the answer match the sources?), answer relevance, citation accuracy.
- Frameworks: **RAGAS**, **TruLens**, custom eval harnesses.

### Interview talking point

> "RAG is what makes a tool like Valency Bond actually trustworthy — without retrieval, the LLM is just making things up about research. The hard parts aren't the LLM call, they're upstream: chunking strategy, hybrid retrieval, reranking, and evaluating retrieval quality. I'd want to understand how Valency measures retrieval quality at the scale of tens of millions of papers — that's the part of the problem I'd most want to learn from the team."

---

## 7. LLM API Patterns

### Basic completion (Anthropic example)

```python
from anthropic import Anthropic

client = Anthropic()
response = client.messages.create(
    model="claude-sonnet-4-5",
    max_tokens=1024,
    messages=[{"role": "user", "content": "Summarize this paper: ..."}]
)
print(response.content[0].text)
```

### Streaming

Return tokens as they're generated instead of waiting for the full response. Crucial for UX.

```python
with client.messages.stream(
    model="claude-sonnet-4-5",
    max_tokens=1024,
    messages=[{"role": "user", "content": "..."}]
) as stream:
    for text in stream.text_stream:
        print(text, end="", flush=True)
```

In a web app, you'd typically stream over SSE (Server-Sent Events) or WebSockets to the frontend.

### Structured output

Force the model to return JSON matching a schema. Two approaches:

**1. JSON mode / response format** — model returns valid JSON.

```python
response = client.messages.create(
    model="claude-sonnet-4-5",
    max_tokens=1024,
    messages=[{"role": "user", "content": "Extract title and year as JSON: ..."}]
)
```

**2. Tool use as structured output** — define a "tool" the model must call, with a schema for arguments. The model's tool call gives you typed data.

```python
tools = [{
    "name": "extract_paper",
    "description": "Extract metadata from a paper",
    "input_schema": {
        "type": "object",
        "properties": {
            "title": {"type": "string"},
            "year": {"type": "integer"},
            "modality": {"type": "string", "enum": ["CT", "MRI", "X-ray", "Ultrasound"]}
        },
        "required": ["title", "year"]
    }
}]
```

### Tool use / function calling

The LLM emits a "I want to call this tool with these args" message. Your code runs the tool, sends the result back, and the LLM continues.

```
User: "How many CT segmentation papers are there from 2024?"
  ↓
LLM: <tool_use: search_papers, args={modality: "CT", year: 2024, task: "segmentation"}>
  ↓
Your code: runs search, returns 47 results
  ↓
LLM: "There are 47 papers on CT segmentation from 2024. The most cited is..."
```

This is the foundation of agentic workflows and what MCP standardizes across servers.

### Cost & latency considerations

- **Tokens are money** — input + output tokens cost. Minimize prompt bloat.
- **Caching** — Anthropic and OpenAI offer prompt caching: identical prefixes get a discount. Huge for RAG where the system prompt is constant.
- **Batch APIs** — for non-real-time work, batch processing is ~50% cheaper.
- **Model tiering** — Haiku for cheap/fast, Sonnet for balanced, Opus for hardest tasks. Route based on complexity.

### Interview talking point

> "One thing I learned at Darena is that the cheap, fast model is usually right for tool-use steps — the LLM doesn't need to be a genius to decide which tool to call, it just needs to be a competent router. I'd save the bigger model for the final synthesis step where reasoning quality actually matters. That kind of tiering is how you make latency and cost work in production."

---

## 8. Git & GitHub

They explicitly said they'll look at your GitHub. Don't show up with messy repos.

### Core commands

```bash
# Daily workflow
git status                          # what's changed
git add file.py                     # stage a file
git add -p                          # stage interactively, hunk by hunk
git commit -m "Add paper ingestion" # commit staged changes
git push                            # push to remote
git pull --rebase                   # pull and rebase your work on top

# Branching
git checkout -b feature/mcp-tools   # create + switch to branch
git switch main                     # newer alternative to checkout for branches
git merge feature/mcp-tools         # merge branch into current
git rebase main                     # replay your commits on top of main

# Inspecting
git log --oneline --graph --all     # visual history
git diff                            # unstaged changes
git diff --staged                   # staged changes
git blame file.py                   # who wrote what, when

# Undoing
git restore file.py                 # discard unstaged changes
git restore --staged file.py        # unstage
git reset --soft HEAD~1             # undo last commit, keep changes staged
git reset --hard HEAD~1             # nuke last commit and changes (DANGEROUS)
git revert <sha>                    # create a new commit that undoes <sha>
```

### Commit hygiene

**Good commit messages:**

```
Add MCP search_papers tool with hybrid retrieval

- Implement BM25 + vector search merging via RRF
- Add tests for relevance ordering
- Cap max results at 50 per spec
```

**Bad commit messages:**

```
fix
wip
asdf
update stuff
```

Rules of thumb:
- One commit = one logical change
- Summary line ≤ 72 chars, imperative mood ("Add", not "Added")
- Blank line, then optional body explaining *why* (not what — diff shows what)
- Don't commit broken code to main

### Branching strategies

- **Trunk-based** (small teams, fast) — short-lived feature branches off `main`, merged frequently
- **GitFlow** (heavyweight) — `main`, `develop`, `feature/*`, `release/*`, `hotfix/*` branches
- **GitHub Flow** (most common) — `main` is deployable; feature branches → PR → review → merge

For a small startup like Valency, expect trunk-based or GitHub Flow.

### Pull requests

PR best practices:
1. **Small PRs** — easier to review, faster to merge. Aim for <400 lines changed.
2. **Clear description** — what, why, how to test, screenshots if UI.
3. **Self-review first** — read your own diff before requesting review.
4. **Address all comments** — either change the code or explain why not.
5. **Squash on merge** for noisy histories; preserve commits for meaningful ones.

### Things that make recruiters/engineers groan

- Repos called `untitled-1`, `test`, `project2`
- No README, or "Created with Create React App"
- 50 commits all named "update"
- `node_modules/` committed
- `.env` files committed (security issue)
- Main branch with broken builds
- Massive PRs you can't actually review

### Things that signal quality

- Clear README with hook, screenshots/GIF, quickstart
- Conventional commit messages
- Reasonable PR history (not just direct-to-main spam)
- Tests, even a few
- `LICENSE` file
- `.gitignore` that's actually correct
- CI badge that's green

### Interview talking point

> "I treat my GitHub as a portfolio — even on solo projects, I work in branches and write real commit messages, because the discipline transfers. The repo I'd most want you to look at is [project] — the README walks through the architecture, and the commit history shows how I broke the work into reviewable chunks."

---

## 9. Coding Patterns for the Screen

These are the patterns that come up in entry-level coding screens. For each, I'll give the pattern, when to use it, a canonical example, and complexity.

### Hash maps (dicts/objects)

**Use when:** you need O(1) lookups, counting occurrences, deduping, or remembering what you've seen.

**Canonical: Two Sum**
```python
def two_sum(nums: list[int], target: int) -> list[int]:
    seen = {}  # value → index
    for i, n in enumerate(nums):
        complement = target - n
        if complement in seen:
            return [seen[complement], i]
        seen[n] = i
    return []
```

**Counting:**
```python
from collections import Counter
counts = Counter("hello")  # {'l': 2, 'h': 1, 'e': 1, 'o': 1}
```

**Grouping:**
```python
from collections import defaultdict
by_year = defaultdict(list)
for paper in papers:
    by_year[paper.year].append(paper)
```

**Complexity:** lookups/inserts O(1) average, O(n) worst case (hash collisions).

### Arrays/strings

**Common operations to be fluent in:**

```python
# Slicing
arr[1:4]        # indices 1, 2, 3
arr[::-1]       # reverse
arr[::2]        # every other element

# Building strings (don't concatenate in a loop)
parts = []
for word in words:
    parts.append(word.upper())
result = " ".join(parts)

# Common pattern: parse, transform, return
def parse_csv_line(line: str) -> list[str]:
    return [field.strip() for field in line.split(",")]
```

**String tricks:**
- `s.split(sep)` / `sep.join(list)`
- `s.strip()`, `s.lower()`, `s.upper()`
- `s.startswith()`, `s.endswith()`, `s.replace(a, b)`
- `s.find(sub)` returns -1 if not found; `s.index(sub)` raises

### Recursion

**Use when:** the problem has a natural recursive structure (trees, divide-and-conquer, backtracking).

**Canonical: tree traversal**
```python
class Node:
    def __init__(self, val, left=None, right=None):
        self.val, self.left, self.right = val, left, right

def inorder(node: Node | None) -> list:
    if not node:
        return []
    return inorder(node.left) + [node.val] + inorder(node.right)
```

**Recursion checklist:**
1. **Base case** — what's the simplest input? Return immediately.
2. **Recursive case** — break problem into smaller subproblems.
3. **Combine** — how do subproblem answers form the full answer?

**Watch out for:** stack overflow on deep recursion (Python default limit ~1000). For deep trees, iterate instead.

### BFS (Breadth-First Search)

**Use when:** shortest path in an unweighted graph, level-order traversal, "fewest steps" problems.

```python
from collections import deque

def bfs(graph: dict, start) -> list:
    visited = {start}
    queue = deque([start])
    order = []
    while queue:
        node = queue.popleft()
        order.append(node)
        for neighbor in graph[node]:
            if neighbor not in visited:
                visited.add(neighbor)
                queue.append(neighbor)
    return order
```

**Key data structure:** queue (FIFO). `deque.popleft()` is O(1); `list.pop(0)` is O(n).

### DFS (Depth-First Search)

**Use when:** explore all paths, detect cycles, topological sort, connected components.

```python
def dfs(graph: dict, start, visited=None) -> list:
    if visited is None:
        visited = set()
    visited.add(start)
    order = [start]
    for neighbor in graph[start]:
        if neighbor not in visited:
            order.extend(dfs(graph, neighbor, visited))
    return order

# Iterative version with stack:
def dfs_iter(graph, start):
    visited = set()
    stack = [start]
    order = []
    while stack:
        node = stack.pop()
        if node in visited:
            continue
        visited.add(node)
        order.append(node)
        for neighbor in graph[node]:
            if neighbor not in visited:
                stack.append(neighbor)
    return order
```

### BFS vs. DFS

| | BFS | DFS |
|---|---|---|
| Data structure | Queue | Stack (or recursion) |
| Memory | O(width) | O(depth) |
| Finds shortest path? | Yes (unweighted) | No |
| Use for | Shortest path, level order | All paths, cycles, topo sort |

### Two pointers

**Use when:** sorted array, palindromes, removing duplicates, partitioning.

```python
# Reverse in place
def reverse(arr: list):
    l, r = 0, len(arr) - 1
    while l < r:
        arr[l], arr[r] = arr[r], arr[l]
        l += 1
        r -= 1

# Two sum on sorted array
def two_sum_sorted(arr: list[int], target: int) -> list[int]:
    l, r = 0, len(arr) - 1
    while l < r:
        s = arr[l] + arr[r]
        if s == target:
            return [l, r]
        elif s < target:
            l += 1
        else:
            r -= 1
    return []
```

### Sliding window

**Use when:** find a contiguous subarray/substring matching some condition.

```python
# Longest substring with at most K distinct characters
def longest_k_distinct(s: str, k: int) -> int:
    counts = {}
    left = 0
    best = 0
    for right, ch in enumerate(s):
        counts[ch] = counts.get(ch, 0) + 1
        while len(counts) > k:
            counts[s[left]] -= 1
            if counts[s[left]] == 0:
                del counts[s[left]]
            left += 1
        best = max(best, right - left + 1)
    return best
```

**Pattern:**
1. Expand the right pointer
2. While the window violates the constraint, shrink from the left
3. Track the best window seen

### Simple parsing / data transformation

Real-world version of "easy" coding problems — these come up constantly in startup interviews.

```python
# Parse a log line, group by IP
import re
from collections import Counter

def top_ips(logs: list[str], n: int) -> list[tuple[str, int]]:
    pattern = re.compile(r"^(\d+\.\d+\.\d+\.\d+)")
    counts = Counter()
    for line in logs:
        match = pattern.match(line)
        if match:
            counts[match.group(1)] += 1
    return counts.most_common(n)

# Transform list of dicts → dict of lists
def pivot(rows: list[dict]) -> dict:
    if not rows:
        return {}
    result = {key: [] for key in rows[0]}
    for row in rows:
        for key, val in row.items():
            result[key].append(val)
    return result
```

### Complexity cheat sheet

| Operation | List | Set/Dict | Deque |
|---|---|---|---|
| Index/key access | O(1) | O(1) avg | O(n) middle, O(1) ends |
| Search | O(n) | O(1) avg | O(n) |
| Append | O(1) amortized | — | O(1) |
| Insert/delete middle | O(n) | — | O(n) |
| Insert/delete end | O(1) | — | O(1) |
| Insert/delete front | O(n) | — | O(1) |

### Communication during the screen

Equally important as solving the problem:

1. **Restate the problem** in your own words
2. **Ask clarifying questions** — input size, edge cases, can input be empty/negative/duplicates?
3. **Talk through approaches** before coding — brute force first, then optimize
4. **State complexity** of your approach (time and space)
5. **Walk through an example** by hand before coding
6. **Code cleanly** — meaningful names, small functions
7. **Test** — walk through your code with the example, then edge cases

---

## 10. Behavioral Hooks for Technical Answers

The best technical interviews aren't pure CS quizzes — they reward candidates who connect technical concepts to real experience. For each topic above, have a short "I've actually done this" hook ready:

| Topic | Your hook |
|---|---|
| HTTP/REST | "At Darena, the REST API between our agent and the EHR system was where most of the integration bugs lived — handling rate limits and idempotency on retry was a real engineering problem." |
| Async | "The MCP server tools fan out to multiple data sources per call. I used `asyncio.gather` to keep latency reasonable." |
| SQL | "I worked with clinical data pipelines at Darena — joining patient, encounter, and observation tables was day-to-day work." |
| Vector DB | "For the medical imaging research MCP project I'm building, I'm using pgvector to keep relational metadata and embeddings in one place — lets me do hybrid filters in a single query." |
| Embeddings | "I've used `text-embedding-3-small` and open-source sentence-transformer models — picked based on whether the project needed cloud or could run locally." |
| MCP | "I built an MCP server from scratch at Darena for a live EHR system — that's where I learned the hard part isn't the protocol, it's grounding the model in trustworthy structured data." |
| RAG | "From my deep learning coursework and the MCP project, the lesson I keep coming back to is that retrieval quality matters more than model choice. A great LLM with mediocre retrieval still hallucinates." |
| LLM APIs | "At Prompt Opinion I worked on LLM agent integration end-to-end — streaming, tool use, structured outputs. The pattern that scaled best was tiering: cheap model for routing, expensive model for synthesis." |
| Git | "I treat solo projects the same way I'd treat team projects — branches, real commit messages, PRs to myself. The discipline transfers." |

---

## Final prep checklist

**Two days before:**
- Re-read this cheat sheet end-to-end
- Drill 5 LeetCode easy/medium problems on hash maps, two pointers, BFS/DFS
- Make sure your project repo's README is polished and the demo works from clean clone
- Look at Valency's GitHub (if public) — pick one repo, one file, one design decision you can ask about

**Day of:**
- Have water, a notepad, and your résumé in front of you
- For each technical question: restate → clarify → approach → code → test → complexity
- Use your behavioral hooks — connect every technical answer to something you've actually built when you can
- Have 3–5 specific questions ready for the end

**After:**
- Send the follow-up note within 24 hours, with a specific callback to something they said
- Note down questions they asked you, for prep on the next round

Good luck.
