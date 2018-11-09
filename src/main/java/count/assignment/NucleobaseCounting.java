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
package count.assignment;

import static edu.wustl.cse231s.rice.habanero.Habanero.async;
import static edu.wustl.cse231s.rice.habanero.Habanero.finish;

import java.util.ArrayList;

import edu.rice.hj.api.SuspendableException;
import edu.wustl.cse231s.NotYetImplementedException;
import edu.wustl.cse231s.bioinformatics.Nucleobase;

/**
 * @author David Qiu
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 * 
 *         test: {@link CountTestSuite}.
 */
public class NucleobaseCounting {
	
	

	
	
	public static int calculateMidpoint(int lowerBound, int upperBound) {
		int mid = (lowerBound + upperBound)/2;
		return mid;
	}

	/**
	 * This method should sequentially count all of the instances of a specific
	 * nucleobase. No parallelization needed; nothing needs to run
	 * asynchronously. Without using async or finish, just count the total
	 * number of bases in the sequence that are equal to the given base.
	 * 
	 * @param chromosome
	 *            The chromosome to examine, represented as an array of bytes.
	 *            Each byte in the sequence represents one nucleobase--that is,
	 *            cytosine (C), guanine (G), adenine (A), thymine (T), or
	 *            unknown (N). A byte is used (rather than char, int, String, or
	 *            enum, for example) because a byte is a primitive data type
	 *            that takes up very little memory.
	 * @param targetNucleobase
	 *            The nucleobase to look for in the chromosome. The byte value
	 *            representing this nucleobase occurs some number of times in
	 *            the chromosome. In other words, if you call
	 *            nucleobase.toByte(), the return value can be compared to 
	 *            the bytes in the chromosome array.
	 * @return The total number of times that the given nucleobase occurs in the
	 *         given chromosome.
	 */
	public static int countSequential(byte[] chromosome, Nucleobase targetNucleobase) {
		byte target = targetNucleobase.toByte();
		int count = 0;
		for(int i =0;i<chromosome.length;++i) {
			if (chromosome[i]==target) {
				count++;
			}
		}
		return count;
	}

	/**
	 * This method should asynchronously count all of the instances of a
	 * specific nucleobase, creating two tasks. The chromosome should be split
	 * into two halves, and the "upper" half should be counted at the same time
	 * (asynchronously) as the "lower" half. You will need async and finish for
	 * this method.
	 * 
	 * @param chromosome
	 *            The chromosome to examine, represented as an array of bytes.
	 * @param targetNucleobase
	 *            The nucleobase to look for in the chromosome.
	 * @return The total number of times that the given nucleobase occurs in the
	 *         given chromosome.
	 * @throws SuspendableException used by Habanero to initiate control transfer
	 */
	public static int countParallelUpperLowerSplit(byte[] chromosome, Nucleobase targetNucleobase)
			throws SuspendableException {
		int midpoint = calculateMidpoint(0,chromosome.length);
		byte target = targetNucleobase.toByte();
		int count[]= new int[2];
		
		finish(()->{
			async(()->{
				
				
				for(int i =0;i<midpoint;++i) {
				if(chromosome[i]==target) {
					count[0]++;
				}
				
			}	
				
			});
				
			for(int j = midpoint; j<chromosome.length;++j) {
				if(chromosome[j]==target) {
					count[1]++;
				}
				
			}
				
			
			
		});
		
		
		final int totalSum = count[0]+count[1];
		return totalSum;
		
		
	}



	/**
	 * This method should asynchronously count all of the instances of a
	 * specific nucleobase, creating the given number of tasks. In other words,
	 * you should spawn n tasks, each of which counts for 1/n of the chromosome.
	 * For example, if numTasks is 8, the chromosome should be divided into 8
	 * pieces, and each of these pieces should be counted in a separate
	 * asynchronous task. You should enclose each of these tasks in an async
	 * block, so that each task can run in parallel. Note: if numTasks is 2, the
	 * behavior of this method will be the same as countParallelUpperLowerSplit.
	 * 
	 * @param chromosome
	 *            The chromosome to examine, represented as an array of bytes.
	 * @param targetNucleobase
	 *            The nucleobase to look for in the chromosome.
	 * @param numTasks
	 *            The number of tasks to create.
	 * @return The total number of times that the given nucleobase occurs in the
	 *         given chromosome.
	 * @throws SuspendableException used by Habanero to initiate control transfer
	 */
	
	public static int countArray(byte[] chromosome, byte target, int loop, int length, boolean last) {
		int start = loop*length;
		int count = 0;
		if(!last) {
		
		int end = start + length;
		for(int i =start;i<end;i++) {
			if(chromosome[i]==target) {
				count++;
			}
		}
		return count;
		}
		else {
			for(int i = start;i<chromosome.length;i++) {
				if(chromosome[i]==target) {
					count++;
				}
			}
			return count;
		}
		
	}
	
	public static int countParallelNWaySplit(byte[] chromosome, Nucleobase targetNucleobase, int numTasks)
			throws SuspendableException {
		
		
		
		
		int[] parCount = new int[numTasks];
		byte target = targetNucleobase.toByte();
		
		int length = (chromosome.length)/numTasks;
		
		
		int[] loop = new int[1];
		
	
		finish(()->{
			
				
			
			for(loop[0]=0;loop[0]<numTasks;loop[0]++) {
				final int ii = loop[0];
				
				
				async(()->{
					if(ii==(numTasks-1)) {
						parCount[ii] = countArray(chromosome,target,ii, length,true);
					}
					else {
						parCount[ii] = countArray(chromosome,target,ii, length, false);
					}
					
				});
			
					
			}
			
		});
		int finalCount = 0;
		for(int k = 0;k<numTasks;k++) {
			finalCount = finalCount + parCount[k];
		}
		return finalCount;
	}
}
