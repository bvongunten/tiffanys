package ch.nostromo.tiffanys.engine.commons.events;

public interface EngineEventListener {

	public void engineUpdateEventOccured(EngineEvent event);

	public void engineFinishedEventOccured(EngineEvent event);

}
