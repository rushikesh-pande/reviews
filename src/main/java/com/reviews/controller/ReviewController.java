package com.reviews.controller;
import com.reviews.dto.*;
import com.reviews.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController @RequestMapping("/api/reviews") @RequiredArgsConstructor
public class ReviewController {
    private final ReviewService service;

    @PostMapping
    public ResponseEntity<ReviewResponse> submit(@Valid @RequestBody ReviewRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.submitReview(req));
    }
    @GetMapping("/product/{productId}")
    public ResponseEntity<List<ReviewResponse>> getProductReviews(@PathVariable String productId) {
        return ResponseEntity.ok(service.getProductReviews(productId));
    }
    @GetMapping("/product/{productId}/summary")
    public ResponseEntity<ProductRatingSummary> getSummary(@PathVariable String productId) {
        return ResponseEntity.ok(service.getRatingSummary(productId));
    }
    @GetMapping("/pending")
    public ResponseEntity<List<ReviewResponse>> getPending() { return ResponseEntity.ok(service.getPendingReviews()); }
    @PutMapping("/{id}/moderate")
    public ResponseEntity<ReviewResponse> moderate(@PathVariable Long id, @RequestParam boolean approve) {
        return ResponseEntity.ok(service.moderate(id, approve));
    }
    @PutMapping("/{id}/helpful")
    public ResponseEntity<ReviewResponse> helpful(@PathVariable Long id, @RequestParam boolean helpful) {
        return ResponseEntity.ok(service.voteHelpful(id, helpful));
    }
}
