package ch.nostromo.tiffanys.dragonborn.commons;

import ch.nostromo.tiffanys.dragonborn.commons.opening.OpeningBook;
import ch.nostromo.tiffanys.dragonborn.engine.DragonbornEngine;

public class EngineFactory {

    public static Engine createDefaultEngine(EngineSettings engineSettings) {
        return new DragonbornEngine(engineSettings);
    }

    public static Engine createDefaultEngine(EngineSettings engineSettings, OpeningBook openingBook) {
        return new DragonbornEngine(engineSettings, openingBook);
    }

}
