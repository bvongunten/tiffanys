package ch.nostromo.tiffanys.commons.uci.protocol;

import ch.nostromo.tiffanys.commons.ChessGame;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Position line sent from server to client
 */
@Data
@ToString
@AllArgsConstructor
@EqualsAndHashCode
public class ServerPosition {

    public enum PositionType {STARTPOS, FEN}

    PositionType positionType;
    ChessGame chessGame;


}
