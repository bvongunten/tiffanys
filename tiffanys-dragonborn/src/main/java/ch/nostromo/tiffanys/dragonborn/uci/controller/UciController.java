package ch.nostromo.tiffanys.dragonborn.uci.controller;

import ch.nostromo.tiffanys.commons.ChessGame;
import ch.nostromo.tiffanys.commons.Side;
import ch.nostromo.tiffanys.commons.engine.Engine;
import ch.nostromo.tiffanys.commons.engine.EngineListener;
import ch.nostromo.tiffanys.commons.engine.EngineResult;
import ch.nostromo.tiffanys.commons.engine.settings.EngineSettings;
import ch.nostromo.tiffanys.commons.engine.settings.EngineSettingsDepth;
import ch.nostromo.tiffanys.commons.engine.settings.EngineSettingsTimePerMove;
import ch.nostromo.tiffanys.commons.move.Move;
import ch.nostromo.tiffanys.commons.uci.client.UciClient;
import ch.nostromo.tiffanys.commons.uci.client.UciClientListener;
import ch.nostromo.tiffanys.commons.uci.protocol.*;
import ch.nostromo.tiffanys.dragonborn.engine.DragonbornEngine;
import ch.nostromo.tiffanys.dragonborn.uci.UciConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UciController implements EngineListener, UciClientListener {

    private static final Logger LOG = LoggerFactory.getLogger(UciController.class);

    UciClient uciClient;

    ChessGame game = null;

    Engine engine = null;

    /**
     * Initialize the application (read parameters, start logging etc.)
     *
     */
    public void init() {

        uciClient = new UciClient(this);
        uciClient.startScanner();

        LOG.info("Uci client started.");
        LOG.info("Version: " + UciConstants.VERSION);

        Thread.setDefaultUncaughtExceptionHandler((t, e) -> {
            LOG.error("Shutting down, due to exception.", e);
            if (uciClient != null) {
                uciClient.sendError("Shutting down, due to exception: " + e.getMessage());
            }
            handleQuit();
        });

    }


    @Override
    public void handleUci() {

        ClientUciResponse clientUciResponse = new ClientUciResponse();
        clientUciResponse.setName(UciConstants.APPLICATION);
        clientUciResponse.setAuthor(UciConstants.AUTHOR);

        uciClient.sendUci(clientUciResponse);

    }

    @Override
    public void handleDebugOn() {
        // Do nothing
    }

    @Override
    public void handleDebugOff() {
        // Do nothing
    }

    @Override
    public void handleSetOption(ServerOption serverOption) {
        // Do nothing, we do not take any instructions ;)
    }

    @Override
    public void handleRegister(ServerRegister serverRegister) {
        // Do nothing
    }

    @Override
    public void handleIsReady() {
        uciClient.sendReadyOk();
    }

    @Override
    public void handleUciNewGame() {
        game = null;
    }

    @Override
    public void handlePosition(ServerPosition uciServerPosition) {
        game = uciServerPosition.getChessGame();
    }

    @Override
    public void handleGo(ServerGo serverGo) {

        EngineSettings engineSettings;

        if (serverGo.getDepth() != null) {

            engineSettings = new EngineSettingsDepth(serverGo.getDepth());

            LOG.debug("Fixed depth, engine settings: {}", engineSettings);

        } else if (serverGo.getMovesToGo() != null) {

            int remainingTime = (game.getCurrentSide() == Side.WHITE ? serverGo.getTimeWhite() : serverGo.getTimeBlack()) / serverGo.getMovesToGo();

            engineSettings = new EngineSettingsTimePerMove(remainingTime);

            LOG.debug("MovesToGo: Time for remaining moves ({}), engine settings: {}", serverGo.getMovesToGo(), engineSettings );

        } else if (serverGo.getMoveTime() != null) {
            engineSettings = new EngineSettingsTimePerMove(serverGo.getMoveTime());

            LOG.debug("MovesToGo: Fix move time {} engine settings: {}", serverGo.getMovesToGo(), engineSettings );

        } else {

            int remainingTime = game.getCurrentSide() == Side.WHITE ? serverGo.getTimeWhite() : serverGo.getTimeBlack();
            int increase = game.getCurrentSide() == Side.WHITE ? serverGo.getIncreaseWhite() : serverGo.getIncreaseBlack();

            // Always keep time for 30 moves
            int time = remainingTime / 30;

            // If the increas is bigger than the remaining time, it's our target time
            if (increase > remainingTime) {
                time = increase;
            }

            engineSettings = new EngineSettingsTimePerMove(time);

            LOG.debug("Fixed time, engine settings: {}", engineSettings);

        }

        // threads  / hash

        engine = new DragonbornEngine(engineSettings);
        engine.startAsyncSearch(game, this);


    }

    @Override
    public void handleStop() {
        engine.stopAsyncSearch();
    }

    @Override
    public void handlePonderHit() {
        // do nothing
    }

    @Override
    public void handleQuit() {
        uciClient.stopScanner();

        LOG.info("Quitting UCI app");

        System.exit(0);
    }

    @Override
    public void onEngineUpdate(EngineResult engineResult) {
        LOG.debug("Update received from engine. Best move: {}", engineResult.getSelectedMove());

        sendInfoLine(engineResult);
    }

    @Override
    public void onEngineFinished(EngineResult engineResult) {
        LOG.debug("Engine finished: Best move: {}", engineResult.getSelectedMove());
        game.applyMove(engineResult.getSelectedMove());

        sendInfoLine(engineResult);

        uciClient.sendBestMove(engineResult.getSelectedMove());
    }

    void sendInfoLine(EngineResult engineResult) {

        if (engineResult.getSelectedMove().getMoveAttributes() != null) {
            Move selectedMove = engineResult.getSelectedMove();

            ClientInfo clientInfo = new ClientInfo();
            clientInfo.setDepth(selectedMove.getMoveAttributes().getDepth());
            clientInfo.setCurrentMove(selectedMove);

            if (selectedMove.getMoveAttributes().getMateIn() != 0) {
                clientInfo.setScoreMate(selectedMove.getMoveAttributes().getMateIn());
            } else {
                clientInfo.setScoreCp((int) selectedMove.getMoveAttributes().getScore());
            }

            clientInfo.setTime(engineResult.getTotalTimeInMs());

            // Use nodes from MoveAttributes if available, otherwise sum from legalMoves
            if (selectedMove.getMoveAttributes().getNodes() > 0) {
                clientInfo.setNodes(selectedMove.getMoveAttributes().getNodes());
            }

            clientInfo.setPv(selectedMove.getMoveAttributes().getPrincipalVariations());

            uciClient.sendInfo(clientInfo);
        }
    }

}
