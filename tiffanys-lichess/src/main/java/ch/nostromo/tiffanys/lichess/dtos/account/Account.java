package ch.nostromo.tiffanys.lichess.dtos.account;

import ch.nostromo.tiffanys.lichess.dtos.commons.Count;
import ch.nostromo.tiffanys.lichess.dtos.commons.Perfs;
import lombok.Data;

@Data
public class Account {

    String id;
    String username;
    String title;
    Boolean online;
    Boolean playing;
    Boolean streaming;
    Long createdAt;
    Long seenAt;

    Long nbFollowers;
    Long nbFollowing;
    Double completionRate;

    String language;

    Boolean patron;
    Boolean disabled;
    Boolean engine;
    Boolean booster;

    AccountProfile profile;
    Count count;
    Perfs perfs;
}
