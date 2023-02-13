package com.epam.kafka.kafkafinal.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DistanceInfo {

    private final Integer vehicleId;
    private final Double distance;
    private final Double lastLatitude;
    private final Double lastLongitude;
}
