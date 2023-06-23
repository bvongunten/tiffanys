package ch.nostromo.tiffanys.ui.fx.enginestate;

import ch.nostromo.tiffanys.commons.move.Move;
import ch.nostromo.tiffanys.dragonborn.commons.events.EngineEvent;
import ch.nostromo.tiffanys.ui.utils.frontends.Frontends;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;

import java.util.List;

public class EngineStatePane extends BorderPane {

	TextArea textArea;

	public EngineStatePane() {

		this.textArea = new TextArea();
		this.textArea.setWrapText(true);
		this.textArea.setEditable(false);
	    this.textArea.setFont(Font.font("Courier New", 12));
	    this.textArea.setWrapText(false);
	    
		this.setCenter(textArea);
	}

	public void addEngineUpdate(EngineEvent engineEvent) {
		List<Move> moves = engineEvent.getEngineResult().getLegalMoves();

		if (moves != null) {
			textArea.clear();
			for (Move move : moves) {
				textArea.appendText(move.getMoveAttributes().getScore() + " " +  Frontends.moveToString(move) + " [ " + move.getMoveAttributes().getMaxDepth() + " pv ");

				for (Move pvMove : move.getMoveAttributes().getPrincipalVariations()) {
					textArea.appendText(Frontends.moveToString(pvMove) + " ");
				}
				textArea.appendText("]" + System.lineSeparator());
			}
		}
		
		textArea.positionCaret(0);
		
	}

}
