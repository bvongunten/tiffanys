package ch.nostromo.tiffanys.engine.commons;

/**
 * Engine Exception
 */
public class EngineException extends Exception {

    public EngineException(String message, Exception e) {
        super(message, e);
    }

}
