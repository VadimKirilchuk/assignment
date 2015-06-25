package ru.games;

/**
 * Created by Андрей on 21.06.2015.
 */
public class Move {
    private Coordinate pieceCoordinate;
    private Coordinate targetCoordinate;
    private Castling castlingType;

    public Move(Coordinate pieceCoordinate,
                Coordinate targetCoordinate) {

        this.pieceCoordinate = pieceCoordinate;
        this.targetCoordinate = targetCoordinate;
    }

    public Move(Coordinate pieceCoordinate,
                Castling castlingType) {
        this.pieceCoordinate = pieceCoordinate;
        this.castlingType = castlingType;
    }

    public boolean isRotateMove() {
        return castlingType == null;
    }

    public Castling getRotate() {
        return castlingType;
    }

    public Coordinate getTargetCoordinate() {
        return targetCoordinate;
    }

    public Coordinate getPieceCoordinate() {
        return pieceCoordinate;
    }
}