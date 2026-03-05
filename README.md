# Reviews & Ratings Microservice

## Overview
Spring Boot microservice for post-purchase product reviews and ratings.

## Features
- Post-purchase review requests (triggered via Kafka `order.completed`)
- Star ratings (1–5)
- Photo/video review support
- Verified purchase badge
- Helpful review voting
- Review moderation (approve/reject)

## Kafka Topics
| Topic              | Direction | Description                    |
|--------------------|-----------|--------------------------------|
| `order.completed`  | CONSUME   | Triggers review request        |
| `review.submitted` | PRODUCE   | New review submitted            |
| `review.approved`  | PRODUCE   | Review approved by moderator   |
| `review.helpful`   | PRODUCE   | Review voted as helpful        |

## API Endpoints
| Method | Path                                    | Description                    |
|--------|-----------------------------------------|--------------------------------|
| POST   | `/api/reviews`                          | Submit a new review            |
| GET    | `/api/reviews/product/{productId}`      | Get approved product reviews   |
| GET    | `/api/reviews/product/{productId}/rating` | Get average rating           |
| GET    | `/api/reviews/pending`                  | Get reviews pending moderation |
| PUT    | `/api/reviews/{id}/approve`             | Approve a review               |
| PUT    | `/api/reviews/{id}/reject`              | Reject a review                |
| POST   | `/api/reviews/{id}/helpful`             | Vote review as helpful         |
| GET    | `/api/reviews/customer/{customerId}`    | Get reviews by customer        |

## Tech Stack
- Java 17
- Spring Boot 3.2.2
- Spring Data JPA / H2 (dev)
- Spring Kafka
- Lombok
- Maven

## Running Locally
```bash
mvn spring-boot:run
```
Server starts on port **8087**.
