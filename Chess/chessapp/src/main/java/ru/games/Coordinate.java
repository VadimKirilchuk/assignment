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
/*
    @Override
    public boolean equals(Object object) {
        if (object == null) {
            return false;
        }
        if (this == object) {
            return true;
        }
        if (this.getClass() == object.getClass()) {
            Coordinate opponentCoordinate = (Coordinate) object;
            return (coordinateY == opponentCoordinate.coordinateY)
                    && (coordinateX == opponentCoordinate.coordinateX);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        int hashCode = 1;
        hashCode = 31 * hashCode + coordinateY;
        hashCode = 31 * hashCode + coordinateX;
        return hashCode;
    }
    */
}
