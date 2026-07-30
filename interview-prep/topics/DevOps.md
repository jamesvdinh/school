**Philosophy**: breaking down the wall between development and operations teams
- devs take more *ownership* of how code runs in prod
**Infrastructure as Code (IaC)**: defining *infra* (servers, networking) in config files (e.g. Terraform, ARM templates for Azure) instead of *manually* clicking through a cloud console
- makes environments reproducible and version-controlled
**Monitoring/observability**: logs, metrics, and alerts that tell you *what's happening* in prod
- DevOps: how do we know it's broken *before* a user tells us?
**Incident response basics**: *rollback* strategy -- revert to a previous known-good deployment quickly as a safety net

## CI/CD
**Continuous Integration (CI)**: every code change is automatically *built* and *tested* when pushed
- this allows integration issues to surface *immediately* rather than piling up
- frequently merge code from main to handle *merge conflicts* early
**Continuous Deployment/Delivery (CD)**: *automatically* ship code that passes CI to staging or production rather than manual deploy steps
- "*Delivery*": ready to deploy with a manual trigger
- "*Deployment*": fully automatic, no human gate
**Pipeline stages**: build -> test -> stage -> prod

```ad-important
title: Why this matters at CaseWorthy
Most likely develops products with AI-assisted code that ships code faster, so automated testing gates in CI become the safety net for AI-introduced bugs.
```
