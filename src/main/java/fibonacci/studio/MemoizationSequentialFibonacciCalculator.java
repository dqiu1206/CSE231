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
package fibonacci.studio;

import static edu.wustl.cse231s.rice.habanero.Habanero.doWork;
import static edu.wustl.cse231s.rice.habanero.Habanero.future;

import edu.rice.hj.api.HjFuture;
import edu.wustl.cse231s.NotYetImplementedException;
import edu.wustl.cse231s.asymptoticanalysis.OrderOfGrowth;
import fibonacci.core.FibonacciCalculator;

/**
 * @author David Qiu
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
public class MemoizationSequentialFibonacciCalculator implements FibonacciCalculator {
	private long fibonacciMemo(long[] memos, int n) {
		
		if(n==0) {
			return memos[(memos.length-1)];
		}
		
		int curr = memos.length - n -1;
		int next = curr+1;
		if(n==memos.length-1) {
			memos[0]=0;
			memos[1]=1;
		}
		else {
			long value = memos[curr]+memos[curr-1];
			memos[next] = value;
		}
		return fibonacciMemo(memos,n-1);
	}
	//0 1 1 2 3 5 8
	//0 1 2 3 4 5 6
	//n=6 length=7
	//6 curr= 0 next = 1 value[1] = 1
	//5 curr=1 next = 2 value[2]= 1
	//4 2 3 2 value[3]=2
	//3 3 4 3
	//2 4 5 5
	//1 5 6 8 value[6]=8
	//0 return value[6]
	//n=0 return memos[0]
	//n=1 0 1 
	@Override
	public long fibonacci(int n) {
		long[] memos = new long[n + 1];
		return fibonacciMemo(memos, n);
	}

	@Override
	public OrderOfGrowth getOrderOfGrowth() {
		return OrderOfGrowth.LINEAR;
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
