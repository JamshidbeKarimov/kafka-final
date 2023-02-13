package com.epam.kafka.kafkafinal.config;

import com.epam.kafka.kafkafinal.model.VehicleSignal;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

@Configuration
public class KafkaConfig {

    @Bean
    public KafkaTemplate<String, VehicleSignal> signalKafkaTemplate(
            ProducerFactory<String, VehicleSignal> producerFactory,
            ConcurrentKafkaListenerContainerFactory<String, VehicleSignal> listenerFactory) {

        KafkaTemplate<String, VehicleSignal> template = new KafkaTemplate<>(producerFactory);
        listenerFactory.getContainerProperties().setMissingTopicsFatal(false);
        listenerFactory.setReplyTemplate(template);

        return template;
    }
}
