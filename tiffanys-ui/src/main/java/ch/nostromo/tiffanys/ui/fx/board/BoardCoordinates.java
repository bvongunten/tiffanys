package ch.nostromo.tiffanys.ui.fx.board;

import lombok.Data;

import java.io.Serializable;

@Data
public class BoardCoordinates implements Serializable {

	private static final long serialVersionUID = 1L;

	private int row;
	private int col;

	public BoardCoordinates(int row, int col) {
		this.row = row;
		this.col = col;
	}


}
