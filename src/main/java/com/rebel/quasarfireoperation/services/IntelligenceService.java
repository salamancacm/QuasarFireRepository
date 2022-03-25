package com.rebel.quasarfireoperation.services;

import com.rebel.quasarfireoperation.exception.InexistentSatelliteException;
import com.rebel.quasarfireoperation.exception.InsufficientInformationException;
import com.rebel.quasarfireoperation.exception.LocationException;
import com.rebel.quasarfireoperation.exception.MessageException;
import com.rebel.quasarfireoperation.model.CargoShip;
import com.rebel.quasarfireoperation.model.Satellite;
import com.rebel.quasarfireoperation.model.SatelliteWrapper;

public interface IntelligenceService {

    CargoShip getCargoShip(SatelliteWrapper satelliteWrapper) throws MessageException, LocationException;

    void retreiveMessageFromSatellite(Satellite satellite, String satelliteName) throws InexistentSatelliteException;

    CargoShip getCargoShipFromSplitSatellites() throws MessageException, LocationException, InsufficientInformationException;

    void clearCargoHistory();
}
