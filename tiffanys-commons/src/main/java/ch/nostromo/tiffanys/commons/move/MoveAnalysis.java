package ch.nostromo.tiffanys.commons.move;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MoveAnalysis {

    double scoreChange;
    double scoreDiffToBest;

}
