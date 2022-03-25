package com.rebel.quasarfireoperation.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SatelliteWrapper {

    private List<Satellite> satellites;

    public List<Satellite> getSatellites() {
        return satellites;
    }

    public void setSatellites(List<Satellite> satellites) {
        this.satellites = satellites;
    }

    public double[] getDistances() {
        return satellites.stream()
                .map(Satellite::getDistance)
                .mapToDouble(Double::doubleValue)
                .toArray();
    }

    public double[][] getPositions() {
        double[][] positions = new double[satellites.size()][];
        String[] points;
        for (int i = 0; i < satellites.size(); i++) {
            if (satellites.get(i).getPosition() != null) {
                points = satellites.get(i).getPosition().toString().split(",");
                positions[i] = Arrays.stream(points)
                        .map(Double::valueOf)
                        .mapToDouble(Double::doubleValue)
                        .toArray();
            }
        }
        return positions;
    }

    public void setPositions(double[][] pointsList) {
        Position position;
        for (int i = 0; i < pointsList.length; i++) {
            position = new Position(pointsList[i]);
            satellites.get(i).setPosition(position);
        }
    }

    public List<List<String>> getMessages() {
        List<List<String>> messages = new ArrayList<>();
        for (Satellite s : satellites) {
            messages.add(s.getMessage());
        }
        return messages;
    }
}
