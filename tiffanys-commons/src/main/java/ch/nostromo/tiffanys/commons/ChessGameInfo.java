package ch.nostromo.tiffanys.commons;

import lombok.Getter;
import lombok.Setter;

import java.text.SimpleDateFormat;
import java.util.Date;

@Getter
@Setter
public class ChessGameInfo implements Cloneable {

    String site;
    String date;
    String round;
    String whitePlayer;
    String blackPlayer;

    public ChessGameInfo(String whitePlayer, String blackPlayer, String site, String date, String round) {
        super();
        this.whitePlayer = whitePlayer;
        this.blackPlayer = blackPlayer;
        this.site = site;
        this.date = date;
        this.round = round;
    }

    public ChessGameInfo() {
    	this("Player White", "Player Black", "Site", new SimpleDateFormat("yyyy.MM.DD").format(new Date()), "1");
    }

    public ChessGameInfo clone() {
        try {
            return (ChessGameInfo) super.clone();
        } catch (CloneNotSupportedException ignore) {
            return null;
        }
    }

}
