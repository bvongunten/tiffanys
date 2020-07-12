package ch.nostromo.tiffanys.chesslink.device;

public interface ChessLinkDeviceChangedListener {

    void onBoardReceived(char[] board);
}
