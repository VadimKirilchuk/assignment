package ru.games;

/**
 * Created by Андрей on 22.06.2015.
 */
public class Coordinate {
    private int coordinateY;
    private int coordinateX;

    public Coordinate(int coordinateY, int coordinateX) {
        this.coordinateY = coordinateY;
        this.coordinateX = coordinateX;
    }

    public int getCoordinateY() {
        return coordinateY;
    }

    public int getCoordinateX() {
        return coordinateX;
    }
}
