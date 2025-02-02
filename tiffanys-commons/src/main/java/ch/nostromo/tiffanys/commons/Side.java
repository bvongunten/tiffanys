package ch.nostromo.tiffanys.commons;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Defines the Side (color) including a color code used in PGN/FEN formats and the calculation modifier for move generation.
 */
@Getter
@AllArgsConstructor
public enum Side {

    BLACK("B", -1) {
        @Override
        public Side invert() {
            return WHITE;
        }
    },

    WHITE("W", 1) {
        @Override
        public Side invert() {
            return BLACK;
        }
    };

    /**
     * Invert the side from B
     *
     * @return Side
     */
    public abstract Side invert();

    private final String colorCode;
    private final int calculationModifier;

}
