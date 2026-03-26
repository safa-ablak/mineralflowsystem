# 🪨 KdG - Mineral Flow System

💻 **Course Name:** Programming 6   
🧑‍🎓 **Student Name:** Hüseyin Safa Ablak  
📧 **Email Address:** huseyin.ablak@student.kdg.be  
🆔 **Student ID:** 0163388-40  
👥 **Group:** ACS301

---

## 🔍 Project Overview

**Krystal Distribution Group (KdG): Efficient and Seamless Mineral Logistics**

Krystal Distribution Group (**KdG**) specializes in the **efficient and seamless distribution** of essential raw materials, including **gypsum, iron ore, cement, petcoke, and slag**.  
They want to **renew their logistics system**, as it is **outdated and hard to change**.  
They need a new system that can be **adapted easily** and **makes data available for other systems**.

The new logistics system was baptized as **KdG MineralFlow**, and it aims to **optimize the flow of materials** from **arrival scheduling to final warehousing**.

Currently, **KdG** handles the raw materials listed above, but their **state-of-the-art warehouses** are capable of adapting to **all sorts of raw materials**,  
so the system must be **capable of handling these evolutions**.

---

## Architectural Highlights

KdG MineralFlow is built following **Domain-Driven Design (DDD)** principles, ensuring **clear separation of concerns**, **scalability**, and **maintainability**.

### CQRS
The system also applies the **CQRS (Command Query Responsibility Segregation)** pattern to cleanly separate:
- **Commands**, which mutate the domain state.
- **Queries**, which retrieve data from optimized read models.

### Hexagonal Architecture
Each bounded context is structured as a **hexagonal (ports & adapters)** module, keeping the domain and business logic independent of infrastructure concerns:
- **`domain/`** – Entities, value objects, and domain exceptions.
- **`port/in/`** – Inbound ports: commands, queries, and use case interfaces that define what the application can do.
- **`port/out/`** – Outbound ports: interfaces for persistence, messaging, and external services.
- **`core/`** – Use case implementations containing the business logic, depending only on ports.
- **`adapter/in/`** – Inbound adapters: REST controllers and message listeners (RabbitMQ) that drive the application.
- **`adapter/out/`** – Outbound adapters: JPA repositories and event publishers that implement outbound ports.

### Activity-Sourced Ledger (Warehousing Context)
The Warehousing bounded context tracks warehouse stock levels through an **append-only activity ledger** rather than mutable state. Every delivery and shipment is recorded as an immutable entry in the `StockLedger`, and the current balance is derived by replaying these entries on top of a stored snapshot.

Key elements of this approach:
- **StockLedger** – An append-only ledger of `Delivery`, `Shipment`, and `ShipmentAllocation` records. The current balance is calculated by summing activity deltas on top of a `baseBalance` snapshot.
- **Snapshot optimization** – Periodic snapshots collapse the computed balance into a new `baseBalance`, so only activities recorded after the last snapshot need to be replayed.
- **FIFO shipment allocation** – When raw materials are shipped, the system allocates from the oldest unshipped deliveries first. `ShipmentAllocation` records link each shipment back to the specific deliveries it drew from, providing full traceability.
- **Tailored loading strategies** – The database adapter provides multiple reconstruction paths depending on the use case: recent activities only (for balance queries), all activities with optional Delivery-Shipment traceability (for drill-down views), all deliveries with allocations (for FIFO shipment recording and storage reporting), and time-windowed activities (for net balance change reports).

---

## 📑 Table of Contents

