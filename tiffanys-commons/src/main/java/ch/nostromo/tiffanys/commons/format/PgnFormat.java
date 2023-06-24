package ch.nostromo.tiffanys.commons.format;

import ch.nostromo.tiffanys.commons.ChessGameInformation;
import ch.nostromo.tiffanys.commons.ChessGameState;
import ch.nostromo.tiffanys.commons.format.pgn.PgnMove;
import ch.nostromo.tiffanys.commons.format.pgn.PgnParser;
import ch.nostromo.tiffanys.commons.format.pgn.PgnWriter;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;

/**
 * PGN Format can pre-parse a PGN String (Tags & moves string). The parsing is done during construction.</p>
 * Is dependent on PgnParser and PgnWriter
 */
@Data
@NoArgsConstructor
public class PgnFormat {

    ChessGameInformation chessGameInformation;

    // List ov moves including comments and variants
    List<PgnMove> moves = new ArrayList<>();

    /**
     * Create pgn format based on full parsing of the given pgn
     * @param pgn game
     */
    public PgnFormat(String pgn) {
        PgnParser pgnParser = new PgnParser(pgn);

        Map<String, String> tags = new HashMap<>(pgnParser.getTags());
        this.chessGameInformation = new ChessGameInformation();
        this.chessGameInformation.setEvent(tags.remove("Event"));
        this.chessGameInformation.setSite(tags.remove("Site"));
        this.chessGameInformation.setDate(tags.remove("Date"));
        this.chessGameInformation.setRound(tags.remove("Round"));
        this.chessGameInformation.setWhite(tags.remove("White"));
        this.chessGameInformation.setBlack(tags.remove("Black"));
        this.chessGameInformation.setResult(tags.remove("Result"));

        this.chessGameInformation.getOptionalTags().putAll(tags);
        this.chessGameInformation.setChessGameState(pgnParser.extractChessGameState());
        this.chessGameInformation.setPreambleComments(pgnParser.extractPreambleComments());

        this.moves = pgnParser.extractMoves();
    }



    @Override
    public String toString() {
        return PgnWriter.createPgn(this);
    }

}
