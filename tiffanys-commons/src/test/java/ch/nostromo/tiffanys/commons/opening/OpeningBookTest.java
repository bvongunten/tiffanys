package ch.nostromo.tiffanys.commons.opening;

import ch.nostromo.tiffanys.commons.ChessGame;
import ch.nostromo.tiffanys.commons.move.Move;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OpeningBookTest {

    @Test
    void openingBookTestBestMovesOnly() {

        OpeningBook openingBook = new OpeningBook();
        ChessGame chessGame = new ChessGame();

        while (true) {
            Move move = openingBook.queryBestOpening(chessGame.createFen());

            if (move != null) {
                chessGame.applyMove(move);
            } else {
                break;
            }

        }

        assertEquals("r1bq1rk1/5ppp/p1pb4/1p1n4/8/1BPP4/PP3PPP/RNBQR1K1 b - - 0 13", chessGame.createFen().toString());
    }

}
