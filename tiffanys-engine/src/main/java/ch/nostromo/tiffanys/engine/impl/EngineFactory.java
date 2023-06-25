package ch.nostromo.tiffanys.engine.impl;

import ch.nostromo.tiffanys.engine.commons.Engine;
import ch.nostromo.tiffanys.engine.commons.EngineSettings;
import ch.nostromo.tiffanys.engine.commons.opening.OpeningBook;

public class EngineFactory {

    public static Engine createEngine(EngineSettings engineSettings) {
        return new DragonbornEngine(engineSettings);
    }

    public static Engine createEngine(EngineSettings engineSettings, OpeningBook openingBook) {
        return new DragonbornEngine(engineSettings, openingBook);
    }

}
