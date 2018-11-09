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

import org.junit.Test;

import backtrack.assignment.rubric.BacktrackRubric;
import sudoku.core.Square;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 * 
 *         {@link DefaultImmutableSudokuPuzzle#getOptions(Square)}
 */
@BacktrackRubric(BacktrackRubric.Category.IMMUTABLE_SUDOKU_PUZZLE)
public class MutatingFixedSizeAllOptionsTest {

	@Test
	public void test() {
		// source: http://norvig.com/hardest.txt
		final String givens = "12..4......5.69.1...9...5.........7.7...52.9..3......2.9.6...5.4..9..8.1..3...9.4";

		DefaultImmutableSudokuPuzzle puzzle = new DefaultImmutableSudokuPuzzle(givens);
		try {
			puzzle.getOptions(Square.A3);
		} catch (UnsupportedOperationException e) {
			System.err.println(
					"Are you attempting to remove from ALL_OPTIONS?\nALL_OPTIONS is a fixed-size list.\nremove(item) is not supported on fixed-size lists.\nMake a copy of its values, instead.");
			throw e;
		}
	}

}
