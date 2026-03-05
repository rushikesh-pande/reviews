package com.reviews.dto;

import com.reviews.entity.Review;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class ReviewResponse {
    private Long id;
    private String orderId;
    private String customerId;
    private String productId;
    private int rating;
    private String title;
    private String comment;
    private boolean verifiedPurchase;
    private boolean approved;
    private int helpfulVotes;
    private int totalVotes;
    private Review.ReviewStatus status;
    private List<String> mediaUrls;
    private LocalDateTime createdAt;

    public static ReviewResponse from(Review r) {
        return ReviewResponse.builder()
            .id(r.getId()).orderId(r.getOrderId())
            .customerId(r.getCustomerId()).productId(r.getProductId())
            .rating(r.getRating()).title(r.getTitle())
            .comment(r.getComment()).verifiedPurchase(r.isVerifiedPurchase())
            .approved(r.isApproved()).helpfulVotes(r.getHelpfulVotes())
            .totalVotes(r.getTotalVotes()).status(r.getStatus())
            .mediaUrls(r.getMediaUrls()).createdAt(r.getCreatedAt()).build();
    }
}
