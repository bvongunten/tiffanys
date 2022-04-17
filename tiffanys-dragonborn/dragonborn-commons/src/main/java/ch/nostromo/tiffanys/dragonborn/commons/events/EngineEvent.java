package ch.nostromo.tiffanys.dragonborn.commons.events;

import ch.nostromo.tiffanys.dragonborn.commons.EngineResult;

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
