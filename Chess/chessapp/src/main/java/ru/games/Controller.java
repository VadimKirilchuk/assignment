package ru.games;

import ru.games.pieces.*;

import java.util.*;

/**
 * Created by Андрей on 20.06.2015.
 */
public class Controller {
    //приходится передавать в констуркторе данные
    // не будет ли избыточным их ранение в данном классе
    // броблема прежде всего с определнением шаха для которого необходим
    // информация о белых и черных сетах
    private final Piece[][] board;
    private Map<PieceColor, Map<Piece, Coordinate>> pieceMap;
    private Map<PieceColor, King> kingMap;
    private Piece lastProcessedPiece = null;

    public Controller(Piece[][] board, Map<PieceColor, Map<Piece,
            Coordinate>> pieceMap, Map<PieceColor,
            King> kingMap) {
        this.board = board;
        this.pieceMap = pieceMap;
        this.kingMap = kingMap;
    }

    //
    public void processMove(Move move, State currentGameState) {
        Coordinate pieceCoordinate = move.getPieceCoordinate();
        Piece piece = board[pieceCoordinate.getCoordinateY()][pieceCoordinate.getCoordinateX()];
        if (!isValidPieceColor(piece.getColor(), currentGameState)) {
            throw new IllegalStateException();
        }
        if (move.isRotateMove()) {
            Castling castlingType = move.getRotate();
            processPiece(pieceCoordinate, piece, castlingType);
        } else {
            Coordinate targetCoordinate = move.getTargetCoordinate();
            processPiece(pieceCoordinate, targetCoordinate, piece);
        }
        lastProcessedPiece = piece;
    }

    //todo: проверить условие
    private void processPiece(Coordinate pieceCoordinate, Piece piece,
                              Castling castlingType) {
        if ((piece.getType() != PieceType.KING)
                || !((King) piece).isAvailableCastling(castlingType, pieceCoordinate, board)
                || !isValidForCastling(castlingType, pieceCoordinate, piece)) {
            throw new IllegalStateException();
        }
        castlingKingPiece(pieceCoordinate, piece, castlingType);
    }

    private void processPiece(Coordinate pieceCoordinates,
                              Coordinate targetCoordinates, Piece piece) {
        switch (piece.getType()) {
            case PAWN:
                processPawnMove(pieceCoordinates, targetCoordinates, (Pawn) piece);
            default:
                processPieceMove(pieceCoordinates, targetCoordinates, piece);
        }
    }

    private void processPawnMove(Coordinate pieceCoordinates,
                                 Coordinate targetCoordinates,
                                 Pawn pawn) {
        if (pawn.isInPassingMove(pieceCoordinates, targetCoordinates,
                lastProcessedPiece, board)) {
            movePassingPawn(pieceCoordinates, targetCoordinates);
        } else {
            processPieceMove(pieceCoordinates, targetCoordinates, pawn);
        }
        finishPawnMoving(targetCoordinates, pieceCoordinates, pawn);
    }

    //todo: обязательно не забыть про то что эти два метода внутри должны кидать
// разные эксепшины, для того чтобы откатить изменения вернехо если ошибка в нижнем//сделал уже
    public void movePassingPawn(Coordinate pieceCoordinates,
                                Coordinate targetCoordinates) {
        //  movePiece(pieceCoordinates, targetCoordinates);
        try {
            movePiece(pieceCoordinates, targetCoordinates);
            removePassingPawn(pieceCoordinates, targetCoordinates);
        } catch (IllegalArgumentException e) {
            undoChanges(targetCoordinates, pieceCoordinates, null);
            throw new IllegalArgumentException();
        }
    }

