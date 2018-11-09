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

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import edu.wustl.cse231s.IntendedForStaticAccessOnlyError;
import edu.wustl.cse231s.NotYetImplementedException;
import edu.wustl.cse231s.bioinformatics.Nucleobase;
import edu.wustl.cse231s.slices.SliceUtils;
import slice.core.Slice;

/**
 * A parallel nucleobase counter that uses Java's {@link ExecutorService}
 * interface to count different sections of the chromosome in parallel.
 * 
 * @author David Qiu
 * @author Finn Voichick
 */
public class XNucleobaseCount {

	/**
	 * This class is noninstantiable. Do not modify or call this constructor.
	 */
	private XNucleobaseCount() {
		throw new IntendedForStaticAccessOnlyError();
	}

	/**
	 * Sequentially counts all of the instances of a specific nucleobase. This
	 * method is very simple in that all it does is call
	 * {@link #countRangeSequential(byte[], Nucleobase, int, int)}. You do not need
	 * to change this method at all, but you will need to implement the
	 * {@code countRangeSequential} method.
	 * 
	 * @param chromosome
	 *            the chromosome to examine, as an array of bytes
	 * @param nucleobase
	 *            the nucleobase to look for in the chromosome
	 * @return the total number of times that the given nucleobase occurs in the
	 *         given chromosome
	 */
	public static int countSequential(byte[] chromosome, Nucleobase nucleobase) {
		return countRangeSequential(chromosome, nucleobase, 0, chromosome.length);
	}

	/**
	 * Should sequentially count all of the instances of a specific nucleobase that
	 * are within the given range. This method should involve no parallelism. The
	 * range of numbers to search includes all indices in the array that are equal
	 * to or greater than the min index and less than the max index.
	 * 
	 * @param chromosome
	 *            the chromosome to examine, as an array of bytes
	 * @param nucleobase
	 *            the nucleobase to look for in the chromosome
	 * @param min
	 *            the lowest array index in the range to search, inclusive
	 * @param max
	 *            the highest array index in the range to search, exclusive
	 * @return the total number of times that the given nucleobase occurs in the
	 *         given chromosome
	 */
	private static int countRangeSequential(byte[] chromosome, Nucleobase nucleobase, int min, int max) {
		int count = 0;
		for(int i=min; i<max; i++) {
			if(chromosome[i]==nucleobase.toByte()) {
				count++;
			}
		}
		return count;
	}
	public static int count(byte[] chromosome, int low, int high,Nucleobase nucleobase) {
		int count=0;
		for(int i =low;i<high;i++) {
			if(chromosome[i]==nucleobase.toByte()) {
				count++;
			}
		}
		return count;
	}
	/**
	 * Should asynchronously count all of the instances of a specific nucleobase,
	 * submitting two tasks. The chromosome should be split into two halves, and the
	 * "upper" half should be counted at the same time (asynchronously) as the
	 * "lower" half.
	 * 
	 * @param executor
	 *            an {@code ExecutorService} that you should use to submit tasks
	 * @param chromosome
	 *            the chromosome to examine, represented as an array of bytes
	 * @param nucleobase
	 *            the nucleobase to look for in the chromosome
	 * @return the total number of times that the given nucleobase occurs in the
	 *         given chromosome
	 * @throws InterruptedException
	 *             if the current thread was interrupted while waiting
	 * @throws ExecutionException
	 *             if the computation threw an exception
	 * @see Callable
	 * @see Future
	 */
	public static int count2WaySplit(ExecutorService executor, byte[] chromosome, Nucleobase nucleobase)
			throws InterruptedException, ExecutionException {
		int mid = chromosome.length/2;
		Future<Integer> count1Future = executor.submit(()->{
			return count(chromosome,0,mid,nucleobase);
		});
		int count2=count(chromosome,mid,chromosome.length,nucleobase);
		int count1 = count1Future.get();
		return count1+count2;
	}

