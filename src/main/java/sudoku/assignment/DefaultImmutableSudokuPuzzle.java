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
import java.util.Collections;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import edu.wustl.cse231s.NotYetImplementedException;
import net.jcip.annotations.Immutable;
import sudoku.core.ImmutableSudokuPuzzle;
import sudoku.core.Square;

/**
 * Your implementation of an immutable sudoku puzzle. As noted in the
 * {@code ImmutableSudokuPuzzle} documentation, the board in this class cannot
 * change after construction, but it can create new immutable boards with an
 * additional value.
 * 
 * @author David Qiu
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
@Immutable
public final class DefaultImmutableSudokuPuzzle implements ImmutableSudokuPuzzle {

	/** All of the values on the board, in a 2 dimensional array. */
	private final int[][] values;

	public DefaultImmutableSudokuPuzzle(String givens) {
		this.values = new int[9][9];
		int row = 0;
		int column = 0;
		for (char c : givens.toCharArray()) {
			int value;
			if (c == '.') {
				value = 0;
			} else {
				value = c - '0';
			}
			this.values[row][column] = value;
			column++;
			if( column == 9 ) {
				row++;
				column = 0;
			}
		}
	}

	/**
	 * Constructs a new board from this one with a new square filled in. This
	 * board will have values copied over from the given one, but an additional
	 * value will be added in the given square.
	 * 
	 * @param other
	 *            the puzzle to copy values over from
	 * @param square
	 *            the square (unfilled in other) to fill
	 * @param value
	 *            the value to put in square
	 */
	private DefaultImmutableSudokuPuzzle(DefaultImmutableSudokuPuzzle other, Square square, int value) {
		this.values = new int[9][];
		for(int i=0; i<this.values.length; i++) {
			this.values[i] = other.values[i].clone();
		}
		int row = square.getRow();
		int column = square.getColumn();
		this.values[row][column] = value;
	}

	/**
	 * Should create a new board from this one. The new
	 * {@code ImmutableSudokuPuzzle} will have values identical to this one,
	 * except that the given square will be filled in with the given value.
	 */
	@Override
	public ImmutableSudokuPuzzle createNext(Square square, int value) {
		ImmutableSudokuPuzzle newSudoku = new DefaultImmutableSudokuPuzzle(this, square,value);
		return newSudoku;
		
	}

	/**
	 * Should get the number written at a given square. If the square is filled,
	 * this method should return a number between 1 and 9 (inclusive), and if
	 * the square is not filled (if {@link #isAlreadyFilled(Square)} returns
	 * false), it should return 0.
	 * 
	 * @param square
	 *            the location on this board to examine
	 * @return the number written at that location, or 0 if no number is written
	 *         there
	 */
	@Override
	public int getValue(Square square) {
		return values[square.getRow()][square.getColumn()];
	}

	/**
	 * Should get the values possible at the given square, given that all other
	 * board locations are correct. In other words, this method should return
	 * the set of all values not written in any of this square's peers. If this
	 * square already has a value (the same value returned by
	 * {@link #getValue(Square)}), this method should return a set containing
	 * only that value.
	 * 
	 * @param square
	 *            the location in the puzzle to examine
	 * @return the set of all options at that location
	 */
	@Override
	public SortedSet<Integer> getOptions(Square square) {

		TreeSet<Integer> options = new TreeSet<>();

		int value = this.getValue(square);
		if (value == 0) {
			options.addAll(ALL_OPTIONS);
			for(Square peers:square.getPeers()) {
				int peerValue = this.getValue(peers);
				options.remove(peerValue);
			}
		} else {
			options.add(value);
		}

		return options;

	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (int[] row : this.values) {
			for( int value : row ) {
				if (value != 0) {
					sb.append(value);
				} else {
					sb.append('.');
				}
			}
		}
		return sb.toString();
	}

	private static final List<Integer> ALL_OPTIONS = Collections
			.unmodifiableList(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9));

}
