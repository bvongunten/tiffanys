package ch.nostromo.tiffanys.commons.enums;

public enum GameState {

    // @formatter:off
    GAME_OPEN("*"), WIN_WHITE_MATES("1-0"), WIN_BLACK_MATES("0-1"), REMIS("1/2-1/2"), REMIS_BY_STALE_MATE("1/2-1/2"), REMIS_BY_MATERIAL("1/2-1/2"), REMIS_BY_THREE("1/2-1/2"), REMIS_BY_FIFTY("1/2-1/2");
    // @formatter:on

    private String value;

    private GameState(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static GameState getGameStateByValue(String state) {

        if (state.equals(GAME_OPEN.getValue())) {
            return GAME_OPEN;
        } else if (state.equals(WIN_WHITE_MATES.getValue())) {
            return WIN_WHITE_MATES;
        } else if (state.equals(WIN_BLACK_MATES.getValue())) {
            return WIN_BLACK_MATES;
        } else if (state.equals(REMIS.getValue())) {
            return REMIS;
        }

        throw new IllegalArgumentException("Unknown state : " + state);

    }

}