package com.rebel.quasarfireoperation.services;

import com.rebel.quasarfireoperation.config.SatelliteConfigProps;
import com.rebel.quasarfireoperation.exception.InexistentSatelliteException;
import com.rebel.quasarfireoperation.exception.InsufficientInformationException;
import com.rebel.quasarfireoperation.model.CargoShip;
import com.rebel.quasarfireoperation.model.Position;
import com.rebel.quasarfireoperation.model.Satellite;
import com.rebel.quasarfireoperation.model.SatelliteRequest;
import com.rebel.quasarfireoperation.utils.AppUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class DecipherService {

    private SatelliteRequest splitSatelliteRequest = new SatelliteRequest();

    @Autowired
    private SatelliteConfigProps satelliteConfigProps;

    //Metodo para obtener info de la nave
    public CargoShip getCargoShip(SatelliteRequest satelliteRequest) throws InsufficientInformationException {
        int posizionSize = satelliteConfigProps.getSatellites()
                .values().stream().findFirst()
                .get()
                .getPosition()
                .split(",").length;

        setPositions(satelliteRequest, posizionSize);

        double[] distances = AppUtils.getDistances(satelliteRequest);
        double[][] positions = AppUtils.getPositions(
                satelliteRequest,
                posizionSize
        );

        if (AppUtils.extractMessagesFromSatellites(satelliteRequest).size() < 2 || (positions.length < 2 || distances.length < 2)) {
            throw new InsufficientInformationException("Message size is incorrect");
        }

        double[] cargoCoordinates = AppUtils.triangulateLocation(positions, distances);

        return new CargoShip(
                new Position(cargoCoordinates),
                AppUtils.getMessage(AppUtils.extractMessagesFromSatellites(satelliteRequest))
        );
    }

    //Metodo para obtener mensaje del satelite
    public void retreiveMessageFromSatellite(Satellite satelliteRequest, String satelliteName) throws InexistentSatelliteException {
        if (satelliteConfigProps.getSatellites().get(satelliteName) == null) {
            throw new InexistentSatelliteException("Satellite name " + satelliteName + " does not exist in our system");
        }

        List<Satellite> satellites = splitSatelliteRequest.getSatellites();
        if (satellites != null && satellites.size() > 0) {
            boolean satelliteAlreadyAdded = false;
            for (Satellite satellite : satellites) {
                if (satellite.getName().equalsIgnoreCase(satelliteName)) {
                    satellite.setMessage(satelliteRequest.getMessage());
                    satellite.setDistance(satelliteRequest.getDistance());
                    satelliteAlreadyAdded = true;
                }
            }

            if (!satelliteAlreadyAdded) {
                satelliteRequest.setName(satelliteName);
                satellites.add(satelliteRequest);
            }
        } else {
            satellites = new ArrayList<>();
            satelliteRequest.setName(satelliteName);
            satellites.add(satelliteRequest);
            splitSatelliteRequest.setSatellites(satellites);
        }
    }

    public CargoShip getCargoShipFromSplitSatellites() throws InsufficientInformationException {
        if (splitSatelliteRequest.getSatellites() == null || splitSatelliteRequest.getSatellites().size() > 3) {
            throw new InsufficientInformationException("Current data is insufficient to determine message and location");
        }
        return getCargoShip(splitSatelliteRequest);
    }

    //Metodo para limpiar info de naves
    public void clearCargoHistory() {
        splitSatelliteRequest = new SatelliteRequest();
    }

    //Metodo para setear posiciones de satelites
    private void setPositions(SatelliteRequest satelliteRequest, int positionSize) {
        Map<String, SatelliteConfigProps.SatelliteCoordinates> satelliteMap = buildSatelliteList();

        double[][] pointsList = new double[satelliteMap.size()][positionSize];
        String[] satellitePos;

        int i = 0;
        for (Satellite satellite : satelliteRequest.getSatellites()) {
            satellitePos = satelliteMap.get(satellite.getName()).getPosition().split(",");
            pointsList[i][0] = Double.parseDouble(satellitePos[0]);
            pointsList[i][1] = Double.parseDouble(satellitePos[1]);
            i++;
        }

        AppUtils.setPositions(pointsList, satelliteRequest);
    }

    //Metodo para obtener listado de satelites
    private Map<String, SatelliteConfigProps.SatelliteCoordinates> buildSatelliteList() {
        return satelliteConfigProps.getSatellites();
    }

}
