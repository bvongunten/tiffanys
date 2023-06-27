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
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Data
public class ChessGame {

    protected Logger logger = Logger.getLogger(getClass().getName());

    List<Board> historyBoards = new ArrayList<Board>();
    List<Move> historyMoves = new ArrayList<Move>();

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

        if (fenFormat.toString().equalsIgnoreCase(FenFormat.INITIAL_BOARD)) {
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

        String fenPosition = getCurrentBoard().getFenPosition();
        String fenCastling = getCurrentBoard().getFenCastling();
        String fenEnpassant = getCurrentBoard().getFenEnpassant();

        String fenColorToMove;
        if (colorToMove == GameColor.WHITE) {
            fenColorToMove = "w";
        } else {
            fenColorToMove = "b";
        }

        Integer fenHalfMoveClock = getCurrentHalfMoveClock();


        int fenMoveCount = (getCurrentMoveCount() + 1) / 2;
        // FEN miracle :)
        if (fenMoveCount == 0) {
            fenMoveCount = 1;
        }

        return new FenFormat(fenPosition, fenColorToMove, fenCastling, fenEnpassant, fenHalfMoveClock, fenMoveCount);
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


}
