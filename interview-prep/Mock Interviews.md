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

**Prompt**: Let's try to understand the FinancialReportGeneratorSurvice fully
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
	- it is sync -> *SOLVE*: I would move to async process
		- we wouldn't get stuck and we would allow job to be created

```ad-hint
title: Interviewer question

So yes, the main scenario here is that we can defer this process *in the background* and keep the main thread clear. What are some *challenges* to shifting this to an async process?

A: **(1)** even if we go async, if there are flaws that occur *AFTER* the report generation, we still have to wait until the process is finished
**(2)** we need to consider if we even want to support multiple threads calling the report asynchrounously
```
6. *START* prompting Claude to make code changes, but document changes along the way

```ad-check
title: AI prompt: Refactoring a process from sync to async

**Prompt**: Now we want to convert this sync process of generating report into async. Before implementing the code, let's understand the structure we will go through.

Clarify:
- I'm asking the AI to explain the design choices if creates to add that HIL input
- allows me to generate code that I can understand fundamentally
```

```ad-hint
title: Interviewer question

**Question**: What layer should we implement the async layer in?

**A**: so it depends on us whether we want an entirely new service layer to handle this process and call on multiple threads. Or, with keeping the current structure, we can *change the code within the try* of the service layer, then refactor code in the report generation to handle async.

**Q**: How about we do it at the top layer?
```

```ad-check
title: AI prompt: Convert code to async

**Prompt**: Let's convert report generation from sync to asnyc. Convert the ReportGenerator into an sync call.
```

**Key takeaways**:
- using AI to *understand* the code first rather than jumping straight into making changes
- instructing the AI to *target a specific process* instead of just 'make this process async' -> the whole code is a process, but which one

Interviewer grading **gates**:
1. know that the solution is asynchronous
2. where to put async call
	- we want users to receive a "in progress" response while report process is running
3. multiple jobs happening simultaneously -> use multithreading