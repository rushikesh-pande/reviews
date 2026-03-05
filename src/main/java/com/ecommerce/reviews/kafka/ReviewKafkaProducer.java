package com.ecommerce.reviews.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ReviewKafkaProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    @Value("${kafka.topic.review-submitted}")
    private String reviewSubmittedTopic;

    @Value("${kafka.topic.review-approved}")
    private String reviewApprovedTopic;

    @Value("${kafka.topic.review-helpful}")
    private String reviewHelpfulTopic;

    public void sendReviewSubmitted(Long reviewId, Long productId) {
        String message = String.format(
            "{\"reviewId\":%d,\"productId\":%d,\"event\":\"REVIEW_SUBMITTED\"}", reviewId, productId);
        kafkaTemplate.send(reviewSubmittedTopic, String.valueOf(reviewId), message);
        log.info("[Kafka] Sent review.submitted → reviewId={}", reviewId);
    }

    public void sendReviewApproved(Long reviewId, Long productId) {
        String message = String.format(
            "{\"reviewId\":%d,\"productId\":%d,\"event\":\"REVIEW_APPROVED\"}", reviewId, productId);
        kafkaTemplate.send(reviewApprovedTopic, String.valueOf(reviewId), message);
        log.info("[Kafka] Sent review.approved → reviewId={}", reviewId);
    }

    public void sendReviewHelpful(Long reviewId, int votes) {
        String message = String.format(
            "{\"reviewId\":%d,\"helpfulVotes\":%d,\"event\":\"REVIEW_HELPFUL\"}", reviewId, votes);
        kafkaTemplate.send(reviewHelpfulTopic, String.valueOf(reviewId), message);
        log.info("[Kafka] Sent review.helpful → reviewId={}", reviewId);
    }
}
