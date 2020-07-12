package ch.nostromo.tiffanys.chesslink;

import ch.nostromo.tiffanys.commons.board.Board;
import ch.nostromo.tiffanys.commons.enums.GameColor;
import ch.nostromo.tiffanys.commons.fen.FenFormat;
import ch.nostromo.tiffanys.commons.logging.LogUtils;
import ch.nostromo.tiffanys.commons.move.Move;

import javax.usb3.exception.UsbException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PositionEntering implements ChessLinkBoardEventListener {
    protected Logger LOG = Logger.getLogger(getClass().getName());

    ChessLinkBoard chessLinkBoard;

    Board playingBoard;

    public PositionEntering() throws IOException {
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
    public void onChessLinkBoardSetupEntered(Board board) {
        LOG.info("\n" + board.toString());
    }

    @Override
    public void onChessLinkBoardInitialized() {
        chessLinkBoard.setBoardToAwaitPosition();
    }

    @Override
    public void onChessLinkBoardSetupExecuted() {
    }


    @Override
    public void onChessLinkBoardMoveEntered(Move move) {
    }

    @Override
    public void onChessLinkBoardMoveExecuted() {

    }

    public static void main(String... args9) throws UsbException, IOException {
        new PositionEntering();
    }

}


