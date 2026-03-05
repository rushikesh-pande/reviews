package com.ecommerce.reviews.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HelpfulVoteDto {
    private Long reviewId;
    private Long customerId;
}
