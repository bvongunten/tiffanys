package ch.nostromo.tiffanys.dragonborn.engine;

import ch.nostromo.tiffanys.commons.ChessGame;
import ch.nostromo.tiffanys.commons.engine.EngineResult;
import ch.nostromo.tiffanys.commons.engine.settings.EngineSettingsTimePerMove;
import ch.nostromo.tiffanys.commons.format.FenFormat;
import ch.nostromo.tiffanys.commons.engine.Engine;
import ch.nostromo.tiffanys.commons.engine.EngineListener;
import ch.nostromo.tiffanys.commons.engine.settings.EngineSettingsDepth;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

class DragonbornEngineTest {

    @Test
    void dragonbornAsyncEventTest() throws InterruptedException {
        CountDownLatch updateLatch = new CountDownLatch(3);
        CountDownLatch finishedLatch = new CountDownLatch(1);

        AtomicReference<EngineResult> finishedResult = new AtomicReference<>();

        Engine engine = new DragonbornEngine(new EngineSettingsDepth(20));
        EngineListener engineListener = (new EngineListener() {
            @Override
            public void onEngineUpdate(EngineResult event) {
                updateLatch.countDown();
            }

            @Override
            public void onEngineFinished(EngineResult event) {
                finishedResult.set(event);
                finishedLatch.countDown();
            }
        });

        engine.startAsyncSearch(new ChessGame(new FenFormat("4K3/p7/8/8/8/8/7P/3k4 w - - 0 1")), engineListener);

        // Wait for 3 updates, then fire stop
        assertTrue(updateLatch.await(10, TimeUnit.SECONDS), "Did not receive 3 updates");
        engine.stopAsyncSearch();

        assertTrue(finishedLatch.await(5, TimeUnit.SECONDS), "No finished event received");

        assertNotNull(finishedResult.get(), "Finished event should not be null");
        assertNotNull(finishedResult.get().getSelectedMove(), "Should have a selected move");
    }


    @Test
    void dragonbornAsyncEventTestOpeningBook() throws InterruptedException {

        CountDownLatch updateLatch = new CountDownLatch(1);
        CountDownLatch finishedLatch = new CountDownLatch(1);

        AtomicReference<EngineResult> updateResult = new AtomicReference<>();
        AtomicReference<EngineResult> finishedResult = new AtomicReference<>();

        Engine engine = new DragonbornEngine(new EngineSettingsDepth(20));
        EngineListener engineListener = (new EngineListener() {
            @Override
            public void onEngineUpdate(EngineResult event) {
                updateResult.set(event);
                updateLatch.countDown();
            }


            @Override
            public void onEngineFinished(EngineResult event) {
                finishedResult.set(event);
                finishedLatch.countDown();
            }
        });

        engine.startAsyncSearch(new ChessGame(FenFormat.INITIAL_FEN), engineListener);

        // Wait for 3 updates, then fire stop
        assertTrue(updateLatch.await(1, TimeUnit.SECONDS), "Did not receive update");
        assertTrue(finishedLatch.await(1, TimeUnit.SECONDS), "Did not receive finish event");

        engine.stopAsyncSearch();

        assertTrue(updateResult.get().getSelectedMove().isOpeningBook());
        assertTrue(finishedResult.get().getSelectedMove().isOpeningBook());
    }



    @Test
    void dragonBornSyncTestTime(){
        int maxTime = 300;

        Engine engine = new DragonbornEngine(new EngineSettingsTimePerMove(maxTime));

        EngineResult result = engine.startSearch(new ChessGame(new FenFormat("1B3Nbb/1r2pn2/Bnp1P3/3kP3/p2PR3/1Pp1P1N1/5K2/8 w - - 0 1")));

        // Assert that the overhead is < 500ms
        assertTrue(result.getTotalTimeInMs() - maxTime < 500);

    }


    @Test
    void testResultAttributesWinning() {

        Engine engine = new DragonbornEngine(new EngineSettingsDepth(6));
        EngineResult engineResult = engine.startSearch(new ChessGame(new FenFormat("1B1Q1R2/8/qNrn3p/2p1rp2/Rn3k1K/8/5P2/bbN4B w - - 0 1")));

        assertEquals(2, engineResult.getSelectedMove().getMoveAttributes().getMateIn());
        assertEquals(0, engineResult.getSelectedMove().getMoveAttributes().getScore());


    }

    @Test
    void testResultAttributesLoosing() {
        Engine engine = new DragonbornEngine(new EngineSettingsDepth(6));
        EngineResult engineResult = engine.startSearch(new ChessGame(new FenFormat("1B3R2/8/qNrn1Q1p/2p1rp2/Rn3k1K/8/5P2/bbN4B b - - 1 1")));

        assertEquals(-1, engineResult.getSelectedMove().getMoveAttributes().getMateIn());
        assertEquals(0, engineResult.getSelectedMove().getMoveAttributes().getScore());
    }


    @Test
    void testOpeningBookMove() {
        Engine engine = new DragonbornEngine(new EngineSettingsDepth(6));
        EngineResult engineResult = engine.startSearch(new ChessGame(FenFormat.INITIAL_FEN));

        assertTrue(engineResult.getSelectedMove().isOpeningBook());
    }



}
