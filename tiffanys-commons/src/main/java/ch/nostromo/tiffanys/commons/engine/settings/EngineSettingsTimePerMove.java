package ch.nostromo.tiffanys.commons.engine.settings;

/**
 * Engine settings for a fixed amount of time (ms) per move, default threads =Runtime.getRuntime().availableProcessors()
 *
 * @param timeMs per move
 *
 * @param transpositionTableSize transposition table size in MB
 * @param threads amount of threads to be used
 */
public record EngineSettingsTimePerMove(int timeMs, int transpositionTableSize, int threads) implements EngineSettings {
    public EngineSettingsTimePerMove(int timeMs) {
        this(timeMs, DEFAULT_TT_SIZE, DEFAULT_THREADS);
    }
}
