package ch.nostromo.tiffanys.commons.move;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class MoveAttributes {



    private double score;
    private int nodes;
    private int cutOffs;
    private int plannedDepth;
    private int maxDepth;
    private long timeMs;
    private List<Move> principalVariations;

    @Override
    public String toString() {

        String result = this.getClass().getSimpleName() + " [Score=" + score + ", plannedDepth=" + plannedDepth + ", maxDepth=" + maxDepth + ", timeSpent=" + timeMs + ", nodes=" + nodes + ", cutOffs=" + cutOffs;
        if (principalVariations != null && principalVariations.size() > 0) {
            result += ", pv=";

            for (Move pvMove : principalVariations) {
               result += pvMove.toString() + " ";
            }
        }

        result += "]";

        return result;
    }

}
