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

