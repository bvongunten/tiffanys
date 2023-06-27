package ch.nostromo.tiffanys.engine;

import ch.nostromo.tiffanys.engine.commons.Engine;
import ch.nostromo.tiffanys.engine.commons.EngineSettings;
import ch.nostromo.tiffanys.engine.commons.opening.OpeningBook;
import ch.nostromo.tiffanys.engine.dragonborn.Dragonborn;

public class EngineFactory {

    public static Engine createEngine(EngineSettings engineSettings) {
        return new Dragonborn(engineSettings);
    }

    public static Engine createEngine(EngineSettings engineSettings, OpeningBook openingBook) {
        return new Dragonborn(engineSettings, openingBook);
    }

}
