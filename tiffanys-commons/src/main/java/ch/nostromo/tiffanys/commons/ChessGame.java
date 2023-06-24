package ch.nostromo.tiffanys.commons;

import ch.nostromo.tiffanys.commons.board.Board;
import ch.nostromo.tiffanys.commons.board.PieceType;
import ch.nostromo.tiffanys.commons.exception.GameAlreadyDecidedException;
import ch.nostromo.tiffanys.commons.exception.IllegalMoveException;
import ch.nostromo.tiffanys.commons.format.FenFormat;
import ch.nostromo.tiffanys.commons.format.PgnFormat;
import ch.nostromo.tiffanys.commons.format.pgn.PgnMove;
import ch.nostromo.tiffanys.commons.format.pgn.PgnMoveVariation;
import ch.nostromo.tiffanys.commons.move.Move;
import ch.nostromo.tiffanys.commons.move.MoveUtils;
import ch.nostromo.tiffanys.commons.move.MoveVariation;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * ChessGame contains the games information, initial position, history and state of a game. Further it supports
 * initialization by a PGN (moves are applied) or a FEN (game is set up at given position, including move offset).
 * </p>
 * Further provides a list of legal moves on the current position, the current board and game state.
 * </p>
 * Moves can be applied and taken back.
 */
@Data
public class ChessGame {

    // Game info
    ChessGameInformation gameInfo = new ChessGameInformation();

    // First FEN during game creation
    FenFormat initialFen;

    // History of all moves applied during the game
    List<Move> moveHistory = new ArrayList<>();

    // Indicator generated either by imported PGN (if decided) or after a move has been applied that leads to decision.
    ChessGameState gameEndIndicator;


    /**
     * Create a standard chess game
     */
    public ChessGame() {
        this(FenFormat.INITIAL_FEN);
    }

    /**
     * Create a game by a given FEN position
     *
     * @param initialFen Given fen
     */
    public ChessGame(FenFormat initialFen) {
        this.initialFen = initialFen;
    }

    /**
     * Create a game by a given PGN game. Does apply all moves of the given PGN and does set game end state if given.
     *
     * @param pgn given pgn
     */
    public ChessGame(PgnFormat pgn) {
        loadFromPgn(pgn);
    }

    /**
     * Load a given PGN. Adds moves & game state (if finished)
     */
    private void loadFromPgn(PgnFormat pgn) {

        this.gameInfo = pgn.getChessGameInformation().copy();

        if (gameInfo.getOptionalTags().containsKey("FEN")) {
            this.initialFen = new FenFormat(gameInfo.getOptionalTags().get("FEN"));
        } else {
            this.initialFen = FenFormat.INITIAL_FEN;
        }

        for (PgnMove pgnMove : pgn.getMoves()) {
            Move moveInput = MoveUtils.san2Move(pgnMove.getSanMove(), getCurrentBoard(), getCurrentSide());
            moveInput.getComments().addAll(pgnMove.getComments());
            moveInput.setNag(pgnMove.getNag());

            for (PgnMoveVariation pgnMoveVariation : pgnMove.getVariations()) {
                moveInput.getVariations().add(createMoveVariations(pgnMoveVariation, getCurrentBoard(), getCurrentSide()));
            }

            applyMove(moveInput);
        }

        if (pgn.getChessGameInformation().getChessGameState() != ChessGameState.GAME_OPEN) {
            setGameEndIndicator(pgn.getChessGameInformation().getChessGameState());
        }

    }

    /**
     * Create a MoveVariation of given PgnVariation
     */
    private MoveVariation createMoveVariations(PgnMoveVariation pgnMoveVariation, Board currentBoard, Side currentSide) {
        Board workingBoard = currentBoard.copy();
        Side workingSide = currentSide;

        MoveVariation moveVariation = new MoveVariation();
        moveVariation.setComment(pgnMoveVariation.getComment());
        moveVariation.setResult(pgnMoveVariation.getResult());

        for (PgnMove pgnMove : pgnMoveVariation.getMoves()) {
            Move moveInput = MoveUtils.san2Move(pgnMove.getSanMove(), workingBoard, workingSide);
            moveInput.getComments().addAll(pgnMove.getComments());
            moveInput.setNag(pgnMove.getNag());
            moveVariation.getMoves().add(moveInput);

            for (PgnMoveVariation variation : pgnMove.getVariations()) {
                moveInput.getVariations().add(createMoveVariations(variation, workingBoard, workingSide));
            }

            workingBoard.applyMove(moveInput, workingSide);
            workingSide = workingSide.invert();
        }

        return moveVariation;
    }


