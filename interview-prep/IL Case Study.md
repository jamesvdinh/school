## Pre-interview prep
- 4 hours to finish the assignment
- 7 days from issue date to complete it
- Build a toy orchestrator
	- a tiny ReAct-style agent loop with 2-3 fake tools from scratch

## Design template
Default: **ReAct-style loop**
- LLM receives query and tool schemas
- decides on a tool call
- you execute it
- feed result back
- repeat until model emits a final answer
- **Rationale** over *plan-then execute*: ReAct handles arbitrary/unpredictable queries better; plan-then-execute is cleaner when steps are knowable upfront

**Tool definition and routing**
- strongest highlight: MCP work
- define tools with tight JSON schemas
	- name
	- crisp definitions
	- typed params
- routing handled by *model's native function-calling* rather than a hand-rolled 
- Say in Loom: Prompt Opinion experience taught me that the description quality is what makes the routing reliable
	- tightening tool defs and implementing guardrails ensures the reliability aspect of an AI agent

**Termination**: how the agent knows it's done
- put hard cap on iterations / calls to tools so confused model can't loop forever
- cheap to add -> obvious gap that reviewers look for

**Reliability**
- validate tool inputs and outputs
- handle tool failures
	- retry or return a clean error the model can reason about
- guard against malformed model output
- *Tie to story*: making trustworthy production work

**Observability**
- log every tool call and decision
- does 2 functions
	- good practice to trace callbacks
	- makes Loom walkthrough easier to visualize ("here you can see agent choosing X, getting Y back, then deciding Z")

**Scope**
- build CLEAN working vertical slice first: one query flowing end-to-end through the loop
- then, layer reliability on top

## 4-hour Playbook
- ~20 mins reading materials and confirming if design template fits the requirements
- ~2.5 hrs building
	- AI-accelerated, slice first
- ~30 mins testing and strengthening reliability paths
- ~40 mins on design doc and Loom

### Design Doc
1. Problem in my own words + any assumptions I made
2. Architecture and ReAct loop
3. Key decisions with tradeoffs
	- why ReAct, why native function calling, how I handled failures
4. What I'd do with more time
	- what next features or tightening I'd do
	- what quality checks to make
	- how to scale to prod

### Loom walthrough
> Max 5 mins
> Decision-focused, NOT a code read through

Practice once before recording

layout
- ~30s on problem framing
- ~3 mins walking through architecture
	- highlight 2-3 most *important* decisions
- ~1 min showing it actually run on a query
