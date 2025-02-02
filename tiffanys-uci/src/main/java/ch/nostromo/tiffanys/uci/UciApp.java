package ch.nostromo.tiffanys.uci;

import ch.nostromo.tiffanys.commons.utils.Constants;
import ch.nostromo.tiffanys.uci.controller.UciController;

public class UciApp {

    public static final String TITLE = Constants.APPLICATION +  " " + Constants.VERSION;
    public static final String LOG_FILE = Constants.APPLICATION + "-UCI-" + Constants.VERSION +  ".log";


    public static void main(String... args) {
        new UciController().init(args);
    }


}
