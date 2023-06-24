package ch.nostromo.tiffanys.commons.opening;

import ch.nostromo.tiffanys.commons.format.FenFormat;
import ch.nostromo.tiffanys.commons.move.Move;
import ch.nostromo.tiffanys.commons.opening.polyglot.Polyglot;

import java.util.Map;

/**
 * Opening Book based on polyglot bin files. Input is a FenFormat, output a Move object.
 */
public class OpeningBook {

    private final Map<Long, OpeningBookMoveSet> moveSets;

    public OpeningBook() {
        this("/OB_Perfect2023.bin");
    }

    public OpeningBook(String fileName) {
       moveSets = Polyglot.loadOpeningBook(fileName);
    }

    public Move queryRandomOpening(FenFormat fenFormat) {
        return queryOpening(fenFormat, false);
    }

    public Move queryBestOpening(FenFormat fenFormat) {
        return queryOpening(fenFormat, true);
    }

    private Move queryOpening(FenFormat fenFormat, boolean best) {

        Long position = Polyglot.getPolyglotZobristHash(fenFormat);

        OpeningBookMoveSet moveSet = this.moveSets.get(position);
        if (moveSet != null) {
            if (best) {
                return moveSet.getBestMove().getMove(fenFormat);
            } else {
                return moveSet.getRandomMove().getMove(fenFormat);
            }
        }

        return null;
    }




}
