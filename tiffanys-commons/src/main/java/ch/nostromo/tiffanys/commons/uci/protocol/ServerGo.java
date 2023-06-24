package ch.nostromo.tiffanys.commons.uci.protocol;

import ch.nostromo.tiffanys.commons.move.Move;
import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * Go line sent from server to client
 */
@Data
@ToString
public class ServerGo {
    List<Move> searchMoves;

    Integer timeWhite;
    Integer timeBlack;

    Integer increaseWhite;
    Integer increaseBlack;

    Integer movesToGo;

    Integer moveTime;

    Integer depth;
    Integer nodes;
    Integer mate;

    Boolean infinite;
    Boolean ponder;

}
