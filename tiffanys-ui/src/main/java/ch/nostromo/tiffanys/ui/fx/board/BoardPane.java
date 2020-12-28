package ch.nostromo.tiffanys.ui.fx.board;

import ch.nostromo.tiffanys.commons.board.Board;
import ch.nostromo.tiffanys.commons.enums.GameColor;
import ch.nostromo.tiffanys.commons.fields.Field;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.NumberBinding;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;
import java.util.List;

public class BoardPane extends BorderPane {
	public static final DataFormat DRAG_DATA_FORMAT = new DataFormat("board.coordinates");

	private static final int BOARD_SIZE = 8;
	private static final int BOARD_BORDER = 20;

	private GridPane boardGrid;
	private NumberBinding tileSizeBinding;

	private boolean flipped = false;

	private List<BoardPaneEvents> listeners = new ArrayList<>();

	private BoardTile tileClickedFrom = null;

	private boolean clickMovesAllowed = false;

	public void initialize() {

		BoardPiecesSet boardPiecesSet = new BoardPiecesSet();

		// Create the grid (#board-grid)
		boardGrid = new GridPane();
		boardGrid.setMinWidth(0.0);
		boardGrid.setMinHeight(0.0);
		boardGrid.setAlignment(Pos.CENTER);
		boardGrid.setId("board-grid");

		// Create the tile size binding
		tileSizeBinding = Bindings.min(boardGrid.heightProperty(), boardGrid.widthProperty());
		tileSizeBinding = tileSizeBinding.subtract(BOARD_BORDER * 2);
		tileSizeBinding = tileSizeBinding.divide(BOARD_SIZE);
		tileSizeBinding = tileSizeBinding.subtract(1);

		// Add board tiles
		for (int row = 0; row < BOARD_SIZE; row++) {
			for (int col = 0; col < BOARD_SIZE; col++) {
				BoardTile tile;

				if ((row + col) % 2 == 0) {
					tile = new BoardTile(boardPiecesSet, tileSizeBinding, GameColor.WHITE,
							new BoardCoordinates(row, col));
				} else {
					tile = new BoardTile(boardPiecesSet, tileSizeBinding, GameColor.BLACK,
							new BoardCoordinates(row, col));
				}
				// Add relative to (+1 x/y)
				boardGrid.add(tile, col + 1, row + 1);
			}
		}

		// Vertical Border Cells (high)
		for (int i = 1; i < BOARD_SIZE + 1; i++) {
			boardGrid.add(createCoordRowLabel(), 0, i);
			boardGrid.add(createCoordRowLabel(), BOARD_SIZE + 1, i);
		}

		// Horizontal Border Cells (wide)
		for (int i = 1; i < BOARD_SIZE + 1; i++) {
			boardGrid.add(createCoordColumnLabel(), i, 0);
			boardGrid.add(createCoordColumnLabel(), i, BOARD_SIZE + 1);
		}

		// Corners Border Cells
		boardGrid.add(createCornerRowLabel(), 0, 0);
		boardGrid.add(createCornerRowLabel(), 0, BOARD_SIZE + 1);
		boardGrid.add(createCornerRowLabel(), BOARD_SIZE + 1, 0);
		boardGrid.add(createCornerRowLabel(), BOARD_SIZE + 1, BOARD_SIZE + 1);

		// Add a click listener to the grid
		boardGrid.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				for (Node node : boardGrid.getChildren()) {
					if (node instanceof BoardTile) {
						if (node.getBoundsInParent().contains(e.getX(), e.getY())) {
							BoardTile boardTile = (BoardTile) node;
							if (e.getButton() == MouseButton.PRIMARY) {
								if (e.getClickCount() > 1) {
									onTileDoubleClicked(boardTile);
								} else {
									onTileLeftClicked(boardTile);
								}
							} else {
								onTileRightClicked(boardTile);
							}
						}
					}
				}
			}

		});

		// Add the board grid to the border pane ;)
		setCenter(boardGrid);

	}

	private void onTileDoubleClicked(BoardTile tile) {
		fireDoubleClick(tile);
	}

	private void onTileRightClicked(BoardTile tile) {
		fireRightClick(tile);
	}

	private void onTileLeftClicked(BoardTile tile) {
		
		// First initiate move if allowed
		if (clickMovesAllowed) {
			if (tileClickedFrom == null && tile.isPossibleMoveSource()) {
				tileClickedFrom = tile;
				fireClickMoveStarted(tileClickedFrom);
			} else if (tileClickedFrom != null && tile.equals(tileClickedFrom)) {
				fireActionAborted(tile);
			} else if (tileClickedFrom != null && tile.isPossibleMoveTarget()) {
				fireClickMoveFinished(tileClickedFrom.getBoardCoordinates(), tile.getBoardCoordinates());
			}
		}
		
		// Second fire the click
		fireLeftClick(tile);
	}

	private Label createCornerRowLabel() {
		Label result = new Label();
		result.setMinWidth(BOARD_BORDER);
		result.setMinHeight(BOARD_BORDER);
		result.setId("board-coord-label");
		return result;
	}

	private Label createCoordRowLabel() {
		Label result = new Label();
		result.setMinWidth(BOARD_BORDER);
		result.minHeightProperty().bind(tileSizeBinding);
		result.setId("board-coord-label");
		return result;
	}

	private Label createCoordColumnLabel() {
		Label result = new Label();
		result.setMinHeight(BOARD_BORDER);
		result.minWidthProperty().bind(tileSizeBinding);
		result.setId("board-coord-label");
		return result;
	}

	private int getFieldIdxByCoord(BoardCoordinates boardCoordinates) {
		if (flipped) {
			return (28 + (boardCoordinates.getRow() * 10)) - boardCoordinates.getCol();
		} else {
			return (91 - (boardCoordinates.getRow() * 10)) + boardCoordinates.getCol();
		}
	}

	private BoardCoordinates getBoardCoordinatesByFieldIdx(int fieldIdx) {
		int col = (int) ((fieldIdx / Math.pow(10, 0)) % 10) - 1;
		if (flipped) {
			col = 8 - col - 1;
		}

		int row = 9 - (int) ((fieldIdx / Math.pow(10, 1)) % 10);
		if (flipped) {
			row = 8 - row - 1;
		}

		return new BoardCoordinates(row, col);
	}

	private Label getBorderLabelByCoord(int col, int row) {
		for (Node node : boardGrid.getChildren()) {
			if (GridPane.getColumnIndex(node) == col && GridPane.getRowIndex(node) == row) {
				return (Label) node;
			}
		}
		throw new IllegalArgumentException("No border label: col" + col + ", row:" + row);
	}

	private List<BoardTile> getAllBoardTiles() {
		List<BoardTile> result = new ArrayList<>();
		for (Node node : boardGrid.getChildren()) {
			if (node instanceof BoardTile) {
				result.add((BoardTile) node);

			}
		}
		return result;
	}

	private BoardTile getBoardTileByFieldIdx(int fieldIdx) {
		BoardCoordinates coordinatesToFind = getBoardCoordinatesByFieldIdx(fieldIdx);
		List<BoardTile> allBoardTiles = getAllBoardTiles();
		for (BoardTile boardTile : allBoardTiles) {
			if (boardTile.getBoardCoordinates().equals(coordinatesToFind)) {
				return boardTile;
			}
		}

		throw new IllegalArgumentException("No BoardTile found on idx: " + fieldIdx);
	}


	public void setPossibleMoveSourcePoints(List<Integer> possibleSources) {
		List<BoardTile> allBoardTiles = getAllBoardTiles();
		for (BoardTile boardTile : allBoardTiles) {
			boardTile.setAsPossibleMoveSource(false);
			boardTile.setOnDragDetected(null);
			boardTile.setOnDragDone(null);
		}

		for (int fieldIdx : possibleSources) {

			BoardTile boardTile = getBoardTileByFieldIdx(fieldIdx);
			boardTile.setAsPossibleMoveSource(true);

			boardTile.setOnDragDetected((MouseEvent e) -> {

				Dragboard db = boardTile.startDragAndDrop(TransferMode.ANY);
				db.setDragView(boardTile.getImageSnapshot(), tileSizeBinding.intValue() / 2,
						tileSizeBinding.intValue() / 2);
				ClipboardContent content = new ClipboardContent();
				content.put(DRAG_DATA_FORMAT, boardTile.getBoardCoordinates());
				db.setContent(content);

				e.consume();
				boardTile.disableImage();
				fireDragMoveStarted(boardTile);
			});

			boardTile.setOnDragDone((DragEvent e) -> {
				e.consume();

				fireActionAborted(boardTile);
			});

		}
	}

	public void setPossibleMoveTargetTiles(List<Integer> possibleTagets) {
		List<BoardTile> allBoardTiles = getAllBoardTiles();

		for (BoardTile boardTile : allBoardTiles) {
			boardTile.setAsPossibleMoveTarget(false);
		}

		for (int fieldIdx : possibleTagets) {
			BoardTile boardTile = getBoardTileByFieldIdx(fieldIdx);
			boardTile.setAsPossibleMoveTarget(true);

			boardTile.setOnDragDropped((DragEvent event) -> {
				event.setDropCompleted(true);
				event.consume();

				fireDragMoveFinished((BoardCoordinates) event.getDragboard().getContent(DRAG_DATA_FORMAT),
						boardTile.getBoardCoordinates());
			});
		}
	}

	public void addBoardPaneListener(BoardPaneEvents listener) {
		listeners.add(listener);
	}

	public void setBoard(Board board, boolean flipped) {
		this.flipped = flipped;

		this.tileClickedFrom = null;

		// Update the tile images
		List<BoardTile> allBoardTiles = getAllBoardTiles();
		for (BoardTile boardTile : allBoardTiles) {
			Field field = board.getFields()[getFieldIdxByCoord(boardTile.getBoardCoordinates())];
			boardTile.update(field);
			boardTile.viewReset();
		}

		// Add row labels
		for (int i = 1; i < BOARD_SIZE + 1; i++) {
			Label borderLeft = getBorderLabelByCoord(0, i);
			Label borderRight = getBorderLabelByCoord(BOARD_SIZE + 1, i);
			if (flipped) {
				borderLeft.setText(String.valueOf(i));
				borderRight.setText(String.valueOf(i));
			} else {
				borderLeft.setText(String.valueOf(BOARD_SIZE - i + 1));
				borderRight.setText(String.valueOf(BOARD_SIZE - i + 1));
			}
		}

		// Update Col labels
		if (flipped) {
			int count = 1;
			for (char c = 'H'; c >= 'A'; c--) {
				getBorderLabelByCoord(count, 0).setText(String.valueOf(c));
				getBorderLabelByCoord(count, BOARD_SIZE + 1).setText(String.valueOf(c));
				count++;
			}
		} else {
			int count = 1;
			for (char c = 'A'; c <= 'H'; c++) {
				getBorderLabelByCoord(count, 0).setText(String.valueOf(c));
				getBorderLabelByCoord(count, BOARD_SIZE + 1).setText(String.valueOf(c));
				count++;
			}
		}

	}

	public void setFieldMarked(int fieldIdx) {
		getBoardTileByFieldIdx(fieldIdx).viewSelected();
	}


	public void setFieldAsTarget(Integer targetIdx) {
		getBoardTileByFieldIdx(targetIdx).viewDropTarget();
	}

	
	public void setClickMovesAllowed(boolean clickMovesAllowed) {
		this.clickMovesAllowed = clickMovesAllowed;
	}

	// ************************** Listeners *********************************
	
	private void fireLeftClick(BoardTile tile) {
		int idx = getFieldIdxByCoord(tile.getBoardCoordinates());
		for (BoardPaneEvents listener : listeners) {
			listener.leftclick(idx);
		}
	}
	
	private void fireRightClick(BoardTile tile) {
		int idx = getFieldIdxByCoord(tile.getBoardCoordinates());
		for (BoardPaneEvents listener : listeners) {
			listener.rightClick(idx);
		}
	}

	private void fireDoubleClick(BoardTile tile) {
		int idx = getFieldIdxByCoord(tile.getBoardCoordinates());
		for (BoardPaneEvents listener : listeners) {
			listener.doubleClick(idx);
		}
	}

	
	private void fireDragMoveStarted(BoardTile tile) {
		int idx = getFieldIdxByCoord(tile.getBoardCoordinates());
		for (BoardPaneEvents listener : listeners) {
			listener.fireDragMoveStarted(idx);
		}
	}

	private void fireDragMoveFinished(BoardCoordinates from, BoardCoordinates to) {
		for (BoardPaneEvents listener : listeners) {
			int fromIdx = getFieldIdxByCoord(from);
			int toIdx = getFieldIdxByCoord(to);

			listener.fireDragMoveFinished(fromIdx, toIdx);
		}
	}

	private void fireClickMoveStarted(BoardTile tile) {
		int idx = getFieldIdxByCoord(tile.getBoardCoordinates());
		for (BoardPaneEvents listener : listeners) {
			listener.fireClickMoveStarted(idx);
		}
	}

	private void fireClickMoveFinished(BoardCoordinates from, BoardCoordinates to) {
		for (BoardPaneEvents listener : listeners) {
			int fromIdx = getFieldIdxByCoord(from);
			int toIdx = getFieldIdxByCoord(to);

			listener.fireClickMoveFinished(fromIdx, toIdx);
		}
	}

	private void fireActionAborted(BoardTile tile) {
		for (BoardPaneEvents listener : listeners) {
			listener.interactionAborted();
		}
	}

	
}
