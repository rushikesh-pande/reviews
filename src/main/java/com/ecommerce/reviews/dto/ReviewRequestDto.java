package com.ecommerce.reviews.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewRequestDto {

    @NotNull(message = "Order ID is required")
    private Long orderId;

    @NotNull(message = "Product ID is required")
    private Long productId;

    @NotNull(message = "Customer ID is required")
    private Long customerId;

    @NotBlank(message = "Customer name is required")
    private String customerName;

    @Min(value = 1, message = "Rating must be between 1 and 5")
    @Max(value = 5, message = "Rating must be between 1 and 5")
    @NotNull(message = "Star rating is required")
    private Integer starRating;

    @NotBlank(message = "Review title is required")
    private String title;

    @NotBlank(message = "Review content is required")
    @Size(min = 10, max = 2000, message = "Content must be 10-2000 characters")
    private String content;

    private String photoUrl;
    private String videoUrl;
}
