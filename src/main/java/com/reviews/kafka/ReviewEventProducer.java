package com.reviews.kafka;
import com.reviews.entity.Review;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.Map;

@Component @RequiredArgsConstructor @Slf4j
public class ReviewEventProducer {
    private final KafkaTemplate<String, Object> kafkaTemplate;
    public void publishSubmitted(Review r) { send("review.submitted", r, "REVIEW_SUBMITTED"); }
    public void publishApproved(Review r)  { send("review.approved",  r, "REVIEW_APPROVED");  }
    public void publishHelpful(Review r)   { send("review.helpful",   r, "REVIEW_HELPFUL");   }
    private void send(String topic, Review r, String type) {
        Map<String,Object> e=new HashMap<>();
        e.put("eventType",type); e.put("reviewId",r.getId());
        e.put("orderId",r.getOrderId()); e.put("customerId",r.getCustomerId());
        e.put("productId",r.getProductId()); e.put("rating",r.getRating());
        e.put("verifiedPurchase",r.isVerifiedPurchase()); e.put("timestamp",System.currentTimeMillis());
        kafkaTemplate.send(topic, r.getProductId(), e);
        log.info("Published {} for product={}", type, r.getProductId());
    }
}
