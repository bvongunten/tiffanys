package ch.nostromo.tiffanys.chesslink;

import ch.nostromo.tiffanys.commons.board.Board;
import ch.nostromo.tiffanys.commons.enums.GameColor;
import ch.nostromo.tiffanys.commons.fen.FenFormat;
import ch.nostromo.tiffanys.commons.logging.LogUtils;
import ch.nostromo.tiffanys.commons.move.Move;

import javax.usb3.exception.UsbException;
import java.io.IOException;
import java.util.logging.Level;

public class Testclient implements ChessLinkBoardEventListener {

    ChessLinkBoard chessLinkBoard;

    Board playingBoard;

    public Testclient() throws IOException, UsbException {
        LogUtils.initializeLogging(Level.FINE, Level.OFF, null, null);

        chessLinkBoard = new ChessLinkBoard();
        chessLinkBoard.getChessLinkBoardEventListeners().add(this);
        chessLinkBoard.connect();
        chessLinkBoard.disconnect();

        chessLinkBoard.setCableRight(true);
        chessLinkBoard.connect();

        playingBoard = new Board(new FenFormat(FenFormat.INITIAL_BOARD));

    }

    @Override
    public void onChessLinkBoardInitialized() {
        chessLinkBoard.executePositionToBoard(playingBoard);
    }

    @Override
    public void onChessLinkBoardSetupExecuted() {
        chessLinkBoard.setBoardToAwaitMove(playingBoard, GameColor.WHITE);

        chessLinkBoard.setBoardMode(ChessLinkBoard.BoardMode.WAITING_FOR_MOVE_ENTERING);
    }

    @Override
    public void onChessLinkBoardSetupEntered(Board board) {

    }

    @Override
    public void onChessLinkBoardMoveEntered(Move move) {
        playingBoard.applyMove(move, GameColor.WHITE);

        Move aiMove = new Move("E7", "E5");
        playingBoard.applyMove(aiMove, GameColor.BLACK);

        chessLinkBoard.executeMoveToBoard(playingBoard.clone(), GameColor.BLACK);
    }

    @Override
    public void onChessLinkBoardMoveExecuted() {
        chessLinkBoard.setBoardToAwaitMove(playingBoard, GameColor.WHITE);
    }

    public static void main(String... args9) throws UsbException, IOException {
        new Testclient();
    }

}


