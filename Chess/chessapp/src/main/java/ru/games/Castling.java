package ru.games;

import java.util.Map;

/**
 * Created by Андрей on 25.06.2015.
 */
public enum Castling {
    SR("SR"),
    LR("LR");
    private String castlingType;
    private static Map<String, Castling> castlingMap;

    static {
        for (Castling castling : values()) {
            castlingMap.put(castling.castlingType, castling);
        }
    }

    Castling(String castlingType) {
        this.castlingType = castlingType;
    }

    public static Castling getCastling(String castlingType) {
        return castlingMap.get(castlingType);
    }

}