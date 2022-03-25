package com.rebel.quasarfireoperation.model;

import java.util.List;

public class Satellite {
    private double distance;
    private String name;
    private Position position;

    private List<String> message;

    public double getDistance() {
        return distance;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getMessage() {
        return message;
    }

    public void setMessage(List<String> message) {
        this.message = message;
    }
}
