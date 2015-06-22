package ru.games;

import ru.games.pieces.Piece;

import java.util.Map;

/**
 * Created by Андрей on 21.06.2015.
 */
public class MoveNotationAnalyzer {
    private static Map<Character, Integer> columnMap;

    static {
        int index = 0;
        for (char column = 'a'; column <= 'h'; column++) {
            columnMap.put(column, index);
            index++;
        }
    }

    public static Move getMove(String coordinates, Piece[][] board) {
        if (coordinates.length() != 4) {
            throw new IllegalArgumentException();
        }
        Integer coordinatePieceX = columnMap.get(coordinates.charAt(0));
        Integer coordinatePieceY = Character.getNumericValue(coordinates.charAt(1)) - 1;
        if (coordinatePieceX != null
                && (coordinatePieceY >= 0 && coordinatePieceY < 8)
                && (board[coordinatePieceY][coordinatePieceX] != null)) {
            String string = coordinates.substring(2, 4);
            if (string.equals("lr")
                    || string.equals("rr")) {
                return new Move(new Coordinate(coordinatePieceY, coordinatePieceX),
                                string);
            } else {
                Integer coordinateTargetX = columnMap.get(coordinates.charAt(2));
                Integer coordinateTargetY = Character.getNumericValue(coordinates.charAt(3)) - 1;
                if (coordinateTargetX != null
                        && (coordinateTargetY >= 0 && coordinatePieceX < 8)) {
                    return new Move(new Coordinate(coordinatePieceY, coordinatePieceX),
                            new Coordinate(coordinateTargetY, coordinateTargetX));
                }
                throw new IllegalArgumentException();
            }
        }
        throw new IllegalArgumentException();
    }
}
