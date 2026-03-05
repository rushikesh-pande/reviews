package com.ecommerce.reviews;

import com.ecommerce.reviews.dto.ReviewRequestDto;
import com.ecommerce.reviews.dto.ReviewResponseDto;
import com.ecommerce.reviews.entity.ReviewStatus;
import com.ecommerce.reviews.exception.ReviewNotFoundException;
import com.ecommerce.reviews.kafka.ReviewKafkaProducer;
import com.ecommerce.reviews.repository.ReviewRepository;
import com.ecommerce.reviews.service.ReviewService;
import com.ecommerce.reviews.entity.Review;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {

    @Mock ReviewRepository reviewRepository;
    @Mock ReviewKafkaProducer kafkaProducer;
    @InjectMocks ReviewService reviewService;

    @Test
    void submitReview_ShouldReturnPendingStatus() {
        ReviewRequestDto dto = ReviewRequestDto.builder()
                .orderId(1L).productId(2L).customerId(3L)
                .customerName("Test User").starRating(5)
                .title("Great product!").content("Really loved this product, highly recommend.")
                .build();

        Review saved = Review.builder()
                .id(1L).orderId(1L).productId(2L).customerId(3L)
                .customerName("Test User").starRating(5)
                .title("Great product!").content("Really loved this product, highly recommend.")
                .status(ReviewStatus.PENDING).verifiedPurchase(true).helpfulVotes(0)
                .build();

        when(reviewRepository.save(any(Review.class))).thenReturn(saved);
        doNothing().when(kafkaProducer).sendReviewSubmitted(anyLong(), anyLong());

        ReviewResponseDto response = reviewService.submitReview(dto);

        assertNotNull(response);
        assertEquals(ReviewStatus.PENDING, response.getStatus());
        assertEquals(5, response.getStarRating());
        verify(kafkaProducer).sendReviewSubmitted(1L, 2L);
    }

    @Test
    void approveReview_ShouldChangeStatusToApproved() {
        Review review = Review.builder().id(1L).productId(2L).status(ReviewStatus.PENDING).build();
        when(reviewRepository.findById(1L)).thenReturn(Optional.of(review));
        when(reviewRepository.save(any())).thenReturn(review);
        doNothing().when(kafkaProducer).sendReviewApproved(anyLong(), anyLong());

        ReviewResponseDto response = reviewService.approveReview(1L);
        assertEquals(ReviewStatus.APPROVED, response.getStatus());
    }

    @Test
    void approveReview_WhenNotFound_ShouldThrow() {
        when(reviewRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ReviewNotFoundException.class, () -> reviewService.approveReview(99L));
    }

    @Test
    void voteHelpful_ShouldIncrementVotes() {
        Review review = Review.builder().id(1L).productId(2L).helpfulVotes(3).status(ReviewStatus.APPROVED).build();
        when(reviewRepository.findById(1L)).thenReturn(Optional.of(review));
        when(reviewRepository.save(any())).thenReturn(review);
        doNothing().when(kafkaProducer).sendReviewHelpful(anyLong(), anyInt());

        ReviewResponseDto response = reviewService.voteHelpful(1L);
        assertEquals(4, response.getHelpfulVotes());
    }
}
