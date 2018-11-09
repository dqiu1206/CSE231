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
package sort.core.quick;

import edu.rice.hj.api.HjPoint;

/**
 * A location where an array has been partitioned. Because the partition
 * location can be a range (if there are multiple values in the array that are
 * equal to the partition value), the partition location is represented with two
 * values: the left side of the upper range (inclusive) and the right side of
 * the lower range (inclusive).
 * 
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
public final class PartitionLocation {

	public PartitionLocation(HjPoint pt) {
		this.leftSidesUpperExclusive = pt.get(1)+1;
		this.rightSidesLowerInclusive = pt.get(0);
	}

	/**
	 * Gets the index of the minimum value in the partitioned array that is
	 * greater than the partition value. You can think of this as the left side
	 * of the upper range of the array.
	 * 
	 * @return the left side of the upper range of the array
	 */
	public int getLeftSidesUpperExclusive() {
		return this.leftSidesUpperExclusive;
	}

	/**
	 * Gets the index of the maximum value in the partitioned array that is less
	 * than the partition value. You can think of this as the right side of the
	 * lower range of the array.
	 * 
	 * @return the right side of the lower range of the array
	 */
	public int getRightSidesLowerInclusive() {
		return this.rightSidesLowerInclusive;
	}

	private final int leftSidesUpperExclusive;
	private final int rightSidesLowerInclusive;

}
