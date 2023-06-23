package ch.nostromo.tiffanys.ui.controllers;


import ch.nostromo.tiffanys.dragonborn.commons.EngineSettings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

public class EditEngineSettings {

	private EngineSettings engineSettings;

	@FXML
	private ToggleGroup mode;

	@FXML
	private TextField txtFixedMovesForGame;

	@FXML
	private RadioButton radioFixedDepth;

	@FXML
	private RadioButton radioFixedTime;

	@FXML
	private RadioButton radioTimeForGame;

	@FXML
	private TextField txtThreads;

	@FXML
	private ToggleGroup openingbook;

	@FXML
	private TextField txtFixedTime;

	@FXML
	private RadioButton radioOpeningBookYes;

	@FXML
	private RadioButton radioOpeningBookNo;

	@FXML
	private TextField txtFixedTimeForGame;

	@FXML
	private TextField txtFixedDepth;

	
	public void setEngineSettings(EngineSettings engineSettings) {
		this.engineSettings = engineSettings;

		switch (engineSettings.getMode()) {
		case DEPTH: {
			this.radioFixedDepth.setSelected(true);
			break;
		}
		case TIME_FOR_MOVE: {
			this.radioFixedTime.setSelected(true);
			break;
		}
		case TIME_MOVES_FOR_GAME:
			this.radioTimeForGame.setSelected(true);
			break;
		default:
			break;
		}

		this.txtFixedDepth.setText(String.valueOf(engineSettings.getDepth()));
		this.txtFixedTime.setText(String.valueOf(engineSettings.getTime()));

		this.txtFixedMovesForGame.setText(String.valueOf(engineSettings.getMovesForGame()));
		this.txtFixedTimeForGame.setText(String.valueOf(engineSettings.getTimeForGame()));

		this.txtThreads.setText(String.valueOf(engineSettings.getThreads()));

		this.radioOpeningBookYes.setSelected(engineSettings.isUseOpeningBook());
		this.radioOpeningBookNo.setSelected(!engineSettings.isUseOpeningBook());

	}

	@FXML
	void onActionOk(ActionEvent event) {
		if (this.radioFixedDepth.isSelected()) {
			engineSettings.setMode(EngineSettings.EngineMode.DEPTH);
		} else if (this.radioFixedTime.isSelected()) {
			engineSettings.setMode(EngineSettings.EngineMode.TIME_FOR_MOVE);
		} else if (this.radioTimeForGame.isSelected()) {
			engineSettings.setMode(EngineSettings.EngineMode.TIME_MOVES_FOR_GAME);
		}
		
		engineSettings.setDepth(Integer.valueOf(txtFixedDepth.getText()));
		engineSettings.setTime(Integer.valueOf(txtFixedTime.getText()));
		engineSettings.setTimeForGame(Integer.valueOf(txtFixedTimeForGame.getText()));
		engineSettings.setMovesForGame(Integer.valueOf(txtFixedMovesForGame.getText()));
		
		engineSettings.setUseOpeningBook(this.radioOpeningBookYes.isSelected());
		
		closeForm();
	}

	@FXML
	void onActionAbort(ActionEvent event) {
		closeForm();
	}

	private void closeForm() {
		// get a handle to the stage
		Stage stage = (Stage) txtThreads.getScene().getWindow();
		stage.close();
	}



}
