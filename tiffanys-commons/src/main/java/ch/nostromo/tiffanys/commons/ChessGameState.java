package ch.nostromo.tiffanys.commons;

import lombok.Getter;

/**
 * Game States
 */
@Getter
public enum ChessGameState {

    // @formatter:off
    GAME_OPEN("*"),
    WHITE_WIN("1-0"),
    WHITE_WIN_BY_MATE("1-0"),
    WHITE_WIN_BY_RESIGNATION("1-0"),
    BLACK_WIN("0-1"),
    BLACK_WIN_BY_MATE("0-1"),
    BLACK_WIN_BY_RESIGNATION("0-1"),
    REMIS("1/2-1/2"),
    REMIS_BY_STALE_MATE("1/2-1/2"),
    REMIS_BY_MATERIAL("1/2-1/2"),
    REMIS_BY_THREE("1/2-1/2"),
    REMIS_BY_FIFTY("1/2-1/2");
    // @formatter:on

    private final String result;

    ChessGameState(String result) {
        this.result = result;
    }

    /**
     * Returns a GameState by given SAN String ("*", "1-0", "0-1", "1/2-1/2"). </p>
     * Remark: All kind of wins or remis are given back by their base values: BLACK_WIN, WHITE_WIN, REMIS
     *
     * @param state
     * @return
     */
    public static ChessGameState getGameStateByValue(String state) {

        // Select the basic value
        if (state.equals(GAME_OPEN.getResult())) {
            return GAME_OPEN;
        } else if (state.equals(WHITE_WIN.getResult())) {
            return WHITE_WIN;
        } else if (state.equals(BLACK_WIN.getResult())) {
            return BLACK_WIN;
        } else if (state.equals(REMIS.getResult())) {
            return REMIS;
        }

        throw new ChessGameException("Unknown state : " + state);

    }

}
