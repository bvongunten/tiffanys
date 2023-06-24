package ch.nostromo.tiffanys.dragonborn.uci.controller;


import ch.nostromo.tiffanys.commons.ChessGame;
import ch.nostromo.tiffanys.commons.engine.EngineResult;
import ch.nostromo.tiffanys.commons.engine.settings.EngineSettingsDepth;
import ch.nostromo.tiffanys.commons.engine.settings.EngineSettingsTimePerMove;
import ch.nostromo.tiffanys.commons.format.FenFormat;
import ch.nostromo.tiffanys.commons.move.Move;
import ch.nostromo.tiffanys.commons.move.MoveAttributes;
import ch.nostromo.tiffanys.commons.uci.client.UciClient;
import ch.nostromo.tiffanys.commons.uci.protocol.ClientInfo;
import ch.nostromo.tiffanys.commons.uci.protocol.ClientUciResponse;
import ch.nostromo.tiffanys.commons.uci.protocol.ServerGo;
import ch.nostromo.tiffanys.commons.uci.protocol.ServerPosition;
import ch.nostromo.tiffanys.dragonborn.engine.DragonbornEngine;
import ch.nostromo.tiffanys.dragonborn.uci.UciConstants;
import org.junit.jupiter.api.Test;


import java.util.ArrayList;
import java.util.List;

import static ch.nostromo.tiffanys.commons.board.Square.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class UciControllerTest {

    @Test
    void testHandleUci() {
        UciController uciController = new UciController();

        uciController.uciClient = new UciClient(null) {
            @Override
            public void sendUci(ClientUciResponse clientUciResponse) {
                assertEquals(UciConstants.AUTHOR, clientUciResponse.getAuthor());
                assertEquals(UciConstants.APPLICATION, clientUciResponse.getName());
            }

            @Override
            public void sendBestMove(Move move) {
                // Ignnore
            }
        };

        uciController.handleUci();
    }

    @Test
    void testUciInfoLine() {
        UciController uciController = new UciController();

        uciController.uciClient = new UciClient(null) {
            @Override
            public void sendInfo(ClientInfo clientInfo) {
                assertEquals(9, clientInfo.getDepth());
                assertEquals(E2, clientInfo.getCurrentMove().getFrom());
                assertEquals(E4, clientInfo.getCurrentMove().getTo());
                assertEquals(30, clientInfo.getScoreCp());
                assertEquals(4500, clientInfo.getNodes());
                assertEquals(2, clientInfo.getPv().size());
                assertEquals(1000, clientInfo.getTime());
            }
        };

        EngineResult engineResult = new EngineResult();
        Move selectedMove = new Move(E2, E4);

        MoveAttributes attributes = new MoveAttributes();
        attributes.setDepth(9);
        attributes.setScore(30.0);
        attributes.setNodes(4500);

        engineResult.setTotalTimeInMs(1000);

        List<Move> pv = new ArrayList<>();
        pv.add(selectedMove);
        pv.add(new Move(E7, E5));
        attributes.setPrincipalVariations(pv);

        selectedMove.setMoveAttributes(attributes);
        engineResult.setSelectedMove(selectedMove);

        uciController.sendInfoLine(engineResult);
    }

    @Test
    void testUciInfoLineMateIn2() {
        UciController uciController = new UciController();

        uciController.uciClient = new UciClient(null) {
            @Override
            public void sendInfo(ClientInfo clientInfo) {
                assertEquals(10, clientInfo.getScoreMate());
            }
        };

        EngineResult engineResult = new EngineResult();
        Move selectedMove = new Move(E2, E4);

        MoveAttributes attributes = new MoveAttributes();
        attributes.setMateIn(10);

        selectedMove.setMoveAttributes(attributes);
        engineResult.setSelectedMove(selectedMove);

        uciController.sendInfoLine(engineResult);
    }


    @Test
    void testHandlePosition() {
        UciController uciController = new UciController();
        uciController.handlePosition(new ServerPosition(ServerPosition.PositionType.FEN, new ChessGame(new FenFormat("1q5r/1b1r1p1k/2p1pPpb/p1Pp4/3B1P1Q/1P4P1/P4KB1/2RR4 w - - 0 1"))));

        assertEquals("1q5r/1b1r1p1k/2p1pPpb/p1Pp4/3B1P1Q/1P4P1/P4KB1/2RR4 w - - 0 1", uciController.game.createFen().toString());
    }


    @Test
    void testGoDepthSettings() {
        UciController uciController = new UciController();
        uciController.game = new ChessGame();
        uciController.uciClient = createSilentUciClient();

        ServerGo serverGo = new ServerGo();
        serverGo.setDepth(1);

        uciController.handleGo(serverGo);

        DragonbornEngine engine = (DragonbornEngine) uciController.engine;

        assertEquals(1, ((EngineSettingsDepth) engine.getEngineSettings()).depth());
    }


    @Test
    void testGoTimeMovesToGoSettings() {
        UciController uciController = new UciController();
        uciController.game = new ChessGame();
        uciController.uciClient = createSilentUciClient();

        ServerGo serverGo = new ServerGo();
        serverGo.setMovesToGo(10);
        serverGo.setTimeWhite(500);
        uciController.handleGo(serverGo);

        DragonbornEngine engine = (DragonbornEngine) uciController.engine;

        assertEquals(50, ((EngineSettingsTimePerMove) engine.getEngineSettings()).timeMs());
    }

    @Test
    void testGoTimeSettings() {
        UciController uciController = new UciController();
        uciController.game = new ChessGame();
        uciController.uciClient = createSilentUciClient();

        ServerGo serverGo = new ServerGo();
        serverGo.setMoveTime(100);
        uciController.handleGo(serverGo);

        DragonbornEngine engine = (DragonbornEngine) uciController.engine;

        assertEquals(100, ((EngineSettingsTimePerMove) engine.getEngineSettings()).timeMs());
    }


    @Test
    void testGoTimeFullGameNoIncSettings() {
        UciController uciController = new UciController();
        uciController.game = new ChessGame();
        uciController.uciClient = createSilentUciClient();

        ServerGo serverGo = new ServerGo();
        serverGo.setTimeWhite(3330);
        serverGo.setIncreaseWhite(0);
        uciController.handleGo(serverGo);

        DragonbornEngine engine = (DragonbornEngine) uciController.engine;

        assertEquals(111, ((EngineSettingsTimePerMove) engine.getEngineSettings()).timeMs());
    }

    @Test
    void testGoTimeFullGameBiggerIncSettings() {
        UciController uciController = new UciController();
        uciController.game = new ChessGame();
        uciController.uciClient = createSilentUciClient();

        ServerGo serverGo = new ServerGo();
        serverGo.setTimeWhite(90);
        serverGo.setIncreaseWhite(100);
        uciController.handleGo(serverGo);

        DragonbornEngine engine = (DragonbornEngine) uciController.engine;

        assertEquals(100, ((EngineSettingsTimePerMove) engine.getEngineSettings()).timeMs());
    }

    private static UciClient createSilentUciClient() {

        return new UciClient(null) {

            @Override
            public void sendBestMove(Move move) {
                // Ignnore
            }

            @Override
            public void sendUci(ClientUciResponse clientUciResponse) {
                // Ignnore
            }

            @Override
            public void sendReadyOk() {
                // Ignnore
            }

            @Override
            public void sendError(String message) {
                // Ignnore
            }

            @Override
            public void sendInfo(ClientInfo clientInfo) {
                // Ignore
            }

        };
    }


}
