package com.epam.kafka.kafkafinal.service;

import com.epam.kafka.kafkafinal.model.DistanceInfo;
import com.epam.kafka.kafkafinal.model.VehicleSignal;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class DistanceCalculatorService {
    private final Map<String, DistanceInfo> distanceData;

    public DistanceCalculatorService() {
        distanceData = new ConcurrentHashMap<>();
    }

    public Double addData(VehicleSignal signal) {
        if (distanceData.containsKey(signal.getVehicleId().toString())) {
            updateDistanceData(signal);
        } else {
            insertNewTaxi(signal);
        }
        return distanceData.get(signal.getVehicleId().toString()).getDistance();
    }

    private void updateDistanceData(VehicleSignal signal) {
        String vehicleId = signal.getVehicleId().toString();
        DistanceInfo distance = distanceData.get(vehicleId);

        Double sinceLastPosition = calculateDistance(distance.getLastLatitude(),distance.getLastLongitude(),
                signal.getLatitude(),signal.getLongitude());

        distanceData.put(vehicleId, DistanceInfo.builder()
                .vehicleId(Integer.valueOf(vehicleId))
                .distance(distance.getDistance()+(sinceLastPosition))
                .lastLongitude(signal.getLongitude())
                .lastLatitude(signal.getLatitude())
                .build());
    }

    private Double calculateDistance(double originalLat, double originalLong, double newLat, double newLong) {

        if ((originalLat == newLat) && (originalLong == newLong)) {
            return 0d;
        } else {
            double theta = originalLong - newLong;
            double dist = Math.sin(Math.toRadians(originalLat)) * Math.sin(Math.toRadians(newLat)) +
                    Math.cos(Math.toRadians(originalLat)) * Math.cos(Math.toRadians(newLat))
                            * Math.cos(Math.toRadians(theta));
            dist = Math.acos(dist);
            dist = Math.toDegrees(dist);
            dist = dist * 60 * 1.1515;
            dist = dist * 1.609344;
            return dist;
        }
    }

    private void insertNewTaxi(VehicleSignal signal) {
        distanceData.put(signal.getVehicleId().toString(), DistanceInfo.builder()
                .vehicleId((signal.getVehicleId()))
                .lastLatitude(signal.getLatitude())
                .lastLongitude(signal.getLongitude())
                .distance(0d)
                .build());
    }
}