    private void finishPawnMoving(Coordinate pawnCoordinates,
                                  Coordinate lastCoordinates, Pawn pawn) {
        int pawnY = pawnCoordinates.getCoordinateY();
        int pawnX = pawnCoordinates.getCoordinateX();
        if (pawn.isReadyToConvert(pawnY)) {
            PieceColor color = pawn.getColor();
            board[pawnY][pawnX] = new Queen(color, PieceType.QUEEN);
            removeFromPieceMap(pawn);
            addToPieceMap(board[pawnY][pawnX], pawnCoordinates);
        } else {
            pawn.setFirstMove();
            int lastY = lastCoordinates.getCoordinateY();
            int lastX = lastCoordinates.getCoordinateX();
            pawn.setImpassingMove(pawnY, pawnX, lastY, lastX);
        }
    }

    private void removePassingPawn(Coordinate pieceCoordinates,
                                   Coordinate targetCoordinates) {
        int coordinateY = pieceCoordinates.getCoordinateY();
        int coordinateX = targetCoordinates.getCoordinateX();
        Piece piece = board[coordinateY][coordinateX];
        removeFromPieceMap(piece);
        board[coordinateY][coordinateX] = null;
        Piece pawn = board[targetCoordinates.getCoordinateY()][targetCoordinates.getCoordinateX()];
        PieceColor pawnColor = pawn.getColor();
        PieceColor opponentPieceColor = getOpponentPieceColor(pawnColor);
        if (isCheck(pawnColor, opponentPieceColor)) {
            board[coordinateY][coordinateX] = piece;
            addToPieceMap(piece, new Coordinate(coordinateY, coordinateX));
            //неправильный ход, сударь
            throw new IllegalArgumentException();
        }
    }

    //нормальная ли конструкция
    private void processPieceMove(Coordinate pieceCoordinates,
                                  Coordinate targetCoordinates, Piece piece) {
        if (piece.isValidMoveTo(pieceCoordinates, targetCoordinates, board)) {
            Piece target = board[targetCoordinates.getCoordinateY()][targetCoordinates.getCoordinateX()];
            try {
                movePiece(pieceCoordinates, targetCoordinates);
            } catch (Exception e) {
                undoChanges(targetCoordinates, pieceCoordinates, target);
                throw new IllegalArgumentException();
            }
        } else {
            throw new IllegalStateException();
        }
    }

    private boolean isValidPieceColor(PieceColor pieceColor, State gameState) {
        if (gameState == State.WHITE_MOVING) {
            return PieceColor.WHITE == pieceColor;
        }
        return PieceColor.BLACK == pieceColor;
    }

    private boolean isCheckMate(List<Piece> checkList,
                                PieceColor attackPieceColor, PieceColor pieceColor) {
        Map<Piece, Coordinate> map = pieceMap.get(pieceColor);
        Map<Piece, Coordinate> attackPieceMap = pieceMap.get(attackPieceColor);
        return canDestroy(checkList, attackPieceMap, map)
                || canHide(checkList, attackPieceMap, map)
                || сдвинуть
    }

    //проветить нет и повтора с процуссмув
    private boolean canDestroy(List<Piece> checkList, Map<Piece, Coordinate> attackPieceMap,
                               Map<Piece, Coordinate> map) {
        Iterator<Piece> iterator = checkList.iterator();
        while (iterator.hasNext()) {
            Piece piece = iterator.next();
            Coordinate attackPieceCoordinates = attackPieceMap.get(piece);
            Set<Map.Entry<Piece, Coordinate>> entrySet = map.entrySet();
            for (Map.Entry<Piece, Coordinate> entry : entrySet) {
                Piece pieceEntry = entry.getKey();
                Coordinate pieceCoordinates = entry.getValue();
                if(removeFromCheckList(pieceEntry,pieceCoordinates,attackPieceCoordinates)){
                    iterator.remove();
                    break;
                }
                /*
                if (pieceEntry.isValidMoveTo(pieceCoordinates, coordinates, board)) {
                    Piece target = board[coordinates.getCoordinateY()][coordinates.getCoordinateX()];
                    try {
                        movePiece(pieceCoordinates, coordinates);
                        iterator.remove();
                        break;
                    } catch (Exception e) {
                        undoChanges(coordinates, pieceCoordinates, target);
                    }
                }
                */
            }
        }
        return checkList.size() == 0;
    }

