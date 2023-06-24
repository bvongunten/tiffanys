package ch.nostromo.tiffanys.commons;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Game information including but not exclusively all PGN header information known (Tags & preamble comments) and
 * chess game state.
 */
@Data
@NoArgsConstructor
public class ChessGameInformation {

    // 7 Tags roster
    String event;
    String site;
    String date;
    String round;
    String white;
    String black;
    String result;

    ChessGameState chessGameState = ChessGameState.GAME_OPEN;

    Map<String, String> optionalTags = new LinkedHashMap<>();

    List<String> preambleComments = new ArrayList<>();


    public ChessGameInformation(String event, String site, String date, String round, String white, String black, String result) {
        this.event = event;
        this.site = site;
        this.date = date;
        this.round = round;
        this.white = white;
        this.black = black;
        this.result = result;

    }

    public ChessGameInformation copy() {
        ChessGameInformation chessGameInformationCopy = new ChessGameInformation(event, site, date, round, white, black, result);

        chessGameInformationCopy.setChessGameState(chessGameState);
        chessGameInformationCopy.getPreambleComments().addAll(preambleComments);
        chessGameInformationCopy.getOptionalTags().putAll(optionalTags);

        return chessGameInformationCopy;
    }


    /**
     * Select result / game state to return by following rules: ChessGameState > Result.
     * If one of both are empty, the other is used. If neither are set, GAME_OPEN is returned.
     *
     */
    private ChessGameState selechtChessGameState() {
        if (chessGameState != null) {
            return chessGameState;
        } else {
            if (result != null) {
                return ChessGameState.isSanGameState(result);
            } else {
                return ChessGameState.GAME_OPEN;
            }
        }
    }

    public String getResult() {
        return selechtChessGameState().getGetSanResult();
    }

    public ChessGameState getChessGameState() {
        return selechtChessGameState();
    }

}
