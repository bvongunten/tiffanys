package ch.nostromo.tiffanys.ui.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import ch.nostromo.tiffanys.ui.fx.board.BoardPane;
import ch.nostromo.tiffanys.ui.fx.board.BoardPaneEvents;
import ch.nostromo.tiffanys.ui.fx.board.BoardPiecesSet;
import ch.nostromo.tiffanys.commons.ChessGame;
import ch.nostromo.tiffanys.commons.ChessGameInfo;
import ch.nostromo.tiffanys.commons.enums.Castling;
import ch.nostromo.tiffanys.commons.enums.GameColor;
import ch.nostromo.tiffanys.commons.enums.Piece;
import ch.nostromo.tiffanys.commons.fen.FenFormat;
import ch.nostromo.tiffanys.commons.fields.Field;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class EditBoard implements Initializable, BoardPaneEvents {

	public enum EditBoardMode {
		PIECE, DELETE, EP
	};

	public enum EditBoardFinishedState {
		OK, CANCEL;
	};

	@FXML
	private BorderPane gamePane;

	@FXML
	private BorderPane infoPane;

	@FXML
	private TextField textEp;

	@FXML
	private TextField textMoveNr;

	@FXML
	private ToggleButton buttonWhite;

	@FXML
	private ToggleButton buttonBlack;

	@FXML
	private ToggleButton buttonBlackBishop;

	@FXML
	private CheckBox cbBlackCastlingLong;

	@FXML
	private ToggleGroup colorToMove;

	@FXML
	private ToggleButton buttonWhiteBishop;

	@FXML
	private ToggleButton buttonBlackRook;

	@FXML
	private CheckBox cbWhiteCastlingShort;

	@FXML
	private ToggleButton buttonWhiteQueen;

	@FXML
	private ToggleButton buttonBlackKing;

	@FXML
	private ToggleButton buttonBlackQueen;

	@FXML
	private ToggleButton buttonWhiteKing;

	@FXML
	private ToggleButton buttonBlackKnight;

	@FXML
	private ToggleButton buttonWhiteRook;

	@FXML
	private CheckBox cbBlackCastlingShort;

	@FXML
	private ToggleGroup pieceGroup;

	@FXML
	private CheckBox cbWhiteCastlingLong;

	@FXML
	private ToggleButton buttonWhitePawn;

	@FXML
	private ToggleButton buttonWhiteKnight;

	@FXML
	private ToggleButton buttonBlackPawn;

	private ChessGame game;

	private BoardPane boardPane;

	private Piece currentPiece = Piece.KING;
	private GameColor currentColor = GameColor.WHITE;
	private EditBoardMode editBoardMode = EditBoardMode.PIECE;
	private EditBoardFinishedState editBoardFinishedState = EditBoardFinishedState.OK;

	@Override
	public void initialize(URL arg0, ResourceBundle bundle) {

		boardPane = new BoardPane();
		boardPane.initialize();
		boardPane.addBoardPaneListener(this);

		boardPane.setClickMovesAllowed(true);

		gamePane.setCenter(boardPane);

		BoardPiecesSet boardPiecesSet = new BoardPiecesSet();

		setButtonImage(buttonWhiteKing, boardPiecesSet.getWhiteKing());
		setButtonImage(buttonWhiteQueen, boardPiecesSet.getWhiteQueen());
		setButtonImage(buttonWhiteRook, boardPiecesSet.getWhiteRook());
		setButtonImage(buttonWhiteBishop, boardPiecesSet.getWhiteBishop());
		setButtonImage(buttonWhiteKnight, boardPiecesSet.getWhiteKnight());
		setButtonImage(buttonWhitePawn, boardPiecesSet.getWhitePawn());

		setButtonImage(buttonBlackKing, boardPiecesSet.getBlackKing());
		setButtonImage(buttonBlackQueen, boardPiecesSet.getBlackQueen());
		setButtonImage(buttonBlackRook, boardPiecesSet.getBlackRook());
		setButtonImage(buttonBlackBishop, boardPiecesSet.getBlackBishop());
		setButtonImage(buttonBlackKnight, boardPiecesSet.getBlackKnight());
		setButtonImage(buttonBlackPawn, boardPiecesSet.getBlackPawn());
	}

	private void setButtonImage(ToggleButton button, Image image) {
		ImageView imageView = new ImageView(image);
		imageView.setPreserveRatio(true);
		imageView.setFitHeight(40.0);
		imageView.setFitWidth(40.0);
		button.setGraphic(imageView);
		button.setText("");
	}

	public void setFen(FenFormat fen) {
		this.game = new ChessGame(new ChessGameInfo(), fen);
		
		this.updatePosition();
	}

	private void updatePosition() {

		if (game.getCurrentBoard().getEnPassantField() > 0) {
			boardPane.setFieldMarked(game.getCurrentBoard().getEnPassantField());
		}

		cbWhiteCastlingLong
				.setSelected(game.getCurrentBoard().isCastlingAllowed(Castling.WHITE_LONG));
		cbWhiteCastlingShort
				.setSelected(game.getCurrentBoard().isCastlingAllowed(Castling.WHITE_SHORT));
		cbBlackCastlingLong
				.setSelected(game.getCurrentBoard().isCastlingAllowed(Castling.BLACK_LONG));
		cbBlackCastlingShort
				.setSelected(game.getCurrentBoard().isCastlingAllowed(Castling.BLACK_SHORT));

		if (game.getCurrentColorToMove() == GameColor.WHITE) {
			buttonWhite.setSelected(true);
			buttonBlack.setSelected(false);
			boardPane.setBoard(game.getCurrentBoard(), false);
		} else {
			buttonWhite.setSelected(false);
			buttonBlack.setSelected(true);
			boardPane.setBoard(game.getCurrentBoard(), true);
		}

		textMoveNr.setText(String.valueOf(game.getCurrentMoveCount()));
		
	}

	private void saveData() {
		this.game.setOffsetHalfMoveClock(Integer.valueOf(textMoveNr.getText()));
	}
	
	@Override
	public void interactionAborted() {
		updatePosition();
	}

	@Override
	public void fireClickMoveStarted(int fieldIdx) {
	}

	@Override
	public void fireClickMoveFinished(int fromFieldIdx, int toFieldIdx) {
	}

	@Override
	public void fireDragMoveStarted(int fieldIdx) {
	}

	@Override
	public void fireDragMoveFinished(int fromFieldIdx, int toFieldIdx) {

	}

	@Override
	public void rightClick(int fieldIdx) {
		// DELETE
		game.getCurrentBoard().getFields()[fieldIdx].setPiece(null);
		game.getCurrentBoard().getFields()[fieldIdx].setPieceColor(null);

		if (game.getCurrentBoard().getEnPassantField() == fieldIdx) {
			this.game.getCurrentBoard().setEnPassantField(Integer.MIN_VALUE);
		}

		this.updatePosition();

	}

	@Override
	public void leftclick(int fieldIdx) {

		if (this.editBoardMode == EditBoardMode.EP) {
			game.getCurrentBoard().setEnPassantField(fieldIdx);
		} else if (this.editBoardMode == EditBoardMode.PIECE) {
			game.getCurrentBoard().getFields()[fieldIdx].setPiece(currentPiece);
			game.getCurrentBoard().getFields()[fieldIdx].setPieceColor(currentColor);
		} else {
			// DELETE
			game.getCurrentBoard().getFields()[fieldIdx].setPiece(null);
			game.getCurrentBoard().getFields()[fieldIdx].setPieceColor(null);

			if (game.getCurrentBoard().getEnPassantField() == fieldIdx) {
				this.game.getCurrentBoard().setEnPassantField(Integer.MIN_VALUE);
			}
		}

		this.updatePosition();
	}

	@Override
	public void doubleClick(int fieldIdx) {
		// Nothing
	}

	public void setupPieceSelected(Piece piece, GameColor color) {
		this.editBoardMode = EditBoardMode.PIECE;
		this.currentPiece = piece;
		this.currentColor = color;
	}

	@FXML
	void onWhiteRook(ActionEvent event) {
		setupPieceSelected(Piece.ROOK, GameColor.WHITE);
	}

	@FXML
	void onWhiteBishop(ActionEvent event) {
		setupPieceSelected(Piece.BISHOP, GameColor.WHITE);
	}

	@FXML
	void onWhiteKing(ActionEvent event) {
		setupPieceSelected(Piece.KING, GameColor.WHITE);
	}

	@FXML
	void onWhiteQueen(ActionEvent event) {
		setupPieceSelected(Piece.QUEEN, GameColor.WHITE);
	}

	@FXML
	void onWhiteKnight(ActionEvent event) {
		setupPieceSelected(Piece.KNIGHT, GameColor.WHITE);
	}

	@FXML
	void onBlackKnight(ActionEvent event) {
		setupPieceSelected(Piece.KNIGHT, GameColor.BLACK);
	}

	@FXML
	void onBlackQueen(ActionEvent event) {
		setupPieceSelected(Piece.QUEEN, GameColor.BLACK);
	}

	@FXML
	void onBlackKing(ActionEvent event) {
		setupPieceSelected(Piece.KING, GameColor.BLACK);
	}

	@FXML
	void onBlackBishop(ActionEvent event) {
		setupPieceSelected(Piece.BISHOP, GameColor.BLACK);
	}

	@FXML
	void onBlackRook(ActionEvent event) {
		setupPieceSelected(Piece.ROOK, GameColor.BLACK);
	}

	@FXML
	void onWhitePawn(ActionEvent event) {
		setupPieceSelected(Piece.PAWN, GameColor.WHITE);
	}

	@FXML
	void onBlackPawn(ActionEvent event) {
		setupPieceSelected(Piece.PAWN, GameColor.BLACK);
	}

	@FXML
	void onClearTile(ActionEvent event) {
		editBoardMode = EditBoardMode.DELETE;
	}

	@FXML
	void onFenFromClipboard(ActionEvent event) {
		final Clipboard clipboard = Clipboard.getSystemClipboard();

		if (clipboard.hasString()) {
			ChessGameInfo existingInfo = this.game.getGameInfo();
			FenFormat fen = new FenFormat(clipboard.getString());
			game = new ChessGame(existingInfo, fen);
		}
		
		updatePosition();
	}

	@FXML
	void onFenToClipboard(ActionEvent event) {
		saveData();
		
		final Clipboard clipboard = Clipboard.getSystemClipboard();
		final ClipboardContent content = new ClipboardContent();
		content.putString(game.getCurrentFenFormat().generateFen());
		clipboard.setContent(content);
	}

	@FXML
	void onOk(ActionEvent event) {
		saveData();
		editBoardFinishedState = EditBoardFinishedState.OK;
		Stage stage = (Stage) buttonBlack.getScene().getWindow();
		stage.close();
	}



	@FXML
	void onCancel(ActionEvent event) {
		saveData();
		editBoardFinishedState = EditBoardFinishedState.CANCEL;
		Stage stage = (Stage) buttonBlack.getScene().getWindow();
		stage.close();
	}

	@FXML
	void onClearBoard(ActionEvent event) {
		Field[] allFields = game.getCurrentBoard().getFields();
		for (Field field : allFields) {
			field.setPiece(null);
			field.setPieceColor(null);
		}
		updatePosition();
	}

	@FXML
	void onInitialBoard(ActionEvent event) {
		ChessGameInfo existingInfo = this.game.getGameInfo();
		game = new ChessGame(existingInfo);
		updatePosition();
	}

	@FXML
	void onCastlingWhiteLong(ActionEvent event) {
		this.game.getCurrentBoard().setCastlingAllowed(Castling.WHITE_LONG,
				cbWhiteCastlingLong.isSelected());
		updatePosition();
	}

	@FXML
	void onCastlingWhiteShort(ActionEvent event) {
		this.game.getCurrentBoard().setCastlingAllowed(Castling.WHITE_SHORT,
				cbWhiteCastlingShort.isSelected());
		updatePosition();
	}

	@FXML
	void onCastlingBlackLong(ActionEvent event) {
		this.game.getCurrentBoard().setCastlingAllowed(Castling.BLACK_LONG,
				cbBlackCastlingLong.isSelected());
		updatePosition();
	}

	@FXML
	void onCastlingBlackShort(ActionEvent event) {
		this.game.getCurrentBoard().setCastlingAllowed(Castling.BLACK_SHORT,
				cbBlackCastlingShort.isSelected());
		updatePosition();
	}

	@FXML
	void onEpField(ActionEvent event) {
		editBoardMode = EditBoardMode.EP;
	}

	@FXML
	void onColorWhite(ActionEvent event) {
		this.game.overWriteColorToMove(GameColor.WHITE);
		updatePosition();
	}

	@FXML
	void onColorBlack(ActionEvent event) {
		this.game.overWriteColorToMove(GameColor.BLACK);
		updatePosition();
	}
	
	public FenFormat getFen() {
		return this.game.getCurrentFenFormat();
	}

	
	public EditBoardFinishedState getEditBoardFinishedState() {
		return editBoardFinishedState;
	}

	
}
