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
package edu.wustl.cse231s.rice.habanero.options;

import edu.wustl.cse231s.rice.habanero.contrib.api.FinishAccumulator;

/**
 * @author Dennis Cosgrove (cosgroved@wustl.edu)
 */
public final class RegisterAccumulatorsOption {
	public RegisterAccumulatorsOption(FinishAccumulator<?> accumulatorA, FinishAccumulator<?>[] accumulatorBtoZ) {
		this.accumulators = new FinishAccumulator[1 + accumulatorBtoZ.length];
		this.accumulators[0] = accumulatorA;
		System.arraycopy(accumulatorBtoZ, 0, this.accumulators, 1, accumulatorBtoZ.length);
	}

	public FinishAccumulator<?>[] getAccumulators() {
		return accumulators;
	}

	private final FinishAccumulator<?>[] accumulators;
}
