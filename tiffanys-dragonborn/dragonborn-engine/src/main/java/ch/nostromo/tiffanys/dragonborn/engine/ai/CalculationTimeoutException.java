package ch.nostromo.tiffanys.dragonborn.engine.ai;

public class CalculationTimeoutException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public CalculationTimeoutException(String msg) {
        super(msg);
    }
}
