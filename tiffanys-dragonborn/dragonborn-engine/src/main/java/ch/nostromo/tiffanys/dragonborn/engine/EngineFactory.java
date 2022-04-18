package ch.nostromo.tiffanys.dragonborn.engine;

import ch.nostromo.tiffanys.dragonborn.commons.Engine;
import ch.nostromo.tiffanys.dragonborn.commons.EngineSettings;
import ch.nostromo.tiffanys.dragonborn.commons.opening.OpeningBook;

public class EngineFactory {

    public static Engine createEngine(EngineSettings engineSettings) {
        return new DragonbornEngine(engineSettings);
    }

    public static Engine createEngine(EngineSettings engineSettings, OpeningBook openingBook) {
        return new DragonbornEngine(engineSettings, openingBook);
    }

}
