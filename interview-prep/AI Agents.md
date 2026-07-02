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
Answers the question: is it possible to search through 500GBs of an entire company's documents? -- Yes, AI assistants can fit them in their context window and generate output

Question: "What's our remote work policy for international employees?"
**Retrieval**: create *word embedding* for question
- run **semantic search**: compare word embedding against embeddings of other documents in vector DB
**Augmentation**: process where retrieved data is *injected* into the prompt at runtime
- allows AI rely on up-to-date info in the database
- semantic search result provides augmented knowledge for the AI to use
**Generation**: AI generates the response given the semantic relevant data retrieved from the vector DB
- AI retrieves data relevant to *remote work* and *policy*
- Then, structures response based on initial criteria of *international employees*