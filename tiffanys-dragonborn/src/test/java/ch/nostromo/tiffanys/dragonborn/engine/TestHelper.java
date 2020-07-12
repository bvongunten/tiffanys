package ch.nostromo.tiffanys.dragonborn.engine;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import ch.nostromo.tiffanys.commons.logging.LogUtils;
import org.junit.Before;

import ch.nostromo.tiffanys.commons.ChessGame;
import ch.nostromo.tiffanys.commons.ChessGameInfo;
import ch.nostromo.tiffanys.commons.board.Board;
import ch.nostromo.tiffanys.commons.enums.GameColor;
import ch.nostromo.tiffanys.commons.enums.Piece;
import ch.nostromo.tiffanys.commons.fen.FenFormat;
import ch.nostromo.tiffanys.commons.move.Move;
import ch.nostromo.tiffanys.commons.rules.RulesUtil;
import ch.nostromo.tiffanys.dragonborn.engine.board.RobustBoard;
import ch.nostromo.tiffanys.dragonborn.engine.move.EngineMove;

public class TestHelper {

    private Level logLevel = Level.INFO;

    protected Logger logger = Logger.getLogger(getClass().getName());

    @Before
    public void setUp() throws Exception {
        LogUtils.initializeLogging(logLevel, Level.OFF, null, null);
    }

    class LoggingConsoleFormatter extends java.util.logging.Formatter {

        Date dat = new Date();
        private Object args[] = new Object[1];
        private String lineSeparator = System.getProperty("line.separator");

        @Override
        public synchronized String format(LogRecord record) {
            StringBuffer sb = new StringBuffer();
            this.dat.setTime(record.getMillis());
            this.args[0] = this.dat;

            sb.append("[");
            sb.append(record.getLevel().getName());
            sb.append("] ");

            String message = formatMessage(record);

            sb.append(message);

            sb.append(this.lineSeparator);

            if (record.getThrown() != null) {
                try {
                    StringWriter sw = new StringWriter();
                    PrintWriter pw = new PrintWriter(sw);
                    record.getThrown().printStackTrace(pw);
                    pw.close();
                    sb.append(sw.toString());
                } catch (Throwable ignored) {
                    // Ignored
                }
            }
            return sb.toString();
        }
    }

    public List<Move> convertToMove(EngineMove[] legalMoves, GameColor colorToMove) {
        List<Move> result = new ArrayList<Move>();
        for (EngineMove tiffanysMove : legalMoves) {
            result.add(tiffanysMove.convertToMove(colorToMove));
        }

        return result;
    }

    public void printMoves(List<Move> moves) {
        for (Move move : moves) {
            logger.info(move.toString());
        }
    }
    

    public void printMoves(EngineMove[] moves, GameColor colorToMove) {
        for (EngineMove move : moves) {
            logger.info(move.convertToMove(colorToMove).toString());
        }
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

    public Move getMoveInList(List<Move> moves, Move move) {
        for (Move scoredMove : moves) {
            if (scoredMove.equals(move)) {
                return scoredMove;
            }
        }
        fail("Moves not found");
        return null;

    }

    public boolean bestMovesContains(List<Move> moves, Move move) {
        if (moves.size() == 0) {
            fail("Moves list is empty");
        }

        int maxScore = (int) moves.get(0).getMoveAttributes().getScore();
        for (Move scoredMove : moves) {

            if (scoredMove.equals(move)) {
                return true;
            }

            if (scoredMove.getMoveAttributes().getScore() < maxScore) {
                return false;
            }
        }

        fail("Move not found!");
        return false;

    }

    public void compareBaseAndEngineMoves(FenFormat fen) {
        ChessGame game = new ChessGame(new ChessGameInfo(), fen);

        RobustBoard tiffBoard = new RobustBoard(game.getCurrentBoard(), game.getCurrentColorToMove());

        EngineMove[] movesBuffer = tiffBoard.generateLegalMovesList();
        List<Move> engineMovesList = new ArrayList<Move>();
        for (EngineMove move : movesBuffer) {
            engineMovesList.add(move.convertToMove(game.getCurrentColorToMove()));
        }

        EngineMove[] hitMovesBuffer = tiffBoard.generateLegalHitMovesList();
        List<Move> engineHitMovesList = new ArrayList<Move>();
        for (EngineMove move : hitMovesBuffer) {
            engineHitMovesList.add(move.convertToMove(game.getCurrentColorToMove()));
        }

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
