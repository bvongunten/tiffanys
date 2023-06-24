package ch.nostromo.tiffanys.commons.engine.settings;

/**
 * Engine settings
 *
 * default transposition table size: 128MB
 * default threads: Runtime.getRuntime().availableProcessors();
 *
 */
public sealed interface EngineSettings permits EngineSettingsDepth, EngineSettingsTimePerMove {

    int DEFAULT_TT_SIZE = 128;
    int DEFAULT_THREADS =  Runtime.getRuntime().availableProcessors();

    int transpositionTableSize();
    int threads();
}

