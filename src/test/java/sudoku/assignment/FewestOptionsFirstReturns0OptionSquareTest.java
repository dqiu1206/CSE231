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
import static org.junit.Assert.assertNotEquals;

import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

import backtrack.assignment.rubric.BacktrackRubric;
import sudoku.core.Square;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 * 
 *         {@link SquareSearchAlgorithms#FEWEST_OPTIONS_FIRST}
 */
@BacktrackRubric(BacktrackRubric.Category.SQUARE_SEARCH_ALGORITHM)
public class FewestOptionsFirstReturns0OptionSquareTest {
	@Test
	public void test() {
		// source of original givens (now constrained to no hope):
		// http://norvig.com/hardest.txt
		String givensWithNoHope = "1..4.7.96.3..2...8..96..5....53..9...1358...26982741533.2....1..418....7..7...3..";
		DefaultImmutableSudokuPuzzle puzzle = new DefaultImmutableSudokuPuzzle(givensWithNoHope);
		Square expectedSquare = Square.A3;
		int expectedSquareOptionsLength = puzzle.getOptions(expectedSquare).size();
		assertEquals(0, expectedSquareOptionsLength);
		for (Square square : Square.values()) {
			if (expectedSquare != square) {
				assertNotEquals(0, puzzle.getOptions(square).size());
			}
		}

		Square actualSquare = SquareSearchAlgorithms.FEWEST_OPTIONS_FIRST.selectNextUnfilledSquare(puzzle);
		Set<Integer> options = puzzle.getOptions(actualSquare);
		Assert.assertEquals("The number of options for the square is incorrect", 0, options.size());
		Assert.assertEquals(expectedSquare, actualSquare);
	}
}
