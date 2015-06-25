package ru.games.pieces;

/**
 * Created by Андрей on 20.06.2015.
 */
public class Rook extends Piece implements CastlingMember {
    private boolean castling = true;

    public Rook(PieceColor color, PieceType type) {
        super(color, type);
    }
//упростить

    @Override
    protected boolean isPieceOnTrajectory(int pieceCoordinateY, int pieceCoordinateX,
                                          int targetCoordinateY, int targetCoordinateX) {

        return !((pieceCoordinateX != targetCoordinateX)
                && (pieceCoordinateY != targetCoordinateY));
    }

    @Override
    protected boolean checkPosition(int pieceCoordinateY, int pieceCoordinateX,
                                    int targetCoordinateY, int targetCoordinateX,
                                    Piece[][] board) {

        if (pieceCoordinateX == targetCoordinateX) {
            return checkPositionsX(pieceCoordinateY, pieceCoordinateX,
                    targetCoordinateY, board);
        }
        return checkPositionsY(pieceCoordinateY, pieceCoordinateX,
                targetCoordinateX, board);
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

    public boolean getCastling() {
        return castling;
    }

    public void setCastling(boolean castling) {
        this.castling = castling;
    }
}
