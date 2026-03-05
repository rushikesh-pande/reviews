package com.reviews.controller;

import com.reviews.dto.*;
import com.reviews.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    public ResponseEntity<ReviewResponse> submitReview(@Valid @RequestBody ReviewRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(reviewService.submitReview(request));
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<ReviewResponse>> getProductReviews(@PathVariable String productId) {
        return ResponseEntity.ok(reviewService.getProductReviews(productId));
    }

    @GetMapping("/product/{productId}/summary")
    public ResponseEntity<ProductRatingSummary> getProductRatingSummary(@PathVariable String productId) {
        return ResponseEntity.ok(reviewService.getProductRatingSummary(productId));
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<ReviewResponse>> getCustomerReviews(@PathVariable String customerId) {
        return ResponseEntity.ok(reviewService.getCustomerReviews(customerId));
    }

    @GetMapping("/pending")
    public ResponseEntity<List<ReviewResponse>> getPendingReviews() {
        return ResponseEntity.ok(reviewService.getPendingReviews());
    }

    @PutMapping("/{reviewId}/moderate")
    public ResponseEntity<ReviewResponse> moderateReview(
            @PathVariable Long reviewId,
            @RequestParam boolean approve) {
        return ResponseEntity.ok(reviewService.moderateReview(reviewId, approve));
    }

    @PutMapping("/{reviewId}/helpful")
    public ResponseEntity<ReviewResponse> voteHelpful(
            @PathVariable Long reviewId,
            @RequestParam boolean helpful) {
        return ResponseEntity.ok(reviewService.voteHelpful(reviewId, helpful));
    }
}
