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
package fibonacci.fun;

import static edu.wustl.cse231s.rice.habanero.Habanero.doWork;
import static edu.wustl.cse231s.rice.habanero.Habanero.future;

import edu.rice.hj.api.HjFuture;
import edu.wustl.cse231s.NotYetImplementedException;
import edu.wustl.cse231s.asymptoticanalysis.OrderOfGrowth;
import edu.wustl.cse231s.goldenratio.GoldenRatioUtils;
import fibonacci.core.FibonacciCalculator;

/**
 * @author David Qiu
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
public class RoundPhiToTheNOverSqrt5SequentialFibonacciCalculator implements FibonacciCalculator {
	@Override
	public long fibonacci(int n) {
		throw new NotYetImplementedException();
	}

	@Override
	public OrderOfGrowth getOrderOfGrowth() {
		// order of sqrt (likely log(n)) and pow (likely constant)
		return OrderOfGrowth.LOGARITHMIC;
	}
	
	@Override
	public boolean isSusceptibleToStackOverflowError() {
		return true;
	}
	
	@Override
	public String toString() {
		return this.getClass().getSimpleName();
	}
}