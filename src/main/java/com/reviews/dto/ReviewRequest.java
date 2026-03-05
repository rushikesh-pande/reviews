package com.reviews.dto;
import jakarta.validation.constraints.*;
import lombok.*;
import java.util.List;
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class ReviewRequest {
    @NotBlank private String orderId;
    @NotBlank private String customerId;
    @NotBlank private String productId;
    @Min(1) @Max(5) private int rating;
    private String title;
    @Size(max=2000) private String comment;
    private List<String> mediaUrls;
}
