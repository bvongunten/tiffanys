package ch.nostromo.tiffanys.commons.epd;

import ch.nostromo.tiffanys.commons.fen.FenFormat;
import lombok.Getter;
import lombok.Setter;

import java.util.StringTokenizer;

@Getter
@Setter
public class EpdFormat {


    protected String position;
    protected String colorToMove;
    protected String castling;
    protected String enPassant;

    protected String command = "";

    public EpdFormat(String position, String colorToMove, String castling, String enPassant, String command) {
        this.position = position;
        this.colorToMove = colorToMove;
        this.castling = castling;
        this.enPassant = enPassant;
        this.command = command;
    }

    public EpdFormat(String epd) {
        StringTokenizer fenTokenizer = new StringTokenizer(epd, " ");
        position = fenTokenizer.nextToken();
        colorToMove = fenTokenizer.nextToken();
        castling = fenTokenizer.nextToken();
        enPassant = fenTokenizer.nextToken();

        while (fenTokenizer.hasMoreTokens()) {
            command += fenTokenizer.nextToken() + " ";
        }
        command = command.trim();

    }

    public String generateEpd() {
        return position + " " + colorToMove + " " + castling + " " + enPassant + " " + command;
    }

    @Override
    public String toString() {
        return generateEpd();
    }

    public String getCommand() {
        return command;
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

    public FenFormat toFenFormat() {
        return new FenFormat(position, colorToMove, castling, enPassant, 0, 0);
    }


}
