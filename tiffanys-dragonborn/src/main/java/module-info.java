module ch.nostromo.tiffanys.dragonborn {
    requires java.logging;
    requires static lombok;

    requires ch.nostromo.tiffanys.commons;

    exports ch.nostromo.tiffanys.dragonborn.commons;
    exports ch.nostromo.tiffanys.dragonborn.commons.events;
    exports ch.nostromo.tiffanys.dragonborn.commons.opening;

    exports ch.nostromo.tiffanys.dragonborn.engine;

}