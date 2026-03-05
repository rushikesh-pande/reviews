package com.ecommerce.reviews.dto;

import com.ecommerce.reviews.entity.ReviewStatus;
import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewResponseDto {
    private Long id;
    private Long orderId;
    private Long productId;
    private Long customerId;
    private String customerName;
    private Integer starRating;
    private String title;
    private String content;
    private String photoUrl;
    private String videoUrl;
    private boolean verifiedPurchase;
    private int helpfulVotes;
    private ReviewStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime approvedAt;
}
