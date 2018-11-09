/*******************************************************************************
 * Copyright (C) 2016-2017 Dennis Cosgrove
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 ******************************************************************************/
package sudoku.assignment;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

import backtrack.assignment.rubric.BacktrackRubric;
import sudoku.core.Square;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 * 
 *         {@link SquareSearchAlgorithms#FEWEST_OPTIONS_FIRST}
 */
@BacktrackRubric(BacktrackRubric.Category.SQUARE_SEARCH_ALGORITHM)
public class FewestOptionsFirstReturns1OptionSquareTest {
	@Test
	public void test() {
		// source: http://norvig.com/hardest.txt
		String givensWithSquareWithOneOption = "1...34.8....8..5....4.6..21.18......3..1.2..6......81.52..7.9....6..9....9.64...2";

		DefaultImmutableSudokuPuzzle puzzle = new DefaultImmutableSudokuPuzzle(givensWithSquareWithOneOption);
		Square expectedSquare = Square.G4;

		int expectedOptionsSize = puzzle.getOptions(expectedSquare).size();

		assertFalse(puzzle.isAlreadyFilled(expectedSquare));
		assertEquals(1, expectedOptionsSize);

		Square actualSquare = SquareSearchAlgorithms.FEWEST_OPTIONS_FIRST.selectNextUnfilledSquare(puzzle);

		assertNotEquals("first square <A1> returned despite being already filled", Square.A1, actualSquare);
		assertNotEquals("last square <I9> returned despite being already filled", Square.I9, actualSquare);

		Square firstUnfilledSquare = Square.A2;
		assertNotEquals(
				"first unfilled square <" + firstUnfilledSquare + "> returned despite not having fewest options",
				firstUnfilledSquare, actualSquare);
		Square lastUnfilledSquare = Square.I8;
		assertNotEquals("last unfilled square <" + lastUnfilledSquare + "> returned despite not having fewest options",
				lastUnfilledSquare, actualSquare);

		int actualOptionsSize = puzzle.getOptions(actualSquare).size();

		assertEquals(expectedOptionsSize, actualOptionsSize);
		assertEquals(expectedSquare, actualSquare);
	}
}
