package ru.games.pieces;

import ru.games.Coordinate;

/**
 * Created by Андрей on 20.06.2015.
 */
public abstract class Piece {
    protected PieceColor color;
    protected PieceType type;

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

    public abstract boolean isValidMoveTo(Coordinate pieceCoordinate,
                                          Coordinate targetCoordinate,Piece [][] board);
}
