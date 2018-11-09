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
package sort;

import static edu.wustl.cse231s.rice.habanero.Habanero.launchHabaneroApp;

import java.util.Arrays;
import java.util.Random;

import edu.rice.hj.api.SuspendableException;
import edu.wustl.cse231s.NotYetImplementedException;
import edu.wustl.cse231s.timing.ImmutableTimer;
import sort.core.SortUtils;
import sort.core.merge.ArrayBuffersMergeableData;
import sort.core.merge.MergeableData;
import sort.demo.quick.Quicksort;
import sort.studio.merge.MergeSort;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
public class SortTiming {
	private static void timeArraysSort(int[] original) {
		int[] array = Arrays.copyOf(original, original.length);
		ImmutableTimer timer = new ImmutableTimer("                  Arrays.sort");
		Arrays.sort(array);
		long dt = timer.mark();
		if (SortUtils.isInSortedOrder(array)) {
			timer.printResults(dt);
		} else {
			throw new RuntimeException("result is not in sorted order");
		}
	}

	private static void timeArraysParallelSort(int[] original) {
		int[] array = Arrays.copyOf(original, original.length);
		ImmutableTimer timer = new ImmutableTimer("          Arrays.parallelSort");
		Arrays.parallelSort(array);
		long dt = timer.mark();
		if (SortUtils.isInSortedOrder(array)) {
			timer.printResults(dt);
		} else {
			throw new RuntimeException("result is not in sorted order");
		}
	}

	private static void timeSequentialQuicksort(int[] original) {
		int[] array = Arrays.copyOf(original, original.length);
		ImmutableTimer timer = new ImmutableTimer("Quicksort.sequentialQuicksort");
		Quicksort.sequentialQuicksort(array);
		long dt = timer.mark();
		if (SortUtils.isInSortedOrder(array)) {
			timer.printResults(dt);
		} else {
			throw new RuntimeException("result is not in sorted order");
		}
	}

	private static void timeParallelQuicksort(int[] original, int threshold) throws SuspendableException {
		int[] array = Arrays.copyOf(original, original.length);
		ImmutableTimer timer = new ImmutableTimer("  Quicksort.parallelQuicksort");
		Quicksort.parallelQuicksort(array, threshold);
		long dt = timer.mark();
		if (SortUtils.isInSortedOrder(array)) {
			timer.printResults(dt);
		} else {
			throw new RuntimeException("result is not in sorted order");
		}
	}

	private static void timeSequentialMergeSort(int[] original) {
		try {
			MergeableData data = new ArrayBuffersMergeableData(original);
			ImmutableTimer timer = new ImmutableTimer("MergeSort.sequentialMergesort");
			MergeSort.sequentialMergeSort(data);
			long dt = timer.mark();
			if (SortUtils.isInSortedOrder(data.getSolution())) {
				timer.printResults(dt);
			} else {
				throw new RuntimeException("result is not in sorted order");
			}
		} catch (NotYetImplementedException nyie) {
			System.out.println("MergeSort.sequentialMergesort: NOT YET IMPLEMENTED");
		}
	}

	private static void timeParallelMergeSort(int[] original, int threshold) throws SuspendableException {
		try {
			MergeableData data = new ArrayBuffersMergeableData(original);
			ImmutableTimer timer = new ImmutableTimer("  MergeSort.parallelMergesort");
			MergeSort.parallelMergeSort(data, threshold);
			long dt = timer.mark();
			if (SortUtils.isInSortedOrder(data.getSolution())) {
				timer.printResults(dt);
			} else {
				throw new RuntimeException("result is not in sorted order");
			}
		} catch (NotYetImplementedException nyie) {
			System.out.println("  MergeSort.parallelMergesort: NOT YET IMPLEMENTED");
		}
	}

	public static void main(String[] args) {
		int ARRAY_LENGTH = 1_000_000;
		int[] original = new int[ARRAY_LENGTH];
		Random random = new Random();
		for (int i = 0; i < original.length; i++) {
			original[i] = random.nextInt();
		}

		int numProcessors = Runtime.getRuntime().availableProcessors();
		int threshold = original.length / (numProcessors * 10);
		launchHabaneroApp(() -> {
			final int ITERATION_COUNT = 10;
			for (int iteration = 0; iteration < ITERATION_COUNT; iteration++) {

				timeArraysSort(original);
				timeArraysParallelSort(original);

				timeSequentialQuicksort(original);
				timeParallelQuicksort(original, threshold);

				timeSequentialMergeSort(original);
				timeParallelMergeSort(original, threshold);

				System.out.println();
			}
		});

	}
}
