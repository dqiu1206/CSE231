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
package sudoku.core;

import java.util.Collection;
import java.util.LinkedList;

import edu.wustl.cse231s.IntendedForStaticAccessOnlyError;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
/* package-private */ class BoxUnitUtils {
	@SuppressWarnings("unchecked")
	private static Collection<Square>[][] boxUnitMatrix = new Collection[3][3];
	static {
		int i = 0;
		for (String boxRows : new String[] { "ABC", "DEF", "GHI" }) {
			int j = 0;
			for (String boxCols : new String[] { "123", "456", "789" }) {
				boxUnitMatrix[i][j] = new LinkedList<>();
				for (char cr : boxRows.toCharArray()) {
					for (char cc : boxCols.toCharArray()) {
						StringBuilder sb = new StringBuilder();
						sb.append(cr);
						sb.append(cc);
						boxUnitMatrix[i][j].add(Square.valueOf(sb.toString()));
					}
				}
				j++;
			}
			i++;
		}
	}

	private BoxUnitUtils() {
		throw new IntendedForStaticAccessOnlyError();
	}

	/* package-private */ static Collection<Square>[][] getBoxUnitMatrix() {
		return boxUnitMatrix;
	}
}
