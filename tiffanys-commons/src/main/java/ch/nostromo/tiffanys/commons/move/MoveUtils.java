package ch.nostromo.tiffanys.commons.move;

import ch.nostromo.tiffanys.commons.ChessGameException;
import ch.nostromo.tiffanys.commons.Side;
import ch.nostromo.tiffanys.commons.board.Board;
import ch.nostromo.tiffanys.commons.board.Piece;
import ch.nostromo.tiffanys.commons.board.PieceType;

import java.util.List;


public class MoveUtils {

    /**
     * Does create a String containing the principal variation as San Moves of a given move and board.
     */
    public static String generateSanPrincipalVariation(Move move, Board initialBoard, Side sideToMove) {
        StringBuilder result = new StringBuilder();

        Board board = initialBoard.clone();

        // Selected Move
        result.append(move2San(move, board, sideToMove));

        board.applyMove(move, sideToMove);

        for (Move pvMove : move.moveAttributes.getPrincipalVariations()) {
            sideToMove = sideToMove.invert();

            result.append(" ");
            result.append(move2San(pvMove, board, sideToMove));

            board.applyMove(pvMove, sideToMove);
        }

        return result.toString();
    }


    /**
     * Does create a San String of a given move and board.
     */
    public static String move2San(Move move, Board board, Side sideToMove) {

        if (move.getCastling() == Castling.WHITE_LONG || move.getCastling() == Castling.BLACK_LONG) {

            return "O-O-O" + generateSanGameState(board, move, sideToMove);

        } else if (move.getCastling() == Castling.WHITE_SHORT || move.getCastling() == Castling.BLACK_SHORT) {

            return "O-O" + generateSanGameState(board, move, sideToMove);

        } else {

            String result = "";

            List<Move> legalMoves = board.getLegalMoves(sideToMove);
            Piece movedPiece = board.getBoardSquares()[move.getFrom().getBoardIdx()].getPiece();

            String row = move.getFrom().getLowerCaseName().substring(1, 2);
            String col = move.getFrom().getLowerCaseName().substring(0, 1);

            boolean onSameRow = false;
            boolean onSameCol = false;
            boolean same = false;

            for (Move legalMove : legalMoves) {
                if (!legalMove.isCastling() && board.getBoardSquares()[legalMove.getFrom().getBoardIdx()].getPiece().getPieceType() == movedPiece.getPieceType()
                        && legalMove.getFrom() != move.getFrom() && legalMove.getTo() == move.getTo()) {
                    if (legalMove.getFrom().getLowerCaseName().endsWith(row)) {
                        onSameRow = true;
                    }
                    if (legalMove.getFrom().getLowerCaseName().startsWith(col)) {
                        onSameCol = true;
                    }
                    same = true;
                }
            }

            if (movedPiece.getPieceType() == PieceType.PAWN) {

                if (onSameRow || board.getBoardSquares()[move.getTo().getBoardIdx()].getPiece() != null
                        || (board.getEnPassantField() != null && board.getEnPassantField().getBoardIdx() == move.getTo().getBoardIdx())) {
                    result += move.getFrom().getLowerCaseName().substring(0, 1);
                }

                if (board.getBoardSquares()[move.getTo().getBoardIdx()].getPiece() != null || (board.getEnPassantField() != null && board.getEnPassantField().getBoardIdx() == move.getTo().getBoardIdx())) {
                    result += "x";
                }

            } else {

                boolean both = (onSameCol && onSameRow);
                boolean none = (!onSameCol && !onSameRow && !same);

                if (none) {
                    result += movedPiece.getCharCode().toUpperCase();
                } else if (both) {
                    result += movedPiece.getCharCode().toUpperCase() + move.getFrom().getLowerCaseName();
                } else if (onSameCol) {
                    result += movedPiece.getCharCode().toUpperCase() + move.getFrom().getLowerCaseName().charAt(1);
                } else {
                    result += movedPiece.getCharCode().toUpperCase() + move.getFrom().getLowerCaseName().charAt(0);
                }

                if (board.getBoardSquares()[move.getTo().getBoardIdx()].getPiece() != null) {
                    result += "x";
                }

            }

            result += move.getTo().getLowerCaseName();

            if (move.isPromotion()) {
                result += "=" + move.getPromotion().getCharCode().toUpperCase();
            }

            result += generateSanGameState(board, move, sideToMove);

            return result;
        }
    }

