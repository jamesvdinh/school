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