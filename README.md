# Reviews Microservice — Enhancement 13

**Product Reviews & Ratings** — Spring Boot 3.2.2, Java 17, Kafka

## Features
- Star ratings (1-5), verified purchase badge, photo/video URLs
- Helpful voting, review moderation (approve/reject), product rating summary

## Kafka Topics
| Topic | Trigger |
|-------|---------|
| `review.submitted` | New review posted |
| `review.approved`  | Moderator approves |
| `review.helpful`   | Helpful vote cast  |

## API
| Method | Path | Description |
|--------|------|-------------|
| POST | `/api/reviews` | Submit review |
| GET | `/api/reviews/product/{id}` | Product reviews |
| GET | `/api/reviews/product/{id}/summary` | Rating summary |
| GET | `/api/reviews/pending` | Pending moderation |
| PUT | `/api/reviews/{id}/moderate?approve=true` | Moderate |
| PUT | `/api/reviews/{id}/helpful?helpful=true` | Vote helpful |

## Run
```bash
mvn spring-boot:run
```
