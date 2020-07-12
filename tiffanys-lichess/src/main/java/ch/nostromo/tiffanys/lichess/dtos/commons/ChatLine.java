package ch.nostromo.tiffanys.lichess.dtos.commons;

import lombok.Data;

@Data
public class ChatLine {
    String type;
    String username;
    String text;
    Boolean room;
}
