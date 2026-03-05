package com.ecommerce.reviews.controller;

import com.ecommerce.reviews.dto.*;
import com.ecommerce.reviews.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    /**
     * POST /api/reviews — Submit a new review
     */
    @PostMapping
    public ResponseEntity<ReviewResponseDto> submitReview(@Valid @RequestBody ReviewRequestDto dto) {
        ReviewResponseDto response = reviewService.submitReview(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * GET /api/reviews/product/{productId} — Get approved reviews for a product
     */
    @GetMapping("/product/{productId}")
    public ResponseEntity<List<ReviewResponseDto>> getProductReviews(@PathVariable Long productId) {
        return ResponseEntity.ok(reviewService.getApprovedReviewsForProduct(productId));
    }

    /**
     * GET /api/reviews/product/{productId}/rating — Get average star rating
     */
    @GetMapping("/product/{productId}/rating")
    public ResponseEntity<Map<String, Object>> getProductRating(@PathVariable Long productId) {
        return ResponseEntity.ok(Map.of(
            "productId", productId,
            "averageRating", reviewService.getAverageRating(productId),
            "reviewCount", reviewService.getReviewCount(productId)
        ));
    }

    /**
     * GET /api/reviews/pending — Get reviews pending moderation
     */
    @GetMapping("/pending")
    public ResponseEntity<List<ReviewResponseDto>> getPendingReviews() {
        return ResponseEntity.ok(reviewService.getPendingReviews());
    }

    /**
     * PUT /api/reviews/{id}/approve — Approve a review (moderation)
     */
    @PutMapping("/{id}/approve")
    public ResponseEntity<ReviewResponseDto> approveReview(@PathVariable Long id) {
        return ResponseEntity.ok(reviewService.approveReview(id));
    }

    /**
     * PUT /api/reviews/{id}/reject — Reject a review (moderation)
     */
    @PutMapping("/{id}/reject")
    public ResponseEntity<ReviewResponseDto> rejectReview(@PathVariable Long id) {
        return ResponseEntity.ok(reviewService.rejectReview(id));
    }

    /**
     * POST /api/reviews/{id}/helpful — Vote review as helpful
     */
    @PostMapping("/{id}/helpful")
    public ResponseEntity<ReviewResponseDto> voteHelpful(@PathVariable Long id) {
        return ResponseEntity.ok(reviewService.voteHelpful(id));
    }

    /**
     * GET /api/reviews/customer/{customerId} — Get reviews by customer
     */
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<ReviewResponseDto>> getCustomerReviews(@PathVariable Long customerId) {
        return ResponseEntity.ok(reviewService.getReviewsByCustomer(customerId));
    }
}
