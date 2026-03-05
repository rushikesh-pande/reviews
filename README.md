# Reviews Microservice

## Enhancement 13 — Product Reviews & Ratings

Production-ready Spring Boot 3.2.2 microservice for managing product reviews and ratings.

### Features
- ✅ Post-purchase review submission
- ✅ Star ratings (1–5)
- ✅ Photo/video review URLs
- ✅ Verified purchase badge
- ✅ Helpful review voting
- ✅ Review moderation (approve/reject)
- ✅ Product rating summary with breakdown
- ✅ Kafka event publishing

### Kafka Topics
| Topic | Description |
|-------|-------------|
| `review.submitted` | New review submitted by customer |
| `review.approved` | Review approved by moderator |
| `review.helpful` | Review marked as helpful |

### API Endpoints
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/reviews` | Submit a review |
| GET | `/api/reviews/product/{productId}` | Get product reviews |
| GET | `/api/reviews/product/{productId}/summary` | Rating summary |
| GET | `/api/reviews/customer/{customerId}` | Customer reviews |
| GET | `/api/reviews/pending` | Pending moderation |
| PUT | `/api/reviews/{id}/moderate?approve=true` | Moderate review |
| PUT | `/api/reviews/{id}/helpful?helpful=true` | Vote helpful |

### Tech Stack
- Java 17, Spring Boot 3.2.2, Maven
- Spring Data JPA + H2
- Spring Kafka
- Lombok, Bean Validation

### Run
```bash
mvn spring-boot:run
```
