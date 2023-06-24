package ch.nostromo.tiffanys.commons.uci.client;

import ch.nostromo.tiffanys.commons.ChessGame;
import ch.nostromo.tiffanys.commons.exception.TiffanysException;
import ch.nostromo.tiffanys.commons.format.FenFormat;
import ch.nostromo.tiffanys.commons.move.Move;
import ch.nostromo.tiffanys.commons.uci.protocol.*;
import ch.nostromo.tiffanys.commons.uci.utils.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * UCI Client implementation handles the communication from and to an UCI server and provides a simple interface for a
 * chess engine with standard ChessGame, Board and Move objects.
 */
public class UciClient implements UciConsoleScannerListener {

    private static final Logger LOG = LoggerFactory.getLogger(UciClient.class);


    // last known position by position command
    FenFormat currentPosition;

    // Console scanner
    UciConsoleScanner uciConsoleScanner;

    // Console writer (replacable)
    UciConsoleWriter uciConsoleWriter;

    // Listener (engine) of this uci client
    UciClientListener uciClientListener;


    /**
     * Create a uci client for a given uci client listener
     */
    public UciClient(UciClientListener uciClientListener) {
        this(uciClientListener, new UciConsoleWriter());
    }

    /**
     * Create a uci client for a given uci client listener and console writer for testing purposes.
     */
    public UciClient(UciClientListener uciClientListener, UciConsoleWriter uciConsoleWriter) {
        this.uciClientListener = uciClientListener;
        this.uciConsoleWriter = uciConsoleWriter;

        uciConsoleScanner = new UciConsoleScanner(this);
    }

    /**
     * Start the retrieval of commands from UCI Server
     */
    public void startScanner() {
        uciConsoleScanner.start();
    }

    /**
     * Stop the retrieval of commands from UCI Server
     */
    public void stopScanner() {
        uciConsoleScanner.interrupt();
    }


    /**
     * Retrieves a command from UCI Server, parses the input, creates a request for a listening engine
     *
     * @param command
     */
    @Override
    public void handleConsoleInput(String command) {

        LOG.info("<==== from UCI: {}", command);

        // Filter commands
        if (command.equals("uci")) {
            uciClientListener.handleUci();
        } else if (command.startsWith("setoption")) {
            uciClientListener.handleSetOption(generateUciOption(command));
        } else if (command.startsWith("debug on")) {
            uciClientListener.handleDebugOn();
        } else if (command.startsWith("debug off")) {
            uciClientListener.handleDebugOff();
        } else if (command.startsWith("register")) {
            uciClientListener.handleRegister(generateRegister(command));
        } else if (command.startsWith("isready")) {
            uciClientListener.handleIsReady();
        } else if (command.startsWith("ucinewgame")) {
            uciClientListener.handleUciNewGame();
        } else if (command.startsWith("position")) {
            uciClientListener.handlePosition(generatePosition(command));
        } else if (command.startsWith("go")) {
            uciClientListener.handleGo(generateGo(command));
        } else if (command.startsWith("stop")) {
            uciClientListener.handleStop();
        } else if (command.startsWith("ponderhit")) {
            uciClientListener.handlePonderHit();
        } else if (command.startsWith("quit")) {
            uciClientListener.handleQuit();
        } else {
            throw new UciException("Unknown command: " + command);
        }

    }