	/**
	 * Should asynchronously count all of the instances of a specific nucleobase,
	 * creating the given number of tasks. In other words, you should submit n
	 * tasks, each of which counts 1/n of the chromosome. For example, if numTasks
	 * is 8, the chromosome should be divided into 8 pieces, and each of these
	 * pieces should be counted in a separate task. You should use
	 * {@link SliceUtils} for this slicing. You are encouraged (but not required) to
	 * use the {@link ExecutorService#invokeAll(Collection)} method for this part of
	 * the assignment. Note: if numTasks is 2, the behavior of this method will be
	 * the same as {@link #count2WaySplit(byte[], Nucleobase)}.
	 * 
	 * @param executor
	 *            an {@code ExecutorService} that you should use to invoke tasks
	 * @param chromosome
	 *            the chromosome to examine, represented as an array of bytes
	 * @param nucleobase
	 *            the nucleobase to look for in the chromosome
	 * @param numTasks
	 *            the number of tasks to create
	 * @return the total number of times that the given nucleobase occurs in the
	 *         given chromosome
	 * @throws InterruptedException
	 *             if the current thread was interrupted while waiting
	 * @throws ExecutionException
	 *             if the computation threw an exception
	 * @see Callable
	 * @see ExecutorService
	 * @see Future
	 * @see Slice
	 * @see ExecutorService#invokeAll(Collection)
	 */
	public static int countNWaySplit(ExecutorService executor, byte[] chromosome, Nucleobase nucleobase, int numTasks)
			throws InterruptedException, ExecutionException {
		int length = chromosome.length/numTasks;
		List<Callable<Integer>> tasks = new ArrayList<>(numTasks);
		for(int i=0; i< numTasks-1; i++) {
			int low = i*length;
			int high = (i+1)*length;
			
			tasks.add(()->{
				return count(chromosome, low, high, nucleobase);
			});
		}
		tasks.add(()->{
			return count(chromosome,(numTasks-1)*length,chromosome.length,nucleobase);
		});
		List<Future<Integer>> futures = executor.invokeAll(tasks);
		int sum=0;
		for(Future<Integer> future:futures) {
			sum+=future.get();
		}
		return sum;
	}

	/**
	 * Should use a divide-and-conquer approach to count all of the instances of a
	 * specific nucleobase. This public method exists only to start off the
	 * recursion in the private
	 * {@link #countDivideAndConquerKernel(ExecutorService, byte[], Nucleobase, int, int, int)}
	 * method, so no submitting or getting of tasks needs to happen here.
	 * 
	 * @param executor
	 *            an {@code ExecutorService} that you should use to submit tasks
	 * @param chromosome
	 *            the chromosome to examine, represented as an array of bytes
	 * @param nucleobase
	 *            the nucleobase to look for in the chromosome
	 * @param threshold
	 *            the threshold at which the work is no longer worth parallelizing
	 * @return the total number of times that the given nucleobase occurs in the
	 *         given chromosome
	 * @throws InterruptedException
	 *             if the current thread was interrupted while waiting
	 * @throws ExecutionException
	 *             if the computation threw an exception
	 */
	public static int countDivideAndConquer(ExecutorService executor, byte[] chromosome, Nucleobase nucleobase,
			int threshold) throws InterruptedException, ExecutionException {
		return countDivideAndConquerKernel(executor, chromosome, nucleobase, 0,chromosome.length,threshold);
	}

	/**
	 * Should recursively use a divide-and-conquer approach to count all of the
	 * instances of a specific nucleobase in the given range. If the length is less
	 * than or equal to the threshold, it is not worth parallelizing, and this
	 * method should count sequentially using
	 * {@link #countRangeSequential(byte[], Nucleobase, int, int)}. Otherwise, it
	 * should recursively create two tasks for the lower and upper halves of this
	 * range.
	 * 
	 * @param executor
	 *            an {@code ExecutorService} that you should use to submit tasks
	 * @param chromosome
	 *            the chromosome to examine, represented as an array of bytes
	 * @param nucleobase
	 *            the nucleobase to look for in the chromosome
	 * @param min
	 *            the lowest array index in the range to search, inclusive
	 * @param max
	 *            the highest array index in the range to search, exclusive
	 * @param threshold
	 *            the threshold at which the work is no longer worth parallelizing
	 * @return the total number of times that the given nucleobase occurs in the
	 *         given chromosome within the given range
	 * @throws InterruptedException
	 *             if the current thread was interrupted while waiting
	 * @throws ExecutionException
	 *             if the computation threw an exception
	 * @see Callable
	 * @see Future
	 */
	private static int countDivideAndConquerKernel(ExecutorService executor, byte[] chromosome, Nucleobase nucleobase,
			int min, int max, int threshold) throws InterruptedException, ExecutionException {
		int mid = (min+max)/2;
		int length = max-min;
		if(length==0) {
			return 0;
		}
		if(length==1) {
			return count(chromosome,min,max,nucleobase);
		}
		Future<Integer> count1 = executor.submit(()->{
			if(chromosome.length>threshold) {
			return countDivideAndConquerKernel(executor, chromosome, nucleobase, min, mid, threshold);
			}
			else {
				return countRangeSequential(chromosome, nucleobase, min, mid);
			}
		});
		
		
		Future<Integer> count2 = executor.submit(()->{
			if(chromosome.length>threshold) {
				return countDivideAndConquerKernel(executor, chromosome, nucleobase, mid, max, threshold);
				}
				else {
					return countRangeSequential(chromosome, nucleobase, mid, max);
				}
		});
		int sum1 = count1.get();
		int sum2 = count2.get();
		return sum1+sum2;  
		
		
	}
}
