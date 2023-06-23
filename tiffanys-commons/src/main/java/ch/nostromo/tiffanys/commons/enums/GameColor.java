package ch.nostromo.tiffanys.commons.enums;

import lombok.Getter;

@Getter
public enum GameColor implements ColorInvertor {

    BLACK("B", -1) {
        @Override
        public GameColor invert() {
            return WHITE;
        }
    },

    WHITE("W", 1) {
        @Override
        public GameColor invert() {
            return BLACK;
        }
    };


    private String colorCode;
    private int calculationModificator;

    private GameColor(String colorCode, int calculationModificator) {
        this.colorCode = colorCode;
        this.calculationModificator = calculationModificator;
    }

}
