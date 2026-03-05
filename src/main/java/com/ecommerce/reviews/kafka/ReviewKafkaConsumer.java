package com.ecommerce.reviews.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ReviewKafkaConsumer {

    @KafkaListener(topics = "order.completed", groupId = "reviews-group")
    public void onOrderCompleted(ConsumerRecord<String, String> record) {
        log.info("[Kafka] Received order.completed — triggering post-purchase review request. Payload: {}", record.value());
        // TODO: send email/push notification to customer requesting review
    }
}
