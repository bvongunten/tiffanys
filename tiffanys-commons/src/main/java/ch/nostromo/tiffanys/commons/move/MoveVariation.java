package ch.nostromo.tiffanys.commons.move;

import ch.nostromo.tiffanys.commons.ChessGameState;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MoveVariation {
    private List<Move> moves = new ArrayList<>();
    private String comment;
    private ChessGameState result;
}