    /**
     * Parses and generates an uci go command and provides an UciGo request object for an engine. Does use the last FenFormat
     * position if available to generate the searchmoves list when limited.
     */
    ServerGo generateGo(String command) {
        try {
            ServerGo serverGo = new ServerGo();
            String[] tokens = command.trim().split("\\s+");

            int i = 1; // Start after "go"
            while (i < tokens.length) {
                String token = tokens[i++];

                switch (token) {
                    case "searchmoves": {
                        List<Move> moves = new ArrayList<>();
                        while (i < tokens.length && tokens[i].matches("[a-h][1-8][a-h][1-8][qrbn]?")) {
                            ChessGame chessGame = (currentPosition == null)
                                    ? new ChessGame()
                                    : new ChessGame(currentPosition);

                            Move moveToAdd = UciMoveTranslator.uciToMove(tokens[i], chessGame.getCurrentBoard());
                            chessGame.applyMove(moveToAdd); // validate move
                            moves.add(moveToAdd);
                            i++;
                        }
                        serverGo.setSearchMoves(moves);
                        break;
                    }
                    case "wtime":
                        serverGo.setTimeWhite(parseNextInt(tokens, i++));
                        break;
                    case "btime":
                        serverGo.setTimeBlack(parseNextInt(tokens, i++));
                        break;
                    case "winc":
                        serverGo.setIncreaseWhite(parseNextInt(tokens, i++));
                        break;
                    case "binc":
                        serverGo.setIncreaseBlack(parseNextInt(tokens, i++));
                        break;
                    case "movestogo":
                        serverGo.setMovesToGo(parseNextInt(tokens, i++));
                        break;
                    case "depth":
                        serverGo.setDepth(parseNextInt(tokens, i++));
                        break;
                    case "nodes":
                        serverGo.setNodes(parseNextInt(tokens, i++));
                        break;
                    case "mate":
                        serverGo.setMate(parseNextInt(tokens, i++));
                        break;
                    case "movetime":
                        serverGo.setMoveTime(parseNextInt(tokens, i++));
                        break;
                    case "infinite":
                        serverGo.setInfinite(true);
                        break;
                    case "ponder":
                        serverGo.setPonder(true);
                        break;
                    default:
                        throw new UciException("Unknown token: " + token + ", command: " + command);
                }
            }

            return serverGo;

        } catch (Exception e) {
            throw new UciException("Unable to parse go command: " + command + ", with message: " + e.getMessage(), e);
        }
    }

    private int parseNextInt(String[] tokens, int index) {
        if (index >= tokens.length) {
            throw new UciException("Expected number after token, but reached end of input.");
        }
        return Integer.parseInt(tokens[index]);
    }

    /**
     * Does parse an uci setoption command and does create a SetOption object with name and value of the option for an engine.
     */
    ServerOption generateUciOption(String command) {
        String lower = command.toLowerCase(Locale.ROOT);

        String commandPrefix = "setoption name ";

        if (!lower.startsWith(commandPrefix)) {
            throw new UciException("Unable to parse uci option command: " + command);
        }

        String rest = command.substring(commandPrefix.length());

        String name;
        String value = null;

        int valueIndex = lower.indexOf(" value ", commandPrefix.length());

        if (valueIndex >= 0) {
            name = command.substring(commandPrefix.length(), valueIndex).trim();
            value = command.substring(valueIndex + " value ".length()).trim();
        } else {
            name = rest.trim();
        }

        return new ServerOption(name, value);
    }
    /**
     * Does parse an uci register command and does create a UciRegister object with name, code or later setting for an engine.
     */
    ServerRegister generateRegister(String command) {
        if (command.equals("register later")) {
            return new ServerRegister(ServerRegister.RegisterType.LATER, null, null);
        } else {
            Matcher nameMatcher = Pattern.compile("name (.+)(?= code|$)", Pattern.CASE_INSENSITIVE).matcher(command);
            Matcher codeMatcher = Pattern.compile("code (.+)", Pattern.CASE_INSENSITIVE).matcher(command);

            String name = nameMatcher.find() ? nameMatcher.group(1).trim() : null;
            String code = codeMatcher.find() ? codeMatcher.group(1).trim() : null;

            if (name != null) {
                return new ServerRegister(ServerRegister.RegisterType.NAME, name, null);
            } else if (code != null) {
                return new ServerRegister(ServerRegister.RegisterType.CODE, null, code);
            } else {
                throw new UciException("Unable to parse uci register command: " + command);
            }
        }
    }

    /**
     * Does parse an uci position command and does create a UciPosition object with a complete ChessGame object including Moves.
     * <p>
     * Does save the last fen position for future use in a later go command.
     */
    ServerPosition generatePosition(String command) {

        PositionCommand positionCmd = parsePositionCommand(command);

        try {
            // Create chess game
            ChessGame chessGame;
            ServerPosition.PositionType positionType;

            if (positionCmd.fen() == null) {
                chessGame = new ChessGame();
                positionType = ServerPosition.PositionType.STARTPOS;
            } else {
                chessGame = new ChessGame(new FenFormat(positionCmd.fen()));
                positionType = ServerPosition.PositionType.FEN;
            }

            // Apply moves
            for (String move : positionCmd.moves()) {
                chessGame.applyMove(UciMoveTranslator.uciToMove(move, chessGame.getCurrentBoard()));
            }

            // Save fen for current position for possible later use in a go command
            currentPosition = chessGame.createFen();

            return new ServerPosition(positionType, chessGame);

        } catch (TiffanysException tiffanysException) {
            throw new UciException("Unable to create ChessGame by uci position command: " + command
                    + ", message: " + tiffanysException.getMessage(), tiffanysException);
        }
    }

