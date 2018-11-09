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

import static org.junit.Assert.assertNotEquals;

import java.util.Collection;
import java.util.LinkedList;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import backtrack.assignment.rubric.BacktrackRubric;
import sudoku.core.Square;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 * 
 *         {@link SquareSearchAlgorithms#FEWEST_OPTIONS_FIRST}
 */
@BacktrackRubric(BacktrackRubric.Category.SQUARE_SEARCH_ALGORITHM)
@RunWith(Parameterized.class)
public class FewestOptionsFirstIsAlreadySetPointedTest {
	private final String givens;
	private final Square expectedSquare;

	public FewestOptionsFirstIsAlreadySetPointedTest(String givens, Square expectedSquare) {
		this.givens = givens;
		this.expectedSquare = expectedSquare;
	}

	@Test
	public void testAlreadySet() {
		DefaultImmutableSudokuPuzzle puzzle = new DefaultImmutableSudokuPuzzle(givens);
		Square actualSquare = SquareSearchAlgorithms.FEWEST_OPTIONS_FIRST.selectNextUnfilledSquare(puzzle);
		assertNotEquals("The selected square is already filled, it should be an unfilled square", expectedSquare,
				actualSquare);
	}

	@Parameters(name = "givens:{0} expectedSquare:{1}")
	public static Collection<Object[]> getConstructorArguments() {
		Collection<Object[]> results = new LinkedList<>();
		results.add(new Object[] { "1................................................................................",
				Square.A1 });
		results.add(new Object[] { "................................................................................1",
				Square.I9 });
		results.add(new Object[] { "........................................1........................................",
				Square.E5 });
		return results;
	}

}
