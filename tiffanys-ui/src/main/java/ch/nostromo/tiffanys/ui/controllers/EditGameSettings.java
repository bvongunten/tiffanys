package ch.nostromo.tiffanys.ui.controllers;

import java.io.IOException;

import ch.nostromo.tiffanys.ui.TiffanysFxGuiCentral;
import ch.nostromo.tiffanys.ui.utils.game.AppGameSettings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

public class EditGameSettings  {

	private AppGameSettings appGameSettings;
		
    @FXML
    private Button btnEngineSettingsBlack;

    @FXML
    private ToggleButton btnUseChessLink;

    @FXML
    private CheckBox cbChessLinkCableRight;

    @FXML
    private Button btnEngineSettingsWhite;
    
    @FXML
    private ToggleButton btnPlayerWhiteHuman;

    @FXML
    private ToggleButton btnPlayerWhiteCpu;

    @FXML
    private ToggleButton btnPlayerBlackHuman;

    @FXML
    private ToggleButton btnPlayerBlackCpu;

    @FXML
    void onActionBtnPlayerWhiteHuman(ActionEvent event) {
    	this.btnEngineSettingsWhite.setDisable(true);
    }

    @FXML
    void onActionBtnPlayerWhiteCpu(ActionEvent event) {
    	this.btnEngineSettingsWhite.setDisable(false);
    }

    @FXML
    void onActionBtnPlayerBlackHuman(ActionEvent event) {
    	this.btnEngineSettingsBlack.setDisable(true);
    }

    @FXML
    void onActionBtnPlayerBlackCpu(ActionEvent event) {
    	this.btnEngineSettingsBlack.setDisable(false);
    }

    @FXML
    void onActionEngineSettingsBlack(ActionEvent event) throws IOException {
    	TiffanysFxGuiCentral.getInstance().showEngineSettingsForm(this.appGameSettings.getEngineSettingsBlack());
    }

    @FXML
    void onActionEngineSettingsWhite(ActionEvent event) throws IOException {
    	TiffanysFxGuiCentral.getInstance().showEngineSettingsForm(this.appGameSettings.getEngineSettingsWhite());
    }
    
	public void setAppGameSettings(AppGameSettings appGameSettings) {
		this.appGameSettings = appGameSettings;
		
		this.btnPlayerBlackCpu.setSelected(appGameSettings.getPlayerTypeBlack() == AppGameSettings.PlayerType.CPU);
		this.btnPlayerBlackHuman.setSelected(appGameSettings.getPlayerTypeBlack() == AppGameSettings.PlayerType.HUMAN);
		
		this.btnPlayerWhiteCpu.setSelected(appGameSettings.getPlayerTypeWhite() == AppGameSettings.PlayerType.CPU);
		this.btnPlayerWhiteHuman.setSelected(appGameSettings.getPlayerTypeWhite() == AppGameSettings.PlayerType.HUMAN);
		
		this.btnEngineSettingsBlack.setDisable(appGameSettings.getPlayerTypeBlack() == AppGameSettings.PlayerType.HUMAN);
		this.btnEngineSettingsWhite.setDisable(appGameSettings.getPlayerTypeWhite() == AppGameSettings.PlayerType.HUMAN);
	}

	public AppGameSettings getAppGameSettings() {
		return appGameSettings;
	}
	
	@FXML
	void onActionOk(ActionEvent event) {
		appGameSettings.setPlayerTypeBlack(this.btnPlayerBlackCpu.isSelected() ? AppGameSettings.PlayerType.CPU : AppGameSettings.PlayerType.HUMAN);
		appGameSettings.setPlayerTypeWhite(this.btnPlayerWhiteCpu.isSelected() ? AppGameSettings.PlayerType.CPU : AppGameSettings.PlayerType.HUMAN);

        appGameSettings.setUseChessLinkBoard(this.btnUseChessLink.isSelected());
        appGameSettings.setChessLinkCableRight(this.cbChessLinkCableRight.isSelected());

		closeForm();
	}


    @FXML
    void onActionAbort(ActionEvent event) {
        this.appGameSettings = null;
        closeForm();
    }

	private void closeForm() {
		// get a handle to the stage
		Stage stage = (Stage) btnEngineSettingsBlack.getScene().getWindow();
		stage.close();
    }

	
}
