package ch.nostromo.tiffanys.ui.controllers;

import java.io.IOException;

import ch.nostromo.tiffanys.ui.TiffanysFxGuiCentral;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

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
	void onActionNewGame(ActionEvent event) throws IOException {
		TiffanysFxGuiCentral.getInstance().createNewGame();
	}

	@FXML
	void onActionLoadGame(ActionEvent event) throws IOException {
		// TODO: Implement
	}


	
}
