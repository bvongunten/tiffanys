package ch.nostromo.tiffanys.ui.controllers;

import ch.nostromo.tiffanys.lichess.LichessController;
import ch.nostromo.tiffanys.lichess.dtos.playing.OngoingGame;
import ch.nostromo.tiffanys.lichess.dtos.playing.OngoingGames;
import ch.nostromo.tiffanys.lichess.streams.EventStream;
import ch.nostromo.tiffanys.ui.TiffanysFxGuiCentral;
import ch.nostromo.tiffanys.ui.preferences.TiffanysConfig;
import ch.nostromo.tiffanys.ui.utils.game.AppGameSettings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class EditLichessGameSettings  implements Initializable  {

	private AppGameSettings appGameSettings;

    @FXML
    private ToggleButton btnUseChessLink;

    @FXML
    private CheckBox cbChessLinkCableRight;

    @FXML
    private TextField txtPlayerWhiteName;

    @FXML
    private TextField txtPlayerBlackName;

    @FXML
    private TextField txtGameId;

    @FXML
    private ChoiceBox choiceGames;

    @FXML
    private Button btnOk;

	public void setAppGameSettings(AppGameSettings appGameSettings) {
		this.appGameSettings = appGameSettings;
	}

	public AppGameSettings getAppGameSettings() {
		return appGameSettings;
	}
	
	@FXML
	void onActionOk(ActionEvent event) {
        appGameSettings.setLichessGame(true);
        appGameSettings.setPlayerWhiteName(txtPlayerWhiteName.getText());
        appGameSettings.setPlayerBlackName(txtPlayerBlackName.getText());
        appGameSettings.setLichessGameId(txtGameId.getText());

        String userName = TiffanysConfig.getStringValue(TiffanysConfig.KEY_LICHESS_USER, "");

        if (txtPlayerWhiteName.getText().equals(userName)) {
            appGameSettings.setPlayerTypeWhite(AppGameSettings.PlayerType.HUMAN);
            appGameSettings.setPlayerTypeBlack(AppGameSettings.PlayerType.LICHESS_OPPONENT);
        } else {
            appGameSettings.setPlayerTypeWhite(AppGameSettings.PlayerType.LICHESS_OPPONENT);
            appGameSettings.setPlayerTypeBlack(AppGameSettings.PlayerType.HUMAN);
        }

        appGameSettings.setUseChessLinkBoard(this.btnUseChessLink.isSelected());
        appGameSettings.setChessLinkCableRight(this.cbChessLinkCableRight.isSelected());

		closeForm();
	}

    @FXML
    void onActionFetch(ActionEvent event) {
        LichessController lc = TiffanysFxGuiCentral.getInstance().connectToLichess();

        OngoingGames ongoingGames = lc.getOpenGames();

        ObservableList<OngoingGame> observableList = FXCollections.observableList(ongoingGames.getNowPlaying());

        this.choiceGames.setItems(observableList);

        choiceGames.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
               selectGame((OngoingGame) choiceGames.getItems().get((Integer) number2));
            }
        });


        this.choiceGames.getSelectionModel().select(0);
    }

    private void selectGame(OngoingGame ongoingGame) {
        String userName = TiffanysConfig.getStringValue(TiffanysConfig.KEY_LICHESS_USER, "");

        if (ongoingGame.getColor().equals("black")) {
	        this.txtPlayerWhiteName.setText(ongoingGame.getOpponent().getUsername());
	        this.txtPlayerBlackName.setText(userName);
        } else {
            this.txtPlayerBlackName.setText(ongoingGame.getOpponent().getUsername());
            this.txtPlayerWhiteName.setText(userName);
        }

        this.txtGameId.setText(ongoingGame.getFullId());
    }


    @FXML
    void onActionAbort(ActionEvent event) {
	    this.appGameSettings = null;
		closeForm();
    }

	private void closeForm() {
		// get a handle to the stage
		Stage stage = (Stage) btnUseChessLink.getScene().getWindow();
		stage.close();
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        onActionFetch(null);
    }


    
}
