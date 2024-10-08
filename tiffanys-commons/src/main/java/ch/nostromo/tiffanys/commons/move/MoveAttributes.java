package ch.nostromo.tiffanys.commons.move;

import ch.nostromo.tiffanys.commons.enums.GameColor;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class MoveAttributes {


    private GameColor colorToMove;
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

        String result = this.getClass().getSimpleName() + " [Score=" + score + ", mateIn=" + mateIn + ", plannedDepth=" + plannedDepth + ", maxDepth=" + maxDepth + ", timeSpent=" + timeMs + ", nodes=" + nodes + ", cutOffs=" + cutOffs + "] ";
        if (principalVariations != null && principalVariations.size() > 0) {
            result += ", pv=";

            for (Move pvMove : principalVariations) {
                result += pvMove.generateMoveDetailString() + " ";
            }

        }

        result += "]";

        return result;
    }


}
