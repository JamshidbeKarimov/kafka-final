package com.epam.kafka.kafkafinal.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class VehicleInfo {

    private final Integer vehicleId;
    private final Double totalDistance;

    public VehicleInfo(@JsonProperty("vehicleId") Integer vehicleId,
                       @JsonProperty("totalDistance") Double totalDistance) {
        this.vehicleId = vehicleId;
        this.totalDistance = totalDistance;
    }
}
