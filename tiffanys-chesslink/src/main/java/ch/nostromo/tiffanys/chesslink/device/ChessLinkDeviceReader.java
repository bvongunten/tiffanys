package ch.nostromo.tiffanys.chesslink.device;

import ch.nostromo.tiffanys.chesslink.exception.ChesslinkBoardException;
import com.ftdichip.usb.FTDI;
import lombok.Getter;
import lombok.Setter;

import javax.usb3.exception.UsbException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;


@Getter
@Setter
public class ChessLinkDeviceReader extends Thread {

    protected Logger LOG = Logger.getLogger(getClass().getName());

    private static final int SLEEP_MS = 10;

    private static final char MESSAGE_BOARD_HEADER = 's';
    private static final int MESSAGE_BOARD_LENGTH = 67;

    private static final char MESSAGE_LED_HEADER = 'l';
    private static final int MESSAGe_LED_LENGTH = 3;

    FTDI chesslinkBoard;

    boolean isRunning;

    boolean receivingMessage = false;
    int currentReceivingMessageRemainingBytes = 0;

    List<Character> currentReceivingMessage = new ArrayList<>();

    List<ChessLinkDeviceChangedListener> boardChangedListeners = new ArrayList<>();

    public ChessLinkDeviceReader(FTDI chesslinkBoard) {
        this.chesslinkBoard = chesslinkBoard;
    }

    public void run() {
        LOG.fine("Reading thread started ...");
        isRunning = true;

        try {
            while (isRunning) {
                byte[] usbFrame = chesslinkBoard.read();

                if (usbFrame.length > 0) {
                    workQueue(usbFrame);
                }

                Thread.sleep(SLEEP_MS);
            }
        } catch (UsbException | InterruptedException e) {
            throw new ChesslinkBoardException("Unable to read from device. Message= " + e.getMessage(), e);
        }
    }

    private void workQueue(byte[] usbFrame) throws UsbException {
        LOG.fine("UsbFrame received: " + new String(usbFrame));

        for (int i = 0; i < usbFrame.length; i++) {

            char charByte = (char) usbFrame[i];

            if (charByte == MESSAGE_BOARD_HEADER) {
                LOG.fine("New board message header found.");

                handleUnfiinishedMessage();

                this.currentReceivingMessageRemainingBytes = MESSAGE_BOARD_LENGTH;
            } else if (charByte == MESSAGE_LED_HEADER) {
                LOG.fine("New led message header found.");

                handleUnfiinishedMessage();

                this.currentReceivingMessageRemainingBytes = MESSAGe_LED_LENGTH;
            }

            this.currentReceivingMessage.add(charByte);
            this.currentReceivingMessageRemainingBytes--;

            if (currentReceivingMessageRemainingBytes == 0) {
                handleFinishedMessage();
            }
        }
    }


    private void handleFinishedMessage() {
        LOG.fine("Handing finished message :" + this.currentReceivingMessage);

        if (currentReceivingMessage.get(0) == MESSAGE_BOARD_HEADER) {
            char[] readBoard = new char[64];
            for (int i = 0; i < 64; i++) {
                readBoard[i] = currentReceivingMessage.get(i + 1);
            }

            LOG.fine("Full board received: " + new String(readBoard));

            boardChangedListeners.forEach(e -> e.onBoardReceived(readBoard));

        } else if (currentReceivingMessage.get(0) == MESSAGE_LED_HEADER) {
            LOG.fine("LED ack received.");
        }

        this.currentReceivingMessage = new ArrayList<>();
        this.currentReceivingMessageRemainingBytes = -1;
    }

    private void handleUnfiinishedMessage() {
        if (!currentReceivingMessage.isEmpty()) {
            LOG.warning("Unfinished message purged: " + this.currentReceivingMessage);
        }

        this.currentReceivingMessage = new ArrayList<>();
        this.currentReceivingMessageRemainingBytes = -1;
    }

    public List<ChessLinkDeviceChangedListener> getBoardChangedListeners() {
        return boardChangedListeners;
    }

    public void flushLine() {
        try {
            byte[] usbFrame = chesslinkBoard.read();
            while (usbFrame.length > 0) {
                LOG.fine("Flushing " + usbFrame.length + " bytes ...");
                usbFrame = chesslinkBoard.read();
            }
        } catch (UsbException e) {
            throw new ChesslinkBoardException("Unable to read from device. Message= " + e.getMessage(), e);
        }
    }
}


