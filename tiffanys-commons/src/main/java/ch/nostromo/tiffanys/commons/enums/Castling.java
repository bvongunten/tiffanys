package ch.nostromo.tiffanys.commons.enums;

public enum Castling {

    // @formatter:off
    WHITE_LONG(GameColor.WHITE, 25, 23, 21, 24, new int[] { 22, 23, 24 }, new int[] { 23, 24, 25 }, "O-O-O"), WHITE_SHORT(GameColor.WHITE, 25, 27, 28, 26, new int[] { 26, 27 }, new int[] { 25, 26, 27 }, "O-O"), BLACK_LONG(GameColor.BLACK, 95, 93, 91, 94,
            new int[] { 92, 93, 94 }, new int[] { 93, 94, 95 }, "O-O-O"), BLACK_SHORT(GameColor.BLACK, 95, 97, 98, 96, new int[] { 96, 97 }, new int[] { 95, 96, 97 }, "O-O");
    // @formatter:on

    private GameColor colorToMove;
    private final int fromRook;
    private final int toRook;
    private final int fromKing;
    private final int toKing;
    private final int[] mustBeEmpty;
    private final int[] mustNotBeCheck;
    private String annotation;

    private Castling(GameColor colorToMove, int fromKing, int toKing, int fromRook, int toRook, int[] mustBeEmpty, int[] mustNotBeCheck, String annotation) {
        this.colorToMove = colorToMove;
        this.fromKing = fromKing;
        this.toKing = toKing;
        this.fromRook = fromRook;
        this.toRook = toRook;
        this.mustBeEmpty = mustBeEmpty;
        this.mustNotBeCheck = mustNotBeCheck;
        this.annotation = annotation;
    }

    public int getFromRook() {
        return fromRook;
    }

    public int getToRook() {
        return toRook;
    }

    public int getFromKing() {
        return fromKing;
    }

    public int getToKing() {
        return toKing;
    }

    public int[] getMustBeEmpty() {
        return mustBeEmpty;
    }

    public int[] getMustNotBeCheck() {
        return mustNotBeCheck;
    }

    public GameColor getColorToMove() {
        return colorToMove;
    }

    public String getAnnotation() {
        return annotation;
    }

}
