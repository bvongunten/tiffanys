package ch.nostromo.tiffanys.chesslink;

import ch.nostromo.tiffanys.chesslink.device.ChessLinkDeviceChangedListener;
import ch.nostromo.tiffanys.chesslink.device.ChessLinkDeviceReader;
import ch.nostromo.tiffanys.chesslink.device.ChessLinkDeviceWriter;
import ch.nostromo.tiffanys.chesslink.exception.ChesslinkBoardException;
import ch.nostromo.tiffanys.chesslink.exception.ChesslinkBoardNotFoundException;
import ch.nostromo.tiffanys.commons.board.Board;
import ch.nostromo.tiffanys.commons.enums.GameColor;
import ch.nostromo.tiffanys.commons.enums.Piece;
import ch.nostromo.tiffanys.commons.fen.FenFormat;
import ch.nostromo.tiffanys.commons.fields.Field;
import ch.nostromo.tiffanys.commons.move.Move;
import ch.nostromo.tiffanys.commons.rules.RulesUtil;
import com.ftdichip.usb.FTDI;
import com.ftdichip.usb.FTDIUtility;
import com.ftdichip.usb.enumerated.FlowControl;
import com.ftdichip.usb.enumerated.LineDatabit;
import com.ftdichip.usb.enumerated.LineParity;
import com.ftdichip.usb.enumerated.LineStopbit;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.usb3.IUsbDevice;
import javax.usb3.exception.UsbException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

@Getter
@Setter
@NoArgsConstructor
public class ChessLinkBoard implements ChessLinkDeviceChangedListener {

    protected Logger LOG = Logger.getLogger(getClass().getName());

    private static final short CL_ID_PRODUCT = 24577;
    private static final short CL_ID_VENDOLR = 1027;

    public static final String COMMAND_GET_BOàARD = "S";
    public static final String COMMAND_LED = "L";


    public enum BoardMode {
        BOOTING,
        INITIALIZED,
        WAITING_FOR_MOVE_ENTERING,
        WAITING_FOR_MOVE_EXECUTION,
        WAITING_FOR_POSITION_ENTERING,
        WAITING_FOR_POSITION_EXECUTION,
        FROZEN;
    }

    BoardMode boardMode = BoardMode.BOOTING;

    private int baud = 38400;
    private LineDatabit lineDatabit = LineDatabit.BITS_7;
    private LineStopbit lineStopBit = LineStopbit.STOP_BIT_1;
    private LineParity lineParity = LineParity.ODD;
    private FlowControl flowControl = FlowControl.DTR_DSR_HS;

    private FTDI chesslinkBoardDevice;
    private ChessLinkDeviceReader chessLinkDeviceReader;
    private ChessLinkDeviceWriter chessLinkDeviceWriter;

    private boolean connected = false;

    char[] acceptedDeviceboard;
    Board acceptedGameBoard;
    GameColor acceptedGameColor = GameColor.WHITE;

    char[] lastReadBoard;

    boolean[] leds = new boolean[81];

    boolean cableRight = false;

    List<ChessLinkBoardEventListener> chessLinkBoardEventListeners = new ArrayList<>();

    public void connect() {
        LOG.info("Connecting ChessLink Board ...");

        initializeBoard();

        chessLinkDeviceWriter = new ChessLinkDeviceWriter(chesslinkBoardDevice);

        chessLinkDeviceReader = new ChessLinkDeviceReader(chesslinkBoardDevice);
        chessLinkDeviceReader.getBoardChangedListeners().add(this);
        chessLinkDeviceReader.flushLine();
        chessLinkDeviceReader.start();

        this.connected = true;
    }

    public void disconnect() {
        LOG.info("Disconnecting ChessLink Board ...");
        chessLinkDeviceReader.setRunning(false);
        chessLinkDeviceReader.flushLine();

        chesslinkBoardDevice.close();

        this.connected = false;
    }

    private void initializeBoard() {

        try {
            LOG.fine("Searching for board ...");

            IUsbDevice usbDevice = null;
            Collection<IUsbDevice> devices = FTDIUtility.findFTDIDevices();
            LOG.fine("FTDI devices found: " + devices.size());

            if (devices.isEmpty()) {
                throw new ChesslinkBoardNotFoundException("No FTDI devices found");
            }

            for (IUsbDevice iUsbDevice : devices) {
                if (iUsbDevice.getUsbDeviceDescriptor().idProduct() == this.CL_ID_PRODUCT && iUsbDevice.getUsbDeviceDescriptor().idVendor() == CL_ID_VENDOLR) {
                    usbDevice = iUsbDevice;
                }
            }

            if (usbDevice != null) {
                chesslinkBoardDevice = FTDI.getInstance(usbDevice);
                LOG.fine("Board found: " + chesslinkBoardDevice);
                LOG.info("Board found, configuring line with baud=" + baud + ", LineDatabit=" + lineDatabit + ", LineStopbit=" + lineStopBit + ", LineParity=" + lineParity + ", FlowControl=" + flowControl);
                chesslinkBoardDevice.configureSerialPort(baud, lineDatabit, lineStopBit, lineParity, flowControl);
            } else {
                throw new ChesslinkBoardNotFoundException("Board not found! List of found devices: " + devices);
            }

        } catch (UsbException e) {
            throw new ChesslinkBoardException("Unexbected UsbException while looking for board. Message= " + e.getMessage(), e);
        }
    }

