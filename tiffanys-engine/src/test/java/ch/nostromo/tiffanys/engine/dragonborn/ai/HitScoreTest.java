package ch.nostromo.tiffanys.engine.dragonborn.ai;

import ch.nostromo.tiffanys.commons.board.Board;
import ch.nostromo.tiffanys.commons.enums.GameColor;
import ch.nostromo.tiffanys.commons.fen.FenFormat;
import ch.nostromo.tiffanys.commons.move.Move;
import ch.nostromo.tiffanys.engine.commons.EngineSettings;
import ch.nostromo.tiffanys.engine.dragonborn.board.RobustBoard;
import ch.nostromo.tiffanys.engine.dragonborn.move.EngineMove;
import org.junit.Test;

import static ch.nostromo.tiffanys.commons.enums.Coordinates.E6;
import static ch.nostromo.tiffanys.commons.enums.Coordinates.F7;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class HitScoreTest {

    @Test
    public void testHitScoreQxP() {
        FenFormat fen = new FenFormat("k7/5p2/4Q3/8/8/8/8/K7 w - - 0 1");
        Board board = new Board(fen);

        EngineSettings engineSettings = new EngineSettings();
        engineSettings.setDepth(1);

        RobustBoard tiffBoard = new RobustBoard(board, GameColor.WHITE);
        EngineMove[] legalMoves = tiffBoard.generateLegalMovesList();

        EngineMove qxp = getMoveInList(legalMoves, new Move(E6, F7), GameColor.WHITE);

        assertEquals(300, qxp.hitScore);

    }

    @Test
    public void testHitScorePxQ() {
        FenFormat fen = new FenFormat("k7/5p2/4Q3/8/8/8/8/K7 b - - 0 1");
        Board board = new Board(fen);

        EngineSettings engineSettings = new EngineSettings();
        engineSettings.setDepth(1);

        RobustBoard tiffBoard = new RobustBoard(board, GameColor.BLACK);
        EngineMove[] legalMoves = tiffBoard.generateLegalMovesList();

        EngineMove qxp = getMoveInList(legalMoves, new Move(F7, E6), GameColor.BLACK);

        assertEquals(1700, qxp.hitScore);

    }


    public EngineMove getMoveInList(EngineMove[] moves, Move move, GameColor colorToMove) {
        for (EngineMove scoredMove : moves) {

            Move possibleResult = scoredMove.convertToMove(colorToMove);

            if (possibleResult.equals(move)) {
                return scoredMove;
            }
        }
        fail("Moves not found");
        return null;

    }


}
