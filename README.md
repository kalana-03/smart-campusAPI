# Smart Campus Sensor & Room Management API
**5COSC022W Client-Server Architectures | University of Westminster 2025/26**

---

## API Overview

A JAX-RS RESTful web service built with Jersey 2.32 and deployed on Apache Tomcat, providing full CRUD management of campus Rooms, Sensors, and Sensor Readings. The API follows REST best practices including resource nesting, semantic HTTP status codes, HATEOAS-style discovery, and comprehensive exception handling.

**Base URL:** `http://localhost:8080/smartcampus/api/v1`

---

## Build & Run Instructions

**Prerequisites:** Java 8+, Maven 3.6+, Apache Tomcat 9+ (bundled with NetBeans)

### Option 1 — NetBeans (Recommended)
1. Open NetBeans and select **File → Open Project**
2. Navigate to the `smartcampusAPI` folder and open it
3. Right-click the project → **Clean and Build**
4. Right-click the project → **Run**
5. NetBeans will deploy the WAR to its bundled Tomcat automatically

The server starts at `http://localhost:8080/smartcampus/api/v1`

### Option 2 — Maven + Tomcat manually
```bash
# Clone the repository
git clone https://github.com/<your-username>/smartcampusAPI.git
cd smartcampusAPI

# Build the WAR file
mvn clean package

# Copy the WAR to your Tomcat webapps folder
cp target/smartcampusAPI-1.0-SNAPSHOT.war /path/to/tomcat/webapps/smartcampus.war

# Start Tomcat
/path/to/tomcat/bin/startup.sh
```

The server starts at `http://localhost:8080/smartcampus/api/v1`

---

## Sample curl Commands

### 1. Discovery — GET API metadata
```bash
curl -X GET http://localhost:8080/smartcampus/api/v1/
```

### 2. Create a Room — POST
```bash
curl -X POST http://localhost:8080/smartcampus/api/v1/rooms \
  -H "Content-Type: application/json" \
  -d '{"id": "R001", "name": "Lab 101", "capacity": 30}'
```

### 3. Get all Rooms with minimum capacity filter — GET
```bash
curl -X GET "http://localhost:8080/smartcampus/api/v1/rooms?minCapacity=20"
```

### 4. Create a Sensor linked to a Room — POST
```bash
curl -X POST http://localhost:8080/smartcampus/api/v1/sensors \
  -H "Content-Type: application/json" \
  -d '{"id": "TEMP-001", "type": "Temperature", "status": "ACTIVE", "currentValue": 22.5, "roomId": "R001"}'
```

### 5. Get all Sensors filtered by type — GET
```bash
curl -X GET "http://localhost:8080/smartcampus/api/v1/sensors?type=Temperature"
```

### 6. Post a Sensor Reading — POST
```bash
curl -X POST http://localhost:8080/smartcampus/api/v1/sensors/TEMP-001/readings \
  -H "Content-Type: application/json" \
  -d '{"id": "READ-001", "value": 23.1}'
```

### 7. Delete a Room (fails if sensors are assigned — 409 Conflict) — DELETE
```bash
curl -X DELETE http://localhost:8080/smartcampus/api/v1/rooms/R001
```

---

## Conceptual Report

### Part 1 — Service Architecture & Setup

**Q1.1: Explain the default JAX-RS resource lifecycle and its impact on in-memory data management.**

By default, JAX-RS creates a new instance of a resource class for every incoming HTTP request (per-request scope). This means instance fields are reset on each request, so shared data cannot be stored as instance variables. To persist state across requests, in-memory data stores are held as `static` fields in a dedicated `DataStorage` class, which lives for the JVM's lifetime. Because multiple requests can arrive concurrently, `ConcurrentHashMap` is used instead of `HashMap` to prevent race conditions, and atomic operations are applied where multiple data structures must be updated together.

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

If a client sends data in a format other than `application/json`, the request will not reach the resource method at all. The JAX-RS framework automatically detects the mismatch and responds with `415 Unsupported Media Type`.
This happens before any application code runs, so the method never executes. This built-in behavior ensures that the method only receives data it can properly understand, removing the need to manually check the request format in the code.

---

**Q3.2: Why is `@QueryParam` preferred over path segments for filtering (e.g., `/sensors?type=CO2` vs `/sensors/type/CO2`)?**

Path segments are usually used to identify a specific resource, while query parameters are used to filter or narrow down a collection. So using `/sensors/type/CO2` suggests that "CO2" is a fixed sub-resource with its own identity, which is not accurate when we are just filtering sensors.
Query parameters like `/sensors?type=CO2` make it clear that we are simply requesting a filtered list of sensors. They are also more flexible, since multiple filters can be added easily, such as `?type=CO2&status=ACTIVE`.

---

### Part 4 — Deep Nesting with Sub-Resources

**Q4.1: What are the architectural benefits of the Sub-Resource Locator pattern?**

The sub-resource locator pattern helps organize code by passing parts of a URL to separate, dedicated classes (`SensorReadingResource`). This follows the Single Responsibility Principle, where each class focuses on one specific task, making the code easier to understand, test, and maintain.
By splitting responsibilities across smaller classes, the application stays more modular, cleaner, and easier to scale as new features are added.

---

### Part 5 — Error Handling & Exception Mapping

**Q5.1: Why is HTTP 422 more semantically accurate than 404 when a `roomId` reference in the request body does not exist?**

`404 Not Found` is used when the requested URL itself does not exist. In this case, when a client sends a request to `/api/v1/sensors`, the endpoint is valid and available, so a 404 response would be misleading.
The actual issue is inside the request body — the `roomId` provided does not exist, even though the JSON format is correct. This is where `422 Unprocessable Entity` is more appropriate, because it indicates that the request is syntactically valid but contains invalid or incorrect data.

---

**Q5.2: What are the security risks of exposing Java stack traces to API consumers?**

They can reveal details about the server's technology stack and library versions, which attackers could use to find known vulnerabilities. They may also expose internal class names, method structures, and even file paths, giving insight into how the application is built. In some cases, stack traces can also include parts of real request data.

To avoid this, a global `ExceptionMapper<Throwable>` is used to return only a generic `500` error message to the client, while the full error details are safely logged on the server for debugging.

---
