package ch.nostromo.tiffanys.commons.move;

import ch.nostromo.tiffanys.commons.Side;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Move Attributes given by an engine
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class MoveAttributes {


    private Side side;

    private double score;

    private int mateIn;

    private long nodes;

    private int depth;

    private long timeMs;

    private List<Move> principalVariations;

    @Override
    public String toString() {

        StringBuilder result = new StringBuilder(this.getClass().getSimpleName() + " [Score=" + score + ", mateIn=" + mateIn + ", depth=" + depth + ", timeSpent=" + timeMs + ", nodes=" + nodes + "] ");
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
