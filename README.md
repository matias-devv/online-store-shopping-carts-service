# 🛒 Shopping Carts Service

<br>

## Overview
```shopping-carts-service``` is a core microservice responsible for managing shopping carts within a distributed e-commerce system.
 It operates **prior to the sales process**, handling cart creation, product aggregation, and total price calculation while coordinating with other services in a resilient way.
This service was intentionally designed to **avoid inconsistent carts**, even in partial failure scenarios.


<br>

## 🧰 Technologies
- Java 17


- Spring Boot 4


- Spring Data JPA


- Spring Cloud (Eureka, OpenFeign, LoadBalancer)


- Resilience4j (Circuit Breaker + Retry)


- MySQL


- Swagger / OpenAPI (Springdoc)


- Maven




<br>

## ✨ Key Features
- Create shopping carts associated with a valid user


- Assign products to a cart using **product code + quantity only**


- Automatically calculate and update the total cart price


- Accumulate product quantities if the product already exists in the cart


- Support **multiple active carts per user**


- Validate user existence before cart creation


- Enrich cart products with **name and unit price** via ```products-service```


- Resilient inter-service communication using Circuit Breaker and Retry


- Can run **standalone** or behind an **API Gateway**




<br>

## 🔄 Business Flow (How It Works)
1. A request to create a shopping cart is received with:


- ```id_user```


- List of products (```code + quantity```)


2. The service validates that the user exists (```users-service```)


3. Product details (```name, single_price```) are fetched from ```products-service```


4. The cart:


- Accumulates quantities if a product already exists


- Adds new products if they were not previously present


5. ```total_price``` is calculated and updated internally


6. The shopping cart ID is associated with the user


7. If a dependent service is unavailable:


- The operation fails safely


- No inconsistent cart is persisted



<br>

## 📦 Product Input Format

Only **product code and quantity** are required in the request.
 Price and name are resolved internally via business logic.
```
{
  "id_user": 12,
  "products": [
    { "code": 1, "quantity": 2 },
    { "code": 4, "quantity": 2 }
  ]
}
```


<br>

## 🔗 Service Dependencies
This service has **explicit and intentional dependencies**:
* **users-service**

   - Validate user existence

   - Associate shopping cart IDs with the user


* **products-service**

  - Retrieve product name and unit price


* **eureka-service**

  - Service discovery


* **api-gateway** (optional)

  - Centralized routing in a full microservices setup


No other hidden or implicit dependencies exist.

---

<br>

## 🧠 What I Learned
- Applying MVC architecture in a real microservices environment


- Designing a focused CRUD service with clear responsibility boundaries


- Coordinating data consistency across multiple services


- Using Circuit Breaker and Retry to prevent cascading failures


- Understanding how microservices communicate beyond theory


- Although technically simple, this service was key to **cementing core backend principles** that scale in distributed systems.


<br>

## 🚀 Possible Improvements (Out of Scope for Now)
- Global exception handling strategy


- Authentication and authorization


- Unit and integration testing (Testcontainers / mocks)


- Stock validation and reservation


- Payments, promotions, taxes


These were intentionally excluded to keep the service focused and cohesive.

<br>

## ▶️ How to Run the Project


### Required Databases

Before starting the services, the following MySQL databases must exist:

- ```users_service```

- ```products_service```

- ```shopping_carts_service```

<br>

Each database is used by its corresponding microservice.
**The services do not create databases automatically.**

---

<br>

### Required Services

Before starting ```shopping-carts-service```, the following services must be running:
- ```users-service```


- ```products-service```


- ```eureka-service```


- ```api-gateway``` (optional but recommended)


Each service must be properly registered in Eureka.


---

<br>

## Requirements
- Java 17


- Maven


- MySQL


- Eureka Server running at:
```
http://localhost:8761
```

<br>

## Run the Application


From the project root:

```
mvn spring-boot:run
```


The service will start at:
```
http://localhost:8085
```



<br>

## 🌐 API Access

<br>

## With API Gateway (Recommended)
```
http://localhost:8080/shopping-carts-service/...
```
This is the preferred approach in a full microservices environment.

<br>

## Without API Gateway (Standalone)
```
http://localhost:8085/...
```
Useful for development, testing, or isolated execution.

<br>

## 📘 API Documentation (Swagger)

Once the service is running, Swagger UI is available at:

```
http://localhost:8085/swagger-ui.html
```

All endpoints, request/response schemas, and examples are generated automatically via Springdoc OpenAPI.
