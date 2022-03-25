package com.rebel.quasarfireoperation.controller;

import com.rebel.quasarfireoperation.exception.InexistentSatelliteException;
import com.rebel.quasarfireoperation.exception.InsufficientInformationException;
import com.rebel.quasarfireoperation.exception.LocationException;
import com.rebel.quasarfireoperation.exception.MessageException;
import com.rebel.quasarfireoperation.model.CargoShip;
import com.rebel.quasarfireoperation.model.Satellite;
import com.rebel.quasarfireoperation.model.SatelliteWrapper;
import com.rebel.quasarfireoperation.services.IntelligenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping(path = "/api")
public class RebelCommunicationController {

    @Autowired
    private IntelligenceService intelligenceService;

    @PostMapping("/topsecret")
    public ResponseEntity<CargoShip> topSecret(@RequestBody SatelliteWrapper satelliteWrapper) {
        try {
            return ResponseEntity.ok(intelligenceService.getCargoShip(satelliteWrapper));
        } catch (MessageException | LocationException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    @PostMapping("/topsecret_split/{satelliteName}")
    public ResponseEntity<Void> topSecretSplit(@RequestBody Satellite satellite, @PathVariable String satelliteName) {
        try {
            intelligenceService.retreiveMessageFromSatellite(satellite, satelliteName);
            return ResponseEntity.ok().build();
        } catch (InexistentSatelliteException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
        }
    }

    @GetMapping("/topsecret_split")
    public ResponseEntity<CargoShip> getCargoLocation() {
        try {
            return ResponseEntity.ok(intelligenceService.getCargoShipFromSplitSatellites());
        } catch (MessageException | LocationException | InsufficientInformationException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    @GetMapping("/topsecret_split/clear")
    public ResponseEntity<Void> clearCargoHistory() {
        intelligenceService.clearCargoHistory();
        return ResponseEntity.ok().build();
    }
}