    private PositionCommand parsePositionCommand(String command) {
        String trimmedCommand = command.trim();

        if (!trimmedCommand.toLowerCase().startsWith("position ")) {
            throw new UciException("Not a valid position command: " + command);
        }

        String remainder = trimmedCommand.substring(9).trim();

        // Split at "moves" keyword
        int movesIndex = remainder.toLowerCase().indexOf(" moves ");

        String positionPart;
        String movesPart = "";

        if (movesIndex != -1) {
            positionPart = remainder.substring(0, movesIndex).trim();
            movesPart = remainder.substring(movesIndex + 7).trim();
        } else {
            positionPart = remainder;
        }

        // Parse position type
        String fen = null;
        if (positionPart.toLowerCase().startsWith("fen ")) {
            fen = positionPart.substring(4).trim();
            if (fen.isEmpty()) {
                throw new UciException("FEN string cannot be empty: " + command);
            }
        } else if (!positionPart.equalsIgnoreCase("startpos")) {
            throw new UciException("Position must be 'fen' or 'startpos': " + command);
        }

        // Parse moves
        List<String> moves = parseMoves(movesPart, command);

        return new PositionCommand(fen, moves);
    }

    private static final Pattern UCI_MOVE_PATTERN = Pattern.compile("[a-h][1-8][a-h][1-8][qrbn]?");

    private List<String> parseMoves(String movesStr, String originalCommand) {
        if (movesStr.isBlank()) {
            return Collections.emptyList();
        }

        List<String> moves = new ArrayList<>();

        for (String move : movesStr.split("\\s+")) {
            String trimmedMove = move.trim();
            if (trimmedMove.isEmpty()) {
                continue;
            }

            if (!UCI_MOVE_PATTERN.matcher(trimmedMove).matches()) {
                throw new UciException("Invalid UCI move format '" + trimmedMove + "' in command: " + originalCommand);
            }

            moves.add(trimmedMove);
        }

        return moves;
    }

    private record PositionCommand(String fen, List<String> moves) {
        public boolean isStartPos() {
            return fen == null;
        }
    }

    /**
     * Send uci parameter to the server, including id name, author , given options and a final uciok
     *
     * @param clientUciResponse response
     */
    public void sendUci(ClientUciResponse clientUciResponse) {
        sendLineToWriter("id name " + clientUciResponse.getName());
        sendLineToWriter("id author " + clientUciResponse.getAuthor());

        for (ClientOption clientOption : clientUciResponse.getClientOptions()) {
            StringBuilder optionLine = new StringBuilder("option");

            optionLine.append(" name ").append(clientOption.getName());
            optionLine.append(" type ");

            switch (clientOption.getType()) {
                case CHECK -> optionLine.append("check");
                case SPIN -> optionLine.append("spin");
                case COMBO -> optionLine.append("combo");
                case BUTTON -> optionLine.append("button");
                case STRING -> optionLine.append("string");
            }

            if (clientOption.getDefaultValue() != null) {
                optionLine.append(" default ").append(clientOption.getDefaultValue());
            }

            if (clientOption.getMin() != null) {
                optionLine.append(" min ").append(clientOption.getMin());
            }

            if (clientOption.getMax() != null) {
                optionLine.append(" max ").append(clientOption.getMax());
            }

            if (clientOption.getVars() != null && !clientOption.getVars().isEmpty()) {
                optionLine.append(" var");
                for (String vars : clientOption.getVars()) {
                    optionLine.append(" ").append(vars);
                }
            }
            sendLineToWriter(optionLine.toString());
        }

        sendLineToWriter("uciok");
    }

    /**
     * Send readyok to the server
     */
    public void sendReadyOk() {
        sendLineToWriter("readyok");
    }

    /**
     * Translate and send the given (best) move to the server
     */
    public void sendBestMove(Move move) {
        sendLineToWriter("bestmove " + UciMoveTranslator.moveToUci(move));
    }

    /**
     * Send an error message to the server
     */
    public void sendError(String message) {
        sendLineToWriter("info string error: " + message);
    }


