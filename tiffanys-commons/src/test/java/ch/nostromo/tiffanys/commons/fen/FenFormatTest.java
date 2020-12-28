package ch.nostromo.tiffanys.commons.fen;

import ch.nostromo.tiffanys.commons.TestHelper;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class FenFormatTest extends TestHelper {

	@Test
	public void testFenFormatByString() {
		String fen = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";

		FenFormat fenFormat = new FenFormat(fen);
		assertEquals("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR", fenFormat.getPosition());
		assertEquals("w", fenFormat.getColorToMove());
		assertEquals("KQkq", fenFormat.getCastling());
		assertEquals("-", fenFormat.getEnPassant());
		assertEquals(Integer.valueOf(0), fenFormat.getHalfMoveClock());
		assertEquals(Integer.valueOf(1), fenFormat.getMoveNr());

		assertEquals(fen, fenFormat.generateFen());
	}

	@Test
	public void testFenFormatByConstructor() {
		String fen = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";

		FenFormat fenFormat = new FenFormat("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR", "w", "KQkq", "-", 0, 1);

		assertEquals(fen, fenFormat.generateFen());
	}

}
