package ch.nostromo.tiffanys.ui.fx.pgn;

import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;

public class PgnPane extends BorderPane {

	TextArea textArea;
	
	public PgnPane() {
		this.textArea = new TextArea();
		this.textArea.setWrapText(true);
		this.textArea.setEditable(false);
		
		this.setCenter(textArea);
	}

	public void setPgnText(String text) {
		this.textArea.setText(text);
	}
	
}
