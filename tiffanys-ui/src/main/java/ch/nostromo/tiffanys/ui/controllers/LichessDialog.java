package ch.nostromo.tiffanys.ui.controllers;

import ch.nostromo.tiffanys.commons.ChessGame;
import ch.nostromo.tiffanys.commons.move.Move;
import ch.nostromo.tiffanys.lichess.tools.LichessBoardGameStateHelper;
import ch.nostromo.tiffanys.lichess.tools.LichessBoardGameStateHelperEventListener;
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
public class LichessDialog implements LichessBoardGameStateHelperEventListener {

    private enum Mode {
        WAIT_FOR_INITIAL_GAME, WAIT_FOR_MOVE;
    }

    @FXML
    private Label lblCommand;

    @FXML
    private Label lblMessage;

    private DialogFinishedState finishedState = DialogFinishedState.OK;

    private Mode mode;

    LichessBoardGameStateHelper lichessHelper;

    private ChessGame currentGame;


    private ChessGame chessGame;
    private Move move = null;

    public void prepare(ChessGame currentGame, LichessBoardGameStateHelper lichessHelper) {
        this.currentGame = currentGame;
        this.lichessHelper = lichessHelper;

        this.lichessHelper.getEventListeners().add(this);
    }

    public void waitForGame() {
        this.mode = Mode.WAIT_FOR_INITIAL_GAME;
        this.lblCommand.setText("Waiting for Lichess.org Game State");
        if (lichessHelper.isInitialized()) {
            this.chessGame = lichessHelper.getLichessGame();
            closeFrame();
        }
    }

    public void waitForMove() {
        this.mode = Mode.WAIT_FOR_MOVE;
        this.lblCommand.setText("Waiting for opponent to move on lichess.org");
        if (lichessHelper.getMove(currentGame) != null) {
            this.move = lichessHelper.getMove(currentGame);
            closeFrame();
        }
    }

    @FXML
    void onCancel(ActionEvent event) {
        finishedState = DialogFinishedState.CANCEL;
        closeFrame();
    }

    private void closeFrame() {
        Platform.runLater(() -> {
            this.lichessHelper.getEventListeners().clear();
            Stage stage = (Stage) lblCommand.getScene().getWindow();
            stage.close();
        });
    }


    @Override
    public void onChange() {
        Platform.runLater(() -> {
            // Fancy message ?!
            if (!lichessHelper.getLastReadGameState().equals("started")) {
                this.lblMessage.setText(lichessHelper.getLastReadGameState().getStatus());
            }
        });


        if (this.mode == Mode.WAIT_FOR_INITIAL_GAME) {
            this.chessGame = lichessHelper.getLichessGame();
            closeFrame();
        } else {
            move = lichessHelper.getMove(currentGame);
            if (move != null) {
                closeFrame();
            }
        }
    }

}
