package ch.nostromo.tiffanys.uci.controller;

import lombok.Data;

import java.util.logging.Logger;

@Data
public class UciOptions {

    private static Logger logger = Logger.getLogger(UciController.class.getName());

    private boolean debugMode = false;

	private String engine = "Dragonborn";
	private String engine_flag = "Tiffanys Dragonborn Engine";
	private String engine_options = "option name " + engine_flag + " type combo default " + engine + " var Robust var Dragonborn";

	private int threads = Runtime.getRuntime().availableProcessors();
	private String threads_flag = "Tiffanys Threads";
	private String threads_options = "option name " + threads_flag + " type spin default " + threads + " min 1 max " + threads;

	public String getOptions() {
		return engine_options + "\n"+ threads_options + "\n";
	}

	public void setOption(String cmd) {
	    String seek = "setoption name ";

	    if (cmd.startsWith(seek + engine_flag)) {
	        this.engine = cmd.substring(seek.length() + engine_flag.length());
	        logger.fine("Set engine to " + this.engine);

	    } else if (cmd.startsWith(seek + threads_flag)) {
	        this.threads = Integer.valueOf(cmd.substring(seek.length() + threads_flag.length()));
	        logger.fine("Set threads to " + this.engine);
	    }
	}

}
