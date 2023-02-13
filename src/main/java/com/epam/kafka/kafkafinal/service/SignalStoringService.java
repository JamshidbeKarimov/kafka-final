package com.epam.kafka.kafkafinal.service;

import com.epam.kafka.kafkafinal.model.VehicleInfo;
import com.epam.kafka.kafkafinal.model.VehicleSignal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Service;

@Service
public class SignalStoringService {
        private static final Logger log = LoggerFactory.getLogger(SignalStoringService.class);

        private final DistanceCalculatorService distanceCalculator;

        public SignalStoringService(DistanceCalculatorService distanceCalculator) {
            this.distanceCalculator = distanceCalculator;
        }

        @KafkaListener(topics = "${kafka.signal.topic}", groupId = "${kafka.listeners.group.id}")
        @SendTo("${kafka.distance.log.topic}")
        public VehicleInfo processSignal(VehicleSignal signal, Acknowledgment acknowledgment) {
            log.info("Received signal for processing: {}", signal);
            //at most once consumer
            acknowledgment.acknowledge();

            Double totalDistance = distanceCalculator.addData(signal);
            return new VehicleInfo(signal.getVehicleId(), totalDistance);
        }

}
