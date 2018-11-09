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

import static edu.rice.hj.Module0.newPoint;

import java.util.Random;

import edu.rice.hj.api.HjPoint;

/**
 * Parallel Quicksort program. {@link #partition(int[], int, int)} has not been
 * parallelized. Modified from <a href=
 * "http://www.cs.rice.edu/~vs3/hjlib/code/course-materials/demo-files/Quicksort.java">
 * Habanero's quicksort demo</a>.
 * 
 * @author Vivek Sarkar (vsarkar@rice.edu)
 */
public class PartitionUtils {

	private static HjPoint partition(final int[] A, final int M, final int N) {
		int I;
		int storeIndex = M;

		final Random rand = new Random();
		final int pivot = M + rand.nextInt(N - M + 1);
		final int pivotValue = A[pivot];
		exchange(A, pivot, N);

		for (I = M; I < N; I++) {
			// Only count comparison with pivot value in abstract execution
			// metrics
			if (A[I] <= pivotValue) {
				exchange(A, I, storeIndex);
				storeIndex++;
			}
		}

		exchange(A, storeIndex, N);

		if (storeIndex == N) {
			return newPoint(N, storeIndex - 1);
		} else if (storeIndex == M) {
			return newPoint(storeIndex + 1, M);
		}
		return newPoint(storeIndex + 1, storeIndex - 1);
	}

	private static void exchange(final int[] A, final int x, final int y) {
		int temp = A[x];
		A[x] = A[y];
		A[y] = temp;
	}

	/**
	 * Partitions a range of the given array, returning an HjPoint that
	 * represents the location of the partition. After this method is called,
	 * all values to the left of the partition will be greater than the
	 * partition value, and all values to the right of the partition will be
	 * greater than the partition value.
	 * 
	 * @param array
	 *            the array to partition
	 * @param minInclusive
	 *            the minimum index of the array (inclusive) to partition
	 * @param maxExclusive
	 *            the maximum index of the array (exclusive) to partition
	 * @return the location of the array's partition
	 */
	public static PartitionLocation partitionSubArray(int[] array, int minInclusive, int maxExclusive) {
		HjPoint pt = partition(array, minInclusive, maxExclusive-1);
		return new PartitionLocation(pt);
	}
}
