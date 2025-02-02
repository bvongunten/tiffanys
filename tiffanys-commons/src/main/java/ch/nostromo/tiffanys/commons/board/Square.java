package ch.nostromo.tiffanys.commons.board;

import ch.nostromo.tiffanys.commons.ChessGameException;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Board Squares contain a reference to the board idx / field.
 */
@Getter
@AllArgsConstructor
public enum Square {

    // @formatter:off
    A8(91),  B8(92),  C8(93),  D8(94),  E8(95),  F8(96),  G8(97),  H8(98),
    A7(81),  B7(82),  C7(83),  D7(84),  E7(85),  F7(86),  G7(87),  H7(88),
    A6(71),  B6(72),  C6(73),  D6(74),  E6(75),  F6(76),  G6(77),  H6(78),
    A5(61),  B5(62),  C5(63),  D5(64),  E5(65),  F5(66),  G5(67),  H5(68),
    A4(51),  B4(52),  C4(53),  D4(54),  E4(55),  F4(56),  G4(57),  H4(58),
    A3(41),  B3(42),  C3(43),  D3(44),  E3(45),  F3(46),  G3(47),  H3(48),
    A2(31),  B2(32),  C2(33),  D2(34),  E2(35),  F2(36),  G2(37),  H2(38),
    A1(21),  B1(22),  C1(23),  D1(24),  E1(25),  F1(26),  G1(27),  H1(28);
    // @formatter:on

    private final int boardIdx;

    /**
     * Returns a Square for a given board idx / field
     */
    public static Square byBoardIdx(int boardIdx) {
        for (Square square : Square.values()) {
            if (square.boardIdx == boardIdx) {
                return square;
            }
        }

        throw new ChessGameException("Unknown idx: " + boardIdx);
    }

    /**
     * Returns a Square by a given name. Ignores case
     */
    public static Square byName(String name) {
        for (Square square : Square.values()) {
            if (square.name().equalsIgnoreCase(name)) {
                return square;
            }
        }
        throw new ChessGameException("Unknown name: " + name);

    }

    /**
     * Returns the name of the square in lower case
     */
    public String getLowerCaseName() {
        return name().toLowerCase();
    }


}
