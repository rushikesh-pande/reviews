package com.reviews.service;

import com.reviews.dto.*;
import com.reviews.entity.Review;
import com.reviews.exception.ReviewException;
import com.reviews.kafka.ReviewEventProducer;
import com.reviews.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ReviewEventProducer eventProducer;

    @Transactional
    public ReviewResponse submitReview(ReviewRequest request) {
        if (reviewRepository.existsByOrderIdAndCustomerIdAndProductId(
                request.getOrderId(), request.getCustomerId(), request.getProductId())) {
            throw new ReviewException("Review already submitted for this order and product");
        }

        Review review = Review.builder()
            .orderId(request.getOrderId())
            .customerId(request.getCustomerId())
            .productId(request.getProductId())
            .rating(request.getRating())
            .title(request.getTitle())
            .comment(request.getComment())
            .verifiedPurchase(true)  // assume verified purchase
            .approved(false)
            .helpfulVotes(0)
            .totalVotes(0)
            .status(Review.ReviewStatus.PENDING)
            .mediaUrls(request.getMediaUrls())
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();

        review = reviewRepository.save(review);
        log.info("Review submitted: id={}, product={}, rating={}", review.getId(), review.getProductId(), review.getRating());
        eventProducer.publishReviewSubmitted(review);

        return ReviewResponse.from(review);
    }

    @Transactional
    public ReviewResponse moderateReview(Long reviewId, boolean approve) {
        Review review = reviewRepository.findById(reviewId)
            .orElseThrow(() -> new ReviewException("Review not found: " + reviewId));

        review.setApproved(approve);
        review.setStatus(approve ? Review.ReviewStatus.APPROVED : Review.ReviewStatus.REJECTED);
        review.setUpdatedAt(LocalDateTime.now());
        review = reviewRepository.save(review);

        if (approve) {
            eventProducer.publishReviewApproved(review);
        }
        log.info("Review {} {}: id={}", approve ? "approved" : "rejected", reviewId, review.getProductId());
        return ReviewResponse.from(review);
    }

    @Transactional
    public ReviewResponse voteHelpful(Long reviewId, boolean helpful) {
        Review review = reviewRepository.findById(reviewId)
            .orElseThrow(() -> new ReviewException("Review not found: " + reviewId));

        review.setTotalVotes(review.getTotalVotes() + 1);
        if (helpful) {
            review.setHelpfulVotes(review.getHelpfulVotes() + 1);
            eventProducer.publishReviewHelpful(review);
        }
        review.setUpdatedAt(LocalDateTime.now());
        return ReviewResponse.from(reviewRepository.save(review));
    }

    public List<ReviewResponse> getProductReviews(String productId) {
        return reviewRepository.findByProductIdAndApprovedTrue(productId)
            .stream().map(ReviewResponse::from).collect(Collectors.toList());
    }

    public ProductRatingSummary getProductRatingSummary(String productId) {
        List<Review> reviews = reviewRepository.findByProductIdAndApprovedTrue(productId);
        double avg = reviews.stream().mapToInt(Review::getRating).average().orElse(0.0);

        return ProductRatingSummary.builder()
            .productId(productId)
            .averageRating(Math.round(avg * 10.0) / 10.0)
            .totalReviews(reviewRepository.countApprovedByProductId(productId))
            .approvedReviews(reviews.size())
            .fiveStar((int) reviews.stream().filter(r -> r.getRating() == 5).count())
            .fourStar((int) reviews.stream().filter(r -> r.getRating() == 4).count())
            .threeStar((int) reviews.stream().filter(r -> r.getRating() == 3).count())
            .twoStar((int) reviews.stream().filter(r -> r.getRating() == 2).count())
            .oneStar((int) reviews.stream().filter(r -> r.getRating() == 1).count())
            .build();
    }

    public List<ReviewResponse> getPendingReviews() {
        return reviewRepository.findByStatus(Review.ReviewStatus.PENDING)
            .stream().map(ReviewResponse::from).collect(Collectors.toList());
    }

    public List<ReviewResponse> getCustomerReviews(String customerId) {
        return reviewRepository.findByCustomerId(customerId)
            .stream().map(ReviewResponse::from).collect(Collectors.toList());
    }
}
