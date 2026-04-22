# Smart Campus Sensor & Room Management API
**5COSC022W Client-Server Architectures | University of Westminster 2025/26**

---

## API Overview

A JAX-RS RESTful web service built with Jersey and an embedded Grizzly server, providing full CRUD management of campus Rooms, Sensors, and Sensor Readings. The API follows REST best practices including resource nesting, semantic HTTP status codes, and comprehensive exception handling.

**Base URL:** `http://localhost:8080/api/v1`

---

## Build & Run Instructions

**Prerequisites:** Java 11+, Maven 3.6+

```bash
# Clone the repository
git clone https://github.com/<your-username>/smart-campus-api.git
cd smart-campus-api

# Build the project
mvn clean package

# Start the embedded server
mvn exec:java
```

The server starts at `http://localhost:8080/api/v1`.

---

## Conceptual Report

### Part 1 — Service Architecture & Setup

**Q1.1: Explain the default JAX-RS resource lifecycle and its impact on in-memory data management.**

By default, JAX-RS creates a new instance of a resource class for every incoming HTTP request (per-request scope). This means instance fields are reset on each request, so shared data cannot be stored as instance variables. To persist state across requests, in-memory data stores (e.g., `ConcurrentHashMap`) are held as `static` fields in a dedicated data-store class, which lives for the JVM's lifetime. Because multiple requests can arrive concurrently, `ConcurrentHashMap` is used instead of `HashMap` to prevent race conditions, and `synchronized` blocks are applied where multiple data structures must be updated atomically.

---

**Q1.2: Why is HATEOAS considered a hallmark of advanced REST design, and how does it benefit client developers?**

HATEOAS (Hypermedia As The Engine Of Application State) means that API responses include links that tell the client what actions are available next, instead of requiring the client to know all endpoint URLs in advance.

This makes the API more flexible because clients can discover and navigate available operations dynamically by following these links. As a result, the client is less tightly coupled to the server — if a URL changes, the client still works as long as it follows the links provided in the response.

---

### Part 2 — Room Management

**Q2.1: What are the implications of returning only IDs versus full room objects in a list response?**

Returning only IDs keeps the response small, but it means the client has to make extra requests for each room to get the full details. This creates the N+1 problem, which increases both delay and server load because many requests are needed instead of one.

On the other hand, returning full room objects avoids these extra calls since all the needed information is already included in one response. The downside is that the response becomes larger.

---

**Q2.2: Is DELETE idempotent in your implementation?**

Yes, the DELETE method is idempotent, which means repeating the same request leads to the same final result on the server.

For example, the first `DELETE /rooms/{id}` request removes the room and returns `204 No Content`. If the same request is sent again, the server returns `404 Not Found` because the room is already deleted.

Even though the responses are different, the final state does not change — the room is still gone. So, this does not break idempotency, it simply shows the server is correctly reporting the current state.

---

### Part 3 — Sensor Operations & Linking

**Q3.1: What happens if a client sends data in a format other than `application/json` to a method annotated with `@Consumes(MediaType.APPLICATION_JSON)`?**

The JAX-RS runtime intercepts the request before it reaches the resource method and automatically returns `415 Unsupported Media Type`. No application code executes. This declarative content negotiation prevents the method from receiving a body it cannot deserialise, removing the need for manual format-checking in application logic.

---

**Q3.2: Why is `@QueryParam` preferred over path segments for filtering (e.g., `/sensors?type=CO2` vs `/sensors/type/CO2`)?**

Path segments identify a specific resource. Query parameters qualify a collection. Using `/sensors/type/CO2` implies "CO2" is a named sub-resource with its own identity, which is semantically incorrect for a filtered view. Query parameters are optional by nature, compose easily for multiple filters (`?type=CO2&status=ACTIVE`), and signal to HTTP caches that the result is a derived view rather than a canonical resource.

---

### Part 4 — Deep Nesting with Sub-Resources

**Q4.1: What are the architectural benefits of the Sub-Resource Locator pattern?**

The sub-resource locator delegates URL path segments to dedicated classes (e.g., `SensorReadingResource`), applying the Single Responsibility Principle. Each class has one focused concern, making it easier to read, test, and maintain independently. Without this pattern, all nested paths would accumulate in one large controller class that grows with every new sub-resource, creating a maintenance burden and increasing the risk of merge conflicts in team environments.

---

### Part 5 — Error Handling & Exception Mapping

**Q5.1: Why is HTTP 422 more semantically accurate than 404 when a `roomId` reference in the request body does not exist?**

`404 Not Found` signals that the requested URL does not exist. When a client POSTs to `/api/v1/sensors`, the URL is valid — the endpoint exists. The problem is a broken reference inside an otherwise well-formed JSON payload. `422 Unprocessable Entity` correctly communicates that the request was syntactically valid but semantically invalid, directing the client to fix the payload data rather than the URL.

---

**Q5.2: What are the security risks of exposing Java stack traces to API consumers?**

Stack traces can reveal the server's technology stack and library versions (enabling targeted CVE lookups), internal class and method names (exposing architecture), server file paths (leaking directory structure), and partial data values from live request context. This is classified as CWE-209. The global `ExceptionMapper<Throwable>` prevents this by returning only a generic `500` message to the client while logging full details server-side.

---

## Video Demonstration

[Link to Postman demonstration video — submitted via Blackboard]
