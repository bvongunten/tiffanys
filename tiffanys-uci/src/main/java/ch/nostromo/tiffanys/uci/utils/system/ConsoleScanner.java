package ch.nostromo.tiffanys.uci.utils.system;

import java.util.Scanner;

public class ConsoleScanner extends Thread {

	private ConsoleScannerListener listener;
	
	public ConsoleScanner(ConsoleScannerListener listener) {
		this.listener = listener;
	}

	@Override
	public void run() {
		Scanner sc = new Scanner(System.in);
		
        while (!this.isInterrupted()) {
        	listener.handleInput(sc.nextLine());
		}
		
		sc.close();
	}

}
