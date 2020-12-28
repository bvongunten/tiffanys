package ch.nostromo.tiffanys.ui.controllers;

import ch.nostromo.tiffanys.commons.ChessGame;
import ch.nostromo.tiffanys.commons.ChessGameInfo;
import ch.nostromo.tiffanys.commons.enums.GameColor;
import ch.nostromo.tiffanys.commons.enums.GameState;
import ch.nostromo.tiffanys.commons.enums.Piece;
import ch.nostromo.tiffanys.commons.fen.FenFormat;
import ch.nostromo.tiffanys.commons.move.Move;
import ch.nostromo.tiffanys.commons.pgn.PgnFormat;
import ch.nostromo.tiffanys.commons.pgn.PgnUtil;
import ch.nostromo.tiffanys.commons.rules.RulesUtil;
import ch.nostromo.tiffanys.commons.uci.UciMoveTranslator;
import ch.nostromo.tiffanys.dragonborn.commons.AbstractEngine;
import ch.nostromo.tiffanys.dragonborn.commons.EngineSettings;
import ch.nostromo.tiffanys.dragonborn.commons.events.EngineEvent;
import ch.nostromo.tiffanys.dragonborn.commons.events.EngineEventListener;
import ch.nostromo.tiffanys.dragonborn.commons.opening.OpeningBook;
import ch.nostromo.tiffanys.dragonborn.engine.DragonbornEngine;
import ch.nostromo.tiffanys.lichess.streams.BoardGameStateStream;
import ch.nostromo.tiffanys.lichess.tools.LichessBoardGameStateHelper;
import ch.nostromo.tiffanys.ui.TiffanysFxGuiCentral;
import ch.nostromo.tiffanys.ui.fx.board.BoardPane;
import ch.nostromo.tiffanys.ui.fx.board.BoardPaneEvents;
import ch.nostromo.tiffanys.ui.fx.enginestate.EngineStatePane;
import ch.nostromo.tiffanys.ui.fx.pgn.PgnPane;
import ch.nostromo.tiffanys.ui.utils.frontends.Frontends;
import ch.nostromo.tiffanys.ui.utils.game.AppGameSettings;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Logger;

public class ChessWindow implements Initializable, BoardPaneEvents, EngineEventListener {

    protected Logger LOG = Logger.getLogger(getClass().getName());

    @FXML
    private BorderPane gamePane;

    @FXML
    private SplitPane infoPaneSplit;

    ChessGame game;

    BoardPane boardPane;
    AbstractEngine engineWhite;
    AbstractEngine engineBlack;
    AbstractEngine engineHint;
    AbstractEngine currentEngine;

    AppGameSettings appGameSettings;

    ResourceBundle bundle;

    private PgnPane pgnPane;
    private EngineStatePane engineState;


    LichessBoardGameStateHelper lichessHelper;

    @Override
    public void initialize(URL arg0, ResourceBundle bundle) {
        this.bundle = bundle;

        // Chess Board Visualization
        boardPane = new BoardPane();
        boardPane.initialize();
        boardPane.addBoardPaneListener(this);
        boardPane.setClickMovesAllowed(true);

        gamePane.setCenter(boardPane);

        // PGN & Stats
        this.pgnPane = new PgnPane();
        this.engineState = new EngineStatePane();

        infoPaneSplit.getItems().add(pgnPane);
        infoPaneSplit.getItems().add(engineState);

    }

    public void setChessGame(ChessGame game, AppGameSettings appGameSettings) throws IOException {
        this.game = game;
        this.appGameSettings = appGameSettings;


        // Initialize engines if needed
        if (appGameSettings.getPlayerTypeWhite() == AppGameSettings.PlayerType.CPU) {
            this.engineWhite = new DragonbornEngine(appGameSettings.getEngineSettingsWhite(), new OpeningBook("/opening.txt"));
            engineWhite.addEventListener(this);
        } else {
            engineWhite = null;
        }

        if (appGameSettings.getPlayerTypeBlack() == AppGameSettings.PlayerType.CPU) {
            this.engineBlack = new DragonbornEngine(appGameSettings.getEngineSettingsBlack(), new OpeningBook("/opening.txt"));
            engineBlack.addEventListener(this);
        } else {
            engineBlack = null;
        }

        this.engineHint = new DragonbornEngine(new EngineSettings(), new OpeningBook("/opening.txt"));
        this.engineHint.addEventListener(this);

        if (appGameSettings.isLichessGame()) {
            String gameId = appGameSettings.getLichessGameId();
            LOG.info("Starting lichess game state stream for game id " + gameId);

            BoardGameStateStream stream = TiffanysFxGuiCentral.getInstance().connectToLichess().getBoardGameStateStream(gameId);
            this.lichessHelper = new LichessBoardGameStateHelper(game.getInitialFen(), stream);
            this.lichessHelper.start();

            Bounds bounds = engineState.localToScreen(engineState.getBoundsInLocal());
            ChessGame lichessGame = TiffanysFxGuiCentral.getInstance().showLichessGetGameDialog(game, lichessHelper, bounds);

            this.game.resetToInitialFen();
            for (Move move : lichessGame.getHistoryMoves()) {
                game.applyMove(move);
            }
        }

        this.updatePosition(null);

        if (appGameSettings.isUseChessLinkBoard()) {
            orderChessLinkBoardToUpdate();
        }

        if (isEngineTurn()) {
            calculateEngineMove();
        } else {
            awaitMoveInput();
        }


    }

