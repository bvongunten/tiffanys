package ch.nostromo.tiffanys.chesslink.device;

import ch.nostromo.tiffanys.chesslink.exception.ChesslinkBoardException;
import com.ftdichip.usb.FTDI;

import javax.usb3.exception.UsbException;
import java.util.logging.Logger;

public class ChessLinkDeviceWriter {
    protected Logger LOG = Logger.getLogger(getClass().getName());

    FTDI chesslinkBoard;

    public ChessLinkDeviceWriter(FTDI chesslinkBoard) {
        this.chesslinkBoard = chesslinkBoard;
    }

    public void send(String commandStr) {
        byte[] commandToSend = null;

        try {
            int checkSum = 0;
            for (byte by : commandStr.getBytes()) {
                checkSum = checkSum ^ by;
            }

            commandToSend = (commandStr + Integer.toHexString(checkSum)).getBytes();

            LOG.fine("Sending command to device: " + new String(commandToSend));

            chesslinkBoard.write(commandToSend);
        } catch (UsbException e) {
            throw new ChesslinkBoardException("Unable to send command: " + new String(commandToSend), e);
        }
    }

}
