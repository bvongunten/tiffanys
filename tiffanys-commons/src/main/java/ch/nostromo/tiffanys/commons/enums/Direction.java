package ch.nostromo.tiffanys.commons.enums;

import java.util.ArrayList;
import java.util.List;

public enum Direction {

    //@formatter:off
    NORTH(10),
    SOUTH(-10),
    EAST(1),
    WEST(-1),
    NORTH_WEST(11),
    SOUTH_EAST(-11),
    NORTH_EAST(+9),
    SOUTH_WEST(-9),
    
    KNIGHT_ONE(19),
    KNIGHT_TWO(21),
    KNIGHT_THREE(12),
    KNIGHT_FOUR(8),
    KNIGHT_FIVE(-19),
    KNIGHT_SIX(-21),
    KNIGHT_SEVEN(-12),
    KNIGHT_EIGHT(-8);    
    
    //@formatter:on

    private static List<Direction> knightDirections = new ArrayList<Direction>();
    private static List<Direction> diagonalDirections = new ArrayList<Direction>();
    private static List<Direction> horizontalDirections = new ArrayList<Direction>();

    static {
        diagonalDirections.add(NORTH_EAST);
        diagonalDirections.add(SOUTH_EAST);
        diagonalDirections.add(SOUTH_WEST);
        diagonalDirections.add(NORTH_WEST);

        horizontalDirections.add(NORTH);
        horizontalDirections.add(EAST);
        horizontalDirections.add(SOUTH);
        horizontalDirections.add(WEST);

        knightDirections.add(KNIGHT_ONE);
        knightDirections.add(KNIGHT_TWO);
        knightDirections.add(KNIGHT_THREE);
        knightDirections.add(KNIGHT_FOUR);
        knightDirections.add(KNIGHT_FIVE);
        knightDirections.add(KNIGHT_SIX);
        knightDirections.add(KNIGHT_SEVEN);
        knightDirections.add(KNIGHT_EIGHT);
    }

    private int value;

    private Direction(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static List<Direction> getKnightDirections() {
        return knightDirections;
    }

    public static List<Direction> getHorizontalDirections() {
        return horizontalDirections;
    }

    public static List<Direction> getDiagonalDirections() {
        return diagonalDirections;
    }

}