    public void executePositionToBoard(Board board) {
        LOG.info("Setting mode to position setup execution ...");
        this.acceptedGameBoard = board;
        this.acceptedDeviceboard = createDeviceBoard(board);

        this.setBoardMode(BoardMode.WAITING_FOR_POSITION_EXECUTION);

        // Might be null after a new creation of the Board object
        if (lastReadBoard == null) {
            chessLinkDeviceWriter.send(COMMAND_GET_BOàARD);
        } else {
            if (areBoardsIdentical(lastReadBoard, acceptedDeviceboard)) {
                handleExecutedPosition();
            } else {
                lightDifferences();
            }
        }
    }

    public void setBoardToAwaitMove(Board board, GameColor color) {
        LOG.info("Setting mode to move entering ...");
        this.acceptedGameBoard = board;
        this.acceptedGameColor = color;
        this.acceptedDeviceboard = createDeviceBoard(board);

        this.setBoardMode(BoardMode.WAITING_FOR_MOVE_ENTERING);
    }

    public void setBoardToAwaitPosition() {
        LOG.info("Setting mode to positin entering ...");

        this.setBoardMode(BoardMode.WAITING_FOR_POSITION_ENTERING);
    }


    public void executeMoveToBoard(Board board, GameColor color) {
        LOG.info("Setting mode to move execution ...");

        this.acceptedGameBoard = board;
        this.acceptedGameColor = color;
        this.acceptedDeviceboard = createDeviceBoard(board);

        this.setBoardMode(BoardMode.WAITING_FOR_MOVE_EXECUTION);
        lightDifferences();
    }

    @Override
    public void onBoardReceived(char[] board) {
        this.lastReadBoard = checkAndTurnDeviceBoard(board.clone());

        if (this.boardMode == BoardMode.BOOTING) {

            handleBootingFinished();

        } else if (boardMode == BoardMode.WAITING_FOR_MOVE_ENTERING) {

            // Board changed?
            if (!areBoardsIdentical(acceptedDeviceboard, lastReadBoard)) {
                List<Move> legalMoves = RulesUtil.getLegalMoves(acceptedGameBoard, acceptedGameColor);

                for (Move move : legalMoves) {
                    Board playBoard = acceptedGameBoard.clone();
                    playBoard.applyMove(move, acceptedGameColor);

                    // Is board fitting move ?
                    char[] boardAfterMove = createDeviceBoard(playBoard);

                    if (areBoardsIdentical(lastReadBoard, boardAfterMove)) {
                        handleEnteredMove(move);
                    }
                }

            }

            lightDifferences();

        } else if (boardMode == BoardMode.WAITING_FOR_MOVE_EXECUTION) {

            if (areBoardsIdentical(lastReadBoard, acceptedDeviceboard)) {
                handleExecutedMove();
            } else {
                lightDifferences();
            }

        } else if (boardMode == BoardMode.WAITING_FOR_POSITION_EXECUTION) {

            if (areBoardsIdentical(lastReadBoard, acceptedDeviceboard)) {
                handleExecutedPosition();
            } else {
                lightDifferences();
            }

        } else if (boardMode == BoardMode.WAITING_FOR_POSITION_ENTERING) {

            handlePositionEntered();

        } else {
            lightDifferences();
        }

    }

    private void handlePositionEntered() {
        LOG.info("Board has changed.");

        chessLinkBoardEventListeners.forEach(e -> e.onChessLinkBoardSetupEntered(createGameBaord(this.lastReadBoard)));
    }

    private void handleBootingFinished() {
        LOG.info("Board initialized & read successfully.");

        clearLedArray();
        updateLedsOnBoard();

        this.boardMode = BoardMode.INITIALIZED;

        chessLinkBoardEventListeners.forEach(ChessLinkBoardEventListener::onChessLinkBoardInitialized);
    }

