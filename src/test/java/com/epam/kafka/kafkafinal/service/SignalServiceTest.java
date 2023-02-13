package com.epam.kafka.kafkafinal.service;

import com.epam.kafka.kafkafinal.model.VehicleSignal;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.test.annotation.DirtiesContext;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.utility.DockerImageName;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.stream.StreamSupport;

import static org.apache.kafka.clients.consumer.ConsumerConfig.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.AFTER_CLASS;

@SpringBootTest
@DirtiesContext(classMode = AFTER_CLASS)
class SignalServiceTest {
    static KafkaContainer kafka;

    @Value("${kafka.signal.topic}")
    String topic;

    @BeforeAll
    static void setUp() {
        kafka = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:6.2.1"));
        kafka.start();
        System.setProperty("spring.kafka.bootstrap-servers", kafka.getBootstrapServers());
    }

    @AfterAll
    static void tearDown() {
        kafka.close();
    }

    @Autowired
    SignalService service;

    @Test
    public void sendSignalTest() {
        service.sendSignal(new VehicleSignal(1,45.01, 69.52));

        ConsumerRecords<String, VehicleSignal> taxiIngestionRecords;
        try (KafkaConsumer<String, VehicleSignal> taxiSignalConsumer = sendSignalConsumer()) {
            taxiIngestionRecords = taxiSignalConsumer.poll(Duration.ofSeconds(2));
        }
        List<ConsumerRecord<String, VehicleSignal>> taxiIngestionRecordsList =
                StreamSupport.stream(taxiIngestionRecords.spliterator(), false).toList();
        assertEquals(1, taxiIngestionRecordsList.size());

        ConsumerRecord<String, VehicleSignal> taxiIngestionRecord = taxiIngestionRecordsList.get(0);
        assertEquals("1", taxiIngestionRecord.key());
        VehicleSignal vehicleSignal = taxiIngestionRecord.value();
        assertEquals(45.01, vehicleSignal.getLatitude());
        assertEquals(69.52, vehicleSignal.getLongitude());
    }

    KafkaConsumer<String, VehicleSignal> sendSignalConsumer() {
        Properties properties = new Properties();
        properties.setProperty(BOOTSTRAP_SERVERS_CONFIG, kafka.getBootstrapServers());
        properties.setProperty(KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class.getName());
        properties.setProperty(AUTO_OFFSET_RESET_CONFIG, "earliest");
        properties.setProperty(GROUP_ID_CONFIG, "taxi_signal_test_tcg");
        properties.setProperty(JsonDeserializer.TRUSTED_PACKAGES, "*");

        KafkaConsumer<String, VehicleSignal> consumer = new KafkaConsumer<>(properties);
        consumer.subscribe(Collections.singleton(topic));
        return consumer;
    }
}