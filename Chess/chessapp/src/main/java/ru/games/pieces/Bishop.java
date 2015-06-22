package ru.games.pieces;

import ru.games.Coordinate;

/**
 * Created by Андрей on 20.06.2015.
 */
public class Bishop extends Piece {
    public Bishop(PieceColor color,PieceType type){
        super(color,type);
    }


    @Override
    public boolean isValidMoveTo(Coordinate pieceCoordinate,
                                 Coordinate targetCoordinate, Piece[][] board) {
// сравнить с вариантом на стэкоферфлоу
        int pieceCoordinateY = pieceCoordinate.getCoordinateY();
        int pieceCoordinateX = pieceCoordinate.getCoordinateX();
        int targetCoordinateY = targetCoordinate.getCoordinateY();
        int targetCoordinateX = targetCoordinate.getCoordinateX();

        if ((pieceCoordinateX != targetCoordinateX)
                && (pieceCoordinateY != targetCoordinateY)) {
            return false;
        }

        //верен ли перенос?

        if(Math.abs(targetCoordinateY-pieceCoordinateY)
                !=Math.abs(targetCoordinateX-pieceCoordinateX)){
            return false;
        }

return checkPosition(pieceCoordinateY, pieceCoordinateX,
                     targetCoordinateY, targetCoordinateX,
                     board);


    }


    private boolean checkPosition(int pieceCoordinateY, int pieceCoordinateX,
                                  int targetCoordinateY, int targetCoordinateX,
                                  Piece[][] board){
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
        if ((board[indexY][indexX] != null)
                && (board[indexY][indexX].color
                == board[pieceCoordinateY][pieceCoordinateX].color)) {
            return false;
        }
        return true;
    }
}
