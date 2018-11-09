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

import static edu.wustl.cse231s.rice.habanero.Habanero.forall;
import static edu.wustl.cse231s.rice.habanero.Habanero.forall2d;

import edu.rice.hj.api.SuspendableException;
import edu.wustl.cse231s.NotYetImplementedException;
import matrixmultiply.core.MatrixMultiplier;
import matrixmultiply.core.MatrixUtils;

/**
 * @author David Qiu
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
public class ForallForallMatrixMultiplier implements MatrixMultiplier {
	/**
	 * This implementation should perform matrix multiplication using two parallel
	 * forall loops. This should not look too different from the regular for loop
	 * implementation.
	 */
	@Override
	
	public double[][] multiply(double[][] a, double[][] b) throws SuspendableException {
		double[][] result = MatrixUtils.createMultiplyResultBufferInitializedToZeros(a, b);
		int n = a.length;
		int m = b[0].length;
		int p = a[0].length;
		forall(0,n,(i)->{
			forall(0,p,(j)->{
				for(int k=0;k<m;k++) {
					result[i][j]+=a[i][k]*b[k][j];
				}
			});
		});
		return result;
		
	}
}
