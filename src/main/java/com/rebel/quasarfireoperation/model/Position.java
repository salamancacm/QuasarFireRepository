package com.rebel.quasarfireoperation.model;

public class Position {

    private double x;
    private double y;

    public Position(double[] points){
        this.x = points[0];
        this.y = points[1];
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return x + "," + y;
    }
}
