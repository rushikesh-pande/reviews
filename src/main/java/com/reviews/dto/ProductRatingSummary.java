package com.reviews.dto;

import lombok.*;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class ProductRatingSummary {
    private String productId;
    private double averageRating;
    private long totalReviews;
    private long approvedReviews;
    private int fiveStar;
    private int fourStar;
    private int threeStar;
    private int twoStar;
    private int oneStar;
}
