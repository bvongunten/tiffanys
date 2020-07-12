module ch.nostromo.tiffanys.chesslink {
    exports ch.nostromo.tiffanys.chesslink;
    exports ch.nostromo.tiffanys.chesslink.exception;

    requires java.logging;

    requires static lombok;

    requires ch.nostromo.tiffanys.commons;
    requires lib.usb3.ftdi;
    requires lib.javax.usb3;

}