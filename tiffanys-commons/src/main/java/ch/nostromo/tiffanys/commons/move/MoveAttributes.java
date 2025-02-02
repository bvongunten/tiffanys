package ch.nostromo.tiffanys.commons.move;

import ch.nostromo.tiffanys.commons.Side;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * Move Attributes given by an engine
 */
@Data
@AllArgsConstructor
@EqualsAndHashCode
public class MoveAttributes {


    // TODO: Map of attributes !?

    private Side side;
    private double score;

    private int mateIn;

    private int nodes;
    private int cutOffs;
    private int plannedDepth;
    private int maxDepth;
    private long timeMs;
    private List<Move> principalVariations;

    @Override
    public String toString() {

        StringBuilder result = new StringBuilder(this.getClass().getSimpleName() + " [Score=" + score + ", mateIn=" + mateIn + ", plannedDepth=" + plannedDepth + ", maxDepth=" + maxDepth + ", timeSpent=" + timeMs + ", nodes=" + nodes + ", cutOffs=" + cutOffs + "] ");
        if (principalVariations != null && !principalVariations.isEmpty()) {
            result.append(", pv=");

            for (Move pvMove : principalVariations) {
                result.append(pvMove.generateMoveDetailString()).append(" ");
            }

        }

        result.append("]");

        return result.toString();
    }


}
