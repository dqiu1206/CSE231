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

import java.util.Collection;

import edu.wustl.cse231s.NotYetImplementedException;
import net.jcip.annotations.Immutable;
import sudoku.core.Square;
import sudoku.core.SquareSearchAlgorithm;
import sudoku.core.SudokuPuzzle;

/**
 * Your algorithms for picking a square to examine next. All of your code for
 * these implementations will be in the
 * {@link #selectNextUnfilledSquare(SudokuPuzzle)} methods.
 * 
 * @author David Qiu
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
@Immutable
public enum SquareSearchAlgorithms implements SquareSearchAlgorithm {

	/**
	 * A square search algorithm based on <a href=
	 * "https://en.wikipedia.org/wiki/Row-_and_column-major_order">row-major
	 * ordering</a>. This implementation's
	 * {@link #selectNextUnfilledSquare(SudokuPuzzle)} method should simply
	 * traverse the board in row-major order and return the first square that is
	 * unfilled.
	 */
	ROW_MAJOR {
		@Override
		public Square selectNextUnfilledSquare(SudokuPuzzle puzzle) {
			for (Square currentSquare : Square.values()) {
				if (puzzle.isAlreadyFilled(currentSquare)) {
					// pass
				} else {
					return currentSquare;
				}
			}
			return null;
		}
	},
	/**
	 * A square search algorithm that prefers squares with few options. In other
	 * words, this algorithm should always pick the empty square that is the
	 * most constrained by its peers. This algorithm may return a square with no
	 * options (meaning that the puzzle is impossible to solve), or it may
	 * return null, meaning that the puzzle is already solved.
	 */
	FEWEST_OPTIONS_FIRST {
		@Override
		public Square selectNextUnfilledSquare(SudokuPuzzle puzzle) {
			Square selected = null;
			int peers = 9;
			for(Square square:Square.values()) {
				if(puzzle.isAlreadyFilled(square)) {
				}
				
				else {
					
					int options = puzzle.getOptions(square).size();
					if(options<peers) {
						peers=options;
						selected = square;
					}
					
				}
				
			}
			return selected;
		}
	};
}
