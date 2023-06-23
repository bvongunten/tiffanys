package ch.nostromo.tiffanys.ui.controllers;

import ch.nostromo.tiffanys.lichess.LichessController;
import ch.nostromo.tiffanys.lichess.dtos.commons.Challenge;
import ch.nostromo.tiffanys.lichess.dtos.events.GameStartEvent;
import ch.nostromo.tiffanys.lichess.dtos.games.Game;
import ch.nostromo.tiffanys.lichess.exception.LichessException;
import ch.nostromo.tiffanys.lichess.streams.EventStream;
import ch.nostromo.tiffanys.lichess.streams.EventStreamListener;
import ch.nostromo.tiffanys.ui.TiffanysFxGuiCentral;
import ch.nostromo.tiffanys.ui.preferences.TiffanysConfig;
import ch.nostromo.tiffanys.ui.utils.game.AppGameSettings;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CreateLichessGame implements Initializable, EventStreamListener {
    protected Logger LOG = Logger.getLogger(getClass().getName());

    private AppGameSettings appGameSettings;

    @FXML
    private ToggleButton btnUseChessLink;

    @FXML
    private CheckBox cbChessLinkCableRight;

    @FXML
    private TextField txtGameLength;

    @FXML
    private TextField txtBonusSec;

    @FXML
    private RadioButton colorWhite;

    @FXML
    private RadioButton colorBlack;

    @FXML
    private RadioButton colorRandom;

    @FXML
    private CheckBox cbRtated;

    @FXML
    private TextField txtPlayer;

    @FXML
    private Button btnOk;

    public void setAppGameSettings(AppGameSettings appGameSettings) {
        this.appGameSettings = appGameSettings;
    }

    public AppGameSettings getAppGameSettings() {
        return appGameSettings;
    }

    private Challenge expectedChallenge;

    private LichessController lc = null;

    private EventStream es = null;

    CompletableFuture<List<Object>> future = null;


    @FXML
    void onCreateSeek(ActionEvent event) {
        lc = TiffanysFxGuiCentral.getInstance().connectToLichess();

        es = lc.getEventStream();
        es.getListeners().add(this);
        future = es.startStream();

        String rated = cbRtated.isSelected() ? "true" : "false";
        String clockLimit = String.valueOf(Integer.valueOf(this.txtGameLength.getText()));
        String increment =  this.txtBonusSec.getText();
        String color = "random";
        if (colorWhite.isSelected()) {
            color = "white";
        } else if (colorBlack.isSelected()) {
            color = "black";
        }

        expectedChallenge = lc.createSeek(rated, clockLimit, increment, color);

    }

    @FXML
    void onPostGame(ActionEvent event) {
        lc = TiffanysFxGuiCentral.getInstance().connectToLichess();

         es = lc.getEventStream();
        es.getListeners().add(this);
        future = es.startStream();

        String opponent = txtPlayer.getText();
        String rated = cbRtated.isSelected() ? "true" : "false";
        String clockLimit = String.valueOf(Integer.valueOf(this.txtGameLength.getText()) * 60);
        String increment =  this.txtBonusSec.getText();
        String color = "random";
        if (colorWhite.isSelected()) {
           color = "white";
        } else if (colorBlack.isSelected()) {
           color = "black";
        }

        expectedChallenge = lc.challengePlayer(opponent, rated, clockLimit, increment, color);

    }

    @FXML
    void onActionAbort(ActionEvent event) {
        this.appGameSettings = null;
        closeForm();
    }

    private void closeForm() {

        if (future != null && !future.isDone()) {
            try {
                future.cancel(true);
                es.setRunning(false);
            } catch (Exception ignored) {
                LOG.log(Level.WARNING,"Unable to cleanly close event stream.", ignored);
            }
        }


        // get a handle to the stage
        Stage stage = (Stage) btnUseChessLink.getScene().getWindow();
        stage.close();
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Do nothing
    }


    @Override
    public void onGameStartEvent(GameStartEvent gameStartEvent) {
        Platform.runLater(() -> {
            Game game = lc.getGameByGameId(gameStartEvent.getGame().getId());

            appGameSettings.setLichessGame(true);
            appGameSettings.setPlayerWhiteName(game.getPlayers().getWhite().getUserId());
            appGameSettings.setPlayerBlackName(game.getPlayers().getBlack().getUserId());
            appGameSettings.setLichessGameId(game.getId());

            String userName = TiffanysConfig.getStringValue(TiffanysConfig.KEY_LICHESS_USER, "");

            if (appGameSettings.getPlayerWhiteName().equals(userName)) {
                appGameSettings.setPlayerTypeWhite(AppGameSettings.PlayerType.HUMAN);
                appGameSettings.setPlayerTypeBlack(AppGameSettings.PlayerType.LICHESS_OPPONENT);
            } else {
                appGameSettings.setPlayerTypeWhite(AppGameSettings.PlayerType.LICHESS_OPPONENT);
                appGameSettings.setPlayerTypeBlack(AppGameSettings.PlayerType.HUMAN);
            }

            appGameSettings.setUseChessLinkBoard(this.btnUseChessLink.isSelected());
            appGameSettings.setChessLinkCableRight(this.cbChessLinkCableRight.isSelected());

            closeForm();
        });
    }

    @Override
    public void onStreamException(LichessException exception) {
        Platform.runLater(() -> {
            throw exception;
        });
    }
}
