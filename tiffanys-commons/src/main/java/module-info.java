module ch.nostromo.tiffanys.commons {
    requires static lombok;
    requires org.slf4j;

    exports ch.nostromo.tiffanys.commons;
    exports ch.nostromo.tiffanys.commons.board;
    exports ch.nostromo.tiffanys.commons.move;
    exports ch.nostromo.tiffanys.commons.move.movegenerator;
    exports ch.nostromo.tiffanys.commons.format;
    exports ch.nostromo.tiffanys.commons.format.pgn;
    exports ch.nostromo.tiffanys.commons.uci.protocol;
    exports ch.nostromo.tiffanys.commons.uci.client;
    exports ch.nostromo.tiffanys.commons.exception;
    exports ch.nostromo.tiffanys.commons.opening;
    exports ch.nostromo.tiffanys.commons.engine;
    exports ch.nostromo.tiffanys.commons.engine.settings;

}
