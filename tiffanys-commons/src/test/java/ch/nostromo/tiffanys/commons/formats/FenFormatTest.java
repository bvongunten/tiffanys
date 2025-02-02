package ch.nostromo.tiffanys.commons.formats;

import ch.nostromo.tiffanys.commons.BaseTest;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class FenFormatTest extends BaseTest {

	@Test
	public void testFenFormatByString() {
		String fen = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";

		FenFormat fenFormat = new FenFormat(fen);
		assertEquals("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR", fenFormat.getPosition());
		assertEquals("w", fenFormat.getSideToMove());
		assertEquals("KQkq", fenFormat.getCastling());
		assertEquals("-", fenFormat.getEnPassant());
		assertEquals(Integer.valueOf(0), fenFormat.getHalfMoveClock());
		assertEquals(Integer.valueOf(1), fenFormat.getMoveNr());

		assertEquals(fen, fenFormat.toString());
	}

	@Test
	public void testFenFormatByArguments() {
		String fen = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";

		FenFormat fenFormat = new FenFormat("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR", "w", "KQkq", "-", 0, 1);

		assertEquals(fen, fenFormat.toString());
	}


}
