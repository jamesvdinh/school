**System prompting**: instructing the model to define the role, explicit constraints, and instructions for *edge cases*
- at Prompt Opinion: system prompt telling LLM to say "not found" rather than fabricating data -> injected at the *start* of a session
- used templates/structured formats so downstream code can parse model response instead of guessing
- used few-shot examples when a task required the model to match a specific pattern
**Chain-of-thought prompting**: asking the model to reason *step-by-step* before giving a final answer
- improves accuracy on *multi-step* problems
- introduces *observability*
**Temperature / sampling**: a parameter controlling *randomness*
- *low* temperature = more deterministic/consistent output
- *high* temperature = more varied/creative
- answer the question: How would you make this agent's tool selection *more reliable*?
**Prompt injection**: when untrusted input (document, tool result) contains text trying to *manipulate* the model's behavior
- real concern for any agent pulling in external data
- solution @ Prompt Opinion: implement a lightweight *DOM-parse* library that scans document inputs and returns cleaned data
**Grounding / RAG**: giving the model relevant *retrieved context* (like FHIR data retrieval) rather than relying purely on trained knowledge
- connect to MCP work and grounded medical research paper project
**Structured output / schema-constrained generation**: forcing the model to return output in a specific *format* (JSON matching a schema that fills fields in a list)
- allows downstream code to reliably *parse* it