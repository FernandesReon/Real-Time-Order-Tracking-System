package com.reon.order_backend.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {
    @Bean
    public NewTopic orderTopic() {
        return TopicBuilder
                .name("order_event")
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic orderUpdateTopic() {
        return TopicBuilder
                .name("order_update_event")
                .partitions(3)
                .replicas(1)
                .build();
    }
}
