package ch.nostromo.tiffanys.dragonborn.engine;

import ch.nostromo.tiffanys.commons.ChessGame;
import ch.nostromo.tiffanys.commons.engine.Engine;
import ch.nostromo.tiffanys.commons.engine.EngineListener;
import ch.nostromo.tiffanys.commons.engine.EngineResult;
import ch.nostromo.tiffanys.commons.engine.settings.EngineSettings;
import ch.nostromo.tiffanys.commons.engine.settings.EngineSettingsDepth;
import ch.nostromo.tiffanys.commons.engine.settings.EngineSettingsTimePerMove;
import ch.nostromo.tiffanys.commons.move.Move;
import ch.nostromo.tiffanys.commons.opening.OpeningBook;
import ch.nostromo.tiffanys.dragonborn.engine.board.Board;
import ch.nostromo.tiffanys.dragonborn.engine.search.Search;
import ch.nostromo.tiffanys.dragonborn.engine.search.SearchListener;
import ch.nostromo.tiffanys.dragonborn.engine.search.SearchResult;
import ch.nostromo.tiffanys.dragonborn.engine.transposition.TranspositionTable;
import lombok.Getter;

public class DragonbornEngine implements Engine, SearchListener {

    @Getter
    private final EngineSettings engineSettings;

    private Search currentSearch;
    private EngineListener engineListener = null;

    public DragonbornEngine(EngineSettings engineSettings) {
        this.engineSettings = engineSettings;
    }


    // ========================================================================
    // Engine implementation
    // ========================================================================

    @Override
    public EngineResult startSearch(ChessGame game) {
        return executeSearch(game, false);
    }

    @Override
    public void startAsyncSearch(ChessGame game, EngineListener engineListener) {
        this.engineListener = engineListener;

        Thread searchThread = new Thread(() -> executeSearch(game, true), "dragonborn-async-search");

        searchThread.setDaemon(true);
        searchThread.start();
    }

    @Override
    public void stopAsyncSearch() {
        if (currentSearch != null) {
            currentSearch.stop();
        }
    }

    // ========================================================================
    // Core search logic
    // ========================================================================

    private EngineResult executeSearch(ChessGame game, boolean asyncSearch) {

        Move openingMove = new OpeningBook().queryRandomOpening(game.createFen());
        if (openingMove != null) {

            openingMove.setOpeningBook(true);

            EngineResult result = new EngineResult();
            result.setSelectedMove(openingMove);


            // Events shall be fired anyway
            if (asyncSearch) {
                fireEngineUpdateEvent(result);
                fireEngineFinishedEvent(result);
            }

            return result;

        } else {

            this.currentSearch = new Search(new TranspositionTable(engineSettings.transpositionTableSize()), engineSettings.threads());
            this.currentSearch.setListener(this);

            SearchResult searchResult = switch (engineSettings) {
                case EngineSettingsTimePerMove s -> currentSearch.searchTime(new Board(game), s.timeMs());
                case EngineSettingsDepth s -> currentSearch.searchDepth(new Board(game), s.depth());
            };

            this.currentSearch.shutdown();

            return searchResult.toEngineResult();
        }
    }

    // ========================================================================
    // Listeners handling
    // ========================================================================

    @Override
    public void onDepthCompleted(SearchResult searchResult) {
        fireEngineUpdateEvent(searchResult.toEngineResult());
    }

    @Override
    public void onSearchFinished(SearchResult searchResult) {
        fireEngineFinishedEvent(searchResult.toEngineResult());
    }

    private void fireEngineUpdateEvent(EngineResult engineResult) {
        if (engineListener != null) {
            engineListener.onEngineUpdate(engineResult);
        }
    }

    private void fireEngineFinishedEvent(EngineResult engineResult) {
        if (engineListener != null) {
            engineListener.onEngineFinished(engineResult);
        }
    }



}
