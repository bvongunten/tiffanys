package ch.nostromo.tiffanys.dragonborn.testing.movegen;

import ch.nostromo.tiffanys.commons.ChessGame;
import ch.nostromo.tiffanys.commons.ChessGameInfo;
import ch.nostromo.tiffanys.commons.board.Board;
import ch.nostromo.tiffanys.commons.enums.Piece;
import ch.nostromo.tiffanys.commons.fen.FenFormat;
import ch.nostromo.tiffanys.commons.move.Move;
import ch.nostromo.tiffanys.commons.rules.RulesUtil;
import ch.nostromo.tiffanys.dragonborn.commons.Engine;
import ch.nostromo.tiffanys.dragonborn.commons.EngineSettings;
import ch.nostromo.tiffanys.dragonborn.engine.EngineFactory;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class BaseLegalMovesHelper {


    public void compareBaseAndEngineMoves(FenFormat fen) {
        ChessGame game = new ChessGame(new ChessGameInfo(), fen);

        EngineSettings engineSettings = new EngineSettings();
        engineSettings.setThreads(1);
        engineSettings.setDepth(1);

        Engine engine = EngineFactory.createEngine(engineSettings);

        List<Move> engineMovesList = engine.generateLegalMovesList(game);
        List<Move> engineHitMovesList = engine.generateLegalHitMovesList(game);

        List<Move> baseMovesList = RulesUtil.getLegalMoves(game.getCurrentBoard(), game.getCurrentColorToMove());

        assertEquals(baseMovesList.size(), engineMovesList.size());

        for (Move move : baseMovesList) {
            assertTrue(engineMovesList.contains(move));
        }

        checkEningeHitMoves(game.getCurrentBoard(), baseMovesList, engineHitMovesList);

    }


    public void checkEningeHitMoves(Board board, List<Move> baseMovesList, List<Move> enginesHitMovesList) {
        List<Move> baseHitMovesList = new ArrayList<Move>();

        for (Move move : baseMovesList) {
            if (isHitMove(board, move)) {
                baseHitMovesList.add(move);
            }
        }

        assertEquals(baseHitMovesList.size(), enginesHitMovesList.size());

        for (Move move : baseHitMovesList) {
            assertTrue(enginesHitMovesList.contains(move));
        }
    }

    private boolean isHitMove(Board board, Move move) {
        boolean isEPHit = board.getFields()[move.getFrom()].getPiece() == Piece.PAWN && move.getTo() == board.getEnPassantField();
        boolean isNormalHit = board.getFields()[move.getTo()].getPiece() != null;

        return isEPHit || isNormalHit;
    }


}
