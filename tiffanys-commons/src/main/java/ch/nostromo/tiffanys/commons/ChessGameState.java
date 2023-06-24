package ch.nostromo.tiffanys.commons;

import lombok.Getter;

/**
 * Game States
 */
@Getter
public enum ChessGameState {

    GAME_OPEN(TextConstants.OPEN, false),
    WHITE_WIN(TextConstants.WHITE_WIN, true),
    WHITE_WIN_BY_MATE(TextConstants.WHITE_WIN, true),
    WHITE_WIN_BY_RESIGNATION(TextConstants.WHITE_WIN, true),
    BLACK_WIN(TextConstants.BLACK_WIN, true),
    BLACK_WIN_BY_MATE(TextConstants.BLACK_WIN, true),
    BLACK_WIN_BY_RESIGNATION(TextConstants.BLACK_WIN, true),
    DRAW(TextConstants.DRAW, true),
    DRAW_BY_STALE_MATE(TextConstants.DRAW, true),
    DRAW_BY_MATERIAL(TextConstants.DRAW, false),
    DRAW_BY_THREE(TextConstants.DRAW, false),
    DRAW_BY_FIFTY(TextConstants.DRAW, false);

    private final String getSanResult;
    private final boolean isDecided;

    ChessGameState(String getSanResult, boolean isDecided) {
        this.getSanResult = getSanResult;
        this.isDecided = isDecided;
    }

    // Static inner class avoids initialization order issues
    private static class TextConstants {
        static final String OPEN = "*";
        static final String WHITE_WIN = "1-0";
        static final String BLACK_WIN = "0-1";
        static final String DRAW = "1/2-1/2";
    }

    /**
     * Returns a GameState by given SAN String ("*", "1-0", "0-1", "1/2-1/2"). </p>
     * Remark: All kind of wins or draw are given back by their base values: BLACK_WIN, WHITE_WIN, DRAW </p>
     * Returns null if no known
     *
     * @param state token
     * @return GameState or null
     */
    public static ChessGameState isSanGameState(String state) {

        // Select the basic value
        if (state.equals(GAME_OPEN.getGetSanResult())) {
            return GAME_OPEN;
        } else if (state.equals(WHITE_WIN.getGetSanResult())) {
            return WHITE_WIN;
        } else if (state.equals(BLACK_WIN.getGetSanResult())) {
            return BLACK_WIN;
        } else if (state.equals(DRAW.getGetSanResult())) {
            return DRAW;
        }

        return null;

    }

}
