package ch.nostromo.tiffanys.commons;

import lombok.Data;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Game information.
 */
@Data
public class ChessGameInformation implements Cloneable {

    // 7 Tags roster
    String event;
    String site;
    String date;
    String round;
    String whitePlayer;
    String blackPlayer;

    Map<String, String> optionalTags = new LinkedHashMap<>();

    public ChessGameInformation(String whitePlayer, String blackPlayer, String event, String site, String date, String round, Map<String, String> optionalTags) {
        this.whitePlayer = whitePlayer;
        this.blackPlayer = blackPlayer;
        this.event = event;
        this.site = site;
        this.date = date;
        this.round = round;
        this.optionalTags = optionalTags;
    }

    public ChessGameInformation() {
        this("Player White", "Player Black", "Event", "Site", new SimpleDateFormat("yyyy.MM.DD").format(new Date()), "1", new LinkedHashMap<>());
    }

    @Override
    public ChessGameInformation clone() {
        try {
            ChessGameInformation result = (ChessGameInformation) super.clone();
            result.optionalTags = new LinkedHashMap<>(optionalTags);
            return result;
        } catch (CloneNotSupportedException ignore) {
            throw new ChessGameException("Unable to clone GameInformation");
        }
    }

}
