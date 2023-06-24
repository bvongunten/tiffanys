package ch.nostromo.tiffanys.commons.uci.utils;


import lombok.AllArgsConstructor;

import java.util.Scanner;

/**
 * Thread that scans on System.in for lines and provides them to a listener
 */
@AllArgsConstructor
public class UciConsoleScanner extends Thread {

    private UciConsoleScannerListener listener;

    @Override
    public void run() {
        Scanner sc = new Scanner(System.in);

        while (!isInterrupted()) {
            if (sc.hasNextLine()) {
                String line = sc.nextLine();
                if (listener != null && !line.trim().isEmpty()) {
                    listener.handleConsoleInput(line);
                }
            }
        }

    }

}
