module ch.nostromo.tiffanys.dragonborn.commons {
    requires java.logging;
    requires static lombok;

    requires ch.nostromo.tiffanys.commons;

    exports ch.nostromo.tiffanys.dragonborn.commons;
    exports ch.nostromo.tiffanys.dragonborn.commons.events;
    exports ch.nostromo.tiffanys.dragonborn.commons.opening;

}