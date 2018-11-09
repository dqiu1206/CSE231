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
package matrixmultiply.demo;

import static edu.wustl.cse231s.rice.habanero.Habanero.forall;
import static edu.wustl.cse231s.rice.habanero.Habanero.forseq;
import static edu.wustl.cse231s.rice.habanero.Habanero.forseq2d;
import static edu.wustl.cse231s.rice.habanero.Habanero.myGroup;
import static edu.wustl.cse231s.rice.habanero.Habanero.newRectangularRegion1D;
import static edu.wustl.cse231s.rice.habanero.Habanero.newRectangularRegion2D;
import static edu.wustl.cse231s.rice.habanero.Habanero.numWorkerThreads;

import edu.rice.hj.api.HjRegion.HjRegion1D;
import edu.rice.hj.api.HjRegion.HjRegion2D;
import edu.rice.hj.api.SuspendableException;
import matrixmultiply.core.MatrixMultiplier;
import matrixmultiply.core.MatrixUtils;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
public class Forall2dGroupedMatrixMultiplier implements MatrixMultiplier {
	/**
	 * This is just here as a reference and is not mandatory. This
	 * implementation just shows you how to do iteration grouping manually.
	 */
	@Override
	public double[][] multiply(double[][] a, double[][] b) throws SuspendableException {
		double[][] result = MatrixUtils.createMultiplyResultBufferInitializedToZeros(a, b);
		int n = a.length;
		int m = b[0].length;
		int p = a[0].length;
		int numTasks = numWorkerThreads();
		HjRegion2D iterSpace = newRectangularRegion2D(0, n, 0, m);
		int grid1 = numTasks / 2;
		int grid2 = 2;
		forall(0, numTasks, (groupId) -> {
			int groupId1 = groupId / 2;
			int groupId2 = groupId % 2;
			HjRegion2D group = myGroup(groupId1, groupId2, iterSpace, grid1, grid2);
			forseq2d(group, (i, j) -> {
				// result.values[i][j] = 0.0;
				for (int k = 0; k < p; k++) {
					result[i][j] += a[i][k] * b[k][j];
				}
			});
		});
		return result;
	}
}