    /**
     * Returns the pgn of the game.
     */
    public PgnFormat createPgn() {

        Side sideToMove = initialFen.getSideToMove().equalsIgnoreCase(Side.WHITE.getColorCode()) ? Side.WHITE : Side.BLACK;

        PgnFormat result = new PgnFormat();
        result.setChessGameInformation(gameInfo.copy());

        // Update the game state by best knowledge
        result.getChessGameInformation().setChessGameState(calculateCurrentGameState());

        int moveCounter = initialFen.getMoveNr();

        for (int i = 0; i < moveHistory.size(); i++) {
            PgnMove pgnMove = new PgnMove(moveCounter, MoveUtils.move2San(moveHistory.get(i), getBoardAfterMoveNumber(i), sideToMove), moveHistory.get(i).getNag(), sideToMove);
            pgnMove.getComments().addAll(moveHistory.get(i).getComments());
            result.getMoves().add(pgnMove);

            for (MoveVariation moveVariation : moveHistory.get(i).getVariations()) {
                pgnMove.getVariations().add(createPgnMoveVariation(moveVariation, getBoardAfterMoveNumber(i), sideToMove, moveCounter));
            }

            sideToMove = sideToMove.invert();

            if (sideToMove == Side.WHITE) {
                moveCounter++;
            }
        }

        return result;
    }


    /**
     * Create a PgnMoveVariation of given MoveVariation
     */
    private PgnMoveVariation createPgnMoveVariation(MoveVariation moveVariation, Board board, Side sideToMove, int moveCounter) {

        PgnMoveVariation result = new PgnMoveVariation(moveVariation.getComment(), moveVariation.getResult());

        Board workingBoard = board.copy();
        Side workingSide = sideToMove;
        int workMoveCounter = moveCounter;

        for (Move variantMove : moveVariation.getMoves()) {
            PgnMove pgnMove = new PgnMove(workMoveCounter, MoveUtils.move2San(variantMove, workingBoard, workingSide), variantMove.getNag(), workingSide);
            pgnMove.getComments().addAll(variantMove.getComments());
            result.getMoves().add(pgnMove);

            for (MoveVariation variantVariation : variantMove.getVariations()) {
                pgnMove.getVariations().add(createPgnMoveVariation(variantVariation, workingBoard, workingSide, workMoveCounter));
            }

            workingBoard.applyMove(variantMove, workingSide);

            workingSide = workingSide.invert();

            if (workingSide == Side.WHITE) {
                workMoveCounter++;
            }

        }

        return result;

    }


    /**
     * Returns the FEN of ghe current / latest game position
     */
    public FenFormat createFen() {

        String fenPosition = getCurrentBoard().getFenPosition();
        String fenCastling = getCurrentBoard().getFenCastling();
        String fenEnPassant = getCurrentBoard().getFenEnPassant();
        String fenSideToMove = getCurrentSide().getColorCode().toLowerCase();

        Integer fenHalfMoveClock = getFiftyMoveDrawRuleCount();

        int fenMoveCount = getCurrentMoveNumber();

        // FEN miracle :)
        if (fenMoveCount == 0) {
            fenMoveCount = 1;
        }

        return new FenFormat(fenPosition, fenSideToMove, fenCastling, fenEnPassant, fenHalfMoveClock, fenMoveCount);
    }

    /**
     * Get the current / latest board of the chess game.
     *
     * @return Board
     */
    public Board getCurrentBoard() {
        return getBoardAfterMoveNumber(moveHistory.size());
    }


    /**
     * Returns the board after a given amount of moves in the move history
     *
     * @return Board
     */
    public Board getBoardAfterMoveNumber(int moveNumber) {
        // Initial board (move 0)
        Board result = new Board(this.initialFen);

        Side side = initialFen.getSideToMove().equalsIgnoreCase(Side.WHITE.getColorCode()) ? Side.WHITE : Side.BLACK;

        for (int i = 0; i < moveNumber; i++) {
            result.applyMove(moveHistory.get(i), side);
            side = side.invert();
        }

        return result;
    }


    /**
     * Returns the current move number (iterated by a black move).
     */
    public int getCurrentMoveNumber() {

        // Given by initial fen
        int result = initialFen.getMoveNr() - 1;

        // Current moves
        result += moveHistory.size() / 2;

        // Black +1
        if (getCurrentSide() == Side.BLACK) {
            result += 1;
        }

        // Fun input initial fen could be 0
        if (result < 0) {
            result = 0;
        }

        return result;
    }

    /**
     * Returns the current side to move
     */
    public Side getCurrentSide() {

        // If game is started by BLACK start by 1
        int moves = initialFen.getSideToMove().equalsIgnoreCase(Side.BLACK.getColorCode()) ? 1 : 0;

        // Add moves
        moves += moveHistory.size();

        if (moves % 2 == 0) {
            return Side.WHITE;
        } else {
            return Side.BLACK;
        }
    }