    private void handleExecutedMove() {
        LOG.info("Move executed on the board successfully.");

        clearLedArray();
        updateLedsOnBoard();
        setBoardMode(BoardMode.FROZEN);

        chessLinkBoardEventListeners.forEach(ChessLinkBoardEventListener::onChessLinkBoardMoveExecuted);
    }


    private void handleExecutedPosition() {
        LOG.info("Position executed on the board successfully.");

        clearLedArray();
        updateLedsOnBoard();
        setBoardMode(BoardMode.FROZEN);

        chessLinkBoardEventListeners.forEach(ChessLinkBoardEventListener::onChessLinkBoardSetupExecuted);
    }


    private void handleEnteredMove(Move move) {
        LOG.info("New move entered on the board: " + move);

        // Temporary set accepted board to current board to get lights down ;)
        this.acceptedDeviceboard = this.lastReadBoard;

        clearLedArray();
        updateLedsOnBoard();

        setBoardMode(BoardMode.FROZEN);
        chessLinkBoardEventListeners.forEach(e -> e.onChessLinkBoardMoveEntered(move));
    }


    private void lightDifferences() {
        clearLedArray();

        for (int i = 0; i < 64; i++) {
            if (acceptedDeviceboard[i] != lastReadBoard[i]) {
                turnOnLedsOnField(i);
            }
        }


        updateLedsOnBoard();
    }


    private boolean areBoardsIdentical(char[] boardOne, char[] boardTwo) {
        if (boardOne == null || boardTwo == null) {
            return false;
        }

        for (int i = 0; i < 64; i++) {
            if (boardOne[i] != boardTwo[i]) {
                return false;
            }
        }
        return true;
    }

    private void clearLedArray() {
        for (int i = 0; i < 81; i++) {
            leds[i] = false;
        }
    }

    public void turnOnLedsOnField(int field) {
        // Board leds go from right to left, not up to top

        int col = Math.floorDiv(field, 8);
        int row = field - (col * 8);

        int topLeftcorner = row * 9 + col;
        int topRightcorner = row * 9 + col + 1;

        int bottmLeftCorner = (row + 1) * 9 + col;
        int bottomRightcorner = (row + 1) * 9 + col + 1;

        leds[topLeftcorner] = true;
        leds[topRightcorner] = true;
        leds[bottmLeftCorner] = true;
        leds[bottomRightcorner] = true;
    }

    private void updateLedsOnBoard() {
        String commandStr = "L15";

        if (cableRight) {
            for (int i = 0; i < 81; i++) {
                if (leds[i]) {
                    commandStr += "FF";
                } else {
                    commandStr += "00";
                }
            }
        } else {

            for (int i = 80; i >= 0; i--) {
                if (leds[i]) {
                    commandStr += "FF";
                } else {
                    commandStr += "00";
                }
            }

        }
        chessLinkDeviceWriter.send(commandStr);
    }


    private char[] checkAndTurnDeviceBoard(char[] board) {
        char[] resultBoard = new char[64];
        if (cableRight) {
            resultBoard = board;
        } else {
            int count = 0;
            for (int i = 63; i >= 0; i--) {
                resultBoard[count] = board[i];
                count++;
            }
        }
        return resultBoard;
    }

    public char[] createDeviceBoard(Board board) {
        char[] result = new char[64];

        int count = 0;

        // For every row
        for (int y = 0; y < 8; y++) {
            // every col
            for (int x = 0; x < 8; x++) {
                int arrayPos = 28 - x + (y * 10);
                Field field = board.getFields()[arrayPos];

                if (field.getPiece() == null) {
                    result[count] = '.';
                } else {
                    String charCode = field.getPiece().getPieceCharCode();
                    if (field.getPieceColor() == GameColor.BLACK) {
                        charCode = charCode.toLowerCase();
                    }
                    result[count] = charCode.charAt(0);
                }
                count++;
            }
        }

        return result;
    }


    public Board createGameBaord(char[] board) {
        Board result = new Board(new FenFormat("8/8/8/8/8/8/8/8 w KQkq - 1 1"));


        for (int i = 0; i < 64; i++) {
            if (board[i] == '.') {
                result.getFields()[convertToBoardPos(i)].setPiece(null);
                result.getFields()[convertToBoardPos(i)].setPieceColor(null);
            } else {
                result.getFields()[convertToBoardPos(i)].setPiece(Piece.getPieceByCharCode(String.valueOf(board[i])));
                result.getFields()[convertToBoardPos(i)].setPieceColor(Piece.getPieceColorByCharCode(String.valueOf(board[i])));
            }
        }

        return result;

    }

    public int convertToBoardPos(int chessGameIdx) {

        int row = Math.floorDiv(chessGameIdx, 8);
        int col = chessGameIdx - (row * 8);

        int result = 20 + (10 * row) + (8 - col);

        return result;
    }
}
