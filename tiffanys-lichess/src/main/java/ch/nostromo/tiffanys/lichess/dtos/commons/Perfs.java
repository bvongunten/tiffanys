package ch.nostromo.tiffanys.lichess.dtos.commons;

import lombok.Data;

@Data
public class Perfs {

   PerfsGroup blitz;
   PerfsGroup bullet;
   PerfsGroup chess960;
   PerfsGroup puzzle;

}
