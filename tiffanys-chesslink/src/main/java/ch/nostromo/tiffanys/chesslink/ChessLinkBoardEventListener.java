package ch.nostromo.tiffanys.chesslink;

import ch.nostromo.tiffanys.commons.board.Board;
import ch.nostromo.tiffanys.commons.move.Move;

public interface ChessLinkBoardEventListener {

    void onChessLinkBoardInitialized();

    void onChessLinkBoardMoveEntered(Move move);

    void onChessLinkBoardMoveExecuted();

    void onChessLinkBoardSetupExecuted();

    void onChessLinkBoardSetupEntered(Board board);


}
