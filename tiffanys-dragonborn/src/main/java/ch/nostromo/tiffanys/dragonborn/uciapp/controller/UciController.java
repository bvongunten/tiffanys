package ch.nostromo.tiffanys.dragonborn.uciapp.controller;

import ch.nostromo.tiffanys.commons.ChessGame;
import ch.nostromo.tiffanys.commons.ChessGameInfo;
import ch.nostromo.tiffanys.commons.enums.GameColor;
import ch.nostromo.tiffanys.commons.fen.FenFormat;
import ch.nostromo.tiffanys.commons.move.Move;
import ch.nostromo.tiffanys.commons.uci.UciMoveTranslator;
import ch.nostromo.tiffanys.dragonborn.commons.AbstractEngine;
import ch.nostromo.tiffanys.dragonborn.commons.EngineException;
import ch.nostromo.tiffanys.dragonborn.commons.EngineSettings;
import ch.nostromo.tiffanys.dragonborn.commons.events.EngineEvent;
import ch.nostromo.tiffanys.dragonborn.commons.events.EngineEventListener;
import ch.nostromo.tiffanys.dragonborn.commons.opening.OpeningBook;
import ch.nostromo.tiffanys.dragonborn.engine.DragonbornEngine;
import ch.nostromo.tiffanys.dragonborn.uciapp.UciApp;
import ch.nostromo.tiffanys.dragonborn.uciapp.utils.logging.LogUtils;
import ch.nostromo.tiffanys.dragonborn.uciapp.utils.system.ConsoleScanner;
import ch.nostromo.tiffanys.dragonborn.uciapp.utils.system.ConsoleScannerListener;

