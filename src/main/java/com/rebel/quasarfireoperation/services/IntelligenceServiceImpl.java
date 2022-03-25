package com.rebel.quasarfireoperation.services;

import com.rebel.quasarfireoperation.config.SatelliteConfigProps;
import com.rebel.quasarfireoperation.exception.InexistentSatelliteException;
import com.rebel.quasarfireoperation.exception.InsufficientInformationException;
import com.rebel.quasarfireoperation.exception.LocationException;
import com.rebel.quasarfireoperation.exception.MessageException;
import com.rebel.quasarfireoperation.model.CargoShip;
import com.rebel.quasarfireoperation.model.Position;
import com.rebel.quasarfireoperation.model.Satellite;
import com.rebel.quasarfireoperation.model.SatelliteWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
public class IntelligenceServiceImpl implements IntelligenceService {

    private SatelliteWrapper splitSatelliteWrapper = new SatelliteWrapper();

    @Autowired
    private LocationService locationService;

    @Autowired
    private MessageService messageService;

    @Autowired
    private SatelliteConfigProps satelliteConfigProps;

    @Override
    public CargoShip getCargoShip(SatelliteWrapper satelliteWrapper) throws MessageException, LocationException {

        if (satelliteWrapper.getMessages().size() < 2) {
            throw new MessageException("Message number is insufficient");
        }

        if (satelliteWrapper.getPositions().length < 2 || satelliteWrapper.getDistances().length < 2) {
            throw new LocationException("Position or distance number is insufficient");
        }

        setPositions(satelliteWrapper);
        double[] cargoCoordinates = locationService.triangulateLocation(satelliteWrapper.getPositions(), satelliteWrapper.getDistances());

        return new CargoShip(
                new Position(cargoCoordinates),
                messageService.getMessage(satelliteWrapper.getMessages())
        );
    }

    @Override
    public void retreiveMessageFromSatellite(Satellite satelliteRequest, String satelliteName) throws InexistentSatelliteException {
        if (satelliteConfigProps.getSatellites().get(satelliteName) == null) {
            throw new InexistentSatelliteException("Satellite name " + satelliteName + " does not exist in our system");
        }

        List<Satellite> satellites = splitSatelliteWrapper.getSatellites();
        if (satellites != null && satellites.size() > 0) {
            boolean satelliteAlreadyAdded = false;
            for (Satellite satellite : satellites) {
                if (satellite.getName().equalsIgnoreCase(satelliteName)) {
                    satellite.setMessage(satelliteRequest.getMessage());
                    satellite.setDistance(satelliteRequest.getDistance());
                    satelliteAlreadyAdded = true;
                }
            }

            if(!satelliteAlreadyAdded){
                satelliteRequest.setName(satelliteName);
                satellites.add(satelliteRequest);
            }
        } else {
            satellites = new ArrayList<>();
            satelliteRequest.setName(satelliteName);
            satellites.add(satelliteRequest);
            splitSatelliteWrapper.setSatellites(satellites);
        }
    }

    @Override
    public CargoShip getCargoShipFromSplitSatellites() throws MessageException, LocationException, InsufficientInformationException {
        if (splitSatelliteWrapper.getSatellites() == null || splitSatelliteWrapper.getSatellites().size() > 3) {
            throw new InsufficientInformationException("Current data is insufficient to determine message and location");
        }
        return getCargoShip(splitSatelliteWrapper);
    }

    @Override
    public void clearCargoHistory() {
        splitSatelliteWrapper = new SatelliteWrapper();
    }

    private void setPositions(SatelliteWrapper satelliteWrapper) {
        if (satelliteWrapper.getPositions()[0] == null) {

            Map<String, SatelliteConfigProps.SatelliteCoordinates> satelliteMap = buildSatelliteList();

            int numberSat = satelliteMap.size();

            double[][] pointsList = new double[numberSat][];
            String[] satellitePos;

            int i = 0;
            for (Satellite satellite : satelliteWrapper.getSatellites()) {
                satellitePos = satelliteMap.get(satellite.getName()).getPosition().split(",");
                pointsList[i] = Arrays.stream(satellitePos)
                        .mapToDouble(Double::valueOf)
                        .toArray();
                i++;
            }

            satelliteWrapper.setPositions(pointsList);
        }
    }


    private Map<String, SatelliteConfigProps.SatelliteCoordinates> buildSatelliteList() {
        return satelliteConfigProps.getSatellites();
    }

}
