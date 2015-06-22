package ru.games.pieces;

import ru.games.Coordinate;

/**
 * Created by Андрей on 20.06.2015.
 */
public class Rook extends Piece {
    public Rook(PieceColor color, PieceType type) {
        super(color, type);
    }

    @Override
    public boolean isValidMoveTo(Coordinate pieceCoordinate,
                                 Coordinate targetCoordinate, Piece[][] board) {
        int pieceCoordinateY = pieceCoordinate.getCoordinateY();
        int pieceCoordinateX = pieceCoordinate.getCoordinateX();
        int targetCoordinateY = targetCoordinate.getCoordinateY();
        int targetCoordinateX = targetCoordinate.getCoordinateX();
        //проверить корректность условия
        if ((pieceCoordinateX != targetCoordinateX)
                && (pieceCoordinateY != targetCoordinateY)) {
            return false;
        }
        if ((pieceCoordinateX == targetCoordinateX)
                && (pieceCoordinateY == targetCoordinateY)) {
            return false;
        }

        if (pieceCoordinateX == targetCoordinateX) {
            return checkPositionsX(pieceCoordinateY, pieceCoordinateX,
                    targetCoordinateY, board);
        } else {
            return checkPositionsY(pieceCoordinateY, pieceCoordinateX,
                    targetCoordinateX, board);
        }
    }

    private boolean checkPositionsX(int pieceCoordinateY, int pieceCoordinateX,
                                    int targetCoordinateY, Piece[][] board) {
        int amount = (targetCoordinateY - pieceCoordinateY) > 0 ? 1 : -1;
        int index = pieceCoordinateY + amount;
        while (index != targetCoordinateY) {
            if (board[index][pieceCoordinateX] != null) {
                return false;
            }
            index += amount;
        }
        //можно ли так перенести?
        if ((board[index][pieceCoordinateX] != null)
                && (board[index][pieceCoordinateX].color
                == board[pieceCoordinateY][pieceCoordinateX].color)) {
            return false;
        }
        return true;
    }

    private boolean checkPositionsY(int pieceCoordinateY, int pieceCoordinateX,
                                    int targetCoordinateX, Piece[][] board) {
        int amount = (targetCoordinateX - pieceCoordinateX) > 0 ? 1 : -1;
        int index = pieceCoordinateX + amount;
        while (index != targetCoordinateX) {
            if (board[pieceCoordinateY][index] != null) {
                return false;
            }
            index += amount;
        }
        //можно ли так перенести
        if ((board[pieceCoordinateY][index] != null)
                && (board[pieceCoordinateY][index].color
                == board[pieceCoordinateY][pieceCoordinateX].color)) {
            return false;
        }
        return true;
    }
}
