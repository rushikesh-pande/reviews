package com.reviews.kafka;

import com.reviews.entity.Review;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class ReviewEventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void publishReviewSubmitted(Review review) {
        Map<String, Object> event = buildEvent(review, "REVIEW_SUBMITTED");
        kafkaTemplate.send("review.submitted", review.getProductId(), event);
        log.info("Published review.submitted for product={}", review.getProductId());
    }

    public void publishReviewApproved(Review review) {
        Map<String, Object> event = buildEvent(review, "REVIEW_APPROVED");
        kafkaTemplate.send("review.approved", review.getProductId(), event);
        log.info("Published review.approved for review={}", review.getId());
    }

    public void publishReviewHelpful(Review review) {
        Map<String, Object> event = buildEvent(review, "REVIEW_HELPFUL");
        event.put("helpfulVotes", review.getHelpfulVotes());
        kafkaTemplate.send("review.helpful", review.getProductId(), event);
        log.info("Published review.helpful for review={}", review.getId());
    }

    private Map<String, Object> buildEvent(Review review, String eventType) {
        Map<String, Object> event = new HashMap<>();
        event.put("eventType", eventType);
        event.put("reviewId", review.getId());
        event.put("orderId", review.getOrderId());
        event.put("customerId", review.getCustomerId());
        event.put("productId", review.getProductId());
        event.put("rating", review.getRating());
        event.put("verifiedPurchase", review.isVerifiedPurchase());
        event.put("timestamp", System.currentTimeMillis());
        return event;
    }
}
