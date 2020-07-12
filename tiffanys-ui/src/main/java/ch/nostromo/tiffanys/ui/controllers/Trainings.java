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

public class Trainings implements Initializable {

	
	@FXML
	ChoiceBox<TiffanysConfigTranslation> choiceTrainings;

	@FXML
	void actionOk(ActionEvent event) throws IOException {
		TiffanysConfigTranslation testCase = choiceTrainings.getValue();
		
		ChessGame game = new ChessGame(new ChessGameInfo(), new FenFormat(testCase.getValue()));
		TiffanysFxGuiCentral.getInstance().showChessWindow(game, new AppGameSettings());

		Stage stage = (Stage) choiceTrainings.getScene().getWindow();
		stage.close();
	}

	@FXML
	void actionTraining(ActionEvent event) {

	}

	@Override
	public void initialize(URL arg0, ResourceBundle resourceBundle) {
		choiceTrainings.getItems().add(new TiffanysConfigTranslation("8/8/8/4k3/8/8/8/1Q4K1 w - - 0 1", "Endspiel Matt mit Dame"));
		choiceTrainings.getSelectionModel().select(0);
	}

}
