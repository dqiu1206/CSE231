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

import static edu.wustl.cse231s.rice.habanero.Habanero.launchHabaneroApp;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.mutable.MutableObject;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import backtrack.assignment.rubric.BacktrackRubric;
import sudoku.core.GivensUtils;
import sudoku.core.ImmutableSudokuPuzzle;
import sudoku.core.Square;
import sudoku.core.SquareSearchAlgorithm;
import sudoku.core.SudokuPuzzle;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
@BacktrackRubric(BacktrackRubric.Category.PARALLEL_SUDOKU_CORRECTNESS)
@RunWith(Parameterized.class)
public class SolveTest {
	@Parameters(name = "{0}, givens: {1}")
	public static Collection<Object[]> getConstructorArguments() {
		Collection<Object[]> results = new LinkedList<>();
		for (SquareSearchAlgorithm squareSearchAlgorithm : SquareSearchAlgorithms.values()) {
			List<String> givensList = GivensUtils.getGivensToTest(squareSearchAlgorithm);
			for (String givens : givensList) {
				results.add(new Object[] { squareSearchAlgorithm, givens });
			}
		}
		return results;
	}

	public SolveTest(SquareSearchAlgorithm squareSearchAlgorithm, String givens) {
		this.squareSearchAlgorithm = squareSearchAlgorithm;
		this.givens = givens;
	}

	@Test
	public void test() {
		ImmutableSudokuPuzzle original = new DefaultImmutableSudokuPuzzle(this.givens);
		MutableObject<ImmutableSudokuPuzzle> solution = new MutableObject<>();

		launchHabaneroApp(() -> {
			solution.setValue(ParallelSudoku.solve(original, this.squareSearchAlgorithm));
		});

		Assert.assertNotEquals(this.givens, solution.toString());
		Assert.assertTrue(isCompletelyFilledIn(solution.getValue()));
		Assert.assertTrue(isCompletelyFilledInAndEachSquareIsValid(solution.getValue()));
		Assert.assertTrue(containsOriginal(original, solution.getValue()));
	}

	private final SquareSearchAlgorithm squareSearchAlgorithm;
	private final String givens;

	private static boolean isCompletelyFilledIn(SudokuPuzzle puzzle) {
		for (Square square : Square.values()) {
			int value = puzzle.getValue(square);
			if (value != 0) {
				// pass
			} else {
				return false;
			}
		}
		return true;
	}

	private static boolean isCompletelyFilledInAndEachSquareIsValid(SudokuPuzzle puzzle) {
		for (Square square : Square.values()) {
			int value = puzzle.getValue(square);
			if (value != 0) {
				if (puzzle.isCandidateValid(square, value)) {
					// pass
				} else {
					return false;
				}
			} else {
				return false;
			}
		}
		return true;
	}

	private static boolean containsOriginal(SudokuPuzzle original, SudokuPuzzle solution) {
		for (Square square : Square.values()) {
			int originalValue = original.getValue(square);
			if (originalValue != 0) {
				int value = solution.getValue(square);
				if (value == originalValue) {
					// pass
				} else {
					return false;
				}
			}
		}
		return true;
	}

}
