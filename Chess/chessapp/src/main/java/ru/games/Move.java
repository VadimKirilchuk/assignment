package ru.games;

/**
 * Created by Андрей on 21.06.2015.
 */
public class Move {
    private Coordinate pieceCoordinate;
    private Coordinate targetCoordinate;
    private String rotate;

    public Move(Coordinate pieceCoordinate,
                Coordinate targetCoordinate) {

        this.pieceCoordinate = pieceCoordinate;
        this.targetCoordinate = targetCoordinate;
    }

    public Move(Coordinate pieceCoordinate,
                String rotate) {
        this.pieceCoordinate = pieceCoordinate;
        this.rotate = rotate;
    }

    public boolean isRotateMove() {
        return rotate == null;
    }

    public String getRotate() {
        return rotate;
    }

    public Coordinate getTargetCoordinate() {
        return targetCoordinate;
    }

    public Coordinate getPieceCoordinate() {
        return pieceCoordinate;
    }
}