    private void orderChessLinkBoardToUpdate() {
        Bounds bounds = engineState.localToScreen(engineState.getBoundsInLocal());

        boolean success = TiffanysFxGuiCentral.getInstance().showChessLinkPositionToMake(this.game.getCurrentBoard(), appGameSettings.isOneHumanPlayerAndBlack(), appGameSettings.isChessLinkCableRight(), bounds);

        if (!success) {
            LOG.info("Set chesslink mode to off, as the board has not been set up accordingly.");
            appGameSettings.setUseChessLinkBoard(false);
        }
    }

    private boolean isNextPlayerLichess() {
        if (game.getColorToMove() == GameColor.BLACK) {
            return appGameSettings.getPlayerTypeBlack() == AppGameSettings.PlayerType.LICHESS_OPPONENT;
        } else {
            return appGameSettings.getPlayerTypeWhite() == AppGameSettings.PlayerType.LICHESS_OPPONENT;
        }
    }

    private boolean isCPUInvolved() {
        return appGameSettings.getPlayerTypeBlack() == AppGameSettings.PlayerType.CPU || appGameSettings.getPlayerTypeWhite() == AppGameSettings.PlayerType.CPU;
    }

    private void awaitMoveInput() {
        LOG.info("Awaiting manual input from ui, chesslink board, or lichess opponent...");

        if (appGameSettings.isLichessGame() && isNextPlayerLichess()) {
            Bounds bounds = engineState.localToScreen(engineState.getBoundsInLocal());
            Move move = TiffanysFxGuiCentral.getInstance().showLichessGetMoveDialog(game, lichessHelper, bounds);
            if (move != null) {
                handleFinishedMove(move);
            }
        } else if (appGameSettings.isUseChessLinkBoard()) {
            Bounds bounds = engineState.localToScreen(engineState.getBoundsInLocal());
            Move move = TiffanysFxGuiCentral.getInstance().showChessLinkMakeMove(game.getCurrentBoard(), game.getCurrentColorToMove(), appGameSettings.isOneHumanPlayerAndBlack(), appGameSettings.isChessLinkCableRight(), bounds);
            if (move != null) {
                handleFinishedMove(move);
            }
        }
    }

    private void handleFinishedMove(Move move) {
        LOG.info("Computing move : " + move);

        game.applyMove(move);

        // Promote to ui
        updatePosition(move);

        // Promote to possible chesslink board
        if (appGameSettings.isUseChessLinkBoard()) {
            orderChessLinkBoardToUpdate();
        }


        // Promote to possible lichess user (but only if not re-received from lichess)
        if (appGameSettings.isLichessGame()) {
            executeMoveToLichess(move);
        }

        if (game.getCurrentGameState() != GameState.GAME_OPEN) {
            showGameEndDialog();
        } else {
            if (isEngineTurn()) {
                calculateEngineMove();
            } else {
                awaitMoveInput();
            }
        }

    }

    private void updatePosition(Move lastMove) {

        boardPane.setBoard(game.getCurrentBoard(), appGameSettings.isOneHumanPlayerAndBlack());

        if (lastMove != null) {
            if (lastMove.isCastling()) {
                boardPane.setFieldMarked(lastMove.getCastling().getFromKing());
                boardPane.setFieldMarked(lastMove.getCastling().getToKing());
            } else {
                boardPane.setFieldMarked(lastMove.getFrom());
                boardPane.setFieldMarked(lastMove.getTo());
            }
        }

        PgnFormat pgn = PgnUtil.game2pgn(game);
        pgnPane.setPgnText(pgn.getPgnMoves().replaceAll("[\n\r]", ""));

        List<Move> moves = RulesUtil.getLegalMoves(game.getCurrentBoard(), game.getCurrentColorToMove());
        List<Integer> possibleDragSourceFields = new ArrayList<>();

        for (Move move : moves) {
            if (move.isCastling()) {
                possibleDragSourceFields.add(move.getCastling().getFromKing());
            } else {
                possibleDragSourceFields.add(move.getFrom());
            }
        }

        boardPane.setPossibleMoveSourcePoints(possibleDragSourceFields);

    }

