module ch.nostromo.tiffanys.engine {
    requires java.logging;
    requires static lombok;

    requires ch.nostromo.tiffanys.commons;

    exports ch.nostromo.tiffanys.engine.impl;
    exports ch.nostromo.tiffanys.engine.commons;
    exports ch.nostromo.tiffanys.engine.commons.events;
    exports ch.nostromo.tiffanys.engine.commons.opening;

}