    private boolean removeFromCheckList(Piece pieceEntry, Coordinate pieceCoordinates,
                                        Coordinate attackPieceCoordinates) {

        if (pieceEntry.isValidMoveTo(pieceCoordinates, attackPieceCoordinates, board)) {
            Piece target = board[attackPieceCoordinates.getCoordinateY()][attackPieceCoordinates.getCoordinateX()];
            try {
                movePiece(pieceCoordinates, attackPieceCoordinates);
                return true;
            } catch (Exception e) {
                undoChanges(attackPieceCoordinates, pieceCoordinates, target);
            }
        }
        return false;
    }

    private boolean canHide(List<Piece> checkList, Map<Piece, Coordinate> attackPieceMap,
                            Map<Piece, Coordinate> map) {

        Iterator<Piece> iterator = checkList.iterator();
        while (iterator.hasNext()) {
            Piece piece = iterator.next();
            PieceType pieceType = piece.getType();
        }
    }

    private boolean canMoveKing() {

    }

    //проверить необходимость дефаулта
    private PieceColor getOpponentPieceColor(PieceColor pieceColor) {
        if (pieceColor == PieceColor.WHITE) {
            return PieceColor.BLACK;
        }
        return PieceColor.WHITE;
    }

    private boolean isValidForCastling(Castling castlingType,
                                       Coordinate pieceCoordinate,
                                       Piece king) {
        PieceColor kingColor = king.getColor();
        PieceColor opponentPieceColor = getOpponentPieceColor(kingColor);
        Map<Piece, Coordinate> map = pieceMap.get(opponentPieceColor);
        int coordinateY = pieceCoordinate.getCoordinateY();
        int coordinateX = pieceCoordinate.getCoordinateX();
        int amount = (castlingType == Castling.SR) ? 1 : -1;
        for (int index = coordinateX; Math.abs(index - 4) <= 2; index += amount) {
            Coordinate coordinate = new Coordinate(coordinateY, index);
            if (getListCheckPieces(coordinate, map).size() > 0) {
                return false;
            }
        }
        return true;
    }

    private void castlingKingPiece(Coordinate kingCoordinate, Piece king,
                                   Castling castlingType) {
        int coordinateY = kingCoordinate.getCoordinateY();
        int coordinateX = kingCoordinate.getCoordinateX();

        if (castlingType == Castling.SR) {
            setPiece(coordinateY, coordinateX, coordinateY, coordinateX + 2);
            setPiece(coordinateY, 7, coordinateY, coordinateX + 1);
        } else {
            setPiece(coordinateY, coordinateX, coordinateY, coordinateX - 2);
            setPiece(coordinateY, 0, coordinateY, coordinateX - 1);
        }
    }

    private void setPiece(int pieceY, int pieceX,
                          int targetY, int targetX) {
        board[targetY][targetX] = board[pieceY][pieceX];
        board[pieceY][pieceX] = null;
    }

    private List<Piece> getCheckPieces(PieceColor pieceColor,
                                       PieceColor opponentPieceColor) {

        King king = kingMap.get(pieceColor);
        Map<Piece, Coordinate> map = pieceMap.get(pieceColor);
        Map<Piece, Coordinate> opponentPieceMap = pieceMap.get(opponentPieceColor);
        Coordinate kingCoordinate = map.get(king);
        return getListCheckPieces(kingCoordinate, opponentPieceMap);
    }

