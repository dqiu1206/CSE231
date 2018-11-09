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
import java.util.Collection;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import edu.rice.hj.api.SuspendableException;
import edu.wustl.cse231s.junit.JUnitUtils;
import matrixmultiply.core.MatrixMultiplier;
import matrixmultiply.core.MatrixUtils;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
@RunWith(Parameterized.class)
public class MatrixMultiplyTest {
	@Parameters(name = "{0} size={1}")
	public static Collection<Object[]> getConstructorArguments() {
		MatrixMultiplier[] matrixMultipliers = { new ForallForallMatrixMultiplier(), new Forall2dMatrixMultiplier(),
				new Forall2dChunkedMatrixMultiplier() };
		Integer[] sizes = { 4, 16 };
		return JUnitUtils.toParameterizedArguments2(matrixMultipliers, sizes);
	}

	public MatrixMultiplyTest(MatrixMultiplier matrixMultiplier, int size) {
		this.matrixMultiplier = matrixMultiplier;
		this.size = size;
	}

	private static double[][] copy(double[][] m) {
		double[][] result = new double[m.length][m[0].length];
		for (int i = 0; i < m.length; i++) {
			System.arraycopy(m[i], 0, result[i], 0, result[i].length);
		}
		return result;
	}

	@Test
	public void testRandomlyFilled() throws SuspendableException {
		int A_ROW_COUNT = this.size;
		int B_COL_COUNT = this.size;
		int A_COL_AND_B_ROW_COUNT = this.size;
		double[][] originalA = new double[A_ROW_COUNT][A_COL_AND_B_ROW_COUNT];
		double[][] originalB = new double[A_COL_AND_B_ROW_COUNT][B_COL_COUNT];
		MatrixUtils.setAllRandom(originalA);
		MatrixUtils.setAllRandom(originalB);
		double[][] a = copy(originalA);
		Assert.assertTrue(Arrays.deepEquals(originalA, a));
		double[][] b = copy(originalB);
		Assert.assertTrue(Arrays.deepEquals(originalB, b));
		launchHabaneroApp(() -> {
			double[][] expected = MatrixMultiplyTestUtils.multiply(a, b);
			double[][] actual = this.matrixMultiplier.multiply(a, b);
			Assert.assertTrue("do not mutate parameter a", Arrays.deepEquals(originalA, a));
			Assert.assertTrue("do not mutate parameter b", Arrays.deepEquals(originalB, b));
			Assert.assertTrue("incorrect result", Arrays.deepEquals(expected, actual));
		});
	}

	@Test
	public void testIdentity() throws SuspendableException {
		int A_ROW_COUNT = this.size;
		int B_COL_COUNT = this.size;
		int A_COL_AND_B_ROW_COUNT = this.size;
		double[][] a = new double[A_ROW_COUNT][A_COL_AND_B_ROW_COUNT];
		double[][] b = new double[A_COL_AND_B_ROW_COUNT][B_COL_COUNT];
		MatrixUtils.setIdentity(a);
		MatrixUtils.setIdentity(b);
		Assert.assertTrue(MatrixUtils.isIdentity(a));
		Assert.assertTrue(MatrixUtils.isIdentity(b));
		double[][] expected = MatrixMultiplyTestUtils.multiply(a, b);
		Assert.assertTrue(MatrixUtils.isIdentity(expected));
		launchHabaneroApp(() -> {
			double[][] actual = this.matrixMultiplier.multiply(a, b);
			Assert.assertTrue("incorrect result", Arrays.deepEquals(expected, actual));
		});
	}

	private final MatrixMultiplier matrixMultiplier;
	private final int size;

}