    private void showGameEndDialog() {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(bundle.getString("messages.gameOver"));
        alert.setHeaderText(null);

        switch (game.getCurrentGameState()) {
            case GAME_OPEN:
                break;
            case REMIS:
                alert.setContentText(bundle.getString("messages.remis"));
                break;
            case REMIS_BY_FIFTY:
                alert.setContentText(bundle.getString("messages.remisByFifty"));
                break;
            case REMIS_BY_MATERIAL:
                alert.setContentText(bundle.getString("messages.remisByMaterial"));
                break;
            case REMIS_BY_STALE_MATE:
                alert.setContentText(bundle.getString("messages.remisByStaleMate"));
                break;
            case REMIS_BY_THREE:
                alert.setContentText(bundle.getString("messages.remisByThree"));
                break;
            case WIN_BLACK_MATES:
                alert.setContentText(bundle.getString("messages.blackMates"));
                break;
            case WIN_WHITE_MATES:
                alert.setContentText(bundle.getString("messages.whiteMates"));
                break;
            default:
                break;

        }

        alert.showAndWait();

    }

    // ***************************************** FX BOARD LISTENERS *****************************************

    @Override
    public void interactionAborted() {
        updatePosition(null);
    }

    @Override
    public void fireClickMoveStarted(int fieldIdx) {
        manualMoveEntryStarted(fieldIdx);
    }

    @Override
    public void fireClickMoveFinished(int fromFieldIdx, int toFieldIdx) {
        manualMoveEntryFinished(fromFieldIdx, toFieldIdx);
    }

    @Override
    public void fireDragMoveStarted(int fieldIdx) {
        manualMoveEntryStarted(fieldIdx);
    }

    @Override
    public void fireDragMoveFinished(int fromFieldIdx, int toFieldIdx) {
        manualMoveEntryFinished(fromFieldIdx, toFieldIdx);
    }

    @Override
    public void rightClick(int fieldIdx) {
        // Nothing
    }

    @Override
    public void leftclick(int fieldIdx) {
        // Nothing
    }

    @Override
    public void doubleClick(int fieldIdx) {
        // Nothing
    }


    private void manualMoveEntryStarted(int fieldIdx) {
        List<Move> moves = RulesUtil.getLegalMoves(game.getCurrentBoard(), game.getCurrentColorToMove());
        List<Integer> possibleTargetFields = new ArrayList<>();

        for (Move move : moves) {
            if (move.isCastling()) {
                if (move.getCastling().getFromKing() == fieldIdx) {
                    possibleTargetFields.add(move.getCastling().getToKing());
                }
            } else if (move.isPromotion()) {
                if (move.getFrom() == fieldIdx) {
                    possibleTargetFields.add(move.getTo());
                }
            } else {
                if (move.getFrom() == fieldIdx) {
                    possibleTargetFields.add(move.getTo());
                }
            }
        }

        boardPane.setPossibleMoveTargetTiles(possibleTargetFields);

        // Mark the from field
        boardPane.setFieldMarked(fieldIdx);

        // Mark the possible to fields
        for (Integer targetIdx : possibleTargetFields) {
            boardPane.setFieldAsTarget(targetIdx);
        }
    }

    private void manualMoveEntryFinished(int fromFieldIdx, int toFieldIdx) {
        List<Move> moves = RulesUtil.getLegalMoves(game.getCurrentBoard(), game.getCurrentColorToMove());

        boolean isPromotion = false;
        for (Move move : moves) {
            if (move.getFrom() == fromFieldIdx && move.getTo() == toFieldIdx && move.isPromotion()) {
                isPromotion = true;
            }
        }

        Move move;
        if (isPromotion) {
            List<String> choices = new ArrayList<>();
            choices.add("Queen");
            choices.add("Rook");
            choices.add("Bishop");
            choices.add("Knight");

            ChoiceDialog<String> dialog = new ChoiceDialog<>("Queen", choices);
            dialog.setTitle("Pawn promotion");
            dialog.setHeaderText(null);
            dialog.setContentText("");
            dialog.initStyle(StageStyle.UTILITY);

            // Traditional way to get the response value.
            Optional<String> result = dialog.showAndWait();

            String selection = "Queen";
            if (result.isPresent()) {
                selection = result.get();
            }

            Piece promotion = Piece.QUEEN;

            if (selection.equals("Rook")) {
                promotion = Piece.ROOK;
            } else if (selection.equals("Bishop")) {
                promotion = Piece.BISHOP;
            } else if (selection.equals("Knight")) {
                promotion = Piece.KNIGHT;
            }

            move = new Move(fromFieldIdx, toFieldIdx, promotion);

        } else {
            move = Frontends.fxGuiToMove(game.getCurrentBoard(), fromFieldIdx, toFieldIdx);
        }

        handleFinishedMove(move);

    }


