package ch.nostromo.tiffanys.ui;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.logging.Level;

import ch.nostromo.tiffanys.commons.logging.LogUtils;
import ch.nostromo.tiffanys.ui.fx.tools.ExceptionTools;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Modality;
import javafx.stage.Stage;

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
