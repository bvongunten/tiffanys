package ch.nostromo.tiffanys.ui;

import ch.nostromo.tiffanys.commons.logging.LogUtils;
import ch.nostromo.tiffanys.ui.fx.tools.ExceptionTools;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.logging.Level;

public class TiffanysFxGui extends Application {


    /*
     * (non-Javadoc)
     * 
     * @see javafx.application.Application#start(javafx.stage.Stage)
     */
    @Override
    public void start(Stage primaryStage) throws IOException {
        LogUtils.initializeLogging(Level.FINEST, Level.OFF, null, null);
        Thread.setDefaultUncaughtExceptionHandler(TiffanysFxGui::showError);

        TiffanysFxGuiCentral.createInstance(primaryStage);
    	TiffanysFxGuiCentral.getInstance().showMainMenuForm();
    }

    private static void showError(Thread t, Throwable e) {
        if (Platform.isFxApplicationThread()) {
            ExceptionTools.showExceptionDialog(e);
        } else {
            System.err.println("An unexpected error occurred in "+t);
        }
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
