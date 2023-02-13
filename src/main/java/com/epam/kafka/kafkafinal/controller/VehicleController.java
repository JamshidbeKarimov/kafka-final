package com.epam.kafka.kafkafinal.controller;

import com.epam.kafka.kafkafinal.exception.InvalidCoordinatorException;
import com.epam.kafka.kafkafinal.model.VehicleSignal;
import com.epam.kafka.kafkafinal.service.SignalService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Random;


@RestController
@RequestMapping("api/v1")
@RequiredArgsConstructor
public class VehicleController {

    private static final List<Integer> vehicles = List.of(1, 2, 3, 5, 8, 13, 21, 34);
    private final SignalService signalService;
    private final Random random = new Random();

    @SneakyThrows
    @PostMapping("/vehicle-signal")
    public ResponseEntity<String> getSignal(@RequestBody VehicleSignal vehicleSignal) {

        validateSignal(vehicleSignal);
        for (int i = 0; i < 30; i++) {
            VehicleSignal signal = new VehicleSignal(Math.abs(vehicles.get(random.nextInt(100) % vehicles.size())),
                    random.nextDouble() * 70, random.nextDouble() * 65);
            signalService.sendSignal(signal);
            Thread.sleep(3000);
        }
        return ResponseEntity.ok("success");
    }

    private void validateSignal(VehicleSignal vehicleSignal) {
        if (!(vehicleSignal.getLatitude() >= -90 && vehicleSignal.getLatitude() <= 90))
            throw new InvalidCoordinatorException("The latitude must be a number between -90 and 90 ");

        if (!(vehicleSignal.getLongitude() >= -180 && vehicleSignal.getLongitude() <= 180))
            throw new InvalidCoordinatorException("The longitude must be a number between -180 and 180 ");

    }
}
