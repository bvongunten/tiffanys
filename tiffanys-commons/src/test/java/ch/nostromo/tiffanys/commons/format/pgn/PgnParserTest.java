package ch.nostromo.tiffanys.commons.format.pgn;

import ch.nostromo.tiffanys.commons.ChessGameState;
import ch.nostromo.tiffanys.commons.Side;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PgnParserTest
{

    @Test
    void testExtractChessGameState() {
        assertEquals(ChessGameState.GAME_OPEN, new PgnParser("*").extractChessGameState());
        assertEquals(ChessGameState.GAME_OPEN, new PgnParser("1. e4 e5 2. Nf3 Nc6 *").extractChessGameState());
        assertEquals(ChessGameState.WHITE_WIN, new PgnParser("1-0").extractChessGameState());
        assertEquals(ChessGameState.WHITE_WIN, new PgnParser("1. e4 e5 2. Nf3 Nc6 1-0").extractChessGameState());
        assertEquals(ChessGameState.BLACK_WIN, new PgnParser("0-1").extractChessGameState());
        assertEquals(ChessGameState.BLACK_WIN, new PgnParser("1. e4 e5 2. Nf3 Nc6 0-1").extractChessGameState());
    }

    @Test
    void testExtractComments() {
        assertTrue(new PgnParser("*").extractPreambleComments().isEmpty());

        List<String> comments = new PgnParser("{Hello World} {Hello you too} 1. e4 { Another comment } *") .extractPreambleComments();

        assertEquals(2, comments.size());
        assertEquals("Hello World", comments.get(0));
        assertEquals("Hello you too", comments.get(1));

    }


    @Test
    void testMoveParsing() {
        List<PgnMove> moves = new PgnParser("1. e4 e6 2. Nf3 Nc6 *").extractMoves();

        assertEquals(4, moves.size());
        assertEquals("e4", moves.getFirst().getSanMove());
        assertEquals(Side.WHITE, moves.getFirst().getSide());
        assertEquals(1, moves.getFirst().getMoveNumber());

        assertEquals("e6", moves.get(1).getSanMove());
        assertEquals(Side.BLACK, moves.get(1).getSide());
        assertEquals(1, moves.get(1).getMoveNumber());

        assertEquals("Nf3", moves.get(2).getSanMove());
        assertEquals(Side.WHITE, moves.get(2).getSide());
        assertEquals(2, moves.get(2).getMoveNumber());

        assertEquals("Nc6", moves.get(3).getSanMove());
        assertEquals(Side.BLACK, moves.get(3).getSide());
        assertEquals(2, moves.get(3).getMoveNumber());
    }

    @Test
    void testNagParsing() {
        List<PgnMove> moves = new PgnParser("1. e4 e6!! 2. Nf3? Nc6$5 *").extractMoves();
        assertEquals(4, moves.size());

        assertEquals("e6", moves.get(1).getSanMove());
        assertEquals("!!", moves.get(1).getNag());

        assertEquals("Nf3", moves.get(2).getSanMove());
        assertEquals("?", moves.get(2).getNag());

        assertEquals("Nc6", moves.get(3).getSanMove());
        assertEquals("$5", moves.get(3).getNag());
    }


    @Test
    void testCommentsOnMove() {
        List<PgnMove> moves = new PgnParser("{Some Noise} 1. e4 { Comment 1 } e5 2. Nf3 { Comment 3 } { Comment Z } Nc6 0-1").extractMoves();

        assertEquals(4, moves.size());
        assertEquals("Comment 1", moves.getFirst().getComments().getFirst());
        assertEquals("Comment 3", moves.get(2).getComments().getFirst());
        assertEquals("Comment Z", moves.get(2).getComments().get(1));
    }

    @Test
    void testVariations() {
        List<PgnMove> moves = new PgnParser("1. e4 (1. e3 e6) 1... e5 2. Nf3 Nc6 (2... h6 3. a3)").extractMoves();
        assertEquals(4, moves.size());

        assertEquals(2, moves.getFirst().getVariations().getFirst().getMoves().size());

        assertEquals("e3", moves.getFirst().getVariations().getFirst().getMoves().getFirst().getSanMove());
        assertEquals(1, moves.getFirst().getVariations().getFirst().getMoves().getFirst().getMoveNumber());
        assertEquals("e6", moves.getFirst().getVariations().getFirst().getMoves().get(1).getSanMove());
        assertEquals(1, moves.getFirst().getVariations().getFirst().getMoves().get(1).getMoveNumber());

        assertEquals("h6", moves.get(3).getVariations().getFirst().getMoves().getFirst().getSanMove());
        assertEquals(2, moves.get(3).getVariations().getFirst().getMoves().getFirst().getMoveNumber());
        assertEquals("a3", moves.get(3).getVariations().getFirst().getMoves().get(1).getSanMove());
        assertEquals(3, moves.get(3).getVariations().getFirst().getMoves().get(1).getMoveNumber());
    }

    @Test
    void testNestedVariations() {
        List<PgnMove> moves = new PgnParser("1. e4 (1. e3 e6) 1... e5 2. Nf3 Nc6 (2... h6 3. a3 a5 4. b4 (4. c4 c5 5. b3)) *").extractMoves();

        assertEquals(4, moves.size());

        assertEquals("e3", moves.getFirst().getVariations().getFirst().getMoves().getFirst().getSanMove());
        assertEquals(1, moves.getFirst().getVariations().getFirst().getMoves().getFirst().getMoveNumber());
        assertEquals(Side.WHITE, moves.getFirst().getVariations().getFirst().getMoves().getFirst().getSide());

        assertEquals("e6", moves.getFirst().getVariations().getFirst().getMoves().get(1).getSanMove());
        assertEquals(1, moves.getFirst().getVariations().getFirst().getMoves().get(1).getMoveNumber());
        assertEquals(Side.BLACK, moves.getFirst().getVariations().getFirst().getMoves().get(1).getSide());

        assertEquals("h6", moves.get(3).getVariations().getFirst().getMoves().getFirst().getSanMove());
        assertEquals(2, moves.get(3).getVariations().getFirst().getMoves().getFirst().getMoveNumber());
        assertEquals(Side.BLACK, moves.get(3).getVariations().getFirst().getMoves().getFirst().getSide());

        assertEquals("a3", moves.get(3).getVariations().getFirst().getMoves().get(1).getSanMove());
        assertEquals(3, moves.get(3).getVariations().getFirst().getMoves().get(1).getMoveNumber());
        assertEquals(Side.WHITE, moves.get(3).getVariations().getFirst().getMoves().get(1).getSide());

        assertEquals("b4", moves.get(3).getVariations().getFirst().getMoves().get(3).getSanMove());
        assertEquals(4, moves.get(3).getVariations().getFirst().getMoves().get(3).getMoveNumber());
        assertEquals(Side.WHITE, moves.get(3).getVariations().getFirst().getMoves().get(3).getSide());

        assertEquals(4, moves.get(3).getVariations().getFirst().getMoves().get(3).getVariations().getFirst().getMoves().getFirst().getMoveNumber());
        assertEquals("c4", moves.get(3).getVariations().getFirst().getMoves().get(3).getVariations().getFirst().getMoves().getFirst().getSanMove());
        assertEquals(Side.WHITE, moves.get(3).getVariations().getFirst().getMoves().get(3).getVariations().getFirst().getMoves().getFirst().getSide());

        assertEquals(4, moves.get(3).getVariations().getFirst().getMoves().get(3).getVariations().getFirst().getMoves().get(1).getMoveNumber());
        assertEquals("c5", moves.get(3).getVariations().getFirst().getMoves().get(3).getVariations().getFirst().getMoves().get(1).getSanMove());
        assertEquals(Side.BLACK, moves.get(3).getVariations().getFirst().getMoves().get(3).getVariations().getFirst().getMoves().get(1).getSide());

        assertEquals(5, moves.get(3).getVariations().getFirst().getMoves().get(3).getVariations().getFirst().getMoves().get(2).getMoveNumber());
        assertEquals("b3", moves.get(3).getVariations().getFirst().getMoves().get(3).getVariations().getFirst().getMoves().get(2).getSanMove());
        assertEquals(Side.WHITE, moves.get(3).getVariations().getFirst().getMoves().get(3).getVariations().getFirst().getMoves().get(2).getSide());

    }

    @Test
    void testBlackStart() {
        List<PgnMove> moves = new PgnParser("44... e5 0-1").extractMoves();

        assertEquals(1, moves.size());
        assertEquals("e5", moves.getFirst().getSanMove());
        assertEquals(Side.BLACK, moves.getFirst().getSide());
        assertEquals(44, moves.getFirst().getMoveNumber());
    }

}