import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UciController implements ConsoleScannerListener, EngineEventListener {

    private static Logger logger = Logger.getLogger(UciController.class.getName());

    private ConsoleScanner consoleScanner;

    // The currently running game
    private ChessGame game = null;

    private UciOptions uciOptions = new UciOptions();
    private AbstractEngine engine = null;

    private OpeningBook openingBook;

    /**
     * Initialize the application (read parameters, start logging etc.)
     * 
     * @param args
     *            Command line arguments
     */
    public void init(String[] args) {
        try {
            LogUtils.initializeLogging(Level.OFF, Level.ALL, UciApp.HOME_DIRECTORY, "tiffanys-uci.log");

            openingBook = new OpeningBook("/opening.txt");

            logger.info("Creation of console scanner");
            consoleScanner = new ConsoleScanner(this);
            consoleScanner.start();

            logger.info("Scanner started ...");

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Shutting down, due to exception: " + e.getMessage(), e);
            doOutput("# Shutting down, due to exception: " + e.getMessage());
            handleCommandQuit();
        }
    }

    private void handleCommandUci() {
        doOutput("id name " + UciApp.TITLE);
        doOutput("id author " + UciApp.AUTHOR);

        doOutput(uciOptions.getOptions());

        doOutput("uciok");
    }

    @Override
    public void handleInput(String cmd) {
        try {
            logger.info("<==== From UCI: " + cmd);

            // Filter commands
            if (cmd.equals("uci")) {

                handleCommandUci();

            } else if (cmd.startsWith("debug on")) {

                uciOptions.setDebugMode(true);

            } else if (cmd.startsWith("debug off")) {

                uciOptions.setDebugMode(false);

            } else if (cmd.startsWith("setoption")) {
                uciOptions.setOption(cmd);

            } else if (cmd.startsWith("register")) {

                // Do nothing

            } else if (cmd.startsWith("isready")) {

                doOutput("readyok");

            } else if (cmd.startsWith("ucinewgame")) {

                game = new ChessGame(new ChessGameInfo());

            } else if (cmd.startsWith("position startpos moves")) {

                handleCommandUciStartposMoves(cmd);

            } else if (cmd.startsWith("position startpos")) {

                nandleCommandUciPositionStartPos();

                // Do nothing

            } else if (cmd.startsWith("position fen")) {

                handleCommandUciStartposFen(cmd);

            } else if (cmd.startsWith("go")) {

                handleCommandGo(cmd);

            } else if (cmd.startsWith("stop")) {

                engine.halt();

            } else if (cmd.startsWith("ponderhit")) {

                // Do nothing

            } else if (cmd.startsWith("quit")) {

                handleCommandQuit();

            }

        } catch (Exception e) {
            doOutput("info Exception occured. Command: " + cmd + ", ErrorMessage: " + e.getMessage());
            logger.log(Level.SEVERE, "Unexpected problem: " + e.getMessage(), e);
        }

    }

    private void nandleCommandUciPositionStartPos() {
        game = new ChessGame(new ChessGameInfo());


    }

    private void handleCommandUciStartposFen(String cmd) {
        if (cmd.indexOf("moves") >= 0) {
            String fen = cmd.substring(12, cmd.indexOf("moves"));

            logger.fine("Fen received: " + fen);
            
            game = new ChessGame(new ChessGameInfo(), new FenFormat(fen));

            String moves = cmd.substring(cmd.indexOf("moves") + 6);

            StringTokenizer st = new StringTokenizer(moves);
            while (st.hasMoreTokens()) {
                String move = st.nextToken();
                logger.fine("Move received, entering move: " + move);
                game.applyMove(UciMoveTranslator.uciStringToMove(move, game.getCurrentBoard()));
            }

        } else {
            String fen = cmd.substring(12);
            game = new ChessGame(new ChessGameInfo(), new FenFormat(fen));

        }

    }

    private void handleCommandUciStartposMoves(String cmd) {
        game = new ChessGame(new ChessGameInfo());
        String moves = cmd.substring(23);

        StringTokenizer st = new StringTokenizer(moves);
        while (st.hasMoreTokens()) {
            String move = st.nextToken();
            logger.fine("Move received, entering move: " + move);
            game.applyMove(UciMoveTranslator.uciStringToMove(move, game.getCurrentBoard()));
        }

    }

    private void handleCommandGo(String cmd) throws EngineException {

        if (cmd.contains("depth")) {

            int depth = Integer.valueOf(getCmdVal(cmd,"depth"));

            EngineSettings engineSettings = new EngineSettings();
            engineSettings.setMode(EngineSettings.EngineMode.DEPTH);
            engineSettings.setDepth(depth);

            logger.info("Fixed depth, Enginesettings: " + engineSettings.toString());


            engine = new DragonbornEngine(engineSettings, openingBook);
            engine.addEventListener(this);
            engine.asyncScoreMoves(game);

        } else if (cmd.contains("movestogo")){

            int remainingTime = 0;

            if (game.getCurrentColorToMove() == GameColor.WHITE) {
                remainingTime = Integer.valueOf(getCmdVal(cmd, "wtime"));
            } else {
                remainingTime = Integer.valueOf(getCmdVal(cmd, "btime"));
            }

            int movesToGo = Integer.valueOf(getCmdVal(cmd, "movestogo"));

            EngineSettings engineSettings = new EngineSettings();
            engineSettings.setMode(EngineSettings.EngineMode.TIME_FOR_MOVE);
            engineSettings.setTime(remainingTime / movesToGo);
            engineSettings.setDepth(Integer.MAX_VALUE);

            logger.info("Time for remaining moves (" + movesToGo +"), Enginesettings: " + engineSettings.toString());

            engine = new DragonbornEngine(engineSettings, openingBook);
            engine.addEventListener(this);

            engine.asyncScoreMoves(game);
        } else {
            int remainingTime = 0;
            int increase = 0;

            if (game.getCurrentColorToMove() == GameColor.WHITE) {
                remainingTime = Integer.valueOf(getCmdVal(cmd, "wtime"));
                increase =  Integer.valueOf(getCmdVal(cmd, "winc"));
            } else {
                remainingTime = Integer.valueOf(getCmdVal(cmd, "btime"));
                increase =  Integer.valueOf(getCmdVal(cmd, "binc"));
            }

            // Always keep time for 30 moves
            int time = remainingTime / 30;
            if (increase > remainingTime) {
                time = increase;
            }

            EngineSettings engineSettings = new EngineSettings();
            engineSettings.setMode(EngineSettings.EngineMode.TIME_FOR_MOVE);
            engineSettings.setTime(time);
            engineSettings.setDepth(Integer.MAX_VALUE);

            logger.info("Fix Time, Enginesettings: " + engineSettings.toString());

            engine = new DragonbornEngine(engineSettings, openingBook);
            engine.addEventListener(this);

            engine.asyncScoreMoves(game);
        }
    }

    String getCmdVal(String cmd, String key) {
        int startPos = cmd.indexOf(key);

        if (startPos >= 0) {
            startPos += key.length() + 1;
            int nextBlank = cmd.indexOf(" ", startPos);
            if (nextBlank >= 0) {
                return cmd.substring(startPos, nextBlank);
            } else {
                return cmd.substring(startPos);
            }

        }

        return null;

    }

    private void doOutput(String msg) {
        logger.info("====> To UCI: " + msg);

        System.out.println(msg);
    }

    private void handleCommandQuit() {
        consoleScanner.interrupt();

        logger.info("Quitting UCI app");

        System.exit(0);
    }

    @Override
    public void engineUpdateEventOccured(EngineEvent event) {
        logger.fine("Update received from engine. Best move: " + event.getEngineResult().getSelectedMove());
        sendInfoLine(event);
    }

    @Override
    public void engineFinishedEventOccured(EngineEvent event) {
        logger.fine("Finished received from engine: Best move: " + event.getEngineResult().getSelectedMove());
        game.applyMove(event.getEngineResult().getSelectedMove());

        sendInfoLine(event);
        doOutput("bestmove " + UciMoveTranslator.moveToUciString(event.getEngineResult().getSelectedMove()));
    }

    private void sendInfoLine(EngineEvent event) {
        Move moveTomake = event.getEngineResult().getSelectedMove();

        if (moveTomake.getMoveAttributes() != null) {

            String depth = " depth " + moveTomake.getMoveAttributes().getPlannedDepth();

            String currmove = " currmove " + UciMoveTranslator.moveToUciString(moveTomake);

            int scoreVal = (int) moveTomake.getMoveAttributes().getScore();
            String score = " score cp " + (int) moveTomake.getMoveAttributes().getScore();

            if (Math.abs(scoreVal) > 9000) {
                int plys = 9999 - Math.abs(scoreVal);
                int mateIn = plys / 2;
                score = " score mate " + mateIn;
            } else {
                score = " score cp " + (int) moveTomake.getMoveAttributes().getScore();
            }

            String time = " time " + event.getEngineResult().getTotalTimeInMs();

            int nodesCount = 0;
            for (Move move : event.getEngineResult().getLegalMoves()) {
                nodesCount += move.getMoveAttributes().getNodes();
            }

            String nodes = " nodes " + nodesCount;

            String pvs = " pv ";
            for (Move pv : moveTomake.getMoveAttributes().getPrincipalVariations()) {
                pvs += UciMoveTranslator.moveToUciString(pv) + " ";
            }

            String infoLine = "info" + depth + time + score + currmove + nodes + pvs;

            doOutput(infoLine);
        }
    }

}
