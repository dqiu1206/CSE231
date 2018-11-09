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
package edu.wustl.cse231s.rice.habanero.implementation.classic.contrib.runtime;


import edu.rice.hj.api.HjSuspendable;
import edu.rice.hj.api.SuspendableException;
import edu.wustl.cse231s.rice.habanero.Habanero;
import edu.wustl.cse231s.rice.habanero.contrib.api.AccumulatorReducer;
import edu.wustl.cse231s.rice.habanero.contrib.api.ContentionLevel;
import edu.wustl.cse231s.rice.habanero.contrib.api.DoubleAccumulationDeterminismPolicy;
import edu.wustl.cse231s.rice.habanero.contrib.api.FinishAccumulator;
import edu.wustl.cse231s.rice.habanero.contrib.api.NumberReductionOperator;
import edu.wustl.cse231s.rice.habanero.implementation.classic.contrib.runtime.accumulator.AccumulatorManager;
import edu.wustl.cse231s.rice.habanero.implementation.classic.contrib.runtime.accumulator.EagerBigDecimalAccumulator;
import edu.wustl.cse231s.rice.habanero.implementation.classic.contrib.runtime.accumulator.EagerDoubleAccumulator;
import edu.wustl.cse231s.rice.habanero.implementation.classic.contrib.runtime.accumulator.EagerIntegerAccumulator;
import edu.wustl.cse231s.rice.habanero.implementation.classic.contrib.runtime.accumulator.EagerReducerAccumulator;
import edu.wustl.cse231s.rice.habanero.implementation.classic.contrib.runtime.accumulator.LazyBigDecimalAccumulator;
import edu.wustl.cse231s.rice.habanero.implementation.classic.contrib.runtime.accumulator.LazyDoubleAccumulator;
import edu.wustl.cse231s.rice.habanero.implementation.classic.contrib.runtime.accumulator.LazyIntegerAccumulator;
import edu.wustl.cse231s.rice.habanero.implementation.classic.contrib.runtime.accumulator.LazyReducerAccumulator;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
public enum AccumulatorRuntime {
	;
	public static FinishAccumulator<Integer> newIntegerFinishAccumulator(NumberReductionOperator operator, ContentionLevel contentionLevel) {
		if (contentionLevel == ContentionLevel.HIGH) {
			return new LazyIntegerAccumulator(operator);
		} else {
			return new EagerIntegerAccumulator(operator);
		}
	}

	public static FinishAccumulator<Double> newDoubleFinishAccumulator(NumberReductionOperator operator, ContentionLevel contentionLevel, DoubleAccumulationDeterminismPolicy determinismPolicy) {
		if( determinismPolicy == DoubleAccumulationDeterminismPolicy.DETERMINISTIC ){
			switch( operator ) {
			case SUM:
			case PRODUCT:
				if (contentionLevel == ContentionLevel.HIGH) {
					return new LazyBigDecimalAccumulator(operator);
				} else {
					return new EagerBigDecimalAccumulator(operator);
				}
			default:
				//pass
			}
		}
		if (contentionLevel == ContentionLevel.HIGH) {
			return new LazyDoubleAccumulator(operator);
		} else {
			return new EagerDoubleAccumulator(operator);
		}
	}

	public static <T> FinishAccumulator<T> newReducerFinishAccumulator(AccumulatorReducer<T> reducer, ContentionLevel contentionLevel) {
		if (contentionLevel == ContentionLevel.HIGH) {
			return new LazyReducerAccumulator<>(reducer);
		} else {
			return new EagerReducerAccumulator<>(reducer);
		}
	}

	public static void finish(FinishAccumulator<?>[] accs, HjSuspendable body) throws SuspendableException {
		AccumulatorManager accumulatorManager = new AccumulatorManager(accs);
		accumulatorManager.openFinishAccumulators();
		try {
			Habanero.finish(body);
		} finally {
			accumulatorManager.closeFinishAccumulators();
		}
	}
}
