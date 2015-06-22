package ru.games;

import ru.games.pieces.King;
import ru.games.pieces.Piece;
import ru.games.pieces.PieceColor;
import ru.games.pieces.PieceType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Андрей on 20.06.2015.
 */
public class Controller {
    //приходится передавать в констуркторе данные
    // не будет ли избыточным их ранение в данном классе
    // броблема прежде всего с определнением шаха для которого необходим
    // информация о белых и черных сетах
    private final GameState gameState;
    private final Piece[][] board;
    private final Map<Piece, Coordinate> whitePiecesMap;
    private final Map<Piece, Coordinate> blackPiecesMap;
    private final Piece whiteKing;
    private final Piece blackKing;

    public Controller(Piece[][] board, Map<Piece, Coordinate> whitePiecesMap,
                      Piece whiteKing, Map<Piece, Coordinate> blackPiecesMap,
                      Piece blackKing, GameState gameState) {
        this.board = board;
        this.gameState = gameState;
        this.whitePiecesMap = whitePiecesMap;
        this.blackPiecesMap = blackPiecesMap;
        this.blackKing = blackKing;
        this.whiteKing = whiteKing;
    }

    public void processWhitePiece(Move move) {
        Coordinate pieceCoordinate = move.getPieceCoordinate();
        Piece piece = board[pieceCoordinate.getCoordinateY()][pieceCoordinate.getCoordinateX()];
        switch (piece.getColor()) {
            case WHITE:
                if (move.isRotateMove()) {
                    String rotate = move.getRotate();
                    processPiece(pieceCoordinate, rotate);
                } else {
                    Coordinate targetCoordinate = move.getTargetCoordinate();
                    processPiece(pieceCoordinate, targetCoordinate);
                }
                break;
            case BLACK:
                throw new IllegalStateException();
        }
    }

    public void processBlackPiece(Move move) {
        Coordinate pieceCoordinate = move.getPieceCoordinate();
        Piece piece = board[pieceCoordinate.getCoordinateY()][pieceCoordinate.getCoordinateX()];
        switch (piece.getColor()) {
            case BLACK:
                if (move.isRotateMove()) {
                    String rotate = move.getRotate();
                    processPiece(pieceCoordinate, rotate);
                } else {
                    Coordinate targetCoordinate = move.getTargetCoordinate();
                    processPiece(pieceCoordinate, targetCoordinate);
                }
                break;
            case WHITE:
                throw new IllegalStateException();
        }
    }

    /*
        private void processPiece(Move move) {

            Piece piece = board[move.getPieceCoordinateY()][move.getPieceCoordinateX()];
            if ((piece.getType() != PieceType.KING) && move.isRotateMove()) {
                throw new IllegalStateException();
            }
            if ((piece.getType() == PieceType.KING) && move.isRotateMove()) {
                King kingPiece = (King) piece;
                if (kingPiece.isValidRotate()) {
                    kingPiece.rotateTo();
                }
                throw new IllegalStateException();
            } else {
                if (piece.isValidMoveTo()) {
                    piece.moveTo();
                }
                throw new IllegalStateException();
            }
        }
        */
    private void processPiece(Coordinate pieceCoordinate,
                              String rotate) {

        Piece piece = board[pieceCoordinate.getCoordinateY()][pieceCoordinate.getCoordinateX()];
        if ((piece.getType() != PieceType.KING) || !(((King) piece).isValidRotate())) {
            throw new IllegalStateException();
        }
        (King) piece.rotateTo();
    }

    private void processPiece(Coordinate pieceCoordinate,
                              Coordinate targetCoordinate) {
        Piece piece = board[pieceCoordinate.getCoordinateY()][pieceCoordinate.getCoordinateX()];
        if (piece.isValidMoveTo()) {
            movePiece(pieceCoordinate, targetCoordinate);
        }
        throw new IllegalStateException();
    }

