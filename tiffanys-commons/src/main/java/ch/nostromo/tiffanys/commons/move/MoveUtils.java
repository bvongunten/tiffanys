package ch.nostromo.tiffanys.commons.move;

import ch.nostromo.tiffanys.commons.Side;
import ch.nostromo.tiffanys.commons.board.Board;
import ch.nostromo.tiffanys.commons.board.Piece;
import ch.nostromo.tiffanys.commons.board.PieceType;
import ch.nostromo.tiffanys.commons.exception.SanFormatException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MoveUtils {

    /**
     * Does create a String containing the principal variation as San Moves of a given move and board.
     */
    public static String generateSanPrincipalVariation(Move move, Board initialBoard, Side sideToMove) {
        StringBuilder result = new StringBuilder();

        Board board = initialBoard.copy();

        // Selected Move
        result.append(move2San(move, board, sideToMove));

        board.applyMove(move, sideToMove);

        for (Move pvMove : move.getMoveAttributes().getPrincipalVariations()) {
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

        if (move.getCastling() != null) {
            return createCastlingSanMove(move, board, sideToMove);
        } else {
            String result = "";

            List<Move> legalMoves = board.getLegalMoves(sideToMove);
            Piece movedPiece = board.getBoardSquares()[move.getFrom().getBoardIdx()].getPiece();

            boolean onSameRank = isSameRank(legalMoves, board, move);
            boolean onSameFile = isSameFile(legalMoves, board, move);
            boolean samePiece = isSamePiece(legalMoves, board, move);

            // Prefix (from & hit marker)
            if (movedPiece.getPieceType() == PieceType.PAWN) {
                result = createPawnSanMovePrefix(move, board, onSameRank);
            } else {
                result = createSanMovePrefix(move, board, movedPiece, onSameFile, onSameRank, samePiece);
            }

            // To part
            result += move.getTo().getLowerCaseName();

            // Promotion
            if (move.isPromotion()) {
                result += "=" + move.getPromotion().getCharCode().toUpperCase();
            }

            // Game state
            result += generateSanGameState(board, move, sideToMove);

            return result;
        }
    }

    private static boolean isSameRank(List<Move> legalMoves, Board board, Move move) {
        Piece movedPiece = board.getBoardSquares()[move.getFrom().getBoardIdx()].getPiece();
        String rank = move.getFrom().getLowerCaseName().substring(1, 2);

        for (Move legalMove : legalMoves) {
            if (!legalMove.isCastling()
                    && board.getBoardSquares()[legalMove.getFrom().getBoardIdx()].getPiece().getPieceType() == movedPiece.getPieceType()
                    && legalMove.getFrom() != move.getFrom() && legalMove.getTo() == move.getTo() &&
                    legalMove.getFrom().getLowerCaseName().endsWith(rank)) {
                return true;
            }
        }
        return false;
    }

    private static boolean isSameFile(List<Move> legalMoves, Board board, Move move) {
        Piece movedPiece = board.getBoardSquares()[move.getFrom().getBoardIdx()].getPiece();
        String file = move.getFrom().getLowerCaseName().substring(0, 1);

        for (Move legalMove : legalMoves) {
            if (!legalMove.isCastling()
                    && board.getBoardSquares()[legalMove.getFrom().getBoardIdx()].getPiece().getPieceType() == movedPiece.getPieceType()
                    && legalMove.getFrom() != move.getFrom() && legalMove.getTo() == move.getTo() && legalMove.getFrom().getLowerCaseName().startsWith(file)) {
                return true;
            }
        }
        return false;
    }

    private static boolean isSamePiece(List<Move> legalMoves, Board board, Move move) {
        Piece movedPiece = board.getBoardSquares()[move.getFrom().getBoardIdx()].getPiece();

        for (Move legalMove : legalMoves) {
            if (!legalMove.isCastling()
                    && board.getBoardSquares()[legalMove.getFrom().getBoardIdx()].getPiece().getPieceType() == movedPiece.getPieceType()
                    && legalMove.getFrom() != move.getFrom() && legalMove.getTo() == move.getTo()) {
                return true;
            }
        }
        return false;
    }


    private static String createCastlingSanMove(Move move, Board board, Side sideToMove) {
        if (move.getCastling() == Castling.WHITE_LONG || move.getCastling() == Castling.BLACK_LONG) {
            return "O-O-O" + generateSanGameState(board, move, sideToMove);
        } else {
            return "O-O" + generateSanGameState(board, move, sideToMove);
        }
    }

    private static String createPawnSanMovePrefix(Move move, Board board, boolean onSameRank) {
        String result = "";
        if (onSameRank
                || (board.getBoardSquares()[move.getTo().getBoardIdx()].getPiece() != null)
                || ((board.getEnPassantField() != null) && (board.getEnPassantField().getBoardIdx() == move.getTo().getBoardIdx()))) {
            result += move.getFrom().getLowerCaseName().substring(0, 1);
        }

        if (board.getBoardSquares()[move.getTo().getBoardIdx()].getPiece() != null || (board.getEnPassantField() != null && board.getEnPassantField().getBoardIdx() == move.getTo().getBoardIdx())) {
            result += "x";
        }

        return result;
    }

    private static String createSanMovePrefix(Move move, Board board, Piece movedPiece, boolean onSameFile, boolean onSameRank, boolean samePiece) {
        boolean identicalRankAndFile = (onSameFile && onSameRank);
        boolean singleMoveIndicator = (!onSameFile && !onSameRank && !samePiece);

        String result = "";

        if (singleMoveIndicator) {
            result += movedPiece.getCharCode().toUpperCase();
        } else if (identicalRankAndFile) {
            result += movedPiece.getCharCode().toUpperCase() + move.getFrom().getLowerCaseName();
        } else if (onSameFile) {
            result += movedPiece.getCharCode().toUpperCase() + move.getFrom().getLowerCaseName().charAt(1);
        } else {
            result += movedPiece.getCharCode().toUpperCase() + move.getFrom().getLowerCaseName().charAt(0);
        }

        if (board.getBoardSquares()[move.getTo().getBoardIdx()].getPiece() != null) {
            result += "x";
        }

        return result;
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
    /**
     * Does create a Move on a given San string and board
     */
    public static Move san2Move(String sanMove, Board board, Side sideToMove) {



        if (sanMove.startsWith("O-O") || sanMove.startsWith("O-O-0")) {
            return selectCastlingMoveFromSan(sanMove, board, sideToMove);
        } else {
            return selectMoveFromSan(sanMove, board, sideToMove);
        }

    }

    private static Move selectMoveFromSan(String sanMove, Board board, Side sideToMove) {

        String workingMove = sanMove;

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

            promotedPiece = getPromotionPiece(sideToMove, workingMove, restCount);
            workingMove = workingMove.substring(0, restCount - 2);
            restCount -= 2;
        }

        // Set to field
        String to = workingMove.substring(restCount - 2, restCount);
        workingMove = workingMove.substring(0, restCount - 2);
        restCount -= 2;

        // Generate Piece
        Piece movedPiece;
        if (restCount == 0) {
            // Pawn move
            movedPiece = getPieceBySanDescription(sideToMove, "p");
        } else {
            // Capture move?
            if (workingMove.charAt(restCount - 1) == 'x') {
                workingMove = workingMove.substring(0, restCount - 1);
                restCount--;
            }

            // Moving piece and/or disambiguation
            if (Character.isUpperCase(workingMove.charAt(0))) {
                // This is a piece move
                movedPiece = getPieceBySanDescription(sideToMove, workingMove.substring(0,1));

                if (restCount > 1) {
                    disambiguatePart = workingMove.substring(1);
                }

            } else {
                // This is a pawn move
                movedPiece = getPieceBySanDescription(sideToMove, "p");
                disambiguatePart = workingMove.substring(0, 1);
            }
        }

        // Filter a trailing "-" (possible left over from epd files)
        if (disambiguatePart.endsWith("-")) {
            disambiguatePart = disambiguatePart.substring(0, disambiguatePart.length() - 1);
        }

        return selectSanMove(board, sideToMove, movedPiece, disambiguatePart, isPromotion, promotedPiece, to);
    }


    private static Move selectSanMove(Board board, Side sideToMove, Piece movedPiece, String disambiguatePart, boolean isPromotion, Piece promotedPiece, String to) {
        List<Move> legalMoves = board.getLegalMoves(sideToMove);
        Move legalMoveFound = null;

        // Get legal move by remaining moveDescription
        int filteredMovesCount = 0;
        for (Move legalMove : legalMoves) {
            // @formatter:off
            if (!legalMove.isCastling()
                    && board.getBoardSquares()[legalMove.getFrom().getBoardIdx()].getPiece() == movedPiece
                    && legalMove.getTo().name().equalsIgnoreCase(to)
                    && (disambiguatePart.isEmpty()
                    || disambiguatePart.length() == 2 && legalMove.getFrom().name().equalsIgnoreCase(disambiguatePart)
                    || disambiguatePart.length() == 1 && Character.isDigit(disambiguatePart.charAt(0)) && legalMove.getFrom().getLowerCaseName().endsWith(disambiguatePart)
                    || disambiguatePart.length() == 1 && Character.isLetter(disambiguatePart.charAt(0)) && legalMove.getFrom().getLowerCaseName().startsWith(disambiguatePart))
                    && (!isPromotion || legalMove.getPromotion() == promotedPiece))
            {
                legalMoveFound = legalMove;
                filteredMovesCount++;
            }

            // @formatter:on
        }

        if (filteredMovesCount != 1) {
            throw new SanFormatException( "No unique move found." + filteredMovesCount);
        }

        return legalMoveFound;
    }


    private static Piece getPromotionPiece(Side sideToMove, String workingMove, int restCount) {
        String promotionPieceChar = workingMove.substring(restCount - 1, restCount);
        if (sideToMove == Side.WHITE) {
            promotionPieceChar = promotionPieceChar.toUpperCase();
        } else {
            promotionPieceChar = promotionPieceChar.toLowerCase();
        }

        return Piece.getPieceByCharCode(promotionPieceChar);
    }


    private static Piece getPieceBySanDescription(Side sideToMove, String pieceDescription) {
        if (sideToMove == Side.WHITE) {
            return Piece.getPieceByCharCode(pieceDescription.toUpperCase());
        } else {
            return Piece.getPieceByCharCode(pieceDescription.toLowerCase());
        }
    }

    private static Move selectCastlingMoveFromSan(String sanMove, Board board, Side sideToMove) {

        List<Move> legalMoves = board.getLegalMoves(sideToMove);

        if (sanMove.startsWith("O-O-O")) {
            for (Move legalMove : legalMoves) {
                if (legalMove.getCastling() == Castling.WHITE_LONG || legalMove.getCastling() == Castling.BLACK_LONG) {
                    return legalMove;
                }
            }
        } else {
            for (Move legalMove : legalMoves) {
                if (legalMove.getCastling() == Castling.WHITE_SHORT
                        || legalMove.getCastling() == Castling.BLACK_SHORT) {
                    return legalMove;
                }
            }

        }

        throw new SanFormatException("Castling move not found");
    }





}
