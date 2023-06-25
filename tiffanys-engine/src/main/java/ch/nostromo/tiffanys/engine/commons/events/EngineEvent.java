package ch.nostromo.tiffanys.engine.commons.events;

import ch.nostromo.tiffanys.engine.commons.EngineResult;

public class EngineEvent {

	private EngineResult engineResult;

	public EngineEvent(EngineResult engineResult) {
		this.engineResult = engineResult;
	}

	public EngineResult getEngineResult() {
		return engineResult;
	}

	public void setEngineResult(EngineResult engineResult) {
		this.engineResult = engineResult;
	}




}
