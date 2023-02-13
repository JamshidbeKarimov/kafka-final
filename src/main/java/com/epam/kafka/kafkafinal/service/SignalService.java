package com.epam.kafka.kafkafinal.service;

import com.epam.kafka.kafkafinal.model.VehicleSignal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

@Service
public class SignalService {

    private static final Logger log = LoggerFactory.getLogger(SignalService.class);

    private final KafkaTemplate<String, VehicleSignal> kafkaTemplate;

    private final String topic;

    public SignalService(
            @Value("${kafka.signal.topic}") String topic,
            KafkaTemplate<String, VehicleSignal> kafkaTemplate) {
        this.topic = topic;
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendSignal(VehicleSignal signal) {

        log.info("Sending '{}' data to ingestion topic '{}'.", signal.getVehicleId(), topic);
        String key = signal.getVehicleId().toString();
        try {
            var result =
                    kafkaTemplate.send(topic, key, signal)
                            .get();
            log.info("topic: {}, timestamp: {}",
                    result.getRecordMetadata().topic(), result.getRecordMetadata().timestamp());
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
        log.info("Data is published to topic");
    }
}
