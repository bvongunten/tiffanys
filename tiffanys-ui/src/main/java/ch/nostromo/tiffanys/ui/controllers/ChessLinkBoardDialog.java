package ch.nostromo.tiffanys.ui.controllers;

import ch.nostromo.tiffanys.chesslink.ChessLinkBoardEventListener;
import ch.nostromo.tiffanys.commons.board.Board;
import ch.nostromo.tiffanys.commons.enums.GameColor;
import ch.nostromo.tiffanys.commons.move.Move;
import ch.nostromo.tiffanys.ui.TiffanysFxGuiCentral;
import ch.nostromo.tiffanys.ui.fx.DialogFinishedState;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChessLinkBoardDialog implements ChessLinkBoardEventListener {

    private enum Mode {
        WAIT_FOR_CLEAN_BOARD, WAIT_FOR_MOVE;
    }

    @FXML
    private Label lblCommand;

    private DialogFinishedState finishedState = DialogFinishedState.OK;

    private Board board = null;
    private String commandToShow = null;
    private boolean cableRight = true;
    private boolean flipped = false;

    private GameColor colorToMove;

    private Mode mode = null;

    private Move move = null;

    public void prepare(Board board, GameColor colorToMove, boolean flipped, boolean cableRight) {
        this.board = board;
        this.colorToMove = colorToMove;

        this.flipped = flipped;
        this.cableRight = cableRight;
    }

    public void updateChessLinkBoard() {
        this.mode = Mode.WAIT_FOR_CLEAN_BOARD;
        startFlow();
    }


    public void getMoveFromChessLinkBoard() {
        this.mode = Mode.WAIT_FOR_MOVE;
        startFlow();
    }

    private void startFlow() {
        Platform.runLater(() -> {
            this.lblCommand.setText(TiffanysFxGuiCentral.getInstance().getResourceBundle().getString("label.chesslink.applyPositionToChessLinkBoard"));
            TiffanysFxGuiCentral.getInstance().getOrCreateChesslinkBoard(cableRight).getChessLinkBoardEventListeners().add(this);
            TiffanysFxGuiCentral.getInstance().getOrCreateChesslinkBoard(cableRight).executePositionToBoard(board);
        });
    }


    @FXML
    public void onResetBoard() {
        Platform.runLater(() -> {
            TiffanysFxGuiCentral.getInstance().connectToChessLinkBoard(cableRight);
            startFlow();
        });
    }


    @FXML
    void onCancel(ActionEvent event) {
        finishedState = DialogFinishedState.CANCEL;
        closeFrame();
    }

    private void closeFrame() {
        Platform.runLater(() -> {
            TiffanysFxGuiCentral.getInstance().disconnectFromChessLinkBoard();
            Stage stage = (Stage) lblCommand.getScene().getWindow();
            stage.close();
        });
    }

    @Override
    public void onChessLinkBoardSetupExecuted() {
        Platform.runLater(() -> {
            finishedState = DialogFinishedState.OK;
            if (mode == Mode.WAIT_FOR_CLEAN_BOARD) {
                closeFrame();
            } else {
                TiffanysFxGuiCentral.getInstance().getOrCreateChesslinkBoard(cableRight).setBoardToAwaitMove(board, colorToMove);
                this.lblCommand.setText(TiffanysFxGuiCentral.getInstance().getResourceBundle().getString("label.chesslink.enterMove"));
            }
        });
    }

    @Override
    public void onChessLinkBoardMoveEntered(Move move) {
        this.move = move;
        finishedState = DialogFinishedState.OK;
        closeFrame();
    }

    @Override
    public void onChessLinkBoardInitialized() { }


    @Override
    public void onChessLinkBoardMoveExecuted() { }


    @Override
    public void onChessLinkBoardSetupEntered(Board board) { }

}
