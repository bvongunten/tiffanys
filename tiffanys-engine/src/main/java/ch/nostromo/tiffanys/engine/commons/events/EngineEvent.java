package ch.nostromo.tiffanys.engine.commons.events;

import ch.nostromo.tiffanys.engine.commons.EngineResult;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Engine Event containing a current engine result
 */
@Data
@AllArgsConstructor
public class EngineEvent {

    private EngineResult engineResult;

}
