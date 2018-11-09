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
package edu.wustl.cse231s.rice.habanero.implementation.classic.contrib.runtime.accumulator;

import java.math.BigDecimal;

import edu.wustl.cse231s.rice.habanero.contrib.api.NumberReductionOperator;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
public abstract class BigDecimalAccumulator extends Accumulator<Double> {
	public BigDecimalAccumulator(NumberReductionOperator operator) {
		switch (operator) {
		case SUM:
		case PRODUCT:
			break;
		default:
			throw new IllegalArgumentException();
		}
		this.operator = operator;
		this.resultVal = new BigDecimal(this.operator.getInitialDoubleValue());
	}

	@Override
	public final Double get() {
		if (this.isAccessible()) {
			//pass
		} else {
			this.checkOwnership(CheckOwnershipId.GET);
		}
		return this.resultVal.doubleValue();
	}

	protected abstract void putAccessible(double value);

	@Override
	public final void put(Double v) {
		double value = v.doubleValue();
		if (this.isAccessible()) {
			this.putAccessible(value);
		} else {
			this.checkOwnership(CheckOwnershipId.PUT);
			switch (this.operator) {
			case SUM:
				this.resultVal = this.resultVal.add(new BigDecimal(value));
				break;
			case PRODUCT:
				this.resultVal = this.resultVal.multiply(new BigDecimal(value));
				break;
			default:
				throw new Error();
			}
		}
	}

	protected final NumberReductionOperator operator;
	protected BigDecimal resultVal;

}
