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
package datarace.demo.quicksort;

import static edu.wustl.cse231s.rice.habanero.Habanero.async;
import static edu.wustl.cse231s.rice.habanero.Habanero.finish;

import edu.rice.hj.api.SuspendableException;
import sort.core.quick.PartitionLocation;
import sort.core.quick.PartitionUtils;

/**
 * modified from
 * http://www.cs.rice.edu/~vs3/hjlib/code/course-materials/demo-files/Quicksort.java
 * 
 * Parallel Quicksort program. partition() has not been parallelized.
 *
 * @author Vivek Sarkar (vsarkar@rice.edu)
 */
public class SuspectQuicksort {
	private static void sequentialQuicksortKernel(int[] array, int lowInclusive, int highExclusive) {
		int lengthOfSubArray = (highExclusive - lowInclusive);
		if (lengthOfSubArray > 1) {
			PartitionLocation partitionLocation = PartitionUtils.partitionSubArray(array, lowInclusive, highExclusive);
			sequentialQuicksortKernel(array, lowInclusive, partitionLocation.getLeftSidesUpperExclusive());
			sequentialQuicksortKernel(array, partitionLocation.getRightSidesLowerInclusive(), highExclusive);
		}
	}

	public static void sequentialQuicksort(int[] array) {
		sequentialQuicksortKernel(array, 0, array.length);
	}

	private static void parallelQuicksortKernel(int[] array, int lowInclusive, int highExclusive, int threshold) {
		int lengthOfSubArray = (highExclusive - lowInclusive);
		if (lengthOfSubArray > threshold) {
			PartitionLocation partitionLocation = PartitionUtils.partitionSubArray(array, lowInclusive, highExclusive);
			async(() -> {
				parallelQuicksortKernel(array, lowInclusive, partitionLocation.getLeftSidesUpperExclusive(), threshold);
			});
			parallelQuicksortKernel(array, partitionLocation.getRightSidesLowerInclusive(), highExclusive, threshold);
		} else {
			sequentialQuicksort(array);
		}
	}

	public static void parallelQuicksort(int[] array, int threshold) throws SuspendableException {
		finish(() -> {
			parallelQuicksortKernel(array, 0, array.length, threshold);
		});
	}
}
