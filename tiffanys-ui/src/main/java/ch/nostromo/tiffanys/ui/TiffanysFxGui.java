package ch.nostromo.tiffanys.ui;

import java.io.IOException;
import java.util.logging.Level;

import ch.nostromo.tiffanys.commons.logging.LogUtils;
import javafx.application.Application;
import javafx.stage.Stage;

public class TiffanysFxGui extends Application {
    public static final String VERSION = "0.6.0";

    public static final String APPLICATION = "Tiffanys UI";

    public static final String TITLE = APPLICATION +  " " + VERSION;

    public static final String AUTHOR = "Bernhard von Gunten";

    public static final String HOME_DIRECTORY = System.getProperty("user.home") + "/." + APPLICATION + "-" + VERSION;


    /*
     * (non-Javadoc)
     * 
     * @see javafx.application.Application#start(javafx.stage.Stage)
     */
    @Override
    public void start(Stage primaryStage) throws IOException {
        LogUtils.initializeLogging(Level.INFO, Level.OFF, null, null);

        TiffanysFxGuiCentral.createInstance(primaryStage);
    	TiffanysFxGuiCentral.getInstance().showMainMenuForm();
    }

    /**
     * The main method.
     *
     * @param args
     *            the arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
