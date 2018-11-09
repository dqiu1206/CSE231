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

import java.util.Arrays;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.junit.Assert;
import org.junit.Test;

import backtrack.assignment.rubric.BacktrackRubric;
import sudoku.assignment.DefaultImmutableSudokuPuzzle;
import sudoku.core.ImmutableSudokuPuzzle;
import sudoku.core.Square;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
@BacktrackRubric(BacktrackRubric.Category.IMMUTABLE_SUDOKU_PUZZLE)
public class ImmutableSudokuPuzzleTest {
	// source: http://norvig.com/hardest.txt
	private static final String givens = "85...24..72......9..4.........1.7..23.5...9...4...........8..7..17..........36.4.";

	@Test
	public void testValue() {
		DefaultImmutableSudokuPuzzle puzzle = new DefaultImmutableSudokuPuzzle(givens);
		Assert.assertEquals("Your puzzle is not initializing with the correct value", puzzle.getValue(Square.A1), 8);
		Assert.assertEquals("Your puzzle is not initializing with the correct value", puzzle.getValue(Square.A2), 5);
	}

	@Test
	public void testAlreadySet() {
		DefaultImmutableSudokuPuzzle puzzle = new DefaultImmutableSudokuPuzzle(givens);
		Assert.assertTrue("A square should be filled, but it is not", puzzle.isAlreadyFilled(Square.A1));
		Assert.assertTrue("A square should be filled, but it is not", puzzle.isAlreadyFilled(Square.A2));
		Assert.assertFalse("A square should not be filled, but it is", puzzle.isAlreadyFilled(Square.A3));
		Assert.assertFalse("A square should not be filled, but it is", puzzle.isAlreadyFilled(Square.A4));
		Assert.assertFalse("A square should not be filled, but it is", puzzle.isAlreadyFilled(Square.A5));
	}

	@Test
	public void testOptions() {
		DefaultImmutableSudokuPuzzle puzzle = new DefaultImmutableSudokuPuzzle(givens);
		Set<Integer> options = puzzle.getOptions(Square.A3);
		Assert.assertTrue("You are not getting the correct options for a square",
				CollectionUtils.isEqualCollection(options, Arrays.asList(1, 3, 6, 9)));
	}

	@Test
	public void testCreateNext() {
		ImmutableSudokuPuzzle puzzle = new DefaultImmutableSudokuPuzzle(givens);
		ImmutableSudokuPuzzle next = puzzle.createNext(Square.A3, 3);
		Assert.assertTrue("You did not set the square to any value", next.isAlreadyFilled(Square.A3));
		Assert.assertEquals("You did not set the square to the desired value", 3, next.getValue(Square.A3));
		Assert.assertFalse("You set the value for the wrong square", next.isAlreadyFilled(Square.A4));
		Set<Integer> options = next.getOptions(Square.A4);
		Assert.assertTrue("The options for the neighboring squares did not update",
				CollectionUtils.isEqualCollection(options, Arrays.asList(6, 7, 9)));
	}

}
