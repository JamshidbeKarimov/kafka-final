package com.epam.kafka.kafkafinal.service;

import com.epam.kafka.kafkafinal.model.DistanceInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

@Service
public class DistanceLoggingService {
    private static final Logger log = LoggerFactory.getLogger(DistanceLoggingService.class);

    @KafkaListener(topics = "${kafka.distance.log.topic}", groupId = "${kafka.distance.log.group.id}")
    public void processSignal(DistanceInfo vehicleLog, Acknowledgment acknowledgment) {
        // at most once consumer
        acknowledgment.acknowledge();
        log.info("Total Distance for vehicle : {} is {}.", vehicleLog.getVehicleId(),
                vehicleLog.getDistance().toString());
    }
}
