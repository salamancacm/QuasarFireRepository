package com.rebel.quasarfireoperation.model;

public class CargoShip {

    private String message;
    private Position position;

    public CargoShip(Position position, String message){
        this.setPosition(position);
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    @Override
    public String toString() {
        return "Carrier{" +
                "message='" + message + '\'' +
                ", position=" + position +
                '}';
    }
}