    //piece
    private List<Piece> getListCheckPieces(Coordinate kingCoordinate, Map<Piece,
            Coordinate> opponentPieceMap) {
        List<Piece> checkPiecesList = new ArrayList<>();
        Set<Map.Entry<Piece, Coordinate>> opponentPieces = opponentPieceMap.entrySet();
        for (Map.Entry<Piece, Coordinate> entry : opponentPieces) {
            Piece piece = entry.getKey();
            Coordinate pieceCoordinate = entry.getValue();
            if (piece.isValidMoveTo(pieceCoordinate, kingCoordinate, board)) {
                checkPiecesList.add(piece);
            }
        }
        return checkPiecesList;
    }

    private boolean isCheck(PieceColor pieceColor,
                            PieceColor opponentPieceColor) {
        return getCheckPieces(pieceColor, opponentPieceColor).size() > 0;
    }
//проверить , необходио везде использавть метод если нужно

    private void removeFromPieceMap(Piece piece) {
        PieceColor pieceColor = piece.getColor();
        Map<Piece, Coordinate> map = pieceMap.get(pieceColor);
        map.remove(piece);
    }

    private void addToPieceMap(Piece piece,
                               Coordinate pieceCoordinates) {
        PieceColor pieceColor = piece.getColor();
        Map<Piece, Coordinate> map = pieceMap.get(pieceColor);
        map.put(piece, pieceCoordinates);
    }

    //метод перемещает данные но если ошибка должны сами вызвать undo
    private void movePiece(Coordinate pieceCoordinates,
                           Coordinate targetCoordinates) {
        int pieceCoordinateY = pieceCoordinates.getCoordinateY();
        int pieceCoordinateX = pieceCoordinates.getCoordinateX();
        int targetCoordinateY = targetCoordinates.getCoordinateY();
        int targetCoordinateX = targetCoordinates.getCoordinateX();
        Piece target = board[targetCoordinateY][targetCoordinateX];
        Piece piece = board[pieceCoordinateY][pieceCoordinateX];
        setPiece(pieceCoordinateY, pieceCoordinateX,
                targetCoordinateY, targetCoordinateX);
        /*
        board[targetCoordinateY][targetCoordinateX] = piece;
        board[pieceCoordinateY][pieceCoordinateX] = null;
        */
        //удаляем target=null нужно ли добавить проверку ?
        removeFromPieceMap(target);
        addToPieceMap(piece, new Coordinate(targetCoordinateY, targetCoordinateX));
        PieceColor pieceColor = piece.getColor();
        PieceColor opponentPieceColor = getOpponentPieceColor(pieceColor);
        if (isCheck(pieceColor, opponentPieceColor)) {
            //неправильный ход, сударь
            throw new IllegalArgumentException();
        }
    }

    //undoChanges(targetCoordinates, pieceCoordinates, target);
    //координаты!
    private void undoChanges(Coordinate pieceCoordinates,
                             Coordinate targetCoordinates,
                             Piece target) {
        int pieceCoordinateY = pieceCoordinates.getCoordinateY();
        int pieceCoordinateX = pieceCoordinates.getCoordinateX();
        int targetCoordinateY = targetCoordinates.getCoordinateY();
        int targetCoordinateX = targetCoordinates.getCoordinateX();

        Piece piece = board[pieceCoordinateY][pieceCoordinateX];
        board[pieceCoordinateY][pieceCoordinateX] = target;
        board[targetCoordinateY][targetCoordinateX] = piece;
        addToPieceMap(piece, new Coordinate(targetCoordinateY, targetCoordinateX));
        if (target != null) {
            addToPieceMap(target, new Coordinate(pieceCoordinateY, pieceCoordinateX));
        }
    }

    public boolean finishProcessing() {
        PieceColor color = lastProcessedPiece.getColor();
        PieceColor opponentColor = getOpponentPieceColor(color);
        List<Piece> checkList = getCheckPieces(opponentColor, color);

        //подходит ли конструкция

        switch (checkList.size()) {
            case 0:
                return//пат
            //50 ход
            //3 посл хода

            default:
                if (isCheckMate(checkList, color, opponentColor)) {
                    return false;
                }
                return
            //50 ход
            //3 посл хода

        }
    }
}
