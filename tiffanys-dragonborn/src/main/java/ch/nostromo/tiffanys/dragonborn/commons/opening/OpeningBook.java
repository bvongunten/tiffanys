package ch.nostromo.tiffanys.dragonborn.commons.opening;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import ch.nostromo.tiffanys.commons.ChessGame;
import ch.nostromo.tiffanys.commons.enums.Castling;
import ch.nostromo.tiffanys.commons.enums.Piece;
import ch.nostromo.tiffanys.commons.move.Move;

public class OpeningBook {

    List<String> openingLines = new ArrayList<>();

    Random r = new Random();

    public OpeningBook() {

    }

    public OpeningBook(String file) {

        try {
            InputStream in = getClass().getResourceAsStream(file);
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String sCurrentLine;

            while ((sCurrentLine = reader.readLine()) != null) {
                openingLines.add(sCurrentLine);
            }

        } catch (Exception e) {

        }
    }

    public Move getNextMove(ChessGame game) {
        String gameMoves = OpeningBookUtil.getMovesAsString(game);

        List<String> candidates = new ArrayList<>();

        for (String line : openingLines) {
            if (line.startsWith(gameMoves) && line.length() > gameMoves.length()) {
                candidates.add(line);
            }
        }

        if (candidates.size() > 0) {
            String line = candidates.get(r.nextInt(candidates.size()));

            line = line.substring(gameMoves.length());
            String nextMove = line.substring(0, line.indexOf("|"));

            if (nextMove.length() > 0) {
                String[] tokenz = nextMove.split(";");

                if (tokenz.length == 1) {
                    Castling castling = Castling.valueOf(nextMove);
                    return new Move(castling);
                } else if (tokenz.length == 2) {
                    return new Move(tokenz[0], tokenz[1]);
                } else if (tokenz.length == 3) {
                    Piece p = Piece.valueOf(tokenz[2]);
                    return new Move(tokenz[1], tokenz[2], p);
                }
            }
        }

        return null;

    }

}