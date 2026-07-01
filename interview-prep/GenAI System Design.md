[Home](https://selamy.dev/posts/customer-facing-genai-system-design-interview-loop/)
[Roadmap](https://docs.google.com/document/d/1akqNIetG4R5kpv9AdobaAZv9qqBShTmHpVG3B8IJcds/edit?tab=t.0)

## 00. Orientation
> Golden rule: strong answers don't start with a model or database. They start with the customer's workflow
- drawing boxes before understanding the customer's needs makes you lose the plot for the interview

1. **Core Scenario**: Ask clarifying questions before even designing a system
	- who are the users?
	- what are the data sources?
	- what's the latency and cost envelope?

2. **Good vs bad answers**
	- focus on foundation: state assumptions, draw simple initial system, define how to evaluate it, close with a scoped pilot recommendation that's grounded in measurable success criteria

3. **Architecture checklist**
	- **data flow**: map out what info is moving from source systems -> ingestion path -> retrieval layer -> user
		- explain what *metadata* and *access controls* are traveling alongside that information
	- **control flow**: brain of architecture
		- do we need to retrieve more data?
		- do we execute a tool?
		- do we generate an answer or turn to human
	- do *NOT* blur data and control flow together -> massive red flag
	- **trust boundaries**: draw literal lines on whiteboard to show where identity is checked where tenant scoping is enforced and where logging policies sit

4. **Security / Trust boundaries**
	- design must incorporate least privilege, access-aware retrieval, and prompt-injection defense
	- need to be able to explain whether *logical isolation* is enough for a client or if *hard isolation* is needed at the tenant, index, or network layer
		- shows high-level security thinking for enterprise use
	- **questions to ask**:
		- What data enters the answer path and who can see it?
		- How is the answer grounded in authorized evidence?
		- What simpler strategy was rejected in favor of your current one?

5. **Evaluation / Ops**
	- can't rely on thumbs up/down feedback from users
	- have to design golden examples, permission boundary checks, and adversarial prompts
	- differentiate between *retrieval* vs *generation* quality

