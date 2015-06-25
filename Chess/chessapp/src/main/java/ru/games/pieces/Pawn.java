package ru.games.pieces;

import ru.games.Coordinate;

/**
 * Created by Андрей on 20.06.2015.
 */
public class Pawn extends Piece {
    private boolean inPassingMove = false;
    private boolean firstMove = true;

    public Pawn(PieceColor color, PieceType type) {
        super(color, type);
    }

    @Override
    protected boolean isPieceOnTrajectory(int pieceCoordinateY, int pieceCoordinateX,
                                          int targetCoordinateY, int targetCoordinateX) {

        if (pieceCoordinateX == targetCoordinateX) {
            return isValidVerticalCoordinates(pieceCoordinateY, pieceCoordinateX,
                    targetCoordinateY, targetCoordinateX);
        }
        return isValidDiagonalCoordinate(pieceCoordinateY, pieceCoordinateX,
                targetCoordinateY, targetCoordinateX);
    }

    @Override
    protected boolean checkPosition(int pieceCoordinateY, int pieceCoordinateX,
                                    int targetCoordinateY, int targetCoordinateX,
                                    Piece[][] board) {
        if (pieceCoordinateX == targetCoordinateX) {
            int amount = (targetCoordinateY - pieceCoordinateY) > 0 ? 1 : -1;
            for (int index = pieceCoordinateY + amount; index <= targetCoordinateY; index += amount) {
                if (!checkForNull(board[index][pieceCoordinateX])) {
                    return false;
                }
            }
            return true;
        }
        return isValidTargetPosition(board[targetCoordinateY][targetCoordinateX]);
    }

    @Override
    protected boolean isValidTargetPosition(Piece targetPiece) {
        PieceColor pieceColor = this.getColor();
        return ((targetPiece != null)
                && (targetPiece.getColor()
                != pieceColor));
    }

    private boolean isValidVerticalCoordinates(int pieceCoordinateY, int pieceCoordinateX,
                                               int targetCoordinateY, int targetCoordinateX) {
        if (firstMove) {
            return checkVerticalCoordinates(pieceCoordinateY,
                    targetCoordinateY, 2);
        }
        return checkVerticalCoordinates(pieceCoordinateY,
                targetCoordinateY, 1);
    }

    private boolean checkVerticalCoordinates(int pieceCoordinateY,
                                             int targetCoordinateY,
                                             int limit) {
        if (this.getColor() == PieceColor.WHITE) {
            return (pieceCoordinateY < targetCoordinateY)
                    && checkPoint(pieceCoordinateY, targetCoordinateY, limit);
        }
        return (pieceCoordinateY > targetCoordinateY)
                && checkPoint(pieceCoordinateY, targetCoordinateY, limit);
    }

    private boolean checkPoint(int pieceCoordinateY, int targetCoordinateY,
                               int maxDifference) {
        return ((targetCoordinateY - pieceCoordinateY) <= maxDifference);
    }

    public boolean isInPassingMove(Coordinate pieceCoordinates,
                                   Coordinate targetCoordinates,
                                   Piece lastProcessedPiece, Piece[][] board) {
        if (lastProcessedPiece == null) {
            return false;
        }
        int pieceCoordinateY = pieceCoordinates.getCoordinateY();
        int pieceCoordinateX = pieceCoordinates.getCoordinateX();
        int targetCoordinateY = targetCoordinates.getCoordinateY();
        int targetCoordinateX = targetCoordinates.getCoordinateX();
        return isValidDiagonalCoordinate(pieceCoordinateY, pieceCoordinateX,
                targetCoordinateY, targetCoordinateX)
                && isCoordinatesForPass(pieceCoordinateY)
                && isValidPassingPosition(pieceCoordinateY, pieceCoordinateX,
                targetCoordinateY, targetCoordinateX,
                lastProcessedPiece, board);
    }

    private boolean isValidDiagonalCoordinate(int pieceCoordinateY, int pieceCoordinateX,
                                              int targetCoordinateY, int targetCoordinateX) {
        if (this.getColor() == PieceColor.WHITE) {
            return checkDiagonal(pieceCoordinateY + 1, pieceCoordinateX,
                    targetCoordinateY, targetCoordinateX);
        }
        return checkDiagonal(pieceCoordinateY - 1, pieceCoordinateX,
                targetCoordinateY, targetCoordinateX);
    }

    private boolean checkDiagonal(int validY, int pieceX,
                                  int targetY, int targetX) {
        return (targetY == validY) && (Math.abs(targetX - pieceX) == 1);
    }

    private boolean isCoordinatesForPass(int pieceCoordinateY) {
        return pieceCoordinateY == 4 || pieceCoordinateY == 3;
    }

    private boolean isValidPassingPosition(int pieceCoordinateY, int pieceCoordinateX,
                                           int targetCoordinateY, int targetCoordinateX,
                                           Piece lastProcessedPiece, Piece[][] board) {
        Piece targetPiece = board[targetCoordinateY][targetCoordinateX];
        Piece attackPiece = board[targetCoordinateY][pieceCoordinateX];
        return checkForNull(targetPiece)
                && isValidPawn(attackPiece, lastProcessedPiece);
    }

    private boolean checkForNull(Piece targetPosition) {
        return targetPosition == null;
    }

    private boolean isValidPawn(Piece piece,
                                Piece lastProcessedPiece) {
        return lastProcessedPiece.getType() == PieceType.PAWN
                && (lastProcessedPiece == piece)
                && (((Pawn) piece).getInpassingMove());
    }

    public void setImpassingMove(int pieceY, int pieceX,
                                 int lastY, int lastX) {
        if ((pieceX == lastX) && (Math.abs(pieceY - lastY) == 2)) {
            inPassingMove = true;
        } else {
            inPassingMove = false;
        }
    }

    public boolean getInpassingMove() {
        return inPassingMove;
    }

    public void setFirstMove() {
        if (firstMove) {
            firstMove = false;
        }
    }

    public boolean isReadyToConvert(int coordinateY) {
        if (this.getColor() == PieceColor.WHITE) {
            return coordinateY == 7;
        }
        return coordinateY == 0;
    }
}
