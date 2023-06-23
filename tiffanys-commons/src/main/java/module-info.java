module ch.nostromo.tiffanys.commons {
    requires java.logging;
    requires static lombok;

    exports ch.nostromo.tiffanys.commons;
    exports ch.nostromo.tiffanys.commons.board;
    exports ch.nostromo.tiffanys.commons.enums;
    exports ch.nostromo.tiffanys.commons.fen;
    exports ch.nostromo.tiffanys.commons.fields;
    exports ch.nostromo.tiffanys.commons.move;
    exports ch.nostromo.tiffanys.commons.pgn;
    exports ch.nostromo.tiffanys.commons.pieces;
    exports ch.nostromo.tiffanys.commons.rules;
    exports ch.nostromo.tiffanys.commons.logging;
    exports ch.nostromo.tiffanys.commons.uci;
}