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
package sort.core.merge;

/**
 * @author Aaron Handleman
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
public class ArrayBuffersMergeableData implements MergeableData {
	public ArrayBuffersMergeableData(int[] original) {
		this.solutionBuffer = new int[original.length];
		System.arraycopy(original, 0, this.solutionBuffer, 0, this.solutionBuffer.length);
		this.tempBuffer = new int[this.solutionBuffer.length];
	}

	@Override
	public int getLength() {
		return this.solutionBuffer.length;
	}

	@Override
	public int[] getSolution() {
		return this.solutionBuffer;
	}

	@Override
	public void merge(int min, int mid, int max) {
		int indexA = min;
		int indexB = mid;
		for (int i = min; i < max; i++) {
			if (indexA >= mid) {
				// lower end complete so take from upper end
				this.tempBuffer[i] = this.solutionBuffer[indexB++];
				continue;
			}
			if (indexB >= max) {
				// upper end complete so take from lower end
				this.tempBuffer[i] = this.solutionBuffer[indexA++];
				continue;
			}
			if (this.solutionBuffer[indexA] <= this.solutionBuffer[indexB]) {
				this.tempBuffer[i] = this.solutionBuffer[indexA++];
			} else {
				this.tempBuffer[i] = this.solutionBuffer[indexB++];
			}
		}

		// copy back from temporary buffer
		System.arraycopy(tempBuffer, min, solutionBuffer, min, max - min);
	}

	private final int[] solutionBuffer;
	private final int[] tempBuffer;
}
