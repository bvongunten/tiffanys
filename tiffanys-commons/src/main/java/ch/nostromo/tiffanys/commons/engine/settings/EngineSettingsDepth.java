package ch.nostromo.tiffanys.commons.engine.settings;

/**
 * Engine settings for a fixed depth game, default threads =Runtime.getRuntime().availableProcessors()
 *
 * @param depth in half moves
 *
 * @param transpositionTableSize transposition table size in MB
 * @param threads amount of threads to be used
 */
public record EngineSettingsDepth(int depth, int transpositionTableSize, int threads) implements EngineSettings {
    public EngineSettingsDepth(int depth) {
        this(depth, DEFAULT_TT_SIZE, DEFAULT_THREADS);
    }
}
