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

import static edu.wustl.cse231s.rice.habanero.Habanero.launchHabaneroApp;

import java.io.File;

import count.core.NucleobaseCountUtils;
import edu.rice.hj.api.SuspendableException;
import edu.wustl.cse231s.NotYetImplementedException;
import edu.wustl.cse231s.bioinformatics.Nucleobase;
import edu.wustl.cse231s.bioinformatics.io.ChromosomeResource;
import edu.wustl.cse231s.bioinformatics.io.FastaUtils;
import edu.wustl.cse231s.download.DownloadUtils;
import edu.wustl.cse231s.timing.ImmutableTimer;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
public class NucleobaseCountTiming {
	private static void timeCountSequential(byte[] chromosome, Nucleobase targetNucleobase, int expectedCount) {
		try {
			ImmutableTimer timer = new ImmutableTimer("             NucleobaseCounting.countSequential");
			int count = NucleobaseCounting.countSequential(chromosome, targetNucleobase);
			long dt = timer.mark();
			if (count == expectedCount) {
				timer.printResults(dt);
			} else {
				throw new RuntimeException(count + " does not equal expect count " + expectedCount);
			}
		} catch (NotYetImplementedException nyie) {
			System.out.println("  MergeSort.parallelMergesort: NOT YET IMPLEMENTED");
		}
	}
	private static void timeCountParallelUpperLower(byte[] chromosome, Nucleobase targetNucleobase, int expectedCount) throws SuspendableException {
		try {
			ImmutableTimer timer = new ImmutableTimer("NucleobaseCounting.countParallelUpperLowerSplit");
			int count = NucleobaseCounting.countParallelUpperLowerSplit(chromosome, targetNucleobase);
			long dt = timer.mark();
			if (count == expectedCount) {
				timer.printResults(dt);
			} else {
				throw new RuntimeException(count + " does not equal expect count " + expectedCount);
			}
		} catch (NotYetImplementedException nyie) {
			System.out.println("  MergeSort.parallelMergesort: NOT YET IMPLEMENTED");
		}
	}

	private static void timeCountParallelNWaySplit(byte[] chromosome, Nucleobase targetNucleobase, int numTasks, int expectedCount) throws SuspendableException {
		try {
			ImmutableTimer timer = new ImmutableTimer(String.format("%47s", "NucleobaseCounting.countParallelNWaySplit(" + numTasks + ")"));
			int count = NucleobaseCounting.countParallelNWaySplit(chromosome, targetNucleobase, numTasks);
			long dt = timer.mark();
			if (count == expectedCount) {
				timer.printResults(dt);
			} else {
				throw new RuntimeException(count + " does not equal expect count " + expectedCount);
			}
		} catch (NotYetImplementedException nyie) {
			System.out.println("  MergeSort.parallelMergesort: NOT YET IMPLEMENTED");
		}
	}

	public static void main(String[] args) throws Exception {
		ChromosomeResource chromosomeResource = ChromosomeResource.X;
		File file = DownloadUtils.getDownloadedFile(chromosomeResource.getUrl(),
				chromosomeResource.getFileLength());
		byte[] chromosome = FastaUtils.read(file);
		
		Nucleobase targetNucleobase = Nucleobase.ADENINE;
		int expectedCount = NucleobaseCountUtils.countSequential(chromosome, targetNucleobase);
		
		int numProcessors = Runtime.getRuntime().availableProcessors();
		launchHabaneroApp(() -> {
			final int ITERATION_COUNT = 10;
			for (int iteration = 0; iteration < ITERATION_COUNT; iteration++) {

				timeCountSequential(chromosome, targetNucleobase, expectedCount);
				timeCountParallelUpperLower(chromosome, targetNucleobase, expectedCount);
				timeCountParallelNWaySplit(chromosome, targetNucleobase, numProcessors, expectedCount);
				timeCountParallelNWaySplit(chromosome, targetNucleobase, numProcessors*2, expectedCount);
				timeCountParallelNWaySplit(chromosome, targetNucleobase, numProcessors*10, expectedCount);
				timeCountParallelNWaySplit(chromosome, targetNucleobase, numProcessors*100, expectedCount);

				System.out.println();
			}
		});

	}

}
