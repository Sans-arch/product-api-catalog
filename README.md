# Product Catalog API

A RESTful API for managing a product catalog, built with Spring Boot. Features persistent storage with PostgreSQL and Redis caching.

## Tech Stack

- **Java 25** / **Spring Boot 4.0**
- **PostgreSQL** — primary data store
- **Redis** — read-through cache (10-min TTL)
- **Docker Compose** — local infrastructure
- **TestContainers** — integration testing

## Getting Started

**Prerequisites:** Java 25, Docker

```bash
# Start PostgreSQL and Redis
docker-compose up -d

# Run the application
./mvnw spring-boot:run
```

The API will be available at `http://localhost:8080`.

## API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/api/products` | List all products |
| `GET` | `/api/products/{id}` | Get product by ID |
| `GET` | `/api/products/sku/{sku}` | Get product by SKU |
| `GET` | `/api/products/category/{category}` | Get products by category |
| `GET` | `/api/products/category/{category}/under/{maxPrice}` | Filter by category and max price |
| `POST` | `/api/products` | Create a product |
| `PUT` | `/api/products/{id}` | Update a product |
| `DELETE` | `/api/products/{id}` | Delete a product |

### Request body (POST / PUT)

```json
{
  "name": "MacBook Pro",
  "category": "Electronics",
  "sku": "MBP-001",
  "price": 1999.99,
  "description": "Apple M3 chip"
}
```

## Running Tests

Integration tests use TestContainers and do not require a running local database.

```bash
./mvnw test
```
