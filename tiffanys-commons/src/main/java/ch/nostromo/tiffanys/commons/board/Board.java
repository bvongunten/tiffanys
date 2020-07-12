package ch.nostromo.tiffanys.commons.board;

import java.util.StringTokenizer;
import java.util.logging.Logger;

import ch.nostromo.tiffanys.commons.enums.Castling;
import ch.nostromo.tiffanys.commons.enums.GameColor;
import ch.nostromo.tiffanys.commons.enums.FieldType;
import ch.nostromo.tiffanys.commons.enums.Piece;
import ch.nostromo.tiffanys.commons.fen.FenFormat;
import ch.nostromo.tiffanys.commons.fields.Field;
import ch.nostromo.tiffanys.commons.move.Move;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Board implements Cloneable {

	Logger logger = Logger.getLogger(getClass().getName());

	Field[] fields = new Field[120];

	int enPassantField;
	boolean castlingWhiteShortAllowed;
	boolean castlingBlackShortAllowed;
	boolean castlingWhiteLongAllowed;
	boolean castlingBlackLongAllowed;

	public Board(FenFormat fenFormat) {

		logger.fine("Creating board for fen: " + fenFormat.toString());

		// Legal Fields
		for (int i = 0; i < 8; i++) {
			for (int n = 1; n < 9; n++) {
				int idx = n + 20 + (i * 10);
				fields[idx] = new Field(FieldType.LEGAL);
			}
		}

		// Void fields
		for (int i = 0; i <= 19; i++) {
			fields[i] = new Field(FieldType.VOID);
			fields[i + 100] = new Field(FieldType.VOID);
		}
		for (int i = 20; i <= 90; i += 10) {
			fields[i] = new Field(FieldType.VOID);
			fields[i + 9] = new Field(FieldType.VOID);
		}

		// Set position
		String position = fenFormat.getPosition();
		StringTokenizer positionTokenizer = new StringTokenizer(position, "/");
		int currLineIdx = 9;
		while (positionTokenizer.hasMoreTokens()) {
			String line = positionTokenizer.nextToken();
			int fieldIdx = 1;
			for (int i = 0; i < line.length(); i++) {
				char c = line.charAt(i);
				if (Character.isDigit(c)) {
					fieldIdx += Integer.parseInt(line.substring(i, i + 1));
				} else {

					int toBePlacedId = currLineIdx * 10 + fieldIdx;
					if (c == 'K') {
						setPieceAndColor(toBePlacedId, Piece.KING, GameColor.WHITE);
					} else if (c == 'Q') {
						setPieceAndColor(toBePlacedId, Piece.QUEEN, GameColor.WHITE);
					} else if (c == 'R') {
						setPieceAndColor(toBePlacedId, Piece.ROOK, GameColor.WHITE);
					} else if (c == 'B') {
						setPieceAndColor(toBePlacedId, Piece.BISHOP, GameColor.WHITE);
					} else if (c == 'N') {
						setPieceAndColor(toBePlacedId, Piece.KNIGHT, GameColor.WHITE);
					} else if (c == 'P') {
						setPieceAndColor(toBePlacedId, Piece.PAWN, GameColor.WHITE);
					} else if (c == 'k') {
						setPieceAndColor(toBePlacedId, Piece.KING, GameColor.BLACK);
					} else if (c == 'q') {
						setPieceAndColor(toBePlacedId, Piece.QUEEN, GameColor.BLACK);
					} else if (c == 'r') {
						setPieceAndColor(toBePlacedId, Piece.ROOK, GameColor.BLACK);
					} else if (c == 'b') {
						setPieceAndColor(toBePlacedId, Piece.BISHOP, GameColor.BLACK);
					} else if (c == 'n') {
						setPieceAndColor(toBePlacedId, Piece.KNIGHT, GameColor.BLACK);
					} else if (c == 'p') {
						setPieceAndColor(toBePlacedId, Piece.PAWN, GameColor.BLACK);
					} else {
						throw new IllegalArgumentException(
								"Unknown piece code: " + c + " on fen position: " + position);
					}
					fieldIdx++;
				}
			}
			currLineIdx--;

		}

		// Castling
		String castling = fenFormat.getCastling();
		this.castlingWhiteLongAllowed = castling.indexOf("Q") >= 0;
		this.castlingWhiteShortAllowed = castling.indexOf("K") >= 0;
		this.castlingBlackLongAllowed = castling.indexOf("q") >= 0;
		this.castlingBlackShortAllowed = castling.indexOf("k") >= 0;

		// Ep field
		String epField = fenFormat.getEnPassant();
		if (!epField.equals("-")) {
			this.enPassantField = BoardUtil.coordToField(epField);
		} else {
			this.enPassantField = Integer.MIN_VALUE;
		}

		logger.fine("Board created: \n" + this.toString());
	}

	public FenFormat getFenFormat() {

		String position = "";
		for (int n = 9; n >= 2; n--) {
			String line = "";
			int sinceLast = 0;
			for (int i = n * 10 + 1; i <= n * 10 + 8; i++) {
				if (fields[i].getPiece() == null) {
					sinceLast++;
				} else {
					if (sinceLast > 0) {
						line += sinceLast;
						sinceLast = 0;
					}
					if (fields[i].getPieceColor() == GameColor.WHITE) {
						line += fields[i].getPiece().getPieceCharCode().toUpperCase();
					} else {
						line += fields[i].getPiece().getPieceCharCode().toLowerCase();
					}
				}
			}
			if (sinceLast > 0) {
				line += sinceLast;
			}
			position += line;
			if (n > 2) {
				position += "/";
			}
		}

		String castling = "";
		if (!castlingWhiteLongAllowed && !castlingWhiteShortAllowed && !castlingBlackLongAllowed
				&& !castlingBlackShortAllowed) {
			castling = "-";
		} else {
			if (castlingWhiteLongAllowed) {
				castling += "K";
			}
			if (castlingWhiteShortAllowed) {
				castling += "Q";
			}
			if (castlingBlackLongAllowed) {
				castling += "k";
			}
			if (castlingBlackShortAllowed) {
				castling += "q";
			}
		}

		String enPassant = "";
		if (enPassantField != Integer.MIN_VALUE) {
			enPassant = BoardUtil.fieldToCoord(enPassantField);
		} else {
			enPassant = "-";
		}

		return new FenFormat(position, castling, enPassant);
	}

	public int getPieceCount() {
		int pieceCount = 0;
		for (Field field : fields) {
			if (field.getPiece() != null) {
				pieceCount++;
			}
		}
		return pieceCount;
	}



	public boolean isCastlingAllowed(Castling castling) {
		switch (castling) {
		case WHITE_LONG:
			return castlingWhiteLongAllowed;
		case WHITE_SHORT:
			return castlingWhiteShortAllowed;
		case BLACK_LONG:
			return castlingBlackLongAllowed;
		case BLACK_SHORT:
			return castlingBlackShortAllowed;
		}

		throw new IllegalArgumentException("Unknown Castling enum val: " + castling);

	}

	public void setCastlingAllowed(Castling castling, boolean flag) {
		switch (castling) {
		case WHITE_LONG: {
			castlingWhiteLongAllowed = flag;
			return;
		}
		case WHITE_SHORT: {
			castlingWhiteShortAllowed = flag;
			return;
		}
		case BLACK_LONG: {
			castlingBlackLongAllowed = flag;
			return;
		}
		case BLACK_SHORT: {
			castlingBlackShortAllowed = flag;
			return;
		}
		}

		throw new IllegalArgumentException("Unknown Castling enum val: " + castling);

	}

	public GameColor getPieceColor(int idx) {
		if (fields[idx].getPiece() == null) {
			throw new IllegalArgumentException("No piece at idx: " + idx);
		}
		return fields[idx].getPieceColor();
	}

	public boolean containsPiece(int idx) {
		return fields[idx].getPiece() != null;
	}

	public boolean isVoid(int idx) {
		return fields[idx].getType() == FieldType.VOID;
	}

	public boolean isPieceAndColor(int idx, Piece piece, GameColor color) {
		return fields[idx].getPieceColor() == color && fields[idx].getPiece() == piece;
	}

	private void setPieceAndColor(int idx, Piece piece, GameColor color) {
		fields[idx].setPiece(piece);
		fields[idx].setPieceColor(color);
	}

	private void movePiece(int from, int to) {
		fields[to].setPiece(fields[from].getPiece());
		fields[to].setPieceColor(fields[from].getPieceColor());

		fields[from].setPiece(null);
		fields[from].setPieceColor(null);
	}

	public void applyMove(Move move, GameColor colorToMove) {

		// logger.finer("Applying move: " + move.toString());

		// 7 this.enPassantField = Integer.MIN_VALUE;

		if (move.getPromotion() != null) {
			// promotion

			setPieceAndColor(move.getTo(), move.getPromotion(), colorToMove);
			fields[move.getFrom()].setPiece(null);
			fields[move.getFrom()].setPieceColor(null);

			this.enPassantField = Integer.MIN_VALUE;

		} else if (fields[move.getFrom()].getPiece() == Piece.PAWN && move.getTo() == enPassantField) {
			// en passant

			movePiece(move.getFrom(), move.getTo());
			int epIdx = move.getTo() + (colorToMove.getCalculationModificator() * 10) * -1;
			fields[epIdx].setPiece(null);
			fields[epIdx].setPieceColor(null);

			this.enPassantField = Integer.MIN_VALUE;

		} else if (move.getCastling() != null) {
			// castling

			switch (move.getCastling()) {
			case WHITE_SHORT:
			case WHITE_LONG: {
				castlingWhiteLongAllowed = false;
				castlingWhiteShortAllowed = false;
				break;
			}
			case BLACK_SHORT:
			case BLACK_LONG: {
				castlingBlackLongAllowed = false;
				castlingBlackShortAllowed = false;
				break;
			}
			}

			movePiece(move.getCastling().getFromRook(), move.getCastling().getToRook());
			movePiece(move.getCastling().getFromKing(), move.getCastling().getToKing());

			this.enPassantField = Integer.MIN_VALUE;

		} else {

			// Castling destroyed by movement of Rook / King or movement on Rook
			// field (iE rook hit)
			if (move.getTo() == Castling.WHITE_LONG.getFromRook() || move.getFrom() == Castling.WHITE_LONG.getFromRook()
					|| move.getFrom() == Castling.WHITE_LONG.getFromKing()) {
				castlingWhiteLongAllowed = false;
			}

			if (move.getTo() == Castling.WHITE_SHORT.getFromRook()
					|| move.getFrom() == Castling.WHITE_SHORT.getFromRook()
					|| move.getFrom() == Castling.WHITE_SHORT.getFromKing()) {
				castlingWhiteShortAllowed = false;
			}

			if (move.getTo() == Castling.BLACK_LONG.getFromRook() || move.getFrom() == Castling.BLACK_LONG.getFromRook()
					|| move.getFrom() == Castling.BLACK_LONG.getFromKing()) {
				castlingBlackLongAllowed = false;
			}

			if (move.getTo() == Castling.BLACK_SHORT.getFromRook()
					|| move.getFrom() == Castling.BLACK_SHORT.getFromRook()
					|| move.getFrom() == Castling.BLACK_SHORT.getFromKing()) {
				castlingBlackShortAllowed = false;
			}

			// En Passant field to be set?
			if (fields[move.getFrom()].getPiece() == Piece.PAWN && Math.abs(move.getFrom() - move.getTo()) == 20) {
				this.enPassantField = move.getTo() + (colorToMove.getCalculationModificator() * 10) * -1;
			} else {
				this.enPassantField = Integer.MIN_VALUE;
			}

			// Move piece
			movePiece(move.getFrom(), move.getTo());

		}

	}

	@Override
	public Board clone() {
		Board clone;
		try {
			clone = (Board) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new IllegalStateException("Unable to clone board", e);
		}

		// Clone fields
		clone.fields = new Field[fields.length];

		for (int i = 0; i < fields.length; i++) {
			clone.fields[i] = fields[i].clone();
		}

		return clone;
	}

	@Override
	public String toString() {
		return BoardUtil.dumpBoard(this);
	}

}
