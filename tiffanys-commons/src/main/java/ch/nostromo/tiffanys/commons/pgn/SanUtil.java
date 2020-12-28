package ch.nostromo.tiffanys.commons.pgn;

import ch.nostromo.tiffanys.commons.board.Board;
import ch.nostromo.tiffanys.commons.enums.Castling;
import ch.nostromo.tiffanys.commons.enums.GameColor;
import ch.nostromo.tiffanys.commons.enums.Piece;
import ch.nostromo.tiffanys.commons.move.Move;
import ch.nostromo.tiffanys.commons.rules.RulesUtil;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class SanUtil {

	private static String generateMoveState(Board board, Move move, GameColor colorToMove) {
		if (RulesUtil.leadsToMate(move, board, colorToMove)) {
			return "#";
		} else if (RulesUtil.leadsToCheck(move, board, colorToMove)) {
			return "+";
		} else {
			return "";
		}
	}

	public static String move2San(Move move, Board board, GameColor colorToMove) {

		if (move.getCastling() == Castling.WHITE_LONG || move.getCastling() == Castling.BLACK_LONG) {

			return "O-O-O" + generateMoveState(board, move, colorToMove);

		} else if (move.getCastling() == Castling.WHITE_SHORT || move.getCastling() == Castling.BLACK_SHORT) {

			return "O-O" + generateMoveState(board, move, colorToMove);

		} else {

			String result = "";

			List<Move> legalMoves = RulesUtil.getLegalMoves(board, colorToMove);
			Piece movedPiece = board.getFields()[move.getFrom()].getPiece();

			String row = move.getFromCoord().substring(1, 2);
			String col = move.getFromCoord().substring(0, 1);

			boolean onSameRow = false;
			boolean onSameCol = false;
			boolean same = false;

			for (Move legalMove : legalMoves) {
				if (!legalMove.isCastling() && board.getFields()[legalMove.getFrom()].getPiece() == movedPiece
						&& legalMove.getFrom() != move.getFrom() && legalMove.getTo() == move.getTo()) {
					if (legalMove.getFromCoord().endsWith(row)) {
						onSameRow = true;
					}
					if (legalMove.getFromCoord().startsWith(col)) {
						onSameCol = true;
					}
					same = true;
				}
			}

			if (movedPiece == Piece.PAWN) {

				if (onSameRow || board.getFields()[move.getTo()].getPiece() != null
						|| board.getEnPassantField() == move.getTo()) {
					result += move.getFromCoord().substring(0, 1);
				}

				if (board.getFields()[move.getTo()].getPiece() != null || board.getEnPassantField() == move.getTo()) {
					result += "x";
				}

			} else {

				boolean both = (onSameCol && onSameRow);
				boolean none = (!onSameCol && !onSameRow && !same);

				if (none) {
					result += movedPiece.getPieceCharCode();
				} else if (both) {
					result += movedPiece.getPieceCharCode() + move.getFromCoord();
				} else if (onSameCol) {
					result += movedPiece.getPieceCharCode() + move.getFromCoord().substring(1, 2);
				} else if (onSameRow || same) {
					result += movedPiece.getPieceCharCode() + move.getFromCoord().substring(0, 1);
				}

				if (board.getFields()[move.getTo()].getPiece() != null) {
					result += "x";
				}

			}

			result += move.getToCoord();

			if (move.isPromotion()) {
				result += "=" + move.getPromotion().getPieceCharCode();
			}

			result += generateMoveState(board, move, colorToMove);

			return result;
		}
	}

	public static Move san2Move(String origMove, Board board, GameColor colorToMove) {

		String workingMove = origMove;

		List<Move> legalMoves = RulesUtil.getLegalMoves(board, colorToMove);
		if (workingMove.startsWith("O-O-O")) {

			for (Move legalMove : legalMoves) {
				if (legalMove.getCastling() == Castling.WHITE_LONG || legalMove.getCastling() == Castling.BLACK_LONG) {
					return legalMove;
				}
			}

			throw new IllegalArgumentException("Castling move not found");

		} else if (workingMove.startsWith("O-O")) {

			for (Move legalMove : legalMoves) {
				if (legalMove.getCastling() == Castling.WHITE_SHORT
						|| legalMove.getCastling() == Castling.BLACK_SHORT) {
					return legalMove;
				}
			}

			throw new IllegalArgumentException("Castling move not found");

		} else {

			Move legalMoveFound = null;
			int restCount = workingMove.length();
			String disambiguatePart = "";

			// Strip Check or mate
			if ((workingMove.charAt(restCount - 1) == '+') || (workingMove.charAt(restCount - 1) == '#')) {
				workingMove = workingMove.substring(0, restCount - 1);
				restCount--;
			}

			// Strip possible Promotion
			Piece promotedPiece = null;
			boolean isPromotion = false;
			if (workingMove.charAt(restCount - 2) == '=') {
				isPromotion = true;
				promotedPiece = Piece.getPieceByCharCode(workingMove.substring(restCount - 1, restCount));
				workingMove = workingMove.substring(0, restCount - 2);
				restCount -= 2;
			}

			// Set to field
			String to = workingMove.substring(restCount - 2, restCount);
			workingMove = workingMove.substring(0, restCount - 2);
			restCount -= 2;

			// Generate Piece
			String pieceDescription = "";
			if (restCount == 0) {
				pieceDescription = "p";
			} else {
				// Capture move?
				if (workingMove.charAt(restCount - 1) == 'x') {
					workingMove = workingMove.substring(0, restCount - 1);
					restCount--;
				}

				// Moving piece and/or disambiguation
				if (Character.isUpperCase(workingMove.charAt(0))) {
					// This is a piece move
					pieceDescription = workingMove.substring(0, 1);
					if (restCount > 1) {
						disambiguatePart = workingMove.substring(1);
					}
				} else {
					// This is a pawn move
					pieceDescription = "p";
					disambiguatePart = workingMove.substring(0, 1);
				}
			}

			Piece movedPiece = Piece.getPieceByCharCode(pieceDescription);

			// Get legal move by remaining moveDescription
			int filteredMovesCount = 0;
			for (Move legalMove : legalMoves) {
				if (!legalMove.isCastling() && board.getFields()[legalMove.getFrom()].getPiece() == movedPiece
						&& legalMove.getToCoord().equals(to)) {
					// @formatter:off
					if ((disambiguatePart.length() == 0
							|| disambiguatePart.length() == 2 && legalMove.getFromCoord().equals(disambiguatePart)
							|| disambiguatePart.length() == 1 && Character.isDigit(disambiguatePart.charAt(0))
									&& legalMove.getFromCoord().endsWith(disambiguatePart)
							|| disambiguatePart.length() == 1 && Character.isLetter(disambiguatePart.charAt(0))
									&& legalMove.getFromCoord().startsWith(disambiguatePart))
							&& ((isPromotion && legalMove.getPromotion() == promotedPiece) || !isPromotion)) {
						legalMoveFound = legalMove;
						filteredMovesCount++;
					}
					// @formatter:on

				}
			}

			if (filteredMovesCount != 1) {
				throw new IllegalArgumentException(
						"No unique move found. " + filteredMovesCount + " oldmove: [" + origMove + "]");
			}

			return legalMoveFound;
		}

	}

}
