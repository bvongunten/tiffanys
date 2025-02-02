package ch.nostromo.tiffanys.commons.move.movegenerator;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * Directions (to be applied) for moves on a board. Digaonal, Orthogonal and Knights.
 */
@Getter
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

    public static final List<Direction> KNIGHT_DIRECTIONS = new ArrayList<>();
    public static final List<Direction> DIAGONAL_DIRECTIONS = new ArrayList<>();
    public static final List<Direction> ORTHOGONAL_DIRECTIONS = new ArrayList<>();

    static {
        DIAGONAL_DIRECTIONS.add(NORTH_EAST);
        DIAGONAL_DIRECTIONS.add(SOUTH_EAST);
        DIAGONAL_DIRECTIONS.add(SOUTH_WEST);
        DIAGONAL_DIRECTIONS.add(NORTH_WEST);

        ORTHOGONAL_DIRECTIONS.add(NORTH);
        ORTHOGONAL_DIRECTIONS.add(EAST);
        ORTHOGONAL_DIRECTIONS.add(SOUTH);
        ORTHOGONAL_DIRECTIONS.add(WEST);

        KNIGHT_DIRECTIONS.add(KNIGHT_ONE);
        KNIGHT_DIRECTIONS.add(KNIGHT_TWO);
        KNIGHT_DIRECTIONS.add(KNIGHT_THREE);
        KNIGHT_DIRECTIONS.add(KNIGHT_FOUR);
        KNIGHT_DIRECTIONS.add(KNIGHT_FIVE);
        KNIGHT_DIRECTIONS.add(KNIGHT_SIX);
        KNIGHT_DIRECTIONS.add(KNIGHT_SEVEN);
        KNIGHT_DIRECTIONS.add(KNIGHT_EIGHT);
    }

    private final int value;

    Direction(int value) {
        this.value = value;
    }


}
