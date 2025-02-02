package ch.nostromo.tiffanys.engine.commons.events;

/**
 * Engine event listener
 */
public interface EngineEventListener {

	void handleEngineUpdate(EngineEvent event);

	void handleEngineFinished(EngineEvent event);

}
