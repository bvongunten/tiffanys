package ch.nostromo.tiffanys.lichess.dtos.board;

import lombok.Data;

@Data
public class BoardGameFullPlayer {
    String id;
    String name;
    String title;
    Boolean provisional;
    Integer rating;
}
