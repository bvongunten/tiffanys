package ch.nostromo.tiffanys.uci;

import ch.nostromo.tiffanys.uci.controller.UciController;

public class UciApp {

    public static final String VERSION = "0.1.0";

    public static final String APPLICATION = "Tiffanys Dragonborn";

    public static final String TITLE = APPLICATION +  " " + VERSION;

    public static final String AUTHOR = "Bernhard von Gunten";

    public static final String HOME_DIRECTORY = System.getProperty("user.home") + "/." + APPLICATION + "-" + VERSION;


    public static void main(String... args) {
        new UciController().init(args);
    }

}