    // ***************************************** ENGINE LISTENERS *****************************************

    @Override
    public void engineUpdateEventOccured(EngineEvent event) {
        Platform.runLater(() -> {
            this.engineState.addEngineUpdate(event);
        });
    }

    @Override
    public void engineFinishedEventOccured(EngineEvent event) {

        Platform.runLater(() -> {
            this.engineState.addEngineUpdate(event);

            currentEngine = null;

            Move selectedMove = event.getEngineResult().getSelectedMove();

            handleFinishedMove(selectedMove);

        });

    }

    private boolean isEngineTurn() {
        return game.getCurrentGameState() == GameState.GAME_OPEN
                && ((game.getCurrentColorToMove() == GameColor.WHITE && engineWhite != null)
                || (game.getCurrentColorToMove() == GameColor.BLACK && engineBlack != null));
    }

    private AbstractEngine getEngineForColor(GameColor color) {
        if (color == GameColor.WHITE && engineWhite != null) {
            return engineWhite;
        } else if (color == GameColor.BLACK && engineBlack != null) {
            return engineBlack;
        } else {
            return engineHint;
        }

    }

    private void calculateEngineMove() {
        currentEngine = getEngineForColor(game.getCurrentColorToMove());
        currentEngine.asyncScoreMoves(game);
    }


    // ***************************************** LICHESS LISTENERS  *****************************************


    private void executeMoveToLichess(Move move) {
        if (appGameSettings.isLichessGame()) {
            if (lichessHelper.getMovesKnownByLichess().contains(move)) {
                LOG.info("Ingnoring move " + move + " as it is already kown by Lichess");
            } else {
                TiffanysFxGuiCentral.getInstance().connectToLichess().makeMove(appGameSettings.getLichessGameId(), UciMoveTranslator.moveToUciString(move));
            }
        }
    }
    // ***************************************** WINDOW ACTIONS  *****************************************


    @FXML
    void onActionGameMoveNow(ActionEvent event) {
        onActionMoveNow(event);
    }

    @FXML
    synchronized void onActionMoveNow(ActionEvent event) {
        if (currentEngine != null && currentEngine.isRunning()) {
            currentEngine.halt();
        } else {
            // So trigger a cpu move as a hint
            calculateEngineMove();
        }
    }

    @FXML
    void onEdit(ActionEvent event) throws IOException {
        FenFormat newFen = TiffanysFxGuiCentral.getInstance().showEditBoardForm(this.game.getCurrentFenFormat());

        if (newFen != null) {
            this.game = new ChessGame(game.getGameInfo(), newFen);
            this.updatePosition(null);
        }

    }

    @FXML
    void actionFileClose(ActionEvent event) throws IOException {
        // get a handle to the stage
        Stage stage = (Stage) this.gamePane.getScene().getWindow();
        stage.close();

        TiffanysFxGuiCentral.getInstance().disconnectFromChessLinkBoard();
        TiffanysFxGuiCentral.getInstance().showMainMenuForm();
    }

    @FXML
    void onActionFileNewGame(ActionEvent event) throws IOException {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText("Are you sure?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            this.setChessGame(new ChessGame(), appGameSettings);
        }

    }

    @FXML
    void onActionFileSaveGame(ActionEvent event) {
        //TODO: Implement
    }

    @FXML
    void onActionFileLoadGame(ActionEvent event) {
        //TODO: Implement
    }

    @FXML
    void onActionGameSettings(ActionEvent event) throws IOException {
        TiffanysFxGuiCentral.getInstance().showSettingsForm(appGameSettings);
    }

    @FXML
    void onActionGameHint(ActionEvent event) {
        calculateEngineMove();
    }

    @FXML
    void onActionGamePosition(ActionEvent event) throws IOException {
        FenFormat fen = TiffanysFxGuiCentral.getInstance().showEditBoardForm(game.getCurrentFenFormat());
        ChessGame nextGame = new ChessGame(new ChessGameInfo(), fen);

        this.setChessGame(nextGame, appGameSettings);

    }

    @FXML
    void actionHelpAbout(ActionEvent event) {

    }

}
