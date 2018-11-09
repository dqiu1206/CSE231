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
package tnx.assignment.executor;

import java.util.Queue;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import edu.wustl.cse231s.IntendedForStaticAccessOnlyError;
import edu.wustl.cse231s.NotYetImplementedException;
import sort.core.quick.PartitionLocation;
import sort.core.quick.PartitionUtils;

/**
 * An implementation of the quicksort algorithm that uses Java's Executors.
 * 
 * @author David Qiu
 * @author Finn Voichick
 */
public final class XQuicksort {

	/**
	 * This class is noninstantiable. Do not modify or call this constructor.
	 */
	private XQuicksort() {
		throw new IntendedForStaticAccessOnlyError();
	}

	/**
	 * Sequentially sorts the given array using the quicksort algorithm. It calls
	 * the {@link #sequentialQuicksortKernel(int[], int, int)} method to start off
	 * the recursion.
	 * 
	 * @param array
	 *            the array to sort
	 */
	public static void sequentialQuicksort(int[] array) {
		sequentialQuicksortKernel(array, 0, array.length);
	}

	/**
	 * Should sequentially and recursively sort the given range of the array, from
	 * min (inclusive) to max (exclusive).
	 * 
	 * @param array
	 *            the array to sort
	 * @param min
	 *            the minimum value (inclusive) of the range to sort
	 * @param maxExclusive
	 *            the maximum value (exclusive) of the range to sort
	 * @see PartitionLocation
	 * @see PartitionUtils
	 * @see PartitionLocation#getLeftSidesUpperExclusive()
	 * @see PartitionLocation#getRightSidesLowerInclusive()
	 * @see PartitionUtils#partitionSubArray(int[], int, int)
	 */

	private static void sequentialQuicksortKernel(int[] array, int min, int maxExclusive) {
		int length = maxExclusive - min;
		if(length>1) {
			PartitionLocation pivot = PartitionUtils.partitionSubArray(array, min, maxExclusive);
			sequentialQuicksortKernel(array, min, pivot.getLeftSidesUpperExclusive());
			sequentialQuicksortKernel(array, pivot.getRightSidesLowerInclusive(), maxExclusive);
		}
		
	}

	/**
	 * Should sort the given array in parallel using the quicksort algorithm. You
	 * should call {@link #parallelQuicksortKernel(int[], int, int, int, Queue)} to
	 * start off the recursion.
	 * <p>
	 * You should call {@link Future#get()} in this method, rather in the kernel.
	 * You need to pass a collection of futures to the recursive method, and then
	 * get all of the futures added to the collection.
	 * 
	 * @param executor
	 *            an {@code ExecutorService} that you should use to submit tasks
	 * @param array
	 *            the array to sort
	 * @param threshold
	 *            the threshold at which the work is no longer worth parallelizing
	 * @throws InterruptedException
	 *             if the current thread was interrupted while waiting
	 * @throws ExecutionException
	 *             if the computation threw an exception
	 * @see ConcurrentLinkedQueue
	 * @see Future
	 */
	public static void parallelQuicksort(ExecutorService executor, int[] array, int threshold)
			throws InterruptedException, ExecutionException {
		Queue<Future<?>> futures = new ConcurrentLinkedQueue<>();
		parallelQuicksortKernel(executor, array, 0, array.length, futures, threshold);
		while(!futures.isEmpty()) {
			futures.poll().get();
		}
	}

	/**
	 * Should sort the given range of the array in parallel using the quicksort
	 * algorithm. If the length is less than or equal to the threshold, it is not
	 * worth parallelizing, and this method should count sequentially. Otherwise, it
	 * should submit two tasks to the executor for the lower and upper halves of
	 * this range.
	 * <p>
	 * Note: This method should not call {@link Future#get()}. This method submits
	 * the tasks, and {@link #parallelQuicksort(int[], int)} gets the futures. This
	 * is analogous to the "one finish" model that you can use in Habanero
	 * sometimes, where the recursive method does all of the spawning, and then the
	 * calling method surrounds it with a finish.
	 * 
	 * @param executor
	 *            an {@code ExecutorService} that you should use to submit tasks
	 * @param array
	 *            the array to sort
	 * @param min
	 *            the minimum value (inclusive) of the range to sort
	 * @param maxExclusive
	 *            the maximum value (exclusive) of the range to sort
	 * @param futures
	 *            a thread-safe collection of futures to which newly-submitted tasks
	 *            can be added
	 * @param threshold
	 *            the threshold at which the work is no longer worth parallelizing
	 * @return 
	 * @throws ExecutionException 
	 * @throws InterruptedException 
	 * @see ExecutorService
	 * @see Runnable
	 */
	private static void parallelQuicksortKernel(ExecutorService executor, int[] array, int min, int maxExclusive,
			Queue<Future<?>> futures, int threshold) throws InterruptedException, ExecutionException{
		int length = maxExclusive - min;
		if(length>threshold) {
			
			PartitionLocation pivot = PartitionUtils.partitionSubArray(array, min, maxExclusive);
			Future<?> future = executor.submit(()->{
				try {
					parallelQuicksortKernel(executor,array,min,pivot.getLeftSidesUpperExclusive(),futures,threshold);
				} catch (InterruptedException | ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}				

			});
			Future<?> future2 = executor.submit(()->{
				try {
					parallelQuicksortKernel(executor,array,pivot.getRightSidesLowerInclusive(),maxExclusive,futures,threshold);
				} catch (InterruptedException | ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			});
			futures.add(future);
			futures.add(future2);		
		

			
			
		}
		
		else {
			
			sequentialQuicksortKernel(array,min,maxExclusive);
		}
		
		
	}

}
