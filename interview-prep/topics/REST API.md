## Background
In an interview, there are three main API protocols to choose from, depending on the needs of the project

```ad-summary
title: REST (Representational State Transfer)

REST uses standard HTTP methods (**GET**, **POST**, **PUT**, **DELETE**) to manipulate resources via URLs. This is used for standard *CRUD* operations in a web or mobile app. The go-to protocol for most web services

- Benefits
	- maps naturally to DB operations and HTTP semantics
	- is well-understood by developers
	- client-facing
    
- Tradeoffs
	- API endpoints are static
	- Can possibly over or under-fetch data
```

```ad-summary
title: GraphQL

GraphQL uses a *single endpoint* for all API methods. Uses a query language that lets clients specify *exactly* what data they need. Think mobile app that only needs basic information (use REST) versus web dashboard that displays comprehensive analytics (use GraphQL -- clients can request *exactly* what it needs in a single query)

- Benefits
	- more flexibility with complex queries
	- can serve diverse clients with different data needs
- Tradeoffs
	- poor reusability with queries -> REST standardizes API calls
	- adds complexity -> query parsing, schema validation, complex caching strategies
```

```ad-summary
title: RPC (Remote Procedure Call)

RPC protocols like gRPC (Google-developed, open source) use binary serialization and HTTP/2 for efficient communication betwene services. While REST treats data as resources, RPC allows the use of actions and procedures. Use RPC for microservices or internal APIs.

Ex. quickly validate permissions -> `checkPermission(userID, resource)`
REST -> `GET /api/{userID}` -> `user.permissions.resource`

- Benefits:
	- high-performance connections -> Protocol Buffers + HTTP/2 is much faster than REST's JSON-over-HTTP
	- great for internal APIs
```

```ad-caution
For **real-time** features such as notifications, chat, or live updates, use different protocols like **WebSockets** or **Server-Sent Events (SSE)**: not traditional APIs, they are persistent connections
```

## REST API Design
### Core Entities
When designing the API, you want to first identify and list the **core entities** of your system. This helps define terms and understand the fundamental data of the design that your API will exchange and persist in a **Data Model**. After writing these down, then you can discover new entities and relationships that you didn't know before.

```ad-example
Twitter example:
- User
- Tweet
- Follow
  
These answer guiding questions:
- Who are the actors in the system? Are they overlapping?
- What are the nouns or resources necessary to satisfy the functional requirements?
```

Ex. Design Ticketmaster
```ad-example
Core Entitites w/ corresponding REST resources
- Events
	- **GET** `/events` -> get all events
	- **GET** `/events/{id}` -> get a specific event
- Venues
	- **GET** `/venues/{id}` -> get a specific venue
- Tickets
	- **GET** `/events/{id}/tickets` -> get available tickets for an event
- Bookings
	- **POST** `/events/{id}` -> create a new booking for an event
	- **GET** `/bookings/{id}` -> get a specific booking
```

**Important**: REST resources should represent *things* in the system, **NOT** actions. Instead of thinking about what users can do (like booking or purchase), think about what exists in your system (core entities).
- resources should always be *plural* nouns

**Handling relationships between resources**: two main approaches
1. Nest resources in a clear parent-child relationship via URL (when relationship is required):
	- `/events/{id}/tickets`
2. Keep resources flat and use query parameters (when relationship is optional):
	- `/tickets?event_id=123`

| Method   | Purpose                                                                               | Idempotent? | Safe? |
| -------- | ------------------------------------------------------------------------------------- | ----------- | ----- |
| `GET`    | Read a resource                                                                       | Yes         | Yes   |
| `POST`   | Create a resource (or "do something")                                                 | No          | No    |
| `PUT`    | Replace a resource entirely                                                           | Yes         | No    |
| `PATCH`  | Partially update a resource (set email to X is idempotent, but append to list is not) | No (often)  | No    |
| `DELETE` | Remove a resource                                                                     | Yes         | No    |
*Idempotent* = calling it multiple times has the same effect as calling it once. *Safe* = doesn't change server state.

**Common headers:**

- `Content-Type: application/json` — what the body is
- `Authorization: Bearer <token>` — auth (most APIs)
- `Accept: application/json` — what you want back
- `User-Agent: ...` — who's calling
- `X-RateLimit-Remaining` — common rate-limit header
- `Retry-After: 30` — server says wait N seconds

### Pagination

Three common patterns:

**Offset/limit** (simple but slow at scale):
```
GET /papers?limit=20&offset=40
```

**Cursor-based** (scales better, what most APIs use):
```
GET /papers?limit=20&cursor=eyJpZCI6MTAwfQ
```
The cursor is opaque (often base64-encoded), points to a position in the result set.

**Page-based** (human-friendly):
```
GET /papers?page=3&per_page=20
```

### Returning Data
An API response is made up of two parts:
1. The status code, which indicates whether the request was successful or not
2. The response body, which contains the data returned to the client (typically **JSON**)

**Status codes:**
- `2xx` — success (`200 OK`, `201 Created`, `204 No Content`)
- `3xx` — redirects (`301 Moved Permanently`, `304 Not Modified`)
- `4xx` — client error (`400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `429 Too Many Requests`)
- `5xx` — server error (`500 Internal Server Error`, `502 Bad Gateway`, `503 Service Unavailable`, `504 Gateway Timeout`)

### GraphQL
```ts
query {
  event(id: "123") {
    name
    date
    venue {
      name
      address
    }
    tickets {
      section
      price
      available
    }
  }
}
```
### RPC
```ts
// Instead of GET /events/123
getEvent(eventId: "123")

// Instead of POST /events/123/bookings
createBooking(eventId: "123", userId: "456", tickets: [...])

// Instead of GET /events/123/tickets
getAvailableTickets(eventId: "123", section: "VIP")
```

## Security
**Authentication** verifies identity. **Authorization** verifies permissions.

> Use API keys for internal service communications and external developer access. Use JWT for user sessions in web and mobile applications
### API Keys
Long, randomly generated strings that act like passwords for applications. Clients must include their API key in the **Authorization Header**.

How it works: you generate a unique API key for each client, then store it in your DB along with any permissions or rate limits for that client. Then, verify each incoming request by looking up with the key. Perfect for server-to-server communication where you control *both* sides.
- Ex. when booking service needs to call payment service

Note: **NOT** recommended for user-facing product with user-facing APIs -> API keys don't expire or carry user context -> use JWT

### JWT (JSON Web Tokens)
Encode user information *directly* into the token itself rather than storing session state on server
- contains user ID, permissions, and expiration time

How it works: when a user logs in successfully, server creates a JWT, then *signs* the entire token with a secret key. When that JWT comes back with future requests, verify it's authentic by checking the signature.
- works well for distributed systems

## Rate Limiting and Throttling
Prevents malicious attacks and accidental overuse.

Common strategies:
- **Per-user limits**: 1000 requests per hour per authenticated user
- **Per-IP limits**: 100 requests per hour for unauthenticated requests
- **Endpoint-specific limits**: 10 booking attempts per minute to prevent ticket scalping