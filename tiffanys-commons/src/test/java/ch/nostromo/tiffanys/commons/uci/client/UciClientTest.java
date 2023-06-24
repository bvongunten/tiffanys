package ch.nostromo.tiffanys.commons.uci.client;


import ch.nostromo.tiffanys.commons.BaseTest;
import ch.nostromo.tiffanys.commons.move.Move;
import ch.nostromo.tiffanys.commons.uci.protocol.*;
import ch.nostromo.tiffanys.commons.uci.utils.UciConsoleWriter;
import ch.nostromo.tiffanys.commons.uci.utils.UciException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static ch.nostromo.tiffanys.commons.board.Square.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UciClientTest extends BaseTest {

    @Mock
    UciClientListener listener;

    @Mock
    UciConsoleWriter writer;

    @InjectMocks
    UciClient testee;

    // ******* RECEIVERS ********

    @Test
    void testGenerateOptionFull() {
        ServerOption option = testee.generateUciOption("setoption name Nullmove value true");
        assertEquals("Nullmove", option.getName());
        assertEquals("true", option.getValue());
    }

    @Test
    void testGenerateOptionNoValue() {
        ServerOption option = testee.generateUciOption("setoption name Nullmove");
        assertEquals("Nullmove", option.getName());
        assertNull(option.getValue());
    }

    @Test
    void testGenerateOptionInvalid() {
        assertThrows(UciException.class, () -> {
            testee.generateUciOption("setoption abc");
        });
    }

    @Test
    void testGenerateRegisterLater() {
        ServerRegister serverRegister = testee.generateRegister("register later");
        assertEquals(ServerRegister.RegisterType.LATER, serverRegister.getRegisterType());
        assertNull(serverRegister.getName());
        assertNull(serverRegister.getCode());
    }

    @Test
    void testGenerateRegisterName() {
        ServerRegister serverRegister = testee.generateRegister("register name MyName");
        assertEquals(ServerRegister.RegisterType.NAME, serverRegister.getRegisterType());
        assertEquals("MyName", serverRegister.getName());
        assertNull(serverRegister.getCode());
    }

    @Test
    void testGenerateRegisterCode() {
        ServerRegister serverRegister = testee.generateRegister("register code MyCode");
        assertEquals(ServerRegister.RegisterType.CODE, serverRegister.getRegisterType());
        assertEquals("MyCode", serverRegister.getCode());
        assertNull(serverRegister.getName());
    }


    @Test
    void testGenerateRegisterInvalid() {
        assertThrows(UciException.class, () -> {
            testee.generateUciOption("register abc");
        });
    }

    @Test
    void testGenerateGo() {
        ServerGo serverGo = testee.generateGo("go searchmoves e2e4 d2d4 wtime 30000 btime 29000 winc 1000 binc 900 movestogo 40 depth 10 nodes 1000000 mate 3 movetime 5000 infinite ponder");

        assertEquals(2, serverGo.getSearchMoves().size());
        assertEquals("Move [e2-e4]", serverGo.getSearchMoves().get(0).toString());
        assertEquals("Move [d2-d4]", serverGo.getSearchMoves().get(1).toString());

        assertEquals(30000, serverGo.getTimeWhite());
        assertEquals(29000, serverGo.getTimeBlack());

        assertEquals(1000, serverGo.getIncreaseWhite());
        assertEquals(900, serverGo.getIncreaseBlack());

        assertEquals(40, serverGo.getMovesToGo());

        assertEquals(10, serverGo.getDepth());

        assertEquals(1000000, serverGo.getNodes());

        assertEquals(3, serverGo.getMate());

        assertEquals(5000, serverGo.getMoveTime());

        assertTrue(serverGo.getInfinite());
        assertTrue(serverGo.getPonder());

    }

    @Test
    void testGenerateGoInvalid() {
        assertThrows(UciException.class, () -> {
            testee.generateGo("go abc");
        });
    }

    @Test
    void testGenerateGoIllegalMove() {
        Throwable thrown = assertThrows(UciException.class, () -> {
            testee.generateGo("go searchmoves e2e8");
        });

        assertTrue(thrown.getMessage().contains("Illegal move"));
    }

    @Test
    void testGeneratePositionPos() {
        ServerPosition uciServerPosition = testee.generatePosition("position startpos moves e2e4 e7e5 g1f3");

        assertEquals(ServerPosition.PositionType.STARTPOS, uciServerPosition.getPositionType());

        assertEquals(3, uciServerPosition.getChessGame().getMoveHistory().size());
        assertEquals("Move [e2-e4]", uciServerPosition.getChessGame().getMoveHistory().get(0).toString());
        assertEquals("Move [e7-e5]", uciServerPosition.getChessGame().getMoveHistory().get(1).toString());
        assertEquals("Move [g1-f3]", uciServerPosition.getChessGame().getMoveHistory().get(2).toString());

        assertEquals("rnbqkbnr/pppp1ppp/8/4p3/4P3/5N2/PPPP1PPP/RNBQKB1R b KQkq - 1 2", uciServerPosition.getChessGame().createFen().toString());
    }


    @Test
    void testGeneratePositionFen() {
        ServerPosition uciServerPosition = testee.generatePosition("position fen 8/8/3k4/p7/6b1/3R2P1/r1r1BK1P/8 b - - 9 38 moves d6e5 d3e3");

        assertEquals(ServerPosition.PositionType.FEN, uciServerPosition.getPositionType());

        assertEquals(2, uciServerPosition.getChessGame().getMoveHistory().size());
        assertEquals("Move [d6-e5]", uciServerPosition.getChessGame().getMoveHistory().get(0).toString());
        assertEquals("Move [d3-e3]", uciServerPosition.getChessGame().getMoveHistory().get(1).toString());

        assertEquals("8/8/8/p3k3/6b1/4R1P1/r1r1BK1P/8 b - - 11 39", uciServerPosition.getChessGame().createFen().toString());
    }


    @Test
    void testGeneratePositionForWhitePromotion() {
        ServerPosition position = testee.generatePosition("position startpos moves e2e4 c7c5 g1f3 d7d6 d2d4 c5d4 f3d4 g8f6 b1c3 a7a6 c1g5 e7e6 f2f4 f8e7 d1f3 h7h6 g5h4 d8c7 e1c1 e8g8 f1d3 b8c6 d4c6 b7c6 c1b1 c8b7 h4f2 c6c5 d3c4 f6e4 c3e4 d6d5 c4d5 b7d5 g2g4 e7f6 c2c4 d5c6 h1e1 a8d8 e4f6 g7f6 f3e3 d8d1 e1d1 f8d8 d1e1 d8d4 e3c3 d4e4 f2e3 c7d8 b2b3 d8d6 e1g1 e6e5 f4f5 g8h8 b1c1 h8g7 g1g3 d6c7 c3d2 c7b8 e3h6 g7h7 h6g5 h7g7 g5f6 g7f6 d2h6 f6e7 h6c6 e4e1 c1b2 e1e2 b2b1 e2e1 b1b2 e1e2 b2b1 e2e1 b1c2 e1e2 c2c1 e2a2 c6c5 b8d6 f5f6 e7e6 c5d6 e6d6 h2h3 a2f2 g4g5 f2f5 h3h4 f5f4 g3h3 d6c6 h3h2 f4e4 h2h1 c6b7 c1b2 b7a7 h1d1 e4h4 d1d7 a7b8 d7f7 h4h8 g5g6 h8h2 b2a3 h2h6 f7f8 b8b7 g6g7 h6h1 g7g8q h1d1");
        assertEquals("5RQ1/1k6/p4P2/4p3/2P5/KP6/8/3r4 w - - 1 61", position.getChessGame().createFen().toString());

    }

    @Test
    void testGeneratePositionIllegalMove() {
        Throwable thrown = assertThrows(UciException.class, () -> {
            testee.generatePosition("position startpos moves e2f4");
        });

        assertTrue(thrown.getMessage().contains("Illegal move"));
    }


    // ******* SENDERS **********

    @Test
    void testSendInfo() {
        ClientInfo clientInfo = new ClientInfo();

        List<Move> moveList = new ArrayList<>();
        moveList.add(new Move(E2, E4));
        moveList.add(new Move(E7, E5));
        moveList.add(new Move(G1, F3));


        clientInfo.setDepth(10);
        clientInfo.setCurrentMove(new Move(E2, E4));

        clientInfo.setPv(moveList);
        clientInfo.setMultiPv(33);

        clientInfo.setNodes(10000L);
        clientInfo.setNps(3000);

        clientInfo.setScoreCp(15);
        clientInfo.setScoreMate(3);
        clientInfo.setScoreLowerBound(true);
        clientInfo.setScoreUpperBound(true);

        clientInfo.setTime(500L);

        clientInfo.setCpuload(30);
        clientInfo.setSbhits(2);
        clientInfo.setTbhits(3);

        clientInfo.setRefutation(moveList);
        clientInfo.setCurrentMoveNumber(3);
        clientInfo.setCurrLineCpu(5);
        clientInfo.setCurrLineMoves(moveList);

        clientInfo.setHashFull(30);

        clientInfo.setStringMessage("Hello");

        testee.sendInfo(clientInfo);

        verify(writer).println("info depth 10 currmove e2e4 currmovenumber 3 score mate 3 score cp 15 lowerbound upperbound nodes 10000 nps 3000 time 500 pv e2e4 e7e5 g1f3 multipv 33 hashfull 30 tbhits 3 sbhits 2 cpuload 30 string Hello refutation e2e4 e7e5 g1f3 currline cpunr 5 e2e4 e7e5 g1f3");

    }

    @Test
    void testSendUci() {

        ClientUciResponse clientUciResponse = new ClientUciResponse();


        clientUciResponse.setAuthor("Bernhard von Gunten");
        clientUciResponse.setName("Dragonborn");

        List<String> vars = new ArrayList<>();
        vars.add("Selection 0");
        vars.add("Selection 1");
        vars.add("Selection 2");
        vars.add("Selection 3");

        ClientOption clientOptionCheck = new ClientOption();
        clientOptionCheck.setType(ClientOption.OptionType.CHECK);
        clientOptionCheck.setName("Use Openingbook");
        clientOptionCheck.setValue("true");
        clientOptionCheck.setDefaultValue("false");

        ClientOption clientOptionCombo = new ClientOption();
        clientOptionCombo.setType(ClientOption.OptionType.COMBO);
        clientOptionCombo.setName("Variable Selection");
        clientOptionCombo.setValue("Selection 1");
        clientOptionCombo.setDefaultValue("Selection 0");
        clientOptionCombo.setVars(vars);

        ClientOption clientOptionFull = new ClientOption();
        clientOptionFull.setType(ClientOption.OptionType.STRING);
        clientOptionFull.setName("Full Selection");
        clientOptionFull.setValue("Selection 2");
        clientOptionFull.setDefaultValue("Selection 0");
        clientOptionFull.setVars(vars);
        clientOptionFull.setMax("10");
        clientOptionFull.setMin("1");


        List<ClientOption> clientOptions = new ArrayList<>();
        clientOptions.add(clientOptionCheck);
        clientOptions.add(clientOptionCombo);
        clientOptions.add(clientOptionFull);

        clientUciResponse.setClientOptions(clientOptions);

        testee.sendUci(clientUciResponse);

        verify(writer).println("id name Dragonborn");
        verify(writer).println("id author Bernhard von Gunten");
        verify(writer).println("option name Use Openingbook type check default false");
        verify(writer).println("option name Variable Selection type combo default Selection 0 var Selection 0 Selection 1 Selection 2 Selection 3");
        verify(writer).println("option name Full Selection type string default Selection 0 min 1 max 10 var Selection 0 Selection 1 Selection 2 Selection 3");
        verify(writer).println("uciok");
    }

    @Test
    void testSendBestMove() {
        Move move = new Move(E2, E4);
        testee.sendBestMove(move);
        verify(writer).println("bestmove e2e4");
    }

    @Test
    void testSendReadyOk() {
        testee.sendReadyOk();
        verify(writer).println("readyok");
    }

    @Test
    void testSendError() {
        testee.sendError("exception");
        verify(writer).println("info string error: exception");
    }


    //  ******* COMMANDS *********


    @Test
    void testCommandUci() {
        testee.handleConsoleInput("uci");
        verify(listener).handleUci();
    }

    @Test
    void testCommandDebugOn() {
        testee.handleConsoleInput("debug on");
        verify(listener).handleDebugOn();
    }

    @Test
    void testCommandDebugOff() {
        testee.handleConsoleInput("debug off");
        verify(listener).handleDebugOff();
    }

    @Test
    void testCommandOption() {
        testee.handleConsoleInput("setoption name Max Threads value 33");
        verify(listener).handleSetOption(Mockito.isNotNull());
    }

    @Test
    void testCommandRegisterInvalid() {
        assertThrows(UciException.class, () -> {
            testee.handleConsoleInput("register undefined");
        });
    }

    @Test
    void testCommandRegisterLater() {
        testee.handleConsoleInput("register later");
        verify(listener).handleRegister(Mockito.isNotNull());
    }

    @Test
    void testCommandIsReady() {
        testee.handleConsoleInput("isready");
        verify(listener).handleIsReady();
    }

    @Test
    void testCommandUciNewGame() {
        testee.handleConsoleInput("ucinewgame");
        verify(listener).handleUciNewGame();
    }

    @Test
    void testCommandPosition() {
        testee.handleConsoleInput("position startpos");
        verify(listener).handlePosition(Mockito.isNotNull());
    }

    @Test
    void testCommandGo() {
        testee.handleConsoleInput("go");
        verify(listener).handleGo(Mockito.isNotNull());
    }

    @Test
    void testCommandStop() {
        testee.handleConsoleInput("stop");
        verify(listener).handleStop();
    }

    @Test
    void testCommandPonderHit() {
        testee.handleConsoleInput("ponderhit");
        verify(listener).handlePonderHit();
    }

    @Test
    void testCommandQuit() {
        testee.handleConsoleInput("quit");
        verify(listener).handleQuit();
    }

}
