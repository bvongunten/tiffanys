package ch.nostromo.tiffanys.ui.fx.board;

import ch.nostromo.tiffanys.commons.enums.GameColor;
import ch.nostromo.tiffanys.commons.enums.Piece;
import ch.nostromo.tiffanys.commons.fields.Field;
import javafx.beans.binding.NumberBinding;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class BoardTile extends StackPane {

	private GameColor fieldColor;
	private Rectangle rectangle;
	private ImageView imageView;
	private BoardPiecesSet boardPiecesSet;

	private boolean possibleMoveTarget;
	private boolean possibleMoveSource;

	private BoardCoordinates boardCoordinates;

	public BoardTile(BoardPiecesSet boardPiecesSet, NumberBinding sizeBinding, GameColor fieldColor,
			BoardCoordinates boardCoordinates) {
		this.setId("board-tile");

		this.boardCoordinates = boardCoordinates;

		this.boardPiecesSet = boardPiecesSet;
		this.fieldColor = fieldColor;

		rectangle = new ResizableRectangle(9, 9);
		rectangle = new Rectangle();

		rectangle.widthProperty().bind(sizeBinding);
		rectangle.heightProperty().bind(sizeBinding);

		getChildren().add(rectangle);

		imageView = new ImageView();

		imageView.setPreserveRatio(true);
		imageView.fitHeightProperty().bind(rectangle.heightProperty().subtract(15));

		getChildren().add(imageView);

		viewReset();
	}

	public BoardCoordinates getBoardCoordinates() {
		return boardCoordinates;
	}

	public Image getImage() {
		return imageView.getImage();
	}

	public ImageView getImageView() {
		return imageView;
	}

	public String toString() {
		return "Coord= " + boardCoordinates + ", Id=" + getId();
	}

	public void viewReset() {
		if (fieldColor == GameColor.BLACK) {
			rectangle.setId("board-tile-black");
		} else {
			rectangle.setId("board-tile-white");
		}
	}

	public void viewSelected() {
		if (this.fieldColor == GameColor.BLACK) {
			rectangle.setId("board-tile-black-selected");
		} else {
			rectangle.setId("board-tile-white-selected");
		}
	}

	public void viewDropTarget() {
		if (this.fieldColor == GameColor.BLACK) {
			rectangle.setId("board-tile-black-dropTarget");
		} else {
			rectangle.setId("board-tile-white-dropTarget");
		}
	}

	public void update(Field field) {
		if (field.getPiece() == null) {
			imageView.setImage(null);
		} else {
			if (field.getPieceColor() == GameColor.BLACK) {
				if (field.getPiece() == Piece.PAWN) {
					imageView.setImage(boardPiecesSet.getBlackPawn());
				} else if (field.getPiece() == Piece.KNIGHT) {
					imageView.setImage(boardPiecesSet.getBlackKnight());
				} else if (field.getPiece() == Piece.BISHOP) {
					imageView.setImage(boardPiecesSet.getBlackBishop());
				} else if (field.getPiece() == Piece.ROOK) {
					imageView.setImage(boardPiecesSet.getBlackRook());
				} else if (field.getPiece() == Piece.QUEEN) {
					imageView.setImage(boardPiecesSet.getBlackQueen());
				} else if (field.getPiece() == Piece.KING) {
					imageView.setImage(boardPiecesSet.getBlackKing());
				}
			} else {
				if (field.getPiece() == Piece.PAWN) {
					imageView.setImage(boardPiecesSet.getWhitePawn());
				} else if (field.getPiece() == Piece.KNIGHT) {
					imageView.setImage(boardPiecesSet.getWhiteKnight());
				} else if (field.getPiece() == Piece.BISHOP) {
					imageView.setImage(boardPiecesSet.getWhiteBishop());
				} else if (field.getPiece() == Piece.ROOK) {
					imageView.setImage(boardPiecesSet.getWhiteRook());
				} else if (field.getPiece() == Piece.QUEEN) {
					imageView.setImage(boardPiecesSet.getWhiteQueen());
				} else if (field.getPiece() == Piece.KING) {
					imageView.setImage(boardPiecesSet.getWhiteKing());
				}
			}

			imageView.setCursor(Cursor.HAND);

		}

	}

	static class ResizableRectangle extends Rectangle {
		ResizableRectangle(double w, double h) {
			super(w, h);
		}

		@Override
		public boolean isResizable() {
			return true;
		}

		@Override
		public double minWidth(double height) {
			return 0.0;
		}
	}

	public Image getImageSnapshot() {
		SnapshotParameters snapParams = new SnapshotParameters();
		snapParams.setFill(Color.TRANSPARENT);
		return getImageView().snapshot(snapParams, null);
	}

	public void setAsPossibleMoveSource(boolean flag) {
		this.possibleMoveSource = flag;
		if (flag) {
			imageView.setCursor(Cursor.HAND);
		} else {
			imageView.setCursor(Cursor.DEFAULT);
		}
	}

	public void setAsPossibleMoveTarget(boolean flag) {
		this.possibleMoveTarget = flag;

		setOnDragOver(new EventHandler<DragEvent>() {
			@Override
			public void handle(DragEvent event) {

				if (possibleMoveTarget && event.getGestureSource() != this
						&& event.getDragboard().hasContent(BoardPane.DRAG_DATA_FORMAT)) {
					event.acceptTransferModes(TransferMode.ANY);
				} else {
					event.acceptTransferModes(TransferMode.NONE);

				}
				event.consume();

			}
		});

	}

	public void disableImage() {
		imageView.setImage(null);
	}

	public boolean isPossibleMoveTarget() {
		return possibleMoveTarget;
	}

	public boolean isPossibleMoveSource() {
		return possibleMoveSource;
	}

}
