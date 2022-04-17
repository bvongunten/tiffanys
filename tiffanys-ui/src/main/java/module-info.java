module ch.nostromo.tiffanys.ui {
    requires java.logging;
    requires java.prefs;
    requires javafx.controls;
    requires javafx.graphics;
    requires javafx.fxml;

    requires ch.nostromo.tiffanys.commons;
    requires ch.nostromo.tiffanys.dragonborn.engine;
    requires ch.nostromo.tiffanys.chesslink;
    requires ch.nostromo.tiffanys.lichess;
    requires java.net.http;

    requires static lombok;
    requires ch.nostromo.tiffanys.dragonborn.commons;

    opens ch.nostromo.tiffanys.ui to javafx.graphics;
    opens ch.nostromo.tiffanys.ui.controllers to javafx.fxml;

    exports ch.nostromo.tiffanys.ui;
}