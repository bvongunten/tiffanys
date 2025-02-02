package ch.nostromo.tiffanys.commons;

import ch.nostromo.tiffanys.commons.board.Board;
import ch.nostromo.tiffanys.commons.board.PieceType;
import ch.nostromo.tiffanys.commons.formats.FenFormat;
import ch.nostromo.tiffanys.commons.formats.PgnFormat;
import ch.nostromo.tiffanys.commons.move.Move;
import ch.nostromo.tiffanys.commons.move.MoveUtils;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

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
    ChessGameInformation gameInfo;

    // First FEN during game creation
    FenFormat initialFen;

    // Game decision generated after a move has been applied that leads to the game end or by given PGN
    ChessGameState decidedGameState;

    // History of all moves applied during the game
    List<Move> moveHistory = new ArrayList<>();


    /**
     * Create a standard chess game
     */
    public ChessGame() {
        this(FenFormat.INITIAL_FEN);
    }

    /**
     * Create a game by a given FEN position
     *
     * @param initialFen
     */
    public ChessGame(FenFormat initialFen) {
        this.initialFen = initialFen;
    }

    /**
     * Create a game by a given PGN game. Does apply all moves of the given PGN and does set game end state if given.
     *
     * @param pgn
     */
    public ChessGame(PgnFormat pgn) {
        this(FenFormat.INITIAL_FEN);

        loadFromPgn(pgn);
    }


    /**
     * Load a given PGN. Adds moves & game state (if finished)
     */
    private void loadFromPgn(PgnFormat pgn) {

        this.gameInfo = new ChessGameInformation(pgn.getWhitePlayer(), pgn.getBlackPlayer(), pgn.getEvent(), pgn.getSite(), pgn.getDate(), pgn.getRound(), pgn.getOptionalTags());

        String pgnMoves = pgn.getStripedPgnMoves();

        StringTokenizer pgnLineTokenizer = new StringTokenizer(pgnMoves, "\n");

        while (pgnLineTokenizer.hasMoreTokens()) {
            String line = pgnLineTokenizer.nextToken();

            StringTokenizer st = new StringTokenizer(line, " ");
            while (st.hasMoreTokens()) {

                String move = st.nextToken().replace(" ", "");

                if (move.startsWith("{") || move.startsWith("$")) {
                    continue;
                }

                if (move.startsWith("*")) {
                    return;
                } else if (move.startsWith("1-0") || move.startsWith("0-1") || move.startsWith("1/2-1/2")) {
                    setDecidedGameState(ChessGameState.getGameStateByValue(move));
                    return;
                }

                int point = move.indexOf(".");
                if (point >= 0) {
                    move = move.substring(point + 1);
                }

                // Break ?
                if (move.isEmpty()) {
                    continue;
                }

                Move moveInput = MoveUtils.san2Move(move, getCurrentBoard(), getCurrentSide());
                applyMove(moveInput);

            }
        }

        throw new ChessGameException("Unexpected end of pgn:" + pgn);


    }

    /**
     * Returns the FEN of ghe current / latest game position
     */
    public FenFormat getFen() {

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
     * Returns the pgn of the game.
     */
    public PgnFormat getPgn() {

        Side sideToMove = Side.WHITE;

        int moveCounter = 1;
        String currentLine = "";
        StringBuilder pgnMoves = new StringBuilder();

        for (int i = 0; i < moveHistory.size(); i++) {

            String addOn = "";
            if (sideToMove == Side.WHITE) {
                addOn += moveCounter + ".";
                moveCounter++;
            }
            currentLine += addOn;

            addOn = "";
            addOn += MoveUtils.move2San(moveHistory.get(i), getBoardAfterMoveNumber(i), sideToMove);
            addOn += " ";

            if (currentLine.length() + addOn.length() > 78) {
                pgnMoves.append(currentLine + "\n");
                currentLine = addOn;
            } else {
                currentLine += addOn;
            }

            if (sideToMove == Side.WHITE) {
                sideToMove = Side.BLACK;
            } else {
                sideToMove = Side.WHITE;
            }
        }

        pgnMoves.append(currentLine);

        // add currentResult
        ChessGameState chessGameState = getCurrentGameState();
        pgnMoves.append(chessGameState.getResult());

        ChessGameInformation gameInfo = getGameInfo();
        PgnFormat pgn = new PgnFormat(gameInfo.getEvent(), gameInfo.getSite(), gameInfo.getDate(), gameInfo.getRound(), gameInfo.getWhitePlayer(), gameInfo.getBlackPlayer(), chessGameState.getResult(), pgnMoves.toString(), gameInfo.getOptionalTags());

        return pgn;
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
    public ChessGameState getCurrentGameState() {
        if (getDecidedGameState() != null) {
            return getDecidedGameState();
        }

        if (this.getCurrentBoard().isMate(getCurrentSide())) {
            if (getCurrentSide() == Side.WHITE) {
                return ChessGameState.BLACK_WIN;
            } else {
                return ChessGameState.WHITE_WIN;
            }
        }

        if (this.getCurrentBoard().isStaleMate(getCurrentSide())) {
            return ChessGameState.REMIS_BY_STALE_MATE;
        }

        if (isRemisByThree()) {
            return ChessGameState.REMIS_BY_THREE;
        }

        if (isRemisByFifty()) {
            return ChessGameState.REMIS_BY_FIFTY;
        }

        if (isRemisByMaterial()) {
            return ChessGameState.REMIS_BY_MATERIAL;
        }

        return ChessGameState.GAME_OPEN;

    }


    /**
     * Returns true if only two pieces (kings) are on the board
     */
    private boolean isRemisByMaterial() {
        return getCurrentBoard().getPieceCount() == 2;
    }

    /**
     * Returns true if 50 moves have been played without a capture or pawn move.
     */
    private boolean isRemisByFifty() {
        return getFiftyMoveDrawRuleCount() >= 50;
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
    private boolean isRemisByThree() {
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
     * Throws a ChessGameException if the game is already finished before that move. </p>
     * The move is validated to be legal, if illegal a ChessGameException is thrown.
     *
     * @return The current game state.
     */
    public ChessGameState applyMove(Move moveInput) {

        // Is game finished already?
        if (decidedGameState != null) {
            throw new ChessGameException("Game is already finished. State: " + decidedGameState);
        }

        // Validate move
        List<Move> validMoves = getLegalMoves();
        if (!validMoves.contains(moveInput)) {
            throw new ChessGameException("Illegal move: " + moveInput);
        }

        // Apply move
        moveHistory.add(moveInput);

        // Update state if game end is triggered
        ChessGameState chessGameState = getCurrentGameState();

        if (chessGameState != ChessGameState.GAME_OPEN) {
            decidedGameState = chessGameState;
        }

        return chessGameState;

    }

    /**
     * Take back the latest move. Can not take back the initial position.
     */
    public void takeBackMove() {

        if (!moveHistory.isEmpty()) {

            // boardHistory.removeLast();
            moveHistory.removeLast();

            decidedGameState = null;

        } else {
            throw new ChessGameException("Can not take back further move.");
        }
    }


}
