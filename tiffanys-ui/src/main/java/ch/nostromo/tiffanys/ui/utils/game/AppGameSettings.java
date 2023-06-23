package ch.nostromo.tiffanys.ui.utils.game;

import ch.nostromo.tiffanys.commons.fen.FenFormat;
import ch.nostromo.tiffanys.dragonborn.commons.EngineSettings;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AppGameSettings {

	public enum PlayerType {
		HUMAN, LICHESS_OPPONENT, CPU;
	}

	private boolean isLichessGame = false;
	private String lichessGameId;
	private String playerWhiteName;
	private String playerBlackName;


	private String fen = FenFormat.INITIAL_BOARD;
	
	private PlayerType playerTypeWhite = PlayerType.HUMAN;
	private PlayerType playerTypeBlack = PlayerType.CPU;

	private EngineSettings engineSettingsWhite;
	private EngineSettings engineSettingsBlack;


	private boolean useChessLinkBoard;
	private boolean chessLinkCableRight;

	public AppGameSettings() {
		engineSettingsWhite = new EngineSettings();
		engineSettingsBlack = new EngineSettings();

		engineSettingsBlack.setMode(EngineSettings.EngineMode.TIME_FOR_MOVE);
		engineSettingsWhite.setMode(EngineSettings.EngineMode.TIME_FOR_MOVE);
	}


	public boolean isOneHumanPlayerAndBlack() {
		return playerTypeWhite.equals(PlayerType.CPU) && playerTypeBlack.equals(PlayerType.HUMAN);
	}
	
	
}
