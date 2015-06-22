package ru.games;

import ru.games.pieces.*;

import java.util.Map;
import java.util.Scanner;
import java.util.Set;

/**
 * Created by Андрей on 20.06.2015.
 */
public class Game {
    private final GameState gameState;
    private final Piece[][] board;
    private final Controller controller;
    private Scanner scanner;
    private Map<Piece,Coordinate> whitePiecesMap;
    private Map<Piece,Coordinate> blackPiecesMap;
    private Piece whiteKing;
    private Piece blackKing;

    public Game() {
        gameState = new GameState();
        board = new Piece[8][8];
        controller = new Controller(board, whitePiecesMap, whiteKing,
                blackPiecesMap, blackKing, gameState);
        scanner = new Scanner(System.in);
    }

    private void init() {
        setWhitePieces();
        setBlackPieces();
    }

    private void setWhitePieces() {
        board[0][0] = new Rook(PieceColor.WHITE, PieceType.ROOK);
        board[0][1] = new Knight(PieceColor.WHITE, PieceType.KNIGHT);
        board[0][2] = new Bishop(PieceColor.WHITE, PieceType.BISHOP);
        board[0][3] = new Queen(PieceColor.WHITE, PieceType.QUEEN);
        board[0][4] = new King(PieceColor.WHITE, PieceType.KING);
        board[0][5] = new Bishop(PieceColor.WHITE, PieceType.BISHOP);
        board[0][6] = new Knight(PieceColor.WHITE, PieceType.KNIGHT);
        board[0][7] = new Rook(PieceColor.WHITE, PieceType.ROOK);
        for (int columnIndex = 0; columnIndex < 8; columnIndex++) {
            whitePiecesMap.put(board[0][columnIndex], new Coordinate(0, columnIndex));
        }
        for (int columnIndex = 0; columnIndex < 8; columnIndex++) {
            board[1][columnIndex] = new Pawn(PieceColor.WHITE, PieceType.PAWN);
            whitePiecesMap.put(board[1][columnIndex], new Coordinate(1, columnIndex));
        }
        whiteKing = board[0][4];

    }

    private void setBlackPieces() {
        board[7][0] = new Rook(PieceColor.BLACK, PieceType.ROOK);
        board[7][1] = new Knight(PieceColor.BLACK, PieceType.KNIGHT);
        board[7][2] = new Bishop(PieceColor.BLACK, PieceType.BISHOP);
        board[7][3] = new Queen(PieceColor.BLACK, PieceType.QUEEN);
        board[7][4] = new King(PieceColor.BLACK, PieceType.KING);
        board[7][5] = new Bishop(PieceColor.BLACK, PieceType.BISHOP);
        board[7][6] = new Knight(PieceColor.BLACK, PieceType.KNIGHT);
        board[7][7] = new Rook(PieceColor.BLACK, PieceType.ROOK);
        for (int columnIndex = 0; columnIndex < 8; columnIndex++) {
            blackPiecesMap.put(board[7][columnIndex], new Coordinate(7,columnIndex));
        }
        for (int columnIndex = 0; columnIndex < 8; columnIndex++) {
            board[6][columnIndex] = new Pawn(PieceColor.BLACK, PieceType.PAWN);
            blackPiecesMap.put(board[6][columnIndex], new Coordinate(6,columnIndex));
        }
        blackKing = board[7][4];
    }

    public void play() {
        init();
        launch();
    }

    public void launch() {
        try {
            //подходит?
            while (gameState.getState() != State.END_OF_GAME) {
                System.out.println("Enter your move:");
                String stringCoordinates = getStringCoordinates();
                Move pieceMove = MoveNotationAnalyzer.getMove(stringCoordinates, board);

                switch (gameState.getState()) {
                    case WHITE_MOVING:
                        controller.processWhitePiece(pieceMove);
                        break;
                    case BLACK_MOVING:
                        controller.processBlackPiece(pieceMove);
                        break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
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