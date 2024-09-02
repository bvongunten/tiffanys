package ch.nostromo.tiffanys.commons.board;

import ch.nostromo.tiffanys.commons.enums.FieldType;
import ch.nostromo.tiffanys.commons.enums.GameColor;
import ch.nostromo.tiffanys.commons.enums.Piece;
import lombok.Data;

@Data
public class BoardField implements Cloneable {

    FieldType type;
    Piece piece = null;
    GameColor pieceColor = null;

    public BoardField(FieldType type) {
        this.type = type;
    }

    @Override
    public BoardField clone() {
        try {
            return (BoardField) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new IllegalStateException("Unable to clone field", e);
        }
    }
}
