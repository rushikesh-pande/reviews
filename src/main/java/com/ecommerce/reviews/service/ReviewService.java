package com.ecommerce.reviews.service;

import com.ecommerce.reviews.dto.*;
import com.ecommerce.reviews.entity.*;
import com.ecommerce.reviews.exception.ReviewNotFoundException;
import com.ecommerce.reviews.kafka.ReviewKafkaProducer;
import com.ecommerce.reviews.repository.ReviewRepository;
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
    private final ReviewKafkaProducer kafkaProducer;

    @Transactional
    public ReviewResponseDto submitReview(ReviewRequestDto dto) {
        log.info("Submitting review for orderId={}, productId={}", dto.getOrderId(), dto.getProductId());

        Review review = Review.builder()
                .orderId(dto.getOrderId())
                .productId(dto.getProductId())
                .customerId(dto.getCustomerId())
                .customerName(dto.getCustomerName())
                .starRating(dto.getStarRating())
                .title(dto.getTitle())
                .content(dto.getContent())
                .photoUrl(dto.getPhotoUrl())
                .videoUrl(dto.getVideoUrl())
                .verifiedPurchase(isVerifiedPurchase(dto.getOrderId(), dto.getCustomerId()))
                .status(ReviewStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .build();

        Review saved = reviewRepository.save(review);
        kafkaProducer.sendReviewSubmitted(saved.getId(), saved.getProductId());
        return mapToDto(saved);
    }

    public List<ReviewResponseDto> getApprovedReviewsForProduct(Long productId) {
        return reviewRepository.findByProductIdAndStatus(productId, ReviewStatus.APPROVED)
                .stream().map(this::mapToDto).collect(Collectors.toList());
    }

    public Double getAverageRating(Long productId) {
        Double avg = reviewRepository.findAverageRatingByProductId(productId);
        return avg != null ? avg : 0.0;
    }

    public Long getReviewCount(Long productId) {
        return reviewRepository.countApprovedByProductId(productId);
    }

    @Transactional
    public ReviewResponseDto approveReview(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ReviewNotFoundException(reviewId));
        review.setStatus(ReviewStatus.APPROVED);
        review.setApprovedAt(LocalDateTime.now());
        Review saved = reviewRepository.save(review);
        kafkaProducer.sendReviewApproved(saved.getId(), saved.getProductId());
        return mapToDto(saved);
    }

    @Transactional
    public ReviewResponseDto rejectReview(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ReviewNotFoundException(reviewId));
        review.setStatus(ReviewStatus.REJECTED);
        return mapToDto(reviewRepository.save(review));
    }

    @Transactional
    public ReviewResponseDto voteHelpful(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ReviewNotFoundException(reviewId));
        review.setHelpfulVotes(review.getHelpfulVotes() + 1);
        Review saved = reviewRepository.save(review);
        kafkaProducer.sendReviewHelpful(saved.getId(), saved.getHelpfulVotes());
        return mapToDto(saved);
    }

    public List<ReviewResponseDto> getPendingReviews() {
        return reviewRepository.findByStatus(ReviewStatus.PENDING)
                .stream().map(this::mapToDto).collect(Collectors.toList());
    }

    public List<ReviewResponseDto> getReviewsByCustomer(Long customerId) {
        return reviewRepository.findByCustomerId(customerId)
                .stream().map(this::mapToDto).collect(Collectors.toList());
    }

    private boolean isVerifiedPurchase(Long orderId, Long customerId) {
        // In production: call OrderService to verify the customer actually purchased
        return orderId != null && customerId != null;
    }

    private ReviewResponseDto mapToDto(Review r) {
        return ReviewResponseDto.builder()
                .id(r.getId())
                .orderId(r.getOrderId())
                .productId(r.getProductId())
                .customerId(r.getCustomerId())
                .customerName(r.getCustomerName())
                .starRating(r.getStarRating())
                .title(r.getTitle())
                .content(r.getContent())
                .photoUrl(r.getPhotoUrl())
                .videoUrl(r.getVideoUrl())
                .verifiedPurchase(r.isVerifiedPurchase())
                .helpfulVotes(r.getHelpfulVotes())
                .status(r.getStatus())
                .createdAt(r.getCreatedAt())
                .approvedAt(r.getApprovedAt())
                .build();
    }
}
