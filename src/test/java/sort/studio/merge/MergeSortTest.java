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
package sort.studio.merge;

import static edu.wustl.cse231s.rice.habanero.Habanero.launchHabaneroApp;

import java.util.Arrays;
import java.util.Random;

import org.junit.Assert;
import org.junit.Test;

import sort.core.SortUtils;
import sort.core.merge.ArrayBuffersMergeableData;
import sort.core.merge.MergeableData;
import sort.studio.merge.MergeSort;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
public class MergeSortTest {
	public MergeSortTest() {
		this.original = new int[1_000];
		Random random = new Random();
		for(int i=0; i<original.length; i++) {
			this.original[i] = random.nextInt(10_000);
		}
		
		this.expected = Arrays.copyOf(this.original, this.original.length);
		Arrays.sort(this.expected);
	}

	@Test
	public void testSequential() {
		MergeableData data = new ArrayBuffersMergeableData(original);
		MergeSort.sequentialMergeSort(data);
		int[] actual = data.getSolution();
		Assert.assertTrue(SortUtils.isInSortedOrder(actual));
		Assert.assertArrayEquals(this.expected, actual);
	}

	@Test
	public void testParallel() {
		int numProcessors = Runtime.getRuntime().availableProcessors();
		int threshold = this.original.length / (numProcessors*10);
		launchHabaneroApp(() -> {
			MergeableData data = new ArrayBuffersMergeableData(original);
			MergeSort.parallelMergeSort(data, threshold);
			int[] actual = data.getSolution();
			Assert.assertTrue(SortUtils.isInSortedOrder(actual));
			Assert.assertArrayEquals(this.expected, actual);
		});
	}
	
	private final int[] original;
	private final int[] expected;
}
