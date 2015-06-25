package ru.games.pieces;

import ru.games.Coordinate;

/**
 * Created by Андрей on 20.06.2015.
 */
public abstract class Piece {
    private PieceColor color;
    private PieceType type;

    protected Piece(PieceColor color, PieceType type) {
        this.color = color;
        this.type = type;
    }

    public PieceColor getColor() {
        return color;
    }

    public PieceType getType() {
        return type;
    }

    public boolean isValidMoveTo(Coordinate pieceCoordinates,
                                 Coordinate targetCoordinates,
                                 Piece[][] board) {
        int pieceCoordinateY = pieceCoordinates.getCoordinateY();
        int pieceCoordinateX = pieceCoordinates.getCoordinateX();
        int targetCoordinateY = targetCoordinates.getCoordinateY();
        int targetCoordinateX = targetCoordinates.getCoordinateX();

        return (!isTheSameCoordinate(pieceCoordinateY, pieceCoordinateX,
                                     targetCoordinateY, targetCoordinateX)
                && isPieceOnTrajectory(pieceCoordinateY, pieceCoordinateX,
                                       targetCoordinateY, targetCoordinateX)
                && checkPosition(pieceCoordinateY, pieceCoordinateX,
                                 targetCoordinateY, targetCoordinateX, board));
    }
//возможно лучше с координатами
    protected boolean isTheSameCoordinate(int pieceCoordinateY, int pieceCoordinateX,
                                          int targetCoordinateY, int targetCoordinateX) {
        return ((pieceCoordinateY==targetCoordinateY)
                &&(pieceCoordinateX==targetCoordinateX));
    }

    protected boolean isValidTargetPosition(Piece targetPiece) {
        PieceColor pieceColor = this.getColor();
        return ((targetPiece == null)
                || (targetPiece.getColor()
                != pieceColor));
    }

    protected abstract boolean isPieceOnTrajectory(int pieceCoordinateY, int pieceCoordinateX,
                                                   int targetCoordinateY, int targetCoordinateX);

    protected abstract boolean checkPosition(int pieceCoordinateY, int pieceCoordinateX,
                                             int targetCoordinateY, int targetCoordinateX,
                                             Piece[][] board);
}
