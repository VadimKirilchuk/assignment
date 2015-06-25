package ru.games;

import ru.games.pieces.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Created by Андрей on 20.06.2015.
 */
public class Game {
    private State gameState;
    private final Piece[][] board;
    private final Controller controller;
    private Scanner scanner;
    private Map<PieceColor, Map<Piece, Coordinate>> pieceMap;
    private Map<PieceColor, King> kingMap;

    public Game() {
        //поле для стэйт вместо объекта state
        gameState = State.WHITE_MOVING;
        board = new Piece[8][8];
        pieceMap = new HashMap<>();
        kingMap = new HashMap<>();
        scanner = new Scanner(System.in);
        controller = new Controller(board, pieceMap, kingMap);
    }

    private void init() {
        fillPieceMap();
        fillBoard();
    }

    private void fillPieceMap() {
        pieceMap.put(PieceColor.WHITE, new HashMap<Piece, Coordinate>());
        pieceMap.put(PieceColor.BLACK, new HashMap<Piece, Coordinate>());
    }

    private void fillBoard() {
        setPieces(0, PieceColor.WHITE);
        setPieces(7, PieceColor.BLACK);
    }

    private void setPieces(int line, PieceColor pieceColor) {
        board[line][0] = new Rook(pieceColor, PieceType.ROOK);
        board[line][1] = new Knight(pieceColor, PieceType.KNIGHT);
        board[line][2] = new Bishop(pieceColor, PieceType.BISHOP);
        board[line][3] = new Queen(pieceColor, PieceType.QUEEN);
        board[line][4] = new King(pieceColor, PieceType.KING);
        board[line][5] = new Bishop(pieceColor, PieceType.BISHOP);
        board[line][6] = new Knight(pieceColor, PieceType.KNIGHT);
        board[line][7] = new Rook(pieceColor, PieceType.ROOK);

        Map<Piece, Coordinate> map = pieceMap.get(pieceColor);
        for (int columnIndex = 0; columnIndex < 8; columnIndex++) {
            map.put(board[line][columnIndex], new Coordinate(line, columnIndex));
        }
        int index = Math.abs(line - 1);
        for (int columnIndex = 0; columnIndex < 8; columnIndex++) {
            board[index][columnIndex] = new Pawn(pieceColor, PieceType.PAWN);
            map.put(board[index][columnIndex], new Coordinate(index, columnIndex));
        }
        kingMap.put(pieceColor, (King) board[line][4]);
    }

    public void play() {
        init();
        launch();
    }

    public void launch() {
        try {
          
            while (gameState != State.END_OF_GAME) {
                System.out.println("Enter your move:");
                String stringCoordinates = getStringCoordinates();
                NotationAnalyzer analyzer = new NotationAnalyzer();
                Move pieceMove = analyzer.getMove(stringCoordinates, board);
                controller.processMove(pieceMove, gameState);
                //насколько эта конструкция нормально выглядит?
                if (controller.finishProcessing()) {
                    gameState = State.END_OF_GAME;
                } else {
                    setNextState();
                }
            }
        } catch (
                Exception e
                )

        {
            e.printStackTrace();
        }
    }

    public void setNextState() {
        if (gameState == State.WHITE_MOVING) {
            gameState = State.BLACK_MOVING;
        } else {
            gameState = State.WHITE_MOVING;
        }
    }

    public String getStringCoordinates() {
        if (scanner.hasNextLine()) {
            String userCoordinates = scanner.nextLine();
            return userCoordinates;
        }
        throw new IllegalStateException();
    }
    // можно ли избежать дубликации
}