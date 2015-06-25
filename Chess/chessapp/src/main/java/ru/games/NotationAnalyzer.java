package ru.games;

import ru.games.pieces.Piece;

import java.util.Map;

/**
 * Created by Андрей on 21.06.2015.
 */
public class NotationAnalyzer {
    public Move getMove(String coordinates, Piece[][] board) {
        if (!isValidLength(coordinates)) {
            throw new IllegalArgumentException();
        }

        int coordinatePieceX = coordinates.charAt(0) - 97;
        int coordinatePieceY = Character.getNumericValue(coordinates.charAt(1)) - 1;

        if (isValidCoordinate(coordinatePieceY, coordinatePieceX)
                && isValidBoardPiece(coordinatePieceY, coordinatePieceX, board)) {
            String stringTargetCoordinate = coordinates.substring(2, 4);
            if (isCastling(stringTargetCoordinate)) {
                Castling castlingType = Castling.getCastling(stringTargetCoordinate);
                return new Move(new Coordinate(coordinatePieceY, coordinatePieceX),
                                castlingType);
            } else {
                int coordinateTargetX = coordinates.charAt(2) - 97;
                int coordinateTargetY = Character.getNumericValue(coordinates.charAt(3)) - 1;
                if (isValidCoordinate(coordinateTargetY,coordinateTargetX)) {
                    return new Move(new Coordinate(coordinatePieceY, coordinatePieceX),
                                    new Coordinate(coordinateTargetY, coordinateTargetX));
                }
             //   throw new IllegalArgumentException();
            }
        }
        throw new IllegalArgumentException();
    }

    private boolean isValidLength(String coordinates) {
        return coordinates.length() == 4;
    }

    private boolean isCastling(String castlingType) {
        return Castling.getCastling(castlingType) != null;
    }

    private boolean isValidCoordinate(int y, int x) {
        return (((y >= 0) && (y < 8) )
                && ((x >= 0) && (x < 8)));
    }

    private boolean isValidBoardPiece(int y, int x,
                                      Piece[][] board) {
        return (board[y][x] != null);
    }


}
