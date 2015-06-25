package ru.games.pieces;

import ru.games.Castling;
import ru.games.Coordinate;

/**
 * Created by Андрей on 20.06.2015.
 */
public class King extends Piece implements CastlingMember {
    private boolean castling = true;

    public King(PieceColor color, PieceType type) {
        super(color, type);
    }

    @Override
    protected boolean isPieceOnTrajectory(int pieceCoordinateY, int pieceCoordinateX,
                                          int targetCoordinateY, int targetCoordinateX) {
        return ((Math.abs(targetCoordinateY - pieceCoordinateY) <= 1)
                && (Math.abs(targetCoordinateX - pieceCoordinateX) <= 1));
    }

    @Override
    protected boolean checkPosition(int pieceCoordinateY, int pieceCoordinateX,
                                    int targetCoordinateY, int targetCoordinateX,
                                    Piece[][] board) {
        Piece piece = board[targetCoordinateY][targetCoordinateX];
        return isValidTargetPosition(piece);
    }

    @Override
    protected boolean isValidTargetPosition(Piece targetPiece) {
        PieceColor pieceColor = this.getColor();
        return (targetPiece == null)
                || ((targetPiece.getType() != PieceType.KING)
                && (targetPiece.getColor() != pieceColor));
    }

    public boolean getCastling() {
        return castling;
    }

    public void setCastling(boolean castling) {
        this.castling = castling;
    }

    public boolean isAvailableCastling(Castling castlingType,
                                       Coordinate kingCoordinate, Piece[][] board) {
        return castling && checkCastling(castlingType, kingCoordinate, board);
    }

    private boolean checkCastling(Castling castlingType,
                                  Coordinate kingCoordinate, Piece[][] board) {
        if (castlingType == Castling.SR) {
            return checkRookCastling(kingCoordinate.getCoordinateY(), 7, board);
        }
        return checkRookCastling(kingCoordinate.getCoordinateY(), 0, board);
    }

    private boolean checkRookCastling(int targetY, int targetX, Piece[][] board) {
        Piece piece = board[targetY][targetX];
        return isAvailableRookCastling(piece) && checkRouteToRook(targetY, targetX, board);
    }

    private boolean isAvailableRookCastling(Piece piece) {
        return (piece != null)
                && (piece.getType() == PieceType.ROOK)
                && (((Rook) piece).getCastling());
    }

    private boolean checkRouteToRook(int targetY, int targetX, Piece[][] board) {
        int amount = (targetX - 4) > 0 ? 1 : -1;
        int index = 4+amount;
       while(index != targetX){
            if (board[targetY][index] != null) {
                return false;
            }
           index+=amount;
        }
        return true;
    }
}
