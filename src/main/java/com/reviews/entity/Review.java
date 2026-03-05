package com.reviews.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "reviews")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String orderId;

    @NotBlank
    private String customerId;

    @NotBlank
    private String productId;

    @Min(1) @Max(5)
    private int rating;

    private String title;

    @Size(max = 2000)
    private String comment;

    private boolean verifiedPurchase;

    private boolean approved;

    private int helpfulVotes;

    private int totalVotes;

    @Enumerated(EnumType.STRING)
    private ReviewStatus status;

    @ElementCollection
    private java.util.List<String> mediaUrls;  // photo/video URLs

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public enum ReviewStatus {
        PENDING, APPROVED, REJECTED, FLAGGED
    }
}
