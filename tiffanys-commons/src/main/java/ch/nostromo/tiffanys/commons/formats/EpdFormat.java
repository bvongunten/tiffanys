package ch.nostromo.tiffanys.commons.formats;

import lombok.Data;

import java.util.StringTokenizer;

/**
 * EPD splits and contains all epd parameters
 */
@Data
public class EpdFormat {

    protected String position;
    protected String sideToMove;
    protected String castling;
    protected String enPassant;

    protected String command = "";

    public EpdFormat(String epd) {
        StringTokenizer epdTokenizer = new StringTokenizer(epd, " ");
        position = epdTokenizer.nextToken();
        sideToMove = epdTokenizer.nextToken();
        castling = epdTokenizer.nextToken();
        enPassant = epdTokenizer.nextToken();

        StringBuilder commandSb = new StringBuilder();
        while (epdTokenizer.hasMoreTokens()) {
            commandSb.append(epdTokenizer.nextToken());
            commandSb.append(" ");
        }

        command = commandSb.toString().trim();

    }

    @Override
    public String toString() {
        return position + " " + sideToMove + " " + castling + " " + enPassant + " " + command;
    }

    public String getOpCommand(String opCode) {
        opCode = opCode + " ";
        int startPos = command.indexOf(opCode);
        if (startPos >= 0) {
            int semiColon = command.indexOf(";", startPos);
            if (semiColon > 0) {
                return command.substring(startPos + opCode.length(), semiColon);
            } else {
                return command.substring(startPos + opCode.length());
            }
        } else {
            return null;
        }
    }

    public FenFormat getFen() {
        return new FenFormat(position, sideToMove, castling, enPassant, 0, 0);
    }


}
