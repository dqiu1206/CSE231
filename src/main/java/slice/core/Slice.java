/*******************************************************************************
 * Copyright (C) 2016-2017 Dennis Cosgrove, Finn Voichick
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
package slice.core;

/**
 * @author Finn Voichick
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
public class Slice<T> {

	/**
	 * Creates a new slice composed of data, an index id, an inclusive lower bound,
	 * and an exclusive upper bound.
	 * 
	 * @param data
	 *            context of what was sliced
	 * @param sliceIndexId
	 *            the id/index of the slice, negative values indicate invalidity
	 * @param minInclusive
	 *            the inclusive lower bound
	 * @param maxExclusive
	 *            the exclusive upper bound
	 */
	public Slice(T data, int sliceIndexId, int minInclusive, int maxExclusive) {
		this.data = data;
		this.sliceIndexId = sliceIndexId;
		this.minInclusive = minInclusive;
		this.maxExclusive = maxExclusive;
	}

	/**
	 * Gets the data associated with the slice. This will typically be an array of
	 * which the slice is a section.
	 * 
	 * @return the data associated with the slice
	 */
	public T getData() {
		return this.data;
	}

	/**
	 * Gets the value of the slice index/ID.
	 * 
	 * @return the int ID of the slice
	 */
	public int getSliceIndexId() {
		return this.sliceIndexId;
	}

	/**
	 * Checks to see if the slice index/ID is valid by checking if it is >= 0.
	 * 
	 * @return true if valid, false otherwise
	 */
	public boolean isSliceIndexIdValid() {
		return this.getSliceIndexId() >= 0;
	}

	/**
	 * Gets the inclusive lower bound of the slice.
	 * 
	 * @return inclusive lower bound of the slice
	 */
	public int getMinInclusive() {
		return this.minInclusive;
	}

	/**
	 * Gets the exclusive upper bound of the slice.
	 * 
	 * @return exclusive upper bound of the slice
	 */
	public int getMaxExclusive() {
		return this.maxExclusive;
	}

	private final T data;
	private final int sliceIndexId;
	private final int minInclusive;
	private final int maxExclusive;
}
