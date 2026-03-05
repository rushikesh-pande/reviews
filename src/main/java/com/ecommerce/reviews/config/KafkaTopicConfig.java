package com.ecommerce.reviews.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    @Value("${kafka.topic.review-submitted}")
    private String reviewSubmittedTopic;

    @Value("${kafka.topic.review-approved}")
    private String reviewApprovedTopic;

    @Value("${kafka.topic.review-helpful}")
    private String reviewHelpfulTopic;

    @Bean
    public NewTopic reviewSubmittedTopic() {
        return TopicBuilder.name(reviewSubmittedTopic).partitions(3).replicas(1).build();
    }

    @Bean
    public NewTopic reviewApprovedTopic() {
        return TopicBuilder.name(reviewApprovedTopic).partitions(3).replicas(1).build();
    }

    @Bean
    public NewTopic reviewHelpfulTopic() {
        return TopicBuilder.name(reviewHelpfulTopic).partitions(3).replicas(1).build();
    }
}
