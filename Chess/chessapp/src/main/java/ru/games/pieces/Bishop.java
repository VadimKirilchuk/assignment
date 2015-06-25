package ru.games.pieces;

import ru.games.Coordinate;

/**
 * Created by Андрей on 20.06.2015.
 */
public class Bishop extends Piece {
    public Bishop(PieceColor color, PieceType type) {
        super(color, type);
    }

    @Override
    protected boolean checkPosition(int pieceCoordinateY,int pieceCoordinateX,
                                    int targetCoordinateY,int targetCoordinateX,
                                    Piece[][] board) {

        //проверить условие
        int amountX = (targetCoordinateX - pieceCoordinateX) > 0 ? 1 : -1;
        int amountY = (targetCoordinateY - pieceCoordinateY) > 0 ? 1 : -1;
        int indexX = pieceCoordinateX + amountX;
        int indexY = pieceCoordinateY + amountY;
        while (indexX != targetCoordinateX) {
            if (board[indexY][indexX] != null) {
                return false;
            }
            indexX += amountX;
            indexY += amountY;
        }
        return isValidTargetPosition(board[indexY][indexX]);
    }

    @Override
    protected boolean isPieceOnTrajectory(int pieceCoordinateY,int pieceCoordinateX,
                                          int targetCoordinateY,int targetCoordinateX) {

        return Math.abs(targetCoordinateY - pieceCoordinateY)
                != Math.abs(targetCoordinateX - pieceCoordinateX);
    }
}
