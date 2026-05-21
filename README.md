# SubastasPro - Real-time Auction Platform

A full-stack auction platform where users bid on products in real time. Built to
demonstrate distributed systems patterns used in production environments: CDC,
event streaming, OLAP analytics, and WebSocket-based live updates.

## Architecture

```
┌─────────────┐     HTTP/WS     ┌──────────────────┐     INSERT     ┌───────────┐
│  Vue 3      │ ──────────────► │  subasta-api      │ ────────────► │   MySQL   │
│  Frontend   │                 │  Spring Boot 4    │               │  (OLTP)   │
└─────────────┘                 └──────────────────┘               └─────┬─────┘
                                                                          │
                                                                    Debezium CDC
                                                                    (Phase 3)
                                                                          │
                                                                    ┌─────▼─────┐
                                                                    │   Kafka   │
                                                                    └─────┬─────┘
                                                              ┌───────────┴───────────┐
                                                        ┌─────▼─────┐         ┌──────▼──────┐
                                                        │ subasta-  │         │ ClickHouse  │
                                                        │ streamer  │         │   (OLAP)    │
                                                        │ WebSocket │         └──────┬──────┘
                                                        └───────────┘                │
                                                                               ┌─────▼─────┐
                                                                               │  Grafana  │
                                                                               └───────────┘
```

## Stack

| Layer           | Technology                                |
| --------------- | ----------------------------------------- |
| Frontend        | Vue 3 + Vite + Pinia + Tailwind CSS       |
| Backend API     | Java 21 + Spring Boot 4 + Spring Data JPA |
| Database (OLTP) | MySQL 8.0                                 |
| CDC             | Debezium _(Phase 3)_                      |
| Event Streaming | Kafka / Redpanda _(Phase 2)_              |
| Databse (OLAP)  | ClickHouse _(Phase 3)_                    |
| Dashboards      | Grafana _(Phase 3)_                       |
| Infrastructure  | Docker Compose                            |

## Project Structure

```
subasta-platform/
├── docker-compose.yml       # Infrastructure
├── subasta-api/             # Spring Boot microservice
└── subasta-frontend/        # Vue 3 application
```

## Roadmap

- [x] **Phase 1** - Core auction functionality (OLTP + REST API + Vue frontend)
- [x] **Phase 2** - Real-time updates (Kafka + WebSockets via `subasta-streamer`)
- [ ] **Phase 3** - Analytics pipeline (Debezium CDC + ClickHouse + Grafana dashboard)

## Getting Started

### Prerequisites

- Docker + Docker Compose
- Java 21
- Node.js 20+

### Run

**1 - Start infrastructure:**

```bash
docker compose up -d
```

> **Note:** the `debezium` container may take 40-60 seconds to stabilize after MySQL is
> ready. If it fails on first start, run `docker compose restart debezium`,

**2 - Fix Debezium offset permissions (first time only):**
```bash
chmod 777 config/debezium/data
docker compose restart debezium
```

**3 - Start backend services:**

```bash
# Terminal 1
cd subasta-api
./mvnw spring-boot:run

# Terminal 2
cd subasta-streamer
./mbnw spring-boot:run
```

**4 - Start frontend:**

```bash
cd subasta-frontend
npm install
npm run dev
```

**5 - Open API collection:**

Import the `subastas/` folder in Bruno via `File > Open Collection`.

### Service URLs

| Service | URL |
|---|---|
| Frontend | http://localhost:5173 |
| API | http://localhost:8080 |
| Streamer | http://localhost:8081 |
| Redpanda Console | http://localhost:8090 |
| Grafana | http://localhost:3000 |
| ClickHouse | http://localhost:8123 |

### Known Issues

- **Debezium permissions:** The `config/debezium/data/` directory must have write permissions for the container. Run `chmod 777 config/debezium/data` if Debezium fails to start.
- **MySQL network:** MySQL must be in `subasta-network` for Debezium to resolve the hostname. Verify with `docker network inspect subasta-platform_subasta-network`.
- **Redpanda Connect:** The `mysql_cdc` input requires an Enterprise license. Use Debezium Server instead (already configured).

## API Endpoints

| Method | Endpoint                    | Description          |
| ------ | --------------------------- | -------------------- |
| GET    | `/api/usuarios`             | List all users       |
| POST   | `/api/usuarios`             | Create user          |
| GET    | `/api/productos`            | List active auctions |
| POST   | `/api/productos`            | Create product       |
| GET    | `/api/productos/{id}/pujas` | Get bid history      |
| POST   | `/api/productos/{id}/pujas` | Place a bid          |

## Key Design Decisions

**Why CDC instead of dual writes?** Debezium reads MySQL's binlog directly — the
source of truth. This guarantees zero data loss between the OLTP database
and the analytics pipeline without coupling the application code to Kafka.

**Why ClickHouse for analytics?** Columnar storage makes aggregations over
millions of bid events orders of magnitude faster than MySQL. The native
Kafka engine removes the need for a custom consumer.

**Why two microservices?** `subasta-api` handles write operations and business
rules. `subasta-streamer` handles read/push operations via WebSocket.
Separating them allows independent scaling — the streamer can scale horizontally
as concurrent users grow.
