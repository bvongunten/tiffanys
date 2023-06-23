package ch.nostromo.tiffanys.commons;

import ch.nostromo.tiffanys.commons.board.Board;
import ch.nostromo.tiffanys.commons.enums.GameColor;
import ch.nostromo.tiffanys.commons.enums.GameState;
import ch.nostromo.tiffanys.commons.enums.Piece;
import ch.nostromo.tiffanys.commons.fen.FenFormat;
import ch.nostromo.tiffanys.commons.fields.Field;
import ch.nostromo.tiffanys.commons.move.Move;
import ch.nostromo.tiffanys.commons.pieces.King;
import ch.nostromo.tiffanys.commons.rules.RulesUtil;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Getter
@Setter
public class ChessGame {

    protected Logger logger = Logger.getLogger(getClass().getName());

    List<Board> historyBoards = null;
    List<Move> historyMoves = null;

    Double historyInitialBoardScore = null;

    int offsetHalfMoveClock;
    int offsetMoveCount;

    GameColor colorToMove;
    GameColor startingColor;

    ChessGameInfo gameInfo;

    GameState finishedGameState;

    FenFormat initialFen;

    boolean eligibleForOB = false;

    public ChessGame() {
        this(new ChessGameInfo(), new FenFormat(FenFormat.INITIAL_BOARD));
    }

    public ChessGame(ChessGameInfo gameInfo) {
        this(gameInfo, new FenFormat(FenFormat.INITIAL_BOARD));
    }

    public ChessGame(ChessGameInfo gameInfo, FenFormat fenFormat) {
        this.gameInfo = gameInfo;
        this.initialFen = fenFormat;
        historyBoards = new ArrayList<Board>();
        historyMoves = new ArrayList<Move>();
        setGameByFen(initialFen);
    }

    public void resetToInitialFen() {
        historyBoards = new ArrayList<Board>();
        historyMoves = new ArrayList<Move>();
        setGameByFen(initialFen);
    }

    private void setGameByFen(FenFormat fenFormat) {
        if (fenFormat.generateFen().equalsIgnoreCase(FenFormat.INITIAL_BOARD)) {
            this.eligibleForOB = true;
        }

        historyBoards.add(new Board(fenFormat));

        // color to move
        if (fenFormat.getColorToMove().equalsIgnoreCase(GameColor.WHITE.getColorCode())) {
            colorToMove = GameColor.WHITE;
            startingColor = GameColor.WHITE;
        } else {
            colorToMove = GameColor.BLACK;
            startingColor = GameColor.BLACK;
        }

        this.offsetHalfMoveClock = fenFormat.getHalfMoveClock();
        this.offsetMoveCount = fenFormat.getMoveNr();

    }

    public Board getCurrentBoard() {
        return historyBoards.get(historyBoards.size() - 1);
    }

    public FenFormat getCurrentFenFormat() {

        FenFormat fenFormat = getCurrentBoard().getFenFormat();

        if (colorToMove == GameColor.WHITE) {
            fenFormat.setColorToMove("w");
        } else {
            fenFormat.setColorToMove("b");
        }

        fenFormat.setHalfMoveClock(getCurrentHalfMoveClock());

        int moveCount = (getCurrentMoveCount() + 1) / 2;
        // FEN miracle :)
        if (moveCount == 0) {
            moveCount = 1;
        }
        fenFormat.setMoveNr(moveCount);

        return fenFormat;
    }

    public int getCurrentMoveCount() {
        return this.historyMoves.size() + offsetMoveCount;
    }

    public int getCurrentHalfMoveClock() {
        // TODO: fixme !
        return this.offsetHalfMoveClock;
    }

    public GameColor getCurrentColorToMove() {
        return colorToMove;
    }

    public void overWriteColorToMove(GameColor colorToMove) {
    	this.colorToMove = colorToMove;
    }

    public GameState getCurrentGameState() {
        if (getFinishedGameState() != null) {
            return getFinishedGameState();
        }

        if (RulesUtil.isMate(this.getCurrentBoard(), getCurrentColorToMove())) {
            if (colorToMove == GameColor.WHITE) {
                return GameState.WIN_BLACK_MATES;
            } else {
                return GameState.WIN_WHITE_MATES;
            }
        }

        if (RulesUtil.isStaleMate(this.getCurrentBoard(), getCurrentColorToMove())) {
            return GameState.REMIS_BY_STALE_MATE;
        }

        if (RulesUtil.isRemisByThree(historyBoards)) {
            return GameState.REMIS_BY_THREE;
        }

        if (RulesUtil.isRemisByFifty(historyBoards, historyMoves)) {
            return GameState.REMIS_BY_FIFTY;
        }

        // TODO: Remis by Material

        return GameState.GAME_OPEN;

    }

    public boolean isGameFinished() {
        if (this.getFinishedGameState() != null) {
            return true;
        }

        return getCurrentGameState() != GameState.GAME_OPEN;
    }

	public void applyMove(Move moveInput) {

        Board boardClone;

        boardClone = historyBoards.get(historyBoards.size() - 1).clone();

        boardClone.applyMove(moveInput, colorToMove);
        Field[] playedFields = boardClone.getFields();
        boolean isCheck = false;
        for (int x = 0; x < playedFields.length; x++) {
            if (playedFields[x].getPiece() == Piece.KING && playedFields[x].getPieceColor() == colorToMove) {
                isCheck = (King.isKingAttacked(boardClone, x, colorToMove));
            }
        }

        if (!isCheck) {
            historyBoards.add(boardClone);
            historyMoves.add(moveInput);

            colorToMove = colorToMove.invert();
        } else {
            throw new IllegalArgumentException("Illegal move: " + moveInput);
        }

    }

    public void takeBackMove() {
        historyBoards.remove(historyBoards.size() - 1);
        historyMoves.remove(historyMoves.size() - 1);
        colorToMove = colorToMove.invert();
    }


    public ChessGame duplicateChessGameByStart() {
        return new ChessGame(this.gameInfo, this.initialFen);
    }

}
