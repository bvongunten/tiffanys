package ch.nostromo.tiffanys.commons.fields;

import ch.nostromo.tiffanys.commons.enums.GameColor;
import ch.nostromo.tiffanys.commons.enums.FieldType;
import ch.nostromo.tiffanys.commons.enums.Piece;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Field implements Cloneable {

    FieldType type;
    Piece piece = null;
    GameColor pieceColor = null;

    public Field(FieldType type) {
        this.type = type;
    }

    @Override
    public Field clone() {

        Field clone;
        try {
            clone = (Field) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new IllegalStateException("Unable to clone field", e);
        }

        return clone;

    }
}
