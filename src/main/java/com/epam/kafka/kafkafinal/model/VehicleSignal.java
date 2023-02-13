package com.epam.kafka.kafkafinal.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

import static org.apache.commons.lang3.Validate.notNull;

@Getter
@ToString
public class VehicleSignal {

    private final Integer vehicleId;
    private final Double latitude;
    private final Double longitude;

    public VehicleSignal(@JsonProperty("vehicleId") Integer vehicleId,
                         @JsonProperty("latitude") Double latitude,
                         @JsonProperty("longitude") Double longitude) {
        this.vehicleId = notNull(vehicleId);
        this.latitude = notNull(latitude);
        this.longitude = notNull(longitude);
    }
}

