module ch.nostromo.tiffanys.commons {
    requires java.logging;
    requires static lombok;

    exports ch.nostromo.tiffanys.commons;
    exports ch.nostromo.tiffanys.commons.board;
    exports ch.nostromo.tiffanys.commons.move;
    exports ch.nostromo.tiffanys.commons.move.movegenerator;
    exports ch.nostromo.tiffanys.commons.formats;
    exports ch.nostromo.tiffanys.commons.utils;
}
