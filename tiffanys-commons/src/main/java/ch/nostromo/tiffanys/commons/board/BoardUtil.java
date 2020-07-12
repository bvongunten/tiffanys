package ch.nostromo.tiffanys.commons.board;

import ch.nostromo.tiffanys.commons.enums.GameColor;
import ch.nostromo.tiffanys.commons.fields.Field;
import lombok.experimental.UtilityClass;

@UtilityClass
public class BoardUtil {

  
    public static String dumpBoard(Board board) {
        String result = "";
        result += dumpRow(board, 91, 98);
        result += dumpRow(board, 81, 88);
        result += dumpRow(board, 71, 78);
        result += dumpRow(board, 61, 68);
        result += dumpRow(board, 51, 58);
        result += dumpRow(board, 41, 48);
        result += dumpRow(board, 31, 38);
        result += dumpRow(board, 21, 28);
        result += "  A  B  C  D  E  F  G  H  ";

        return result;
    }

    private static String dumpRow(Board board, int beg, int end) {
        String result = ((beg - 11) / 10) + " ";
        for (int i = beg; i <= end; i++) {

            Field field = board.getFields()[i];

            if (field.getPiece() == null) {
                result += "[] ";
            } else {
                if (field.getPieceColor() == GameColor.WHITE) {
                    result += "W";
                } else {
                    result += "B";
                }
                result += field.getPiece().getPieceCharCode();
                result += " ";
            }

        }
        return result + "\n";
    }

    public static int coordToField(String coord) {
        int result = 0;
        if (coord.substring(0, 1).equalsIgnoreCase("A")) {
            result = 21;
        } else if (coord.substring(0, 1).equalsIgnoreCase("B")) {
            result = 22;
        } else if (coord.substring(0, 1).equalsIgnoreCase("C")) {
            result = 23;
        } else if (coord.substring(0, 1).equalsIgnoreCase("D")) {
            result = 24;
        } else if (coord.substring(0, 1).equalsIgnoreCase("E")) {
            result = 25;
        } else if (coord.substring(0, 1).equalsIgnoreCase("F")) {
            result = 26;
        } else if (coord.substring(0, 1).equalsIgnoreCase("G")) {
            result = 27;
        } else if (coord.substring(0, 1).equalsIgnoreCase("H")) {
            result = 28;
        } else {
            throw new IllegalArgumentException("Unknown collumn: " + coord.substring(0, 1));
        }

        String rowStr = coord.substring(1, 2);
        int row = Integer.valueOf(rowStr).intValue() - 1;

        if (row < 0 || row > 7) {
            throw new IllegalArgumentException("Unknown row: " + rowStr);
        }

        result += (row * 10);
        return result;

    }

    public static String fieldToCoord(int field) {
        String result = "";

        int tmp = (field / 10);
        tmp = tmp * 10;
        if (field - tmp == 1) {
            result += "a";
        } else if (field - tmp == 2) {
            result += "b";
        } else if (field - tmp == 3) {
            result += "c";
        } else if (field - tmp == 4) {
            result += "d";
        } else if (field - tmp == 5) {
            result += "e";
        } else if (field - tmp == 6) {
            result += "f";
        } else if (field - tmp == 7) {
            result += "g";
        } else if (field - tmp == 8) {
            result += "h";
        } else {
            throw new IllegalArgumentException("Invalid field: " + field);
        }

        if (field > 20 && field < 29) {
            result += "1";
        } else if (field > 30 && field < 39) {
            result += "2";
        } else if (field > 40 && field < 49) {
            result += "3";
        } else if (field > 50 && field < 59) {
            result += "4";
        } else if (field > 60 && field < 69) {
            result += "5";
        } else if (field > 70 && field < 79) {
            result += "6";
        } else if (field > 80 && field < 89) {
            result += "7";
        } else if (field > 90 && field < 99) {
            result += "8";
        } else {
            throw new IllegalArgumentException("Invalid field: " + field);
        }

        return result;
    }

    
    
}
