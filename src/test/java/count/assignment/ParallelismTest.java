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
import static org.hamcrest.Matchers.either;
import static org.hamcrest.Matchers.is;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;

import count.assignment.rubric.CountRubric;
import edu.rice.hj.api.HjSuspendable;
import edu.wustl.cse231s.bioinformatics.Nucleobase;
import edu.wustl.cse231s.rice.habanero.implementation.bookkeeping.BookkeepingHabaneroImplementation;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
public class ParallelismTest {
	private BookkeepingHabaneroImplementation bookkeep(HjSuspendable suspendable) {
		BookkeepingHabaneroImplementation bookkeeping = new BookkeepingHabaneroImplementation();
		launchHabaneroApp(bookkeeping, suspendable);
		return bookkeeping;
	}

	private void testFinishAndAsyncCounts(int expectedFinishCount, int expectedAsyncCountAllowingForMinus1,
			HjSuspendable suspendable) {
		BookkeepingHabaneroImplementation bookkeeping = bookkeep(suspendable);

		int actualFinishCount = bookkeeping.getFinishInvocationCount();
		if (expectedFinishCount != 0) {
			Assert.assertNotEquals("finish never called; expected: " + expectedFinishCount, 0, actualFinishCount);
		}
		Assert.assertEquals(expectedFinishCount, actualFinishCount);

		int totalTaskCount = bookkeeping.getTaskCount();

		if (expectedAsyncCountAllowingForMinus1 > 1) {
			Assert.assertNotEquals("async never called; expected: " + (expectedAsyncCountAllowingForMinus1 - 1) + " or "
					+ expectedAsyncCountAllowingForMinus1, 0, totalTaskCount);
		}
		if (expectedAsyncCountAllowingForMinus1 > 2) {
			Assert.assertNotEquals("async only called once; expected: " + (expectedAsyncCountAllowingForMinus1 - 1)
					+ " or " + expectedAsyncCountAllowingForMinus1, 1, totalTaskCount);
		}

		Assert.assertThat(totalTaskCount,
				either(is(expectedAsyncCountAllowingForMinus1)).or(is(expectedAsyncCountAllowingForMinus1 - 1)));
	}

	private static byte[] createChromosome(int length, Nucleobase nucleobase) {
		byte[] result = new byte[length];
		Arrays.fill(result, nucleobase.toByte());
		return result;
	}

	@Test
	@CountRubric(CountRubric.Category.UPPER_LOWER)
	public void testUpperLower() {
		Nucleobase nucleobase = Nucleobase.ADENINE;
		byte[] chromosome = createChromosome(100, nucleobase);
		testFinishAndAsyncCounts(1, 2, () -> {
			NucleobaseCounting.countParallelUpperLowerSplit(chromosome, nucleobase);
		});
	}

	@Test
	@CountRubric(CountRubric.Category.NWAY)
	public void testNWaySplit() {
		Nucleobase nucleobase = Nucleobase.ADENINE;
		byte[] chromosome = createChromosome(100, nucleobase);
		int numTasks = 10;
		testFinishAndAsyncCounts(1, numTasks, () -> {
			NucleobaseCounting.countParallelNWaySplit(chromosome, nucleobase, numTasks);
		});
	}
}
