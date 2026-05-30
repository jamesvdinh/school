## Valency
**Background**: AI-focused research assistant built into popular AI tools such as Claude and Gemini
- Valency builds the AI infrastructure that works alongside a researcher in a curated, user-friendly design. It targets researchers and bridges them to millions of papers while remaining easy to use and transparent. 

**Purpose**: to provide access to thousands of research papers and findings to researchers

**Principles**: open science, AI-accelerated research, providing quality tools to researchers

**Open science**
- *open-access* -- research papers freely available, not paywalled
	- e.g. arXiv, PubMed
- *open data* -- raw research data published alongside papers
- *reproducibility* -- code, protocols, and analysis pipeline published so that experiments can be replicated
- *open peer review* -- transparent review process
- *open source tools* -- free textbooks, courses, materials

**Open science initiatives**
- *Semantic Scholar*: AI-powered research paper search and discovery. Very relevant to Valency.
- *Hugging Face*: open ML models and datasets

**Other companies in this space**
- *Elicit*: AI research assistant for literature review
- *Perplexity*: general AI search with research-focused features
### Behavioral
```ad-question
title: **Why Valency?**
I'm interested in Valency because of its focus on AI grounding which aligns very clearly with the researcher's values: accurate, up-to-date information retrieved from a variety of sources. Valency employs a model context to provide researchers with the most up-to-date multi-sourced information available, and in a format that's readable and designed for transparency. This approach to software resonates with me because I believe transparency is tantamount to a successful product and that communication is a priority for effective AI use. During my time at Darena Health, I built an MCP server from the ground up for a live EHR system. I quickly realized that linking the MCP with the LLM wasn't the hard part; it was grounding it in trustworthy, structured data that actually proved useful for the client. This is adjacent to open science principles -- referencing data rooted in ground truth that is reproducible and clearly cited. I've worked in open source projects before -- open source AI tooling at Prompt Opinion and open data portals in a civic context -- which gave me a strong values for transparency, reproducability, and buidling software that can be extended by research. Open science is the natural next step for me, and a big part of why Valency is interesting to me is that you're not just preaching open science; you're building the infrastructure for it, which I'm very fond of.

*Summary*:
- **mission**: Exposing millions of research papers to researchers using AI-grounded architecture (MCP)
- **product**: AI-grounded research-paper tool
- **my values**: transparenct is important for the client; prev work in open source led me to open science and that building infrastructure is what drives me
```

```ad-question
title: Tell me about yourself

My name is James, a Cal alum living in Berkeley, and I'm interested in exploring how AI and ML can bridge communication gaps. I bring a background in AI healthcare, specifically in building an MCP server from the ground-up for use in a live EHR system. This AI integration with a built-in chat agent allowed primary health care providers to query patient data and surface any risk factors in an environment they already used. This was built with a C# .NET backend and Vue.js frontend. I learned several new technologies, was able to adapt quickly to a new tech stack, and became deeply connected with my team. One thing that resonated with me while working for this company was the focus on readability for the user-side. Knowing that physicians and medical professionals would be naturally skeptical at reading AI-generated information, we made sure that the outputs followed a structured template that only provided queried information. What I enjoyed the most was being able to bridge that communication gap between physicians and patient data using AI wrapped in a familiar LLM, which is similar to Valency Bond.

Valency's approach to broad-topic research discovery wrapped by an LLM makes it versatile for any AI assistant. That's part of why Valency's approach at the research-paper layer sticks out to me.
```

```ad-question
title: Why are you interested in open science?

Open science is exciting to me because it removes the barrier between researchers and for-profit companies. One of the key principles of open science is accessbility and transparency, meaning that research papers aren't paywalled, and that you're guaranteed to be getting untampered, raw data. A big question for a lot of research papers is "how much can I trust these findings?" because actually a majority of research is often faulty, not in the sense that the data is inaccurate, but that there are so many unknowns in experiementing that old research is often disproven frequently due to new findings. This is also why I highly value the reproducibility in open science. Research that can be replicated exactly can often be improved upon via evolving technology and new discovieries. I view open science as an opportunity for open collaboration under a blanket of accessibility and trust.

I want to talk a little about my background in open source contributing and how it relates to open science. I previously contributed to building an open-source MCP server template at Prompt Opinion for developers to fork and use for their own purposes. This MCP server came equipped with a robust C# .NET API architecture and example tools and protocols to overwrite.
```


```ad-important
title: Ending questions
- I've noticed that some companies such as OpenEvidence specifically target clinicians with a vertical product while Valency seems to be building more of a horizontal infrastructure layer for any AI assistant. Is this intentional in staying objective to the end user?
- What does the first 90 days look like on the job?
```

**Common questions**
- Tell me about yourself (2 min max, ending with why you're here)
- Why Valency? Why this role?
- Tell me about a project you're proud of -> *MCP Server (Darena)*
- Tell me about a time you didn't know how to do something — how did you learn it? -> *New to C# .NET -> read up on docs, ask senior engineers, and explore similar open repositories on GitHub to understand architecture*
- Tell me about a bug or technical challenge that stumped you
- How do you use AI coding tools? (this one matters a lot — they explicitly call it out)
	- I use Claude, Gemini, and Cursor heavily as part of my normal workflow rather than a special tool I reach for. One major shift in the past year or so was being able to change how I delegate tasks to them. If I'm trying to prototype an MCP tool quickly, I would tell Claude to generate a skeleton version of a tool definition, then redefine it using the EHR's actual schema. Oftentimes, the LLM does not have the full context and that's an area where it's crucial to constantly monitor -- you don't want to have mistakes that overcomplicate a codebase or that steer the logic off course.
	- One other thing I try to be intentional about is using AI to learn rather than just generate. When I hit a roadblock, whether its a concept I don't understand or inheriting someone else's code, I ask it to explain the underlying concept, not just fix it. Otherwise, it's easy to ship something without actually building at that level, which can become a problem with making nuance and informed decisions about company-specific needs.
- Tell me about a time you worked with someone difficult or had a disagreement
	- *Situation*: disagreed with a study partner's derivation for a proof.
	- *Tension*: My proof didn't consider a specific theorem that we were meant to use and that made it shaky
	- *Action*: They walked through my reasoning, while stopping to answer any clarifications
	- *Resolution*: I reshaped the argument of the proof while the other person worked out the actual derivation
- Where do you see yourself in 2-3 years?
- What questions do you have for us?