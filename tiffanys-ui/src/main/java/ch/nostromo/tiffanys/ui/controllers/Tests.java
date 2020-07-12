package ch.nostromo.tiffanys.ui.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import ch.nostromo.tiffanys.ui.TiffanysFxGuiCentral;
import ch.nostromo.tiffanys.ui.preferences.TiffanysConfigTranslation;
import ch.nostromo.tiffanys.commons.ChessGame;
import ch.nostromo.tiffanys.commons.ChessGameInfo;
import ch.nostromo.tiffanys.commons.fen.FenFormat;
import ch.nostromo.tiffanys.ui.utils.game.AppGameSettings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.stage.Stage;

public class Tests implements Initializable {

	
	@FXML
	ChoiceBox<TiffanysConfigTranslation> choiceTestCase;

	@FXML
	void actionOk(ActionEvent event) throws IOException {
		TiffanysConfigTranslation testCase = choiceTestCase.getValue();
		
		ChessGame game = new ChessGame(new ChessGameInfo(), new FenFormat(testCase.getValue()));
		TiffanysFxGuiCentral.getInstance().showChessWindow(game, new AppGameSettings());

		Stage stage = (Stage) choiceTestCase.getScene().getWindow();
		stage.close();
	}

	@FXML
	void actionTraining(ActionEvent event) {

	}

	@Override
	public void initialize(URL arg0, ResourceBundle resourceBundle) {
		choiceTestCase.getItems().add(new TiffanysConfigTranslation("5k2/R7/1R6/8/8/8/3K4/8 w - - 0 1", "GUI Player Mate test"));
		choiceTestCase.getItems().add(new TiffanysConfigTranslation("5k2/R7/1R6/6q1/8/8/5PPP/7K w - - 0 1", "GUI Engine Mate test"));
		choiceTestCase.getItems().add(new TiffanysConfigTranslation("8/8/6k1/8/3K4/8/6p1/8 w - - 0 1", "Promotion by Engine"));
		choiceTestCase.getSelectionModel().select(0);
	}

}
