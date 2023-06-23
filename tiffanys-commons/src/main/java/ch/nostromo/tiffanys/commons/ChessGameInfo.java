package ch.nostromo.tiffanys.commons;

import lombok.Getter;
import lombok.Setter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

@Getter
@Setter
public class ChessGameInfo implements Cloneable {

    // 7 Tags roster
    String event;
    String site;
    String date;
    String round;
    String whitePlayer;
    String blackPlayer;

    Map<String, String> optionalTags = new LinkedHashMap<>();



    public ChessGameInfo(String whitePlayer, String blackPlayer, String event, String site, String date, String round, Map<String, String> optionalTags) {
        super();
        this.whitePlayer = whitePlayer;
        this.blackPlayer = blackPlayer;
        this.event = event;
        this.site = site;
        this.date = date;
        this.round = round;
        this.optionalTags = optionalTags;
    }

    public ChessGameInfo() {
    	this("Player White", "Player Black", "Event",  "Site", new SimpleDateFormat("yyyy.MM.DD").format(new Date()), "1", new LinkedHashMap<>());
    }

    public ChessGameInfo clone() {
        try {
            return (ChessGameInfo) super.clone();
        } catch (CloneNotSupportedException ignore) {
            return null;
        }
    }

}
