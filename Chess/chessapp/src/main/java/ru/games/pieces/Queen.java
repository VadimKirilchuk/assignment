package ru.games.pieces;

/**
 * Created by Андрей on 20.06.2015.
 */
public class Queen extends Piece {
    public Queen(PieceColor color, PieceType type) {
        super(color, type);
    }

    @Override
    protected boolean isPieceOnTrajectory(int pieceCoordinateY, int pieceCoordinateX,
                                          int targetCoordinateY, int targetCoordinateX) {

        return isOnStraightLine(pieceCoordinateY, pieceCoordinateX,
                targetCoordinateY, targetCoordinateX)
                || isOnDiagonalLine(pieceCoordinateY, pieceCoordinateX,
                targetCoordinateY, targetCoordinateX);
    }
@Override
    protected boolean checkPosition(int pieceCoordinateY, int pieceCoordinateX,
                                    int targetCoordinateY, int targetCoordinateX,
                                    Piece[][] board) {
        if (pieceCoordinateY == targetCoordinateY) {
            return checkPositionsY(pieceCoordinateY, pieceCoordinateX,
                                   targetCoordinateX, board);
        }
        if (pieceCoordinateX == targetCoordinateX) {
            return checkPositionsX(pieceCoordinateY, pieceCoordinateX,
                                   targetCoordinateY, board);
        }
        return checkDiagonalPosition(pieceCoordinateY, pieceCoordinateX,
                                     targetCoordinateY, targetCoordinateX, board);
    }

    private boolean isOnStraightLine(int pieceCoordinateY, int pieceCoordinateX,
                                     int targetCoordinateY, int targetCoordinateX) {
        return !((pieceCoordinateX != targetCoordinateX)
                && (pieceCoordinateY != targetCoordinateY));
    }

    private boolean isOnDiagonalLine(int pieceCoordinateY, int pieceCoordinateX,
                                     int targetCoordinateY, int targetCoordinateX) {
        return Math.abs(targetCoordinateY - pieceCoordinateY)
                != Math.abs(targetCoordinateX - pieceCoordinateX);
    }

    protected boolean checkDiagonalPosition(int pieceCoordinateY, int pieceCoordinateX,
                                            int targetCoordinateY, int targetCoordinateX,
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
        //можно ли так перенести
        return isValidTargetPosition(board[indexY][indexX]);
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
        return isValidTargetPosition(board[index][pieceCoordinateX]);
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
        return isValidTargetPosition(board[pieceCoordinateY][index]);
    }
}
