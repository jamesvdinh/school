## Resources
- [CAP Theorem for AI Agents](https://tianpan.co/blog/2026-04-14-cap-theorem-for-ai-agents)
## CAP Theorem
The CAP theorem states that you can have at most 2 of 3 properties:
1. **consistency** -- refusing to serve data until partition heals
2. **availability** -- serving potentially stale data
3. **partition tolerance** -- distributed system continues to function correctly on network failures (partitions)
## Failure Modes
**Graceful Degradation**
When *non-critical* tools fail in the background, yet the agent continues with *reduced capability*
- Ex. an agent that searches the web, queries the database, and runs calculations uses 3 tools. If one tool fails, it can still output a useful answer using 2 of 3 tools
- key word is *non-critical*

**Partial Failure**
When a tool return *incomplete* or *degraded* results
- Ex. a tool returns 3 results instead of 10
- Ex. database query times out after returning half the rows
- agent receives something, but less than expected
- most common failure mode in prod and least handled in agent frameworks

**Silent Failure**
Tool call appears to succeed but returns *stale* data, cached response from a different query, or truncated results that look complete
- agent proceeds confidently with corrupted input
- no error message, no flag
- failure only surfaces downstream
- Ex. if semantic search returns the wrong result (but the correct result still exists in the vector DB)

```ad-info
The root cause for these is **tool call architecture**

In a ReAct agent loop, each tool is a synchronous, all-or-nothing operation
On failure, the framework has 3 options (in order of priority): retry, error, or hallucinate
```

### Designing the Partial-Availability Path
**First**, classify tools by criticality; less critical if a tool improves agent's answers but might be optional
- should be defined explicitly in agent config

**Second**, define degraded response contracts
- when a tool fails, agent needs structured information about the error, not just error code
- degraded response should include what data is missing, why it's missing, and what confidence level the agent should assign to remaining info

**Third**, build fallback tiers. Production-grade agent systems use this hierarchy of fallbacks:
1. **Primary**: *direct tool execution* under normal conditions
2. **Secondary**: *cached responses* using semantic similarity matching
	- a well-tuned cache with embeddings retrieval can handle 60-70% of similar queries
3. **Tertiary**: *rule-based logic* for common, well-understood scenarios
	- Ex. password resets do not need an LLM
4. **Quaternary**: *deferred processing* with user acknowledgement. Queue the request, tell user it will be answered later, and process it when tool recovers

## LangChain
A single tightened interface to handle testing multiple LLM APIs in your code
- uses "chaining" to query responses from the LLM API

### LangGraph
Builds off of **LangChain** for more complex *multi-step workflows* and *better orchestration*
- multi-step workflows
- conditional branching
- iterative processes

Goes beyond simple question/answer interactions by structuring an agent workflow as a graph of nodes
- each *node* is an individual unit of computation (like a function you can call)
- use *edges* to connect nodes to define *execution flow*

**Shared state**: uses a state graph that stores information throughout the entire workflow

Allow for *powerful* capabilities:
- *loops* for iterative analysis
- *conditional branching* on intermediate results
- *persistent state* that maintains context across the entire workflow
```ad-example
**Prompt**: "I need to understand our data privacy policy for EU customers"

**LangGraph** defines a graph where each *node* handles a specific responsibility
- Node 1: Search & gather privacy policy documents
	- edge routes to node 2
- Node 2: Extract & clean node document content
- Node 3: Evaluate GDPR compliance using LLM analysis
	- conditional edge either routes to node 4 or node 5
- Node 4: Cross-reference EU regulations
- Node 5: Report generation
```

## Prompt Engineering
**Zero-shot Prompting**: asking AI to perform a task *without* providing any examples or a template
- Ex. "Write a policy"
- Ex. "Write a data privacy policy for these customers"
- **pros**: quick

**One-shot Prompting**: provide *one* example for the LLM to base its output on
- Ex. "Here's how we format out policy document .... Now write data privacy policy following this structure"
- best for simple tasks and where one example is available
- *use case*: generating a structured recipe
- **pros**: enforces formatting

**Few-shot Prompting**: providing *multiple* (2-5) examples to the LLM side where it's able to fulfill requests from pattern recognition
- better for complex tasks and for generalizability
- *use case*: emphatic customer support responses
- **pros**: enforces tone and consistency

**Chain-of-thought Prompting**: a *set of steps* to instruct how the LLM reasons through a prompt
- Ex. "Here's how to write a data policy... Review current GDPR req... Then, analyze existing policy... Then research... Finally..."
- **pros**: detailed reasoning

## Vector Databases
Where classic SQL databases require you to search by *value*, vector DBs allow you to search by semantic *meaning*. Easier on the client, yet introduces complexity upfront.

**Embeddings**: convert the data into vectors to store in the DB
**Dimensionality**: words don't have just one meaning -- it depends on context (tone, formality, etc.)
- use *1,536* dimensions to capture most richness (not too large, but gives enough context to allow for search depth)
**Retrieval**: decides on how to return queries
- **scoring**: a *threshold* set to define how similar the results need to be considered a proper match
- **chunk overlap**: leaves overlap between "chunks" of data to allow context to spill over and the search to work properly
	- usually, you'd store input data as "chunks" to allow embeddings to have context

### ChromaDB
Production-ready database that can handle millions of embeddings, perform similarity search quickly, and support metadata filtering.

## RAG (Retrieval Augmented Generation)
**Answers the question**: is it possible to search through 500GBs of an entire company's documents? -- *Yes*, AI assistants can fit them in their *context window* and generate output using **RAG**

Question: "What's our remote work policy for international employees?"
**Retrieval**: create *word embedding* for question
- run **semantic search**: compare word embedding against embeddings of other documents in vector DB
**Augmentation**: process where retrieved data is *injected* into the prompt at runtime
- allows AI rely on up-to-date info in the database
- semantic search result provides augmented knowledge for the AI to use
**Generation**: AI generates the response given the semantic relevant data retrieved from the vector DB
- AI retrieves data relevant to *remote work* and *policy*
- Then, uses own reasoning to generate a reasonable output based on initial criteria of *international employees*

**RAG** vs **Long Context**
- **Long Context** -> if your problem involves a bounded dataset and requires complex global reasoning such as summarizing a book
- **RAG** -> for enterprise "infinite data"

![[Screenshot 2026-07-02 at 12.16.17 AM.png]]
## MCP (Model Context Protocol)
Used to access *3rd party external systems* such as inventory management, customer DB, and external APIs.

**Traditional APIs** expose endpoints that require *rigid integrations* tied to specific systems. **MCP** doesn't just expose tools -- it provides *self-describing interfaces* that AI agents can understand and use.

```ad-important
Unlike traditional APIs, **MCP** puts the burden on the *AI agent* rather than the developer
```

MCP's real value comes in the form of a *plug-and-play* design. MCP developers build MCPs that you can simply use directly in your **AI agents**.

Multiple MCP servers can be *unified* under one **AI agent**. The main function is intelligent tool selection

## Skills
Provides *procedural knowledge* for an **AI Agent**. Allows orchestration for when and how to do a task.

Structure:
```md
my-skill/
├── SKILL.md      # Required: metadata + instructions
├── scripts/      # Optional: executable code
├── references/   # Optional: documentation
├── assets/       # Optional: templates, resources
└── ...           # Any additional files or dirs
```

**`SKILL.md`**
- Frontmatter
	- name: PDF Builder
	- description: use when the user asks to extract a PDF
- Body
	- step-by-step instructions
	- rules & examples of input and output

**Progressive Disclosure**: only reads skill "tiers" if it is tasked by the LLM; helps LLM *index* skills and *frees up space* in context window
- **first tier**: Frontmatter (metadata) -> LLM reads this first to decide whether to use the skill
- **second tier**: body + instructions -> once LLM decides it needs the skill, reads the body
- **third tier**: everything else -> LLM only grabs these at the point of need

**Safety Compliance**
- prompt injection
- tool poisoning
- malware

## Reliability
When APIs fail, need a way to fallback
- retry logic
- timeouts -- so that agent doesn't hang indefinitely
- fallback paths -- choose plan B when plan A doesn't work

**Handling the consequences of model routing**
- *schema* bounds the argument shape
- *existence check* bounds bad tool names
- *try/except* bounds execution failures
- *iteration cap* bounds runaway loops

## Security
**Prompt Injection**: malicious instructions that override system prompts
- use **input validation** to catch malformed requests
- **output filters** to block responses that violate policy
- **permission boundaries** that limit agent capabilities

## Observability
*Trace* the agent's decisions, tool calls w/ parameters, token metrics, retrieval system return
- test cases with *known good answers*
- success rate, latency, cost per task
- automated test that catch discrepancies

## Agentic Tool Orchestrator Flow
**Givens + Tools**
- MCP tools
- native LLM tool-calling API
- schema validator (Pydantic/Zod)
- plain structured logging

Key points
- tool description quality *is* the routing logic

**Orchestrator Flow**
1. *Ingest* the query and tool schemas
	- occurs during API call
	- schemas are passed on *every* iteration -
```python
def run_agent(query: str) -> str:
	messages = [{
		"role": "user",
		"content": query,
	}]
	
	for step in range(1, MAX_ITERS + 1):
		response = client.messages.create(
			model=MODEL,
			max_tokens=1024,
			tools=TOOL_SCHEMAS, # ingest tool schemas
			messages=messages, # ingest query
		)
```

2. LLM *decides* to propose a tool call (with typed args) or emits a final answer
	- use provider's native function-calling
```python
if response.stop_reason != "tool_use": # check if final answer
	final = "".join(b.text for b in response.content if b.type == "text") # use generator for memory efficiency
	print(f"[step {step}] FINAL ANSWER")
	return final.strip()
```

3. *Validate* the proposed call against the tool's schema
	- Pydantic in Python / Zod in TS
	- catches malformed model output
```python
"""Implicit in reference orchestrator
1) Anthropic API contrains and validates the model's input against each tool's input schema
2) TOOL_FUNCTIONS[name](**args) will raise a TypeError at call time if the args don't match the func signature
"""
```

4. *Execute* tool call and capture result or output a **clean error**
	- `json.dumps()`: converts a Python data type into JSON-formatted string
```python
def _execute_tool(name: str, args: dict) -> str:
	if name not in TOOL_FUNCTIONS:
		return f"Error: no tool named '{name}' exists."
	try:
		result = TOOL_FUNCTIONS[name](**args)
		return result if isinstance(result, str) else json.dumps(result) # crucial to return a string
	except Exception as e:
		return f"Error running {name}: {e}"
```

5. *Feed back* the result into context
	- crucial for multi-tool calls of the same query
```python
messages.append({
	"role": "assistant",
	"content": response.content # feed back the content from the LLM
})
```

6. *Loop* back to step 2
	- implement a hard iteration cap (5 max) and clear termination condition
7. *Return* the final answer plus a trace of what happened

**Optimizations**: these are wrapped around the orchestrator loop
- **Reliability**
	- validate inputs/outputs
	- retry or gracefully degrade on tool failure
	- guard against malformed output -- key Prompt Opinion focus
- **Observability**
	- log every decision and tool call as structured output
	- be able to trace calls in Loom:
	> "Here the agent picks tool X, gets Y, then decides Z"
- **Tool description tuning**
	- description quality ensures routing accuracy

**Loom**
- trust the model's routing, verify and contain everything downstream of it