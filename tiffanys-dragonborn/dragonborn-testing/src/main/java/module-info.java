module ch.nostromo.tiffanys.dragonborn.testing {
    requires java.logging;
    requires static lombok;

    requires ch.nostromo.tiffanys.commons;
    requires ch.nostromo.tiffanys.dragonborn.commons;
    requires ch.nostromo.tiffanys.dragonborn.engine;

    exports ch.nostromo.tiffanys.dragonborn.testing;
}