package ch.nostromo.tiffanys.uci;

import ch.nostromo.tiffanys.commons.app.Application;
import ch.nostromo.tiffanys.uci.controller.UciController;

public class UciApp {

    public static final String TITLE = Application.APPLICATION +  " " + Application.VERSION;
    public static final String LOG_FILE = Application.APPLICATION + "-UCI-" + Application.VERSION +  ".log";


    public static void main(String... args) {
        new UciController().init(args);
    }


}
