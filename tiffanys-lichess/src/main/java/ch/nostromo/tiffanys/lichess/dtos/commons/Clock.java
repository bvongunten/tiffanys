package ch.nostromo.tiffanys.lichess.dtos.commons;

import lombok.Data;

@Data
public class Clock {

    Integer initial;
    Integer increment;
    Integer totalTime;

}
