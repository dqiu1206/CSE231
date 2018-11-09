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
package matrixmultiply.studio;

import static edu.wustl.cse231s.rice.habanero.Habanero.launchHabaneroApp;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;

import edu.rice.hj.api.HjSuspendable;
import edu.rice.hj.api.SuspendableException;
import edu.wustl.cse231s.rice.habanero.implementation.bookkeeping.BookkeepingHabaneroImplementation;
import matrixmultiply.core.MatrixMultiplier;
import matrixmultiply.core.MatrixUtils;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
public class MatrixMultiplyParallelTest {
	private static double[][] createRandom(int size) {
		double[][] m = new double[size][size];
		MatrixUtils.setAllRandom(m);
		return m;
	}

	private static BookkeepingHabaneroImplementation bookkeep(HjSuspendable suspendable) {
		BookkeepingHabaneroImplementation bookkeeping = new BookkeepingHabaneroImplementation();
		launchHabaneroApp(bookkeeping, suspendable);
		return bookkeeping;
	}

	private static BookkeepingHabaneroImplementation test(int size, MatrixMultiplier matrixMultiplier) {
		double[][] a = createRandom(size);
		double[][] b = createRandom(size);
		double[][] expected = MatrixMultiplyTestUtils.multiply(a, b);
		return bookkeep(() -> {
			double[][] actual = matrixMultiplier.multiply(a, b);
			Assert.assertTrue(Arrays.deepEquals(expected, actual));
		});
	}

	@Test
	public void testForallForall() throws SuspendableException {
		int SIZE = 16;
		BookkeepingHabaneroImplementation bookkeepingHabanero = test(SIZE,
				new ForallForallMatrixMultiplier());

		int forasyncCount = bookkeepingHabanero.getForasyncRegionInvocationCount();
		int forasync2dCount = bookkeepingHabanero.getForasync2dRegionInvocationCount();
		int finishCount = bookkeepingHabanero.getFinishInvocationCount();
		Assert.assertNotEquals(0, forasyncCount);
		Assert.assertNotEquals(1, forasyncCount);
		Assert.assertEquals(SIZE + 1, forasyncCount);

		Assert.assertNotEquals(0, finishCount);
		Assert.assertNotEquals(1, finishCount);
		Assert.assertEquals(SIZE + 1, finishCount);

		Assert.assertEquals(0, forasync2dCount);
	}

	@Test
	public void testForall2d() throws SuspendableException {
		int SIZE = 16;
		BookkeepingHabaneroImplementation bookkeepingHabanero = test(SIZE,
				new Forall2dMatrixMultiplier());

		int forasyncCount = bookkeepingHabanero.getForasyncRegionInvocationCount();
		int forasync2dCount = bookkeepingHabanero.getForasync2dRegionInvocationCount();
		int forasync2dChunkedCount = bookkeepingHabanero.getForasync2dRegionChunkedInvocationCount();
		int finishCount = bookkeepingHabanero.getFinishInvocationCount();
		Assert.assertEquals(0, forasyncCount);
		Assert.assertEquals(0, forasync2dChunkedCount);
		Assert.assertEquals(1, forasync2dCount);
		Assert.assertEquals(1, finishCount);
	}

	@Test
	public void testForall2dChunked() throws SuspendableException {
		int SIZE = 16;
		BookkeepingHabaneroImplementation bookkeepingHabanero = test(SIZE,
				new Forall2dChunkedMatrixMultiplier());

		int forasyncCount = bookkeepingHabanero.getForasyncRegionInvocationCount();
		int forasync2dCount = bookkeepingHabanero.getForasync2dRegionInvocationCount();
		int forasync2dChunkedCount = bookkeepingHabanero.getForasync2dRegionChunkedInvocationCount();
		int finishCount = bookkeepingHabanero.getFinishInvocationCount();
		Assert.assertEquals(0, forasyncCount);
		Assert.assertEquals(0, forasync2dCount);
		Assert.assertEquals(1, forasync2dChunkedCount);
		Assert.assertEquals(1, finishCount);
	}
}
