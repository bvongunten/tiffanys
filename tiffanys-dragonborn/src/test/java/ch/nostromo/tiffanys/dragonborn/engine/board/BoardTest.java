package ch.nostromo.tiffanys.dragonborn.engine.board;

import ch.nostromo.tiffanys.commons.ChessGame;
import ch.nostromo.tiffanys.commons.format.FenFormat;
import ch.nostromo.tiffanys.dragonborn.BaseTest;
import ch.nostromo.tiffanys.dragonborn.engine.move.MoveGenerator;
import ch.nostromo.tiffanys.dragonborn.engine.move.MoveList;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest extends BaseTest {


    @Test void testSimpleMakeAndUnmakeMove() {
        Board originalBoard = new Board(new ChessGame(new FenFormat("r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq - 0 1")));
        Board moveBoard = originalBoard.copy();

        MoveList ml = new MoveList(); MoveGenerator.generateLegalMoves(moveBoard, ml);
        for (int i = 0; i < ml.size(); i++) {
            long undo = moveBoard.makeMove(ml.get(i));

            moveBoard.unmakeMove(ml.get(i), undo);

            assertBoardsEqual(originalBoard, moveBoard);
        }
    }


    @Test
    void testCopy() {
        Board board1 = new Board();
        Board board2 = board1.copy();

        assertBoardsEqual(board1, board2);

        board1.pieces[0] = 123;
        assertNotEquals(board1.pieces[0], board2.pieces[0]);
    }


    // **** PERFT Based Tests to validate (a lot of) make & unmake move *****

    @Test
    void testStartingPostion() {
        assertPerft("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1", true,
                20L, 400L, 8902L, 197281L);
    }

    @Test
    void testKiwiPete() {
        assertPerft("r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq - 0 1", true,
                48L, 2039L, 97862L);
    }


    @Test
    void testPosition3() {
        assertPerft("8/2p5/3p4/KP5r/1R3p1k/8/4P1P1/8 w - - 0 1", true,
                14L, 191L, 2812L, 43238L);
    }


    @Test
    void testPosition4() {
        assertPerft("r3k2r/Pppp1ppp/1b3nbN/nP6/BBP1P3/q4N2/Pp1P2PP/R2Q1RK1 w kq - 0 1", true,
                6L, 264L, 9467L);
    }




}
