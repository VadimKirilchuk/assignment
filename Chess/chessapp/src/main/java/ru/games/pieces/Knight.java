package ru.games.pieces;

/**
 * Created by Андрей on 20.06.2015.
 */
public class Knight extends Piece {
    public Knight(PieceColor color, PieceType type) {
        super(color, type);
    }
@Override
    protected boolean isPieceOnTrajectory(int pieceCoordinateY, int pieceCoordinateX,
                                          int targetCoordinateY, int targetCoordinateX) {
      /*  int[] x = {1, -1, 1, -1, 2, -2, 2, -2};
        int[] y = {-2, -2, 2, 2, -1, -1, 1, 1};
        for (int index = 0; index < 8; index++) {
            if (isTheSameCoordinate(pieceCoordinateY + y[index], pieceCoordinateX + x[index],
                                    targetCoordinateY, targetCoordinateX)) {
                return true;
            }
        }*/
        return ((Math.abs(targetCoordinateY - pieceCoordinateY)==2)
                &&(Math.abs(targetCoordinateX-pieceCoordinateX)==1))
                ||((Math.abs(targetCoordinateY - pieceCoordinateY)==1)
                &&(Math.abs(targetCoordinateX-pieceCoordinateX)==2));
    }
    @Override
    protected boolean checkPosition(int pieceCoordinateY, int pieceCoordinateX,
                                    int targetCoordinateY, int targetCoordinateX,
                                    Piece[][] board) {
       return isValidTargetPosition(board[targetCoordinateY][targetCoordinateX]);
    }
}
