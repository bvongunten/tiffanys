module ch.nostromo.tiffanys.lichess {
    exports ch.nostromo.tiffanys.lichess;
    exports ch.nostromo.tiffanys.lichess.exception;
    exports ch.nostromo.tiffanys.lichess.dtos;
    exports ch.nostromo.tiffanys.lichess.dtos.board;
    exports ch.nostromo.tiffanys.lichess.dtos.commons;
    exports ch.nostromo.tiffanys.lichess.dtos.account;
    exports ch.nostromo.tiffanys.lichess.dtos.games;


    opens ch.nostromo.tiffanys.lichess.dtos;
    opens ch.nostromo.tiffanys.lichess.dtos.board;
    opens ch.nostromo.tiffanys.lichess.dtos.commons;
    opens ch.nostromo.tiffanys.lichess.dtos.account;
    opens ch.nostromo.tiffanys.lichess.dtos.games;
    opens ch.nostromo.tiffanys.lichess.dtos.playing;

    exports ch.nostromo.tiffanys.lichess.streams;
    exports ch.nostromo.tiffanys.lichess.tools;
    exports ch.nostromo.tiffanys.lichess.dtos.playing;

    requires java.logging;

    requires static lombok;

    requires ch.nostromo.tiffanys.commons;
    requires java.net.http;
    requires com.google.gson;
}