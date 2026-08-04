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
	- made an infra decision that had real optimization improvement
	- Why caching at the edge matters?
		- reduces origin server load and latency by serving repeated requests from CloudFlare's network instead of the backend every time
	- Tradeoff: *cache invalidation* (big points here)
		- how to know when cached data is stale
	- Analytics tracking -> shows *observability*

## Docker
**Image**: the static, packaged blueprint
- contains the app, dependencies, and runtime
**Container**: a running *instance* of the image
- multiple containers can be run from one image
**Dockerfile**: the script that *defines* how an image is built
- base image, dependencies installed, files copied in, startup command
**Volumes**: how you *persist* data outside a container's lifecycle
- since containers are disposable + stateless, any data that needs to survive a restart (DB data, uploaded files) foes in a volume
**Docker Compose**: a way to define and run multiple containers together (e.g. app + database) with one config file

**Why containers over a VM**
- *consistency* across environments: dev/stage/prod behave the same
- *isolation*: one app's dependencies don't conflict with another's
- *portability*: runs the same wherever Docker runs

## Azure
**App Service**: Azure's managed PaaS (Platform as a Service) for hosting web apps. You push a code/container -> Azure handles *VM*, *scaling*, *networking* underneath
- Oracle Cloud gives raw compute instance (more manual)
**Azure Container Instances (ACI) / Azure Kubernetes Service (AKS)**: two ways to run containers on Azure
- ACI for *single-container* workloads, little orchestration
- AKS for *real orchestration* (multiple services, auto-scaling, self-healing)
**Azure Functions**: *server-less*; code that runs in response to an event (HTTP request, timer) without having to manage server at all
**Resource Group**: a logical container that groups related Azure resources together (VM, DB, storage account) for a single project
- can be managed/billed/deleted as a unit
**Microservices**:
- independent deployability
- isolated failure domains: one service crashing doesn't take down the whole app
- technology flexibility per service

## Cloud Management
**Scaling**: horizontal vs vertical
- vertical scaling: giving a single server more resources (CPU/RAM)
- horizontal scaling: adding more instances/servers and distributing load across them; Cloud-native systems like CaseWorthy microservices use horizontal for more flexibility
**Load balancer**: sits in front of multiple instances of a service and distributes incoming traffic across them
- this prevents one single instance from being overwhelmed and traffic can keep flowing if one fails
**Environment vars / secrets management**: how config (API keys, DB connection strings) gets injected into an app without hardcoding in source code
**Networking basics**: VPC/VNet, security groups/firewall rules
- a virtual network isolates your resources
- security groups control what traffic is allowed in/out
**Auto-scaling**: cloud infra automatically adding/removing instances based on load rather than manually
**Managed vs self-managed services**
- managed DB (Azure SQL, AWS RDS) handles backups, patching, and scaling for you
- self-managed = running DB yourself on a VM -> CaseWorthy likely uses managed data services
**Statelessness**: cloud-native services likely designed for stateless (no session data stored)
**CDN (Content Delivery Network)**: caching content at edge locations closer to users to reduce latency

## Deploying an App
1. **Containerize** the app -- write Dockerfile defining base image, dependencies, and startup command
2. **Choose and provision infrastructure** -- this means choosing a self-managed (Docker container) or managed service like Azure App Service/AKS
3. **Handle configuration and secrets** -- figure out how to inject env vars and secrets into app without hardcoding them
4. **Deploy** and expose the app -- run the container on the instance, expose the right port, this is also where you would implement a load balancer for multiple instances
5. Add **observability** -- logging and basic monitoring
6. **CI/CD** (if relevant) -- ideally make the whole flow automated: pipeline builds image -> runs tests -> deploys on merge