1. 🚀 [Getting Started](#getting-started)
2. 🗂️ [Bounded Contexts](#bounded-contexts)
3. 🧩 [Domain Models](#domain-models)
4. 🧪 [Testing](#testing)
5. 👤 [Users and Roles](#users-and-roles)
6. 🔬 [Command, Query & Event Catalogs](#command-query--event-catalogs)
7. ⏰ [Scheduled Task Catalog](#scheduled-task-catalog)
8. 📝 [Notes and Assumptions Made](#notes-and-assumptions-made)
9. 📚 [Implemented User Stories](#implemented-user-stories)
10. ⚖️ [License](#license)

---

## Getting Started

### Prerequisites
- Java 21+
- Gradle
- Docker & Docker Compose
- Keycloak

### Setup

1. Clone the repository.
2. Run [`docker-compose.yml`](infrastructure/docker-compose.yml) – starts **MySQL**, **RabbitMQ**, and **Keycloak** for local development.
On first startup, the following are auto-imported:
   - The database init script ([`init.sql`](infrastructure/mysql/init.sql)) creates the required schemas and grants privileges.
   - The Keycloak realm ([`mineralflow-realm.json`](infrastructure/mineralflow-realm.json)) with all clients, users, roles, and credentials.
3. Run [`MineralFlowApplication.java`](application/src/main/java/be/kdg/prog6/MineralFlowApplication.java)
4. Test the APIs using the provided HTTP files in the [`api`](api) folder

> **Note:** If you change `init.sql` or encounter database issues, delete `infrastructure/mysql/data` and restart the containers.

---

## Bounded Contexts

### 🚚 Landside Context 

- Schedule truck arrivals to ensure efficient processing and avoid congestion.
- Maintain a log of scheduled and actual arrival times.
- Maintain a log of departures.
- Record the weight of trucks arriving and leaving the warehouse.
- Calculate the net weight of raw materials delivered.

### 📦 Warehousing Context

- Manage warehouse information, including storage capacity and raw material stock levels.
- Maintain records of raw material types and quantities stored.

### 🚢 Waterside Context

- Track outgoing shipments.
- Track Inspection and Bunkering Operations.
- Match Shipping Orders with Purchase Orders.

### 🧾 Invoicing Context

- Calculate storage fee each day per customer.
- Calculate commission fee each day per customer.
- At request invoicing to customers (invoice outstanding credit).

---

## Domain Models

For the full list of domain model classes and their responsibilities, see the separate file: [Domain Models](docs/domain-models.md)

---

## Testing

- To run tests, run `./gradlew clean test` in the root directory of the project.

---

## Users and Roles

All users below are automatically provisioned via the Keycloak realm import on first startup – no manual setup required.

| **Role**          | **Username** | **Email**                      | **Password**      |
|-------------------|--------------|--------------------------------|-------------------|
| admin             | adam         | adamanderson@email.com         | admin             |
| -                 | whoami       | whoami@unknown.kdg.be          | unknown           |
| seller            | safa         | huseyin.ablak@student.kdg.be   | seller            |  
| seller            | noah         | noah.guerin@student.kdg.be     | seller            |  
| seller            | alperen      | alperen.doganci@student.kdg.be | seller            |  
| truck_driver      | tyler        | tylerdavis@email.com           | truck_driver      |
| warehouse_manager | william      | williammartin@email.com        | warehouse_manager |
| buyer             | thomas       | thomas.maxwell@kdg.be          | buyer             | 
| buyer             | raoul        | raoul.vandenberge@kdg.be       | buyer             | 
| buyer             | kevin        | kevin.smeyers@kdg.be           | buyer             | 
| buyer             | bart         | bart.vochten@kdg.be            | buyer             | 
| ship_captain      | steve        | stevecollins@email.com         | ship_captain      |
| inspector         | isaac        | isaacingram@email.com          | inspector         |
| bunkering_officer | ben          | benowens@email.com             | bunkering_officer |
| foreman           | franklin     | franklinfoster@email.com       | foreman           |
| accountant        | alex         | alexackerman@email.com         | accountant        |

---

## Command, Query & Event Catalogs

For the full list of commands, queries and events (by bounded context), see the separate catalogs file: [Command, Query & Event Catalogs](docs/command-query-event-catalogs.md)

---

## Scheduled Task Catalog

For the full list of scheduled tasks, see the separate catalog file: [Scheduled Task Catalog](docs/scheduled-task-catalog.md)

---

## Notes and Assumptions Made

To see notes and assumptions made in the project, see the separate file: [Notes and Assumptions Made](docs/notes-and-assumptions-made.md)

---

## Implemented User Stories

For the full backlog and user stories, see the [Issue Board](https://github.com/safa-ablak/mineralflowsystem/issues).

---

## License

This project was developed as part of the **Programming 6** course at Karel de Grote Hogeschool.  
It is released under the [MIT License](LICENSE.txt) for educational and demonstration purposes.

© 2025 Hüseyin Safa Ablak

---