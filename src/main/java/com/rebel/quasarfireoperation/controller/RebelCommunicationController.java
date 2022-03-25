package com.rebel.quasarfireoperation.controller;

import com.rebel.quasarfireoperation.exception.InexistentSatelliteException;
import com.rebel.quasarfireoperation.exception.InsufficientInformationException;
import com.rebel.quasarfireoperation.exception.CoordinateException;
import com.rebel.quasarfireoperation.model.CargoShip;
import com.rebel.quasarfireoperation.model.Satellite;
import com.rebel.quasarfireoperation.model.SatelliteRequest;
import com.rebel.quasarfireoperation.services.DecipherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api")
public class RebelCommunicationController {

    @Autowired
    private DecipherService decipherService;

    @PostMapping("/topsecret")
    public ResponseEntity<CargoShip> topSecret(@RequestBody SatelliteRequest satelliteRequest) {
        try {
            return ResponseEntity.ok(decipherService.getCargoShip(satelliteRequest));
        } catch (CoordinateException | InsufficientInformationException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/topsecret_split/{satelliteName}")
    public ResponseEntity<Void> topSecretSplit(@RequestBody Satellite satellite, @PathVariable String satelliteName) {
        try {
            decipherService.retreiveMessageFromSatellite(satellite, satelliteName);
            return ResponseEntity.ok().build();
        } catch (InexistentSatelliteException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/topsecret_split")
    public ResponseEntity<CargoShip> getCargoLocation() {
        try {
            return ResponseEntity.ok(decipherService.getCargoShipFromSplitSatellites());
        } catch (CoordinateException | InsufficientInformationException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/topsecret_split/clear")
    public ResponseEntity<Void> clearCargoHistory() {
        decipherService.clearCargoHistory();
        return ResponseEntity.ok().build();
    }
}
