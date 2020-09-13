package ch.nostromo.tiffanys.ui;

import ch.nostromo.tiffanys.chesslink.ChessLinkBoard;
import ch.nostromo.tiffanys.commons.ChessGame;
import ch.nostromo.tiffanys.commons.board.Board;
import ch.nostromo.tiffanys.commons.enums.GameColor;
import ch.nostromo.tiffanys.commons.fen.FenFormat;
import ch.nostromo.tiffanys.commons.logging.LogUtils;
import ch.nostromo.tiffanys.commons.move.Move;
import ch.nostromo.tiffanys.dragonborn.commons.EngineSettings;
import ch.nostromo.tiffanys.lichess.LichessController;
import ch.nostromo.tiffanys.lichess.tools.LichessBoardGameStateHelper;
import ch.nostromo.tiffanys.ui.controllers.*;
import ch.nostromo.tiffanys.ui.fx.DialogFinishedState;
import ch.nostromo.tiffanys.ui.preferences.TiffanysConfig;
import ch.nostromo.tiffanys.ui.utils.game.AppGameSettings;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TiffanysFxGuiCentral {

    public static final String VERSION = "0.7.0";

    public static final String APPLICATION = "Tiffanys UI";

    public static final String TITLE = APPLICATION +  " " + VERSION;

    public static final String AUTHOR = "Bernhard von Gunten";

    public static final String HOME_DIRECTORY = System.getProperty("user.home") + "/." + APPLICATION + "-" + VERSION;

    public static final String MESSAGE_BUNDLE = "Messages";

    private static TiffanysFxGuiCentral instance;

    protected Logger LOG = Logger.getLogger(getClass().getName());


    private Stage primaryStage;
    private Stage chessWindowStage;

    private TiffanysFxGuiCentral(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public static void createInstance(Stage primaryStage) throws IOException {
        instance = new TiffanysFxGuiCentral(primaryStage);
        instance.fireUpLogging();
    }

    public static TiffanysFxGuiCentral getInstance() {
        return instance;
    }

    ChessLinkBoard chessLinkBoard;

    public ResourceBundle getResourceBundle() {
        String language = TiffanysConfig.getStringValue(TiffanysConfig.KEY_LANGUAGE, "en");
        Locale locale = new Locale(language);
        return ResourceBundle.getBundle(MESSAGE_BUNDLE, locale);
    }

    public void fireUpLogging() throws IOException {


        String logLevel = TiffanysConfig.getStringValue(TiffanysConfig.KEY_LOG_FILE_LEVEL, "OFF");

        Level level = Level.OFF;
        switch (logLevel) {
            case "ALL": {
                level = Level.ALL;
                break;
            }
            case "INFO": {
                level = Level.INFO;
                break;
            }
            case "WARNING":  {
                level = Level.WARNING;
                break;
            }
            case "SEVERE": {
                level = Level.SEVERE;
                break;
            }
        }
        LogUtils.initializeLogging(Level.ALL, level, HOME_DIRECTORY, "tiffanys.log");

        LOG.fine("Logging started.");


    }

    public void showMainMenuForm() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/Welcome.fxml"), getResourceBundle());

        primaryStage.setTitle(TITLE);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }


    public LichessController connectToLichess() {
        return new LichessController(TiffanysConfig.getStringValue(TiffanysConfig.KEY_LICHESS_APIKEY, ""));
    }

    public ChessLinkBoard connectToChessLinkBoard(Boolean cableRight) {
        disconnectFromChessLinkBoard();

        chessLinkBoard = new ChessLinkBoard();
        chessLinkBoard.setCableRight(cableRight);
        chessLinkBoard.connect();

        return chessLinkBoard;
    }

    public void disconnectFromChessLinkBoard() {
        if (chessLinkBoard != null && chessLinkBoard.isConnected()) {
            try {
                chessLinkBoard.getChessLinkBoardEventListeners().clear();
                chessLinkBoard.disconnect();
            } catch (Exception ignored) {
                // Ignore;
            }
        }
    }

    public ChessLinkBoard getOrCreateChesslinkBoard(Boolean cableRight) {
        if (chessLinkBoard == null || !chessLinkBoard.isConnected()) {
            connectToChessLinkBoard(cableRight);
        }
        return chessLinkBoard;
    }


    public void createNewGame() throws IOException {
        AppGameSettings appGameSettings = showSettingsForm(new AppGameSettings());

        if (appGameSettings != null) {
            ChessGame chessGame = new ChessGame();
            showChessWindow(chessGame, appGameSettings);
        }
    }

    public void showChessWindow(ChessGame chessGame, AppGameSettings appGameSettings) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(TiffanysFxGuiCentral.class.getResource("/fxml/ChessWindow.fxml"),
                getResourceBundle());
        Parent root = (Parent) fxmlLoader.load();
        ChessWindow controller = fxmlLoader.<ChessWindow>getController();


        Scene scene = new Scene(root);
        String css = TiffanysFxGuiCentral.class.getResource("/css/StandardBoard.css").toExternalForm();
        scene.getStylesheets().add(css);

        chessWindowStage = new Stage();
        chessWindowStage.setTitle(chessGame.getGameInfo().getWhitePlayer() + " vs. " + chessGame.getGameInfo().getBlackPlayer());
        chessWindowStage.setScene(scene);
        chessWindowStage.show();

        chessWindowStage.setOnCloseRequest((WindowEvent e) -> {
            primaryStage.show();
        });


        controller.setChessGame(chessGame, appGameSettings);

        primaryStage.hide();
    }

    public void showOptionsWindow() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/EditOptions.fxml"), getResourceBundle());
        Parent root = (Parent) fxmlLoader.load();

        showUncloseableScene(new Scene(root), getResourceBundle().getString("window.options"));
    }

    public AppGameSettings showSettingsForm(AppGameSettings appGameSettings) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(TiffanysFxGuiCentral.class.getResource("/fxml/EditGameSettings.fxml"),
                getResourceBundle());
        Parent root = (Parent) fxmlLoader.load();

        EditGameSettings controller = fxmlLoader.<EditGameSettings>getController();
        controller.setAppGameSettings(appGameSettings);

        Scene scene = new Scene(root);
        String css = TiffanysFxGuiCentral.class.getResource("/css/StandardBoard.css").toExternalForm();
        scene.getStylesheets().add(css);

        Stage stage = new Stage();
        stage.setTitle(getResourceBundle().getString("window.editBoard"));
        stage.setScene(scene);
        stage.showAndWait();


        return controller.getAppGameSettings();
    }

    public AppGameSettings showLichessSettingsForm(AppGameSettings appGameSettings) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(TiffanysFxGuiCentral.class.getResource("/fxml/EditLichessGameSettings.fxml"),
                getResourceBundle());
        Parent root = (Parent) fxmlLoader.load();

        EditLichessGameSettings controller = fxmlLoader.<EditLichessGameSettings>getController();
        controller.setAppGameSettings(appGameSettings);

        Scene scene = new Scene(root);
        String css = TiffanysFxGuiCentral.class.getResource("/css/StandardBoard.css").toExternalForm();
        scene.getStylesheets().add(css);

        Stage stage = new Stage();
        stage.setTitle(getResourceBundle().getString("window.editBoard"));
        stage.setScene(scene);
        stage.showAndWait();

        return controller.getAppGameSettings();

    }

    public AppGameSettings showCreateLichessGameForm(AppGameSettings appGameSettings) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(TiffanysFxGuiCentral.class.getResource("/fxml/CreateLichessGame.fxml"),
                getResourceBundle());
        Parent root = (Parent) fxmlLoader.load();

        CreateLichessGame controller = fxmlLoader.<CreateLichessGame>getController();
        controller.setAppGameSettings(appGameSettings);

        Scene scene = new Scene(root);
        String css = TiffanysFxGuiCentral.class.getResource("/css/StandardBoard.css").toExternalForm();
        scene.getStylesheets().add(css);

        Stage stage = new Stage();
        stage.setTitle(getResourceBundle().getString("window.editBoard"));
        stage.setScene(scene);
        stage.showAndWait();

        return controller.getAppGameSettings();

    }


    public void showEngineSettingsForm(EngineSettings engineSettings) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(TiffanysFxGuiCentral.class.getResource("/fxml/EditEngineSettings.fxml"),
                getResourceBundle());
        Parent root = (Parent) fxmlLoader.load();

        EditEngineSettings controller = fxmlLoader.<EditEngineSettings>getController();
        controller.setEngineSettings(engineSettings);

        Scene scene = new Scene(root);
        String css = TiffanysFxGuiCentral.class.getResource("/css/StandardBoard.css").toExternalForm();
        scene.getStylesheets().add(css);

        Stage stage = new Stage();
        stage.setTitle(getResourceBundle().getString("window.editBoard"));
        stage.setScene(scene);
        stage.showAndWait();
    }

    public FenFormat showEditBoardForm(FenFormat fen) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(TiffanysFxGuiCentral.class.getResource("/fxml/EditBoard.fxml"),
                getResourceBundle());
        Parent root = (Parent) fxmlLoader.load();

        EditBoard controller = fxmlLoader.<EditBoard>getController();

        controller.setFen(fen);

        Scene scene = new Scene(root);
        String css = TiffanysFxGuiCentral.class.getResource("/css/StandardBoard.css").toExternalForm();
        scene.getStylesheets().add(css);

        Stage stage = new Stage();
        stage.setTitle(getResourceBundle().getString("window.editBoard"));
        stage.setScene(scene);
        stage.showAndWait();


        if (controller.getEditBoardFinishedState() == EditBoard.EditBoardFinishedState.OK) {
            return controller.getFen();
        } else {
            return null;
        }

    }


    public boolean showChessLinkPositionToMake(Board board, boolean flipped, boolean cableRight, Bounds bounds) {
        try {

            FXMLLoader fxmlLoader = new FXMLLoader(TiffanysFxGuiCentral.class.getResource("/fxml/ChessLinkBoardDialog.fxml"),
                    getResourceBundle());
            Parent root = null;
            root = (Parent) fxmlLoader.load();

            ChessLinkBoardDialog controller = fxmlLoader.<ChessLinkBoardDialog>getController();

            controller.prepare(board, null, flipped, cableRight);
            controller.updateChessLinkBoard();

            Scene scene = new Scene(root);
            String css = TiffanysFxGuiCentral.class.getResource("/css/StandardBoard.css").toExternalForm();
            scene.getStylesheets().add(css);

            centerCommunicationDialog(scene, bounds, 425, 110);

            if (controller.getFinishedState() == DialogFinishedState.OK) {
                return true;
            } else {
                return false;
            }

        } catch (IOException e) {
            throw new TiffanysFxGuiException("Unable to show ChessLinkBoardDialog with message=" + e.getMessage(), e);
        }

    }


    public Move showChessLinkMakeMove(Board board, GameColor colorToMove, boolean flipped, boolean cableRight, Bounds bounds) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(TiffanysFxGuiCentral.class.getResource("/fxml/ChessLinkBoardDialog.fxml"),
                    getResourceBundle());
            Parent root = (Parent) fxmlLoader.load();

            ChessLinkBoardDialog controller = fxmlLoader.<ChessLinkBoardDialog>getController();

            controller.prepare(board, colorToMove, flipped, cableRight);
            controller.getMoveFromChessLinkBoard();

            Scene scene = new Scene(root);
            String css = TiffanysFxGuiCentral.class.getResource("/css/StandardBoard.css").toExternalForm();
            scene.getStylesheets().add(css);

            centerCommunicationDialog(scene, bounds, 425, 110);
            if (controller.getFinishedState() == DialogFinishedState.OK) {
                return controller.getMove();
            } else {
                return null;
            }
        } catch (IOException e) {
            throw new TiffanysFxGuiException("Unable to show ChessLinkBoardDialog with message=" + e.getMessage(), e);
        }

    }

    public ChessGame showLichessGetGameDialog(ChessGame currentGame, LichessBoardGameStateHelper lichessHelper, Bounds bounds) {
        try {

            FXMLLoader fxmlLoader = new FXMLLoader(TiffanysFxGuiCentral.class.getResource("/fxml/LichessDialog.fxml"),
                    getResourceBundle());
            Parent root = null;
            root = (Parent) fxmlLoader.load();

            LichessDialog controller = fxmlLoader.<LichessDialog>getController();

            controller.prepare(currentGame, lichessHelper);
            controller.waitForGame();

            Scene scene = new Scene(root);
            String css = TiffanysFxGuiCentral.class.getResource("/css/StandardBoard.css").toExternalForm();
            scene.getStylesheets().add(css);

            centerCommunicationDialog(scene, bounds, 425, 110);

            if (controller.getFinishedState() == DialogFinishedState.OK) {
                return controller.getChessGame();
            } else {
                return null;
            }

        } catch (IOException e) {
            throw new TiffanysFxGuiException("Unable to show LichessDialog with message=" + e.getMessage(), e);
        }

    }


    public Move showLichessGetMoveDialog(ChessGame currentGame, LichessBoardGameStateHelper lichessHelper, Bounds bounds) {
        try {

            FXMLLoader fxmlLoader = new FXMLLoader(TiffanysFxGuiCentral.class.getResource("/fxml/LichessDialog.fxml"),
                    getResourceBundle());
            Parent root = null;
            root = (Parent) fxmlLoader.load();

            LichessDialog controller = fxmlLoader.<LichessDialog>getController();

            controller.prepare(currentGame, lichessHelper);
            controller.waitForMove();

            Scene scene = new Scene(root);
            String css = TiffanysFxGuiCentral.class.getResource("/css/StandardBoard.css").toExternalForm();
            scene.getStylesheets().add(css);

            centerCommunicationDialog(scene, bounds, 425, 110);

            if (controller.getFinishedState() == DialogFinishedState.OK) {
                return controller.getMove();
            } else {
                return null;
            }

        } catch (IOException e) {
            throw new TiffanysFxGuiException("Unable to show LichessDialog with message=" + e.getMessage(), e);
        }

    }

    private Stage centerCommunicationDialog(Scene scene, Bounds bounds, double width, double height) {
        Stage stage = new Stage();
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(scene);
        stage.initOwner(chessWindowStage);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setX(bounds.getCenterX() - width/ 2);
        stage.setY(bounds.getCenterY() - height / 2);
        stage.showAndWait();
        return stage;
    }

    private void showUncloseableScene(Scene scene, String title) {
        Stage stage = new Stage();
        stage.setTitle(title);
        stage.setScene(scene);

        stage.initStyle(StageStyle.UTILITY);
        stage.setOnCloseRequest((event) -> event.consume());

        stage.show();

        primaryStage.hide();
    }


    public void lichessGame() throws IOException {

        if (TiffanysConfig.getStringValue(TiffanysConfig.KEY_LICHESS_APIKEY, "").isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Lichess");
            alert.setHeaderText("Please configure an API key!");
            alert.showAndWait();
            return;
        }

        AppGameSettings appGameSettings = showLichessSettingsForm(new AppGameSettings());
        if (appGameSettings != null) {
            ChessGame chessGame = new ChessGame();
            showChessWindow(chessGame, appGameSettings);
        }
    }

    public void createLichessGame() throws IOException {

        if (TiffanysConfig.getStringValue(TiffanysConfig.KEY_LICHESS_APIKEY, "").isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Lichess");
            alert.setHeaderText("Please configure an API key!");
            alert.showAndWait();
            return;
        }

        AppGameSettings appGameSettings = showCreateLichessGameForm(new AppGameSettings());
        if (appGameSettings != null) {
            ChessGame chessGame = new ChessGame();
            showChessWindow(chessGame, appGameSettings);
        }
    }

}