    public List checkWhiteCheck() {
        List<Coordinate> checkPieceList = new ArrayList<>();
        Coordinate whiteKingCoordinate = whitePiecesMap.get(whiteKing);
        Set<Map.Entry<Piece, Coordinate>> whitePieces = blackPiecesMap.entrySet();
        for (Map.Entry<Piece, Coordinate> entry : whitePieces) {
            Piece piece = entry.getKey();
            Coordinate pieceCoordinate = entry.getValue();
            if (piece.isValidMoveTo(pieceCoordinate, whiteKingCoordinate,board)) {
                checkPieceList.add(pieceCoordinate);
            }
        }
        return checkPieceList;
    }
private boolean checkMate(List checkList, Piece king){

}
    public List checkBlackCheck() {
        List<Coordinate> checkPieceList = new ArrayList<>();
        Coordinate blackKingCoordinate = blackPiecesMap.get(blackKing);
        Set<Map.Entry<Piece, Coordinate>> whitePieces = whitePiecesMap.entrySet();
        for (Map.Entry<Piece, Coordinate> entry : whitePieces) {
            Piece piece = entry.getKey();
            Coordinate pieceCoordinate = entry.getValue();
            if (piece.isValidMoveTo(pieceCoordinate, blackKingCoordinate,board)) {
                checkPieceList.add(pieceCoordinate);
            }
        }
        return checkPieceList;
    }

    private boolean isCheck(PieceColor color) {
        switch (color) {
            case WHITE:
                return checkWhiteCheck().size() > 0;
            case BLACK:
                return checkBlackCheck().size() > 0;
            default:
                throw new IllegalStateException();
        }
    }

    public void removeFromPieceMap(Piece piece) {
        PieceColor color = piece.getColor();
        switch (color) {
            case WHITE:
                whitePiecesMap.remove(piece);
                break;
            case BLACK:
                blackPiecesMap.remove(piece);
                break;
        }
    }

    public void addToPieceMap(Piece piece, Coordinate coordinate) {
        PieceColor color = piece.getColor();
        switch (color) {
            case WHITE:
                whitePiecesMap.put(piece, coordinate);
                break;
            case BLACK:
                blackPiecesMap.put(piece, coordinate);
                break;
        }
    }

    private void movePiece(Coordinate pieceCoordinate,
                           Coordinate targetCoordinate) {
        int pieceCoordinateY = pieceCoordinate.getCoordinateY();
        int pieceCoordinateX = pieceCoordinate.getCoordinateX();
        int targetCoordinateY = targetCoordinate.getCoordinateY();
        int targetCoordinateX = targetCoordinate.getCoordinateX();
        Piece target = board[targetCoordinateY][targetCoordinateX];
        Piece piece = board[pieceCoordinateY][pieceCoordinateX];
        board[targetCoordinateY][targetCoordinateX] = piece;
        board[pieceCoordinateY][pieceCoordinateX] = null;
        //удаляем target=null нужно ли добавить проверку ?
        removeFromPieceMap(target);
        addToPieceMap(piece, new Coordinate(targetCoordinateY, targetCoordinateX));
        if (isCheck(piece.getColor())) {
            undoChanges(
                    targetCoordinateY, targetCoordinateX, pieceCoordinateY,
                    pieceCoordinateX, target);
            //неправильный ход, сударь
            throw new IllegalArgumentException();
        }
    }

    private void undoChanges(int pieceCoordinateY, int pieceCoordinateX,
                             int targetCoordinateY, int targetCoordinateX,
                             Piece target) {
        Piece piece = board[pieceCoordinateY][pieceCoordinateX];
        board[pieceCoordinateY][pieceCoordinateX] = target;
        board[targetCoordinateY][targetCoordinateX] = piece;
        addToPieceMap(piece, new Coordinate(targetCoordinateY, targetCoordinateX));
        if (target != null) {
            addToPieceMap(target, new Coordinate(pieceCoordinateY, pieceCoordinateX));
        }
    }
}
