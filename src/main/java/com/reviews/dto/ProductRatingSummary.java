package com.reviews.dto;
import lombok.*;
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class ProductRatingSummary {
    private String productId;
    private double averageRating;
    private long totalReviews;
    private int fiveStar, fourStar, threeStar, twoStar, oneStar;
}