    /**
     * Returns the current or given (by pgn) game state.
     */
    public ChessGameState calculateCurrentGameState() {
        if (getGameEndIndicator() != null) {
            return getGameEndIndicator();
        }

        if (this.getCurrentBoard().isMate(getCurrentSide())) {
            if (getCurrentSide() == Side.WHITE) {
                return ChessGameState.BLACK_WIN;
            } else {
                return ChessGameState.WHITE_WIN;
            }
        }

        if (this.getCurrentBoard().isStaleMate(getCurrentSide())) {
            return ChessGameState.DRAW_BY_STALE_MATE;
        }

        if (isDrawByThree()) {
            return ChessGameState.DRAW_BY_THREE;
        }

        if (isDrawByFifty()) {
            return ChessGameState.DRAW_BY_FIFTY;
        }

        if (isDrawByMaterial()) {
            return ChessGameState.DRAW_BY_MATERIAL;
        }

        return ChessGameState.GAME_OPEN;

    }


    /**
     * Returns true if only two pieces (kings) are on the board
     */
    private boolean isDrawByMaterial() {
        return getCurrentBoard().getPieceCount() == 2;
    }

    /**
     * Returns true if 50 moves have been played without a capture or pawn move.
     */
    private boolean isDrawByFifty() {
        return getFiftyMoveDrawRuleCount() >= 100;
    }

    public int getFiftyMoveDrawRuleCount() {

        int result = initialFen.getHalfMoveClock();

        Side side = initialFen.getSideToMove().equalsIgnoreCase(Side.WHITE.getColorCode()) ? Side.WHITE : Side.BLACK;
        Board board = new Board(initialFen);


        for (Move move : moveHistory) {
            if (!move.isCastling() && (board.getPieceType(move.getFrom()).equals(PieceType.PAWN) || !board.isEmptySquare(move.getTo()))) {
                result = 0;
            } else {
                result++;
            }

            board.applyMove(move, side);
            side = side.invert();
        }

        return result;

    }

    /**
     * Returns true if the current position occurs the third time.
     */
    private boolean isDrawByThree() {
        if (moveHistory.size() >= 6) {

            String currentFenPosition = getCurrentBoard().getFenPosition();

            int count = 0;
            for (int i = 0; i <= moveHistory.size(); i++) {
                Board board = getBoardAfterMoveNumber(i);
                if (board.getFenPosition().equals(currentFenPosition)) {
                    count++;
                }
            }

            return count >= 3;

        } else {
            return false;
        }

    }

    /**
     * Wrapper method to get all legal moves on current board with current side to move.
     */
    public List<Move> getLegalMoves() {
        return getCurrentBoard().getLegalMoves(getCurrentSide());
    }


    /**
     * Applies a move to the current game. </p>
     * Throws a ChessGameException if the game is already finished before that move by a MATE or STALE_MATE.
     * Drawn games due to threefold repetition, fifty move rules or insufficient material still allow moves to be entered.</p>
     * The move is validated to be legal, if an illegal move is entered a IllegalMoveException is thrown.
     *
     * @return The current game state after that move.
     */
    public ChessGameState applyMove(Move moveInput) {

        // Is game finished already?
        if (gameEndIndicator != null && gameEndIndicator != ChessGameState.DRAW_BY_THREE && gameEndIndicator != ChessGameState.DRAW_BY_FIFTY && gameEndIndicator != ChessGameState.DRAW_BY_MATERIAL) {
            throw new GameAlreadyDecidedException("Game is already finished. State: " + gameEndIndicator, gameEndIndicator);
        }

        // Validate move
        List<Move> validMoves = getLegalMoves();
        if (!validMoves.contains(moveInput)) {
            throw new IllegalMoveException("Illegal move: " + moveInput);
        }

        // Apply move
        moveHistory.add(moveInput);

        // Update state if game end is triggered
        ChessGameState chessGameState = calculateCurrentGameState();

        if (chessGameState != ChessGameState.GAME_OPEN) {
            gameEndIndicator = chessGameState;
        }

        return chessGameState;

    }

    /**
     * Take back the latest move. Can not take back the initial position.
     */
    public void takeBackMove() {
        if (moveHistory.isEmpty()) {
            throw new IllegalStateException("Can not take back further move.");
        }

        moveHistory.removeLast();
        gameEndIndicator = null;
    }

    public String dumpMoveTree() {

        StringBuilder sb = new StringBuilder();

        for (Move move : moveHistory) {
            appendMove("", sb, move);
        }

        return sb.toString();
    }

    private void appendMove(String indent, StringBuilder sb, Move pgnMove) {
        sb.append(indent).append(pgnMove.toString()).append("\n");

        for (MoveVariation variation : pgnMove.getVariations()) {
            sb.append(indent).append("-- new Variant --\n");
            for (Move move : variation.getMoves()) {
                appendMove(indent + "  ", sb, move);
            }
        }
    }


}
