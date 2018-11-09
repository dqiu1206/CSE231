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

import static edu.wustl.cse231s.rice.habanero.Habanero.async;
import static edu.wustl.cse231s.rice.habanero.Habanero.finish;

import edu.rice.hj.api.SuspendableException;
import edu.wustl.cse231s.NotYetImplementedException;
import sort.core.merge.MergeableData;

/**
 * @author __STUDENT_NAME__
 * @author Aaron Handleman
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
public class MergeSort {
	/**
	 * Recursively and sequentially sorts the given array by breaking it down
	 * into arrays of size one, comparing the values, and merging them by order
	 * 
	 * @param data,
	 *            the array to sort
	 * @param lowInclusive,
	 *            the lower bound of the examined part of the array
	 * @param highExclusive,
	 *            the upper bound of the examined part of the array
	 */

	private static void sequentialMergeSortKernel(MergeableData data, int lowInclusive, int highExclusive) {
		int length=highExclusive-lowInclusive;
		if(length>1) {
			int mid=(lowInclusive+highExclusive)/2;
			
			sequentialMergeSortKernel(data,lowInclusive,mid);
			sequentialMergeSortKernel(data,mid,highExclusive);
			
			data.merge(lowInclusive, mid, highExclusive);
			
		}
	
		
	}

	/**
	 * Calls the sequential kernel to mergesort recursively
	 * 
	 * @param array,
	 *            the array to sort
	 */
	public static void sequentialMergeSort(MergeableData data) {
		sequentialMergeSortKernel(data,0,data.getLength());
		
	}

	/**
	 * Recursively and concurrently sorts the given array by breaking it down
	 * into arrays of size one, comparing the values, and merging them by order
	 * 
	 * @param data,
	 *            the array to sort
	 * @param lowInclusive,
	 *            the lower bound of the examined part of the array
	 * @param highExclusive,
	 *            the upper bound of the examined part of the array
	 * @param threshold,
	 *            the threshold at which the performance boost of parallel
	 *            mergesort outweighs the overhead
	 * @throws SuspendableException
	 */

	private static void parallelMergeSortKernel(MergeableData data, int lowInclusive, int highExclusive, int threshold)
			throws SuspendableException {
		int length=highExclusive-lowInclusive;
		int mid=(lowInclusive+highExclusive)/2;
		
		
		if(length>1) {
			finish(()->{
				
			
			async(()->{
				parallelMergeSortKernel(data,lowInclusive,mid,threshold);
			});
			async(()->{
				parallelMergeSortKernel(data,mid,highExclusive,threshold);
			});
			
			});
			data.merge(lowInclusive, mid, highExclusive);
			
			
		}
	}

	/**
	 * Calls the parallel kernel to mergesort recursively
	 * 
	 * @param data,
	 *            the array to sort
	 * @param threshold,
	 *            the threshold at which the performance boost of parallel
	 *            mergesort outweighs the overhead
	 * @throws SuspendableException
	 */
	public static void parallelMergeSort(MergeableData data, int threshold) throws SuspendableException {
		parallelMergeSortKernel(data, 0, data.getLength(), threshold);
	}
}
