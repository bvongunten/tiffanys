package ch.nostromo.tiffanys.commons.engine;

/**
 * Engine event listener for async search
 */
public interface EngineListener {

	/**
	 * To be optionally fired every time a new depth is reached during search
	 * @param engineResult current engine result
	 */
	void onEngineUpdate(EngineResult engineResult);

	/**
	 * To be fired once when an engine result has been found
	 * @param engineResult final engine result
	 */
	void onEngineFinished(EngineResult engineResult);

}
