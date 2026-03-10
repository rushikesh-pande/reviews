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

## 🔒 Security Enhancements

This service implements all 7 security enhancements:

| # | Enhancement | Implementation |
|---|-------------|----------------|
| 1 | **OAuth 2.0 / JWT** | `SecurityConfig.java` — stateless JWT auth, Bearer token validation |
| 2 | **API Rate Limiting** | `RateLimitingFilter.java` — 100 req/min per IP using Bucket4j |
| 3 | **Input Validation** | `InputSanitizer.java` — SQL injection, XSS, command injection prevention |
| 4 | **Data Encryption** | `EncryptionService.java` — AES-256-GCM for sensitive data at rest |
| 5 | **PCI DSS** | `PciDssAuditAspect.java` — Full audit trail for payment operations |
| 6 | **GDPR Compliance** | `GdprDataService.java` — Right to erasure, consent management, data export |
| 7 | **Audit Logging** | `AuditLogService.java` — All transactions logged with user, IP, timestamp |

### Security Endpoints
- `GET /api/v1/audit/recent?limit=100` — Recent audit events (ADMIN only)
- `GET /api/v1/audit/user/{userId}` — User's audit trail (ADMIN or self)
- `GET /api/v1/audit/violations` — Security violations (ADMIN only)

### JWT Authentication
```bash
# Include Bearer token in all requests:
curl -H "Authorization: Bearer <JWT_TOKEN>" http://localhost:8087/api/v1/...
```

### Security Headers Added
- `X-Frame-Options: DENY`
- `X-Content-Type-Options: nosniff`
- `Strict-Transport-Security: max-age=31536000; includeSubDomains`
- `Referrer-Policy: strict-origin-when-cross-origin`
- `X-RateLimit-Remaining: <n>` (on every response)
