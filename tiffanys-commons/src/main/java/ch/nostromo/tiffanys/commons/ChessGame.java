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
import java.util.logging.Logger;

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

    protected Logger logger = Logger.getLogger(getClass().getName());

    // Game info
    ChessGameInformation gameInfo;

    // Current side to move
    Side currentSide;

    // Moves offset if started by a mid game fen
    int givenMoveOffset;

    // Game state generated after a move has been applied that leads to the game end
    ChessGameState givenGameFinishedState;

    // History of all boards during the game
    List<Board> boardHistory = new ArrayList<Board>();

    // History of all moves applied during the game
    List<Move> moveHistory = new ArrayList<Move>();

    // Current count of moves counting towards a 50 move draw rule
    int fiftyMoveDrawRuleCount;

    /**
     * Create a game by a given FEN position
     *
     * @param fen
     */
    public ChessGame(FenFormat fen) {
        loadFromFen(fen);
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
     * Create a standard chess game
     */
    public ChessGame() {
        this(FenFormat.INITIAL_FEN);
    }


    /**
     * Get the current / latest board of the chess game.
     *
     * @return Board
     */
    public Board getCurrentBoard() {
        return boardHistory.getLast();
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
                    setGivenGameFinishedState(ChessGameState.getGameStateByValue(move));
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

                Move moveInput = MoveUtils.san2Move(move, getCurrentBoard(), getCurrentSideToMove());
                applyMove(moveInput);

            }
        }

        throw new ChessGameException("Unexpected end of pgn:" + pgn);


    }


    /**
     * Load a given FEN. Does set a given move offset if not initial position.
     */
    private void loadFromFen(FenFormat fenFormat) {

        boardHistory.add(new Board(fenFormat));

        // side to move
        if (fenFormat.getSideToMove().equalsIgnoreCase(Side.WHITE.getColorCode())) {
            currentSide = Side.WHITE;
        } else {
            currentSide = Side.BLACK;
        }

        this.givenMoveOffset = fenFormat.getMoveNr();
        this.fiftyMoveDrawRuleCount = fenFormat.getHalfMoveClock();
    }


    /**
     * Returns the FEN of ghe current / latest game position
     */
    public FenFormat getFen() {

        String fenPosition = getCurrentBoard().getFenPosition();
        String fenCastling = getCurrentBoard().getFenCastling();
        String fenEnPassant = getCurrentBoard().getFenEnPassant();
        String fenSideToMove = currentSide.getColorCode().toLowerCase();

        Integer fenHalfMoveClock = getFiftyMoveDrawRuleCount();

        int fenMoveCount = getCurrentMoveNumber();

        // FEN miracle :)
        if (fenMoveCount == 0) {
            fenMoveCount = 1;
        }

        return new FenFormat(fenPosition, fenSideToMove, fenCastling, fenEnPassant, fenHalfMoveClock, fenMoveCount);
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
            addOn += MoveUtils.move2San(moveHistory.get(i), boardHistory.get(i), sideToMove);
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
        return (moveHistory.size() + givenMoveOffset) / 2;
    }

    /**
     * Returns the current side to move
     */
    public Side getCurrentSideToMove() {
        return currentSide;
    }

    /**
     * Returns the current or given (by pgn) game state.
     */
    public ChessGameState getCurrentGameState() {
        if (getGivenGameFinishedState() != null) {
            return getGivenGameFinishedState();
        }

        if (this.getCurrentBoard().isMate(getCurrentSideToMove())) {
            if (currentSide == Side.WHITE) {
                return ChessGameState.BLACK_WIN;
            } else {
                return ChessGameState.WHITE_WIN;
            }
        }

        if (this.getCurrentBoard().isStaleMate(getCurrentSideToMove())) {
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

    /**
     * Returns true if the current position occurs the third time.
     */
    private boolean isRemisByThree() {
        if (boardHistory.size() >= 6) {

            String currentFenPosition = boardHistory.getLast().getFenPosition();

            int count = 0;
            for (Board board : boardHistory) {
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
     * Applies a move to the current game. </p>
     * Throws a ChessGameException if the game is already finished before that move. </p>
     * The move is validated to be legal, if illegal a ChessGameException is thrown.
     *
     * @return The current game state.
     */
    public ChessGameState applyMove(Move moveInput) {

        // Is game finished already?
        if (givenGameFinishedState != null) {
            throw new ChessGameException("Game is already finished. State: " + givenGameFinishedState);
        }


        // Validate move
        List<Move> validMoves = boardHistory.getLast().getLegalMoves(currentSide);
        if (!validMoves.contains(moveInput)) {
            throw new ChessGameException("Illegal move: " + moveInput);
        }

        // Pawn or hit move ? Reset the fifty Move half move counter (castling increases the move count)
        if (!moveInput.isCastling() && (boardHistory.getLast().getPieceType(moveInput.getFrom()).equals(PieceType.PAWN) || !boardHistory.getLast().isEmptySquare(moveInput.getTo()))) {
            this.fiftyMoveDrawRuleCount = 0;
        } else {
            this.fiftyMoveDrawRuleCount++;
        }

        // Apply move
        Board boardClone = boardHistory.getLast().clone();
        boardClone.applyMove(moveInput, currentSide);
        boardHistory.add(boardClone);
        moveHistory.add(moveInput);

        currentSide = currentSide.invert();


        ChessGameState chessGameState = getCurrentGameState();

        if (chessGameState != ChessGameState.GAME_OPEN) {
            givenGameFinishedState = chessGameState;
        }

        return chessGameState;

    }

    /**
     * Take back the latest move. Can not take back the initial position.
     */
    public void takeBackMove() {

        if (boardHistory.size() > 1) {

            boardHistory.removeLast();
            moveHistory.removeLast();
            currentSide = currentSide.invert();

            givenGameFinishedState = null;
            if (fiftyMoveDrawRuleCount > 0) {
                fiftyMoveDrawRuleCount--;
            }
        } else {
            throw new ChessGameException("Can not take back further move.");
        }
    }


}
