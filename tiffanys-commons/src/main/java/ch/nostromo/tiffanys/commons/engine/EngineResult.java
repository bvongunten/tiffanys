package ch.nostromo.tiffanys.commons.engine;

import ch.nostromo.tiffanys.commons.move.Move;
import lombok.Data;

/**
 * Engine result
 */
@Data
public class EngineResult {

    /**
     * Best move found
     */
    private Move selectedMove;

    /**
     * Computation time
     */
    private long totalTimeInMs;

}
