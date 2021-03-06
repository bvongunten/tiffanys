package ch.nostromo.tiffanys.ui.controllers;

import ch.nostromo.tiffanys.ui.TiffanysFxGuiCentral;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import java.io.IOException;

public class Welcome {

	@FXML
	void onActionQuit(ActionEvent event) {
		Platform.exit();
		System.exit(0);
	}

	@FXML
	void onActionOptions(ActionEvent event) throws IOException {
		TiffanysFxGuiCentral.getInstance().showOptionsWindow();
	}

	@FXML
	void onActionLichess(ActionEvent event) throws IOException {
		TiffanysFxGuiCentral.getInstance().lichessGame();
	}

	@FXML
	void onCreateLichessGame(ActionEvent event) throws IOException {
		TiffanysFxGuiCentral.getInstance().createLichessGame();
	}

	@FXML
	void onActionNewGame(ActionEvent event) throws IOException {
		TiffanysFxGuiCentral.getInstance().createNewGame();
	}

	@FXML
	void onActionLoadGame(ActionEvent event) throws IOException {
		// TODO: Implement
	}


	
}
