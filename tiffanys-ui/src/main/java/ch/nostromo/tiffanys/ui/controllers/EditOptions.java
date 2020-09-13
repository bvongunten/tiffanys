package ch.nostromo.tiffanys.ui.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import ch.nostromo.tiffanys.ui.TiffanysFxGuiCentral;
import ch.nostromo.tiffanys.ui.preferences.TiffanysConfig;
import ch.nostromo.tiffanys.ui.preferences.TiffanysConfigTranslation;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class EditOptions implements Initializable {

	@FXML
	ChoiceBox<TiffanysConfigTranslation> choiceLanguage;

	@FXML
	TextField txtLichessUser;

	@FXML
	TextField txtLichessApiKey;


	@FXML
	ChoiceBox<String> choiceLogLevel;



	@Override
	public void initialize(URL arg0, ResourceBundle resouceBundle) {
		choiceLanguage.getItems().addAll(TiffanysConfig.getTranslationList(TiffanysConfig.KEY_LANGUAGE));
		choiceLanguage.getSelectionModel()
				.select(TiffanysConfig.getTranslatedStringValue(TiffanysConfig.KEY_LANGUAGE, "en"));

		txtLichessUser.setText(TiffanysConfig.getStringValue(TiffanysConfig.KEY_LICHESS_USER, ""));
		txtLichessApiKey.setText(TiffanysConfig.getStringValue(TiffanysConfig.KEY_LICHESS_APIKEY, ""));

		List<String> logLevels = new ArrayList<>();
		logLevels.add("OFF");
		logLevels.add("ALL");
		logLevels.add("INFO");
		logLevels.add("WARNING");
		logLevels.add("SEVERE");

		choiceLogLevel.getItems().addAll(logLevels);
		choiceLogLevel.getSelectionModel().select(TiffanysConfig.getStringValue(TiffanysConfig.KEY_LOG_FILE_LEVEL, "OFF"));

	}
	@FXML
	void actionOk(ActionEvent event) throws IOException {
		TiffanysConfigTranslation language = choiceLanguage.getSelectionModel().getSelectedItem();
		TiffanysConfig.setTranslatedStringValue(TiffanysConfig.KEY_LANGUAGE, language);

		TiffanysConfig.setStringValue(TiffanysConfig.KEY_LICHESS_USER, txtLichessUser.getText());
		TiffanysConfig.setStringValue(TiffanysConfig.KEY_LICHESS_APIKEY, txtLichessApiKey.getText());

		TiffanysConfig.setStringValue(TiffanysConfig.KEY_LOG_FILE_LEVEL,choiceLogLevel.getSelectionModel().getSelectedItem());

		// get a handle to the stage
		Stage stage = (Stage) choiceLanguage.getScene().getWindow();
		stage.close();

		TiffanysFxGuiCentral.getInstance().fireUpLogging();
		TiffanysFxGuiCentral.getInstance().showMainMenuForm();
	}
}