    /**
     * Send an information line to the server
     */
    public void sendInfo(ClientInfo clientInfo) {

        StringBuilder infoLine = new StringBuilder("info");

        appendClientInfo(infoLine, " depth ", clientInfo.getDepth());
        appendClientInfo(infoLine, " seldepth ", clientInfo.getSeldepth());

        appendClientInfoCurrMove(infoLine, clientInfo);

        appendClientInfo(infoLine, " currmovenumber ", clientInfo.getCurrentMoveNumber());
        appendClientInfo(infoLine, " score mate ", clientInfo.getScoreMate());

        appendClientInfoScoreCp(infoLine, clientInfo);

        appendClientInfo(infoLine, " nodes ", clientInfo.getNodes());
        appendClientInfo(infoLine, " nps ", clientInfo.getNps());
        appendClientInfo(infoLine, " time ", clientInfo.getTime());

        appendClientInfoPv(infoLine, clientInfo);

        appendClientInfo(infoLine, " multipv ", clientInfo.getMultiPv());
        appendClientInfo(infoLine, " hashfull ", clientInfo.getHashFull());
        appendClientInfo(infoLine, " tbhits ", clientInfo.getTbhits());
        appendClientInfo(infoLine, " sbhits ", clientInfo.getSbhits());
        appendClientInfo(infoLine, " cpuload ", clientInfo.getCpuload());
        appendClientInfo(infoLine, " string ", clientInfo.getStringMessage());

        appendClientRefutation(infoLine, clientInfo);
        appendClientInfoCurrLineCpu(infoLine, clientInfo);

        sendLineToWriter(infoLine.toString());
    }

    /**
     * Append given client info currmove if not null
     *
     * @param infoLine target sb
     * @param clientInfo client info
     */
    private void appendClientInfoCurrMove(StringBuilder infoLine, ClientInfo clientInfo) {
        if (clientInfo.getCurrentMove() != null) {
            infoLine.append(" currmove ").append(UciMoveTranslator.moveToUci(clientInfo.getCurrentMove()));
        }
    }

    /**
     * Append given client info score if not null
     *
     * @param infoLine target sb
     * @param clientInfo client info
     */
    private void appendClientInfoScoreCp(StringBuilder infoLine, ClientInfo clientInfo) {
        if (clientInfo.getScoreCp() != null) {
            infoLine.append(" score cp ").append(clientInfo.getScoreCp());

            if (Boolean.TRUE.equals(clientInfo.getScoreLowerBound())) {
                infoLine.append(" lowerbound");
            }

            if (Boolean.TRUE.equals(clientInfo.getScoreUpperBound())) {
                infoLine.append(" upperbound");
            }
        }

    }

    /**
     * Append given client info pv if not null
     *
     * @param infoLine target sb
     * @param clientInfo client info
     */
    private void appendClientInfoPv(StringBuilder infoLine, ClientInfo clientInfo) {
        if (clientInfo.getPv() != null && !clientInfo.getPv().isEmpty()) {
            infoLine.append(" pv");
            for (Move move : clientInfo.getPv()) {
                infoLine.append(" ").append(UciMoveTranslator.moveToUci(move));
            }
        }

    }

    /**
     * Append given client info refutation if not null
     *
     * @param infoLine target sb
     * @param clientInfo client info
     */
    private void appendClientRefutation(StringBuilder infoLine, ClientInfo clientInfo) {

        if (clientInfo.getRefutation() != null && !clientInfo.getRefutation().isEmpty()) {
            infoLine.append(" refutation");
            for (Move move : clientInfo.getRefutation()) {
                infoLine.append(" ").append(UciMoveTranslator.moveToUci(move));
            }
        }


    }

    /**
     * Append given client info currline if not null
     *
     * @param infoLine target sb
     * @param clientInfo client info
     */
    private void appendClientInfoCurrLineCpu(StringBuilder infoLine, ClientInfo clientInfo) {

        if (clientInfo.getCurrLineCpu() != null && clientInfo.getCurrLineMoves() != null) {
            infoLine.append(" currline");
            infoLine.append(" cpunr ").append(clientInfo.getCurrLineCpu());

            for (Move move : clientInfo.getCurrLineMoves()) {
                infoLine.append(" ").append(UciMoveTranslator.moveToUci(move));
            }
        }

    }


    /**
     * Append given client info if not null
     *
     * @param infoLine target sb
     * @param key      name
     * @param value    value
     */
    private void appendClientInfo(StringBuilder infoLine, String key, Object value) {
        if (value != null) {
            infoLine.append(key).append(value);
        }
    }

    /**
     * Send the given command to the server
     */
    private void sendLineToWriter(String msg) {
        LOG.info("===> to UCI: {}", msg);
        uciConsoleWriter.println(msg);
    }


}
