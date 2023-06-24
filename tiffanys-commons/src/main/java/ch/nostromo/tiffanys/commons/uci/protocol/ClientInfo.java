package ch.nostromo.tiffanys.commons.uci.protocol;

import ch.nostromo.tiffanys.commons.move.Move;
import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * Info line information sent from client to server
 */
@Data
@ToString
public class ClientInfo {

    Integer depth;

    Integer seldepth;

    Move currentMove;

    Integer currentMoveNumber;

    Integer scoreMate;

    Integer scoreCp;

    Boolean scoreLowerBound;

    Boolean scoreUpperBound;

    Long time;

    Long nodes;

    Integer nps;

    List<Move> pv;

    Integer multiPv;

    Integer hashFull;

    Integer cpuload;

    Integer tbhits;

    Integer sbhits;

    String stringMessage;

    List<Move> refutation;

    Integer currLineCpu;

    List<Move> currLineMoves;


}
