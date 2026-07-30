## Ownership Examples
1. **Spotify Music Trend Dashboard** -- Oracle Cloud free tier + Docker
	- containerized an app and deployed it to Oracle Cloud's free tier myself; provisioned the compute instance myself, configured networking (shape, DNS, API keys); ran the container directly rather than using a managed  platform
	- used *Ampere ARM* to fit container architecture
	- Why containerize at all?
		- consistent environments between local dev and prod, portability, isolating dependencies
	- What does Oracle Cloud provide?
		- raw compute instance (VM)
2. **Capstone Connect** -- CloudFlare
	- server-side caching + analytics
	- Why caching at the edge matters?
		- reduces origin server load and latency by serving repeated requests from CloudFlare's network instead of the backend every time
	- Tradeoff: *cache invalidation* (big points here)
		- how to know when cached data is stale
	- Analytics tracking -> shows *observability*

## Docker
**Image**: the static, packaged blueprint
- contains the app, dependencies, and runtime
**Container**: a running instance of the image
- multiple containers can be run from one image
**Dockerfile**: the script that defines how an image is built
- base image, dependencies installed, files copied in, startup command
**Volumes**: how you persist data outside a container's lifecycle
- since containers are disposable + stateless, any data that needs to survive a restart (DB data, uploaded files) foes in a volume
**Docker Compose**: a way to define and run multiple containers together (e.g. app + database) with one config file

**Why containers over a VM**
- consistency across environments: dev/stage/prod behave the same
- isolation: one app's dependencies don't conflict with another's
- portability: runs the same wherever Docker runs

## Azure
**App Service**: Azure's managed PaaS (Platform as a Service) for hosting web apps. You push a code/container -> Azure handles VM, scaling, networking underneath
- Oracle Cloud gives raw compute instance (more manual)
**Azure Container Instances (ACI) / Azure Kubernetes Service (AKS)**: two ways to run containers on Azure
- ACI for single-container workloads, little orchestration
- AKS for real orchestration (multiple services, auto-scaling, self-healing)
**Azure Functions**: serverless; code that runs in response to an event (HTTP request, timer) without having to manage server at all
**Resource Group**: a logical container that groups related Azure resources together (VM, DB, storage account) for a single project
- can be managed/billed/deleted as a unit
**Microservices**:
- independent deployability
- isolated failure domains: one service crashing doesn't take down the whole app
- technology flexibility per service