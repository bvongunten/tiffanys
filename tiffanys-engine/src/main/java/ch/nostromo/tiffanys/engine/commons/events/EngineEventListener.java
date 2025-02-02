package ch.nostromo.tiffanys.engine.commons.events;

public interface EngineEventListener {

	void engineUpdateEventOccured(EngineEvent event);

	void engineFinishedEventOccured(EngineEvent event);

}