    /**
     * Does create a Move on a given San string and board
     */
    public static Move san2Move(String origMove, Board board, Side sideToMove) {

        String workingMove = origMove;

        List<Move> legalMoves = board.getLegalMoves(sideToMove);
        if (workingMove.startsWith("O-O-O")) {

            for (Move legalMove : legalMoves) {
                if (legalMove.getCastling() == Castling.WHITE_LONG || legalMove.getCastling() == Castling.BLACK_LONG) {
                    return legalMove;
                }
            }

            throw new ChessGameException("Castling move not found");

        } else if (workingMove.startsWith("O-O")) {

            for (Move legalMove : legalMoves) {
                if (legalMove.getCastling() == Castling.WHITE_SHORT
                        || legalMove.getCastling() == Castling.BLACK_SHORT) {
                    return legalMove;
                }
            }

            throw new ChessGameException("Castling move not found");

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


                String promotionPieceChar = workingMove.substring(restCount - 1, restCount);
                if (sideToMove == Side.WHITE) {
                    promotionPieceChar = promotionPieceChar.toUpperCase();
                } else {
                    promotionPieceChar = promotionPieceChar.toLowerCase();
                }

                promotedPiece = Piece.getPieceByCharCode(promotionPieceChar);
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
                if (sideToMove == Side.WHITE) {
                    pieceDescription = Piece.WHITE_PAWN.getCharCode();
                } else {
                    pieceDescription = Piece.BLACK_PAWN.getCharCode();
                }


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

                    if (sideToMove == Side.WHITE) {
                        pieceDescription = Piece.WHITE_PAWN.getCharCode();
                    } else {
                        pieceDescription = Piece.BLACK_PAWN.getCharCode();
                    }

                    disambiguatePart = workingMove.substring(0, 1);
                }
            }

            if (sideToMove == Side.WHITE) {
                pieceDescription = pieceDescription.toUpperCase();
            } else {
                pieceDescription = pieceDescription.toLowerCase();
            }


            Piece movedPiece = Piece.getPieceByCharCode(pieceDescription);

            // Filter a trailing "-" (possible left over from epd files)
            if (disambiguatePart.endsWith("-")) {
                disambiguatePart = disambiguatePart.substring(0, disambiguatePart.length() - 1);
            }


            // Get legal move by remaining moveDescription
            int filteredMovesCount = 0;
            for (Move legalMove : legalMoves) {
                if (!legalMove.isCastling() && board.getBoardSquares()[legalMove.getFrom().getBoardIdx()].getPiece() == movedPiece
                        && legalMove.getTo().name().equalsIgnoreCase(to)) {
                    // @formatter:off
                    if ((disambiguatePart.isEmpty()
                            || disambiguatePart.length() == 2 && legalMove.getFrom().name().equalsIgnoreCase(disambiguatePart)
                            || disambiguatePart.length() == 1 && Character.isDigit(disambiguatePart.charAt(0))
                                    && legalMove.getFrom().getLowerCaseName().endsWith(disambiguatePart)
                            || disambiguatePart.length() == 1 && Character.isLetter(disambiguatePart.charAt(0))
                                    && legalMove.getFrom().getLowerCaseName().startsWith(disambiguatePart))
                            && (!isPromotion || legalMove.getPromotion() == promotedPiece)) {
                        legalMoveFound = legalMove;
                        filteredMovesCount++;
                    }
                    // @formatter:on

                }
            }

            if (filteredMovesCount != 1) {
                throw new ChessGameException(
                        "No unique move found. " + filteredMovesCount + " oldmove: [" + origMove + "]");
            }

            return legalMoveFound;
        }

    }


    private static String generateSanGameState(Board board, Move move, Side sideToMove) {
        if (board.leadsToMate(move, sideToMove)) {
            return "#";
        } else if (board.leadsToCheck(move, sideToMove)) {
            return "+";
        } else {
            return "";
        }
    }

}
