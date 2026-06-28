## [AI Coding Mock Interview (with Senior FAANG Engineer)](https://www.youtube.com/watch?v=ZE_YEn-okfk)

**Problem**: identifying a bottleneck in a finance report generation service and coming up with solutions to prevent long wait times

**Interviewer**:
- we don't care about the financial reporting generation aspect -> happens in backend
- what we *DO* care about is that the process is taking a *very long* time to create the report
	- results in request timeouts from the client request calls
- **Action question**: How would you *improve* this and how would you *fix* this?

**Interviewee**:
1. *ASK* clarifying questions
	- restate the question and code flow
	- what does *long-running* task mean? how long?
2. *STATE* initial approaches (first w/o use of AI)
	- I would look at data or logs of these calls and identify when it started happening and trace back in the code

```ad-hint
title: Interviewer interject

**Let's clarify**: the problem isn't that the service is taking as long as it is (it's inherent, we can't speed it up). The part we want to solve is *what are our options* knowing that the service is supposed to take a long time.

- the service takes a long time and causes a timeout
- but we can't speed up the individual service
- what are our options?
```
3. *EXPLAIN* thought process (w/o use of AI)
	- I would look at the service architecture and identify which step is taking the most amount of time and why
```ad-hint
title: Interviewer interject

So, in this scenario with a large codebase and limited time (45 mins), *I would like to see how you use Claude Code* to figure out which process is taking the most amount of time
```
3. *SHIFT* this work over to an AI agent (Claude)
	- Ask if you're allowed to or if the interviewer brings it up

```ad-check
title: AI prompt: Understanding the codebase

**Prompt**: "Let's try to understand the FinancialReportGeneratorSurvice fully"
- don't ask to make any changes or fixes
- *explain* that you're using this service to understand all the steps in the service
```
4. *ANALYZE* the AI output and identify source of issue
	- talk through what issues were found and the causes
	- then navigate to the source

```ad-hint
title: Interviewer interject

For the sake of the interview, there is a hard-coded timeout set in place to simluate a long-running service

**Reorient**: how can we *prevent the timeout* from happening while *still getting the job done*?
```
5. *ASK & Identify*: either AI or interviewer -> "Is the process **sync or async**?"
	- it is sync: I would move to async process
		- we wouldn't get stuck and we would allow job to be created