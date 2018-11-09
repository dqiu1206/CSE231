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

import static edu.wustl.cse231s.rice.habanero.Habanero.launchHabaneroApp;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import edu.wustl.cse231s.asymptoticanalysis.OrderOfGrowth;
import fibonacci.core.FibonacciCalculator;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
@RunWith(Parameterized.class)
public class IntInsteadOfLongTest {
	@Parameters(name = "{0}")
	public static Collection<Object[]> getConstructorArguments() {
		List<FibonacciCalculator> fibonacciCalculators = Arrays.asList(
				new RecurrenceRelationSequentialFibonacciCalculator(),
				new RecurrenceRelationParallelFibonacciCalculator(), 
				new MemoizationSequentialFibonacciCalculator(),
				new MemoizationParallelFibonacciCalculator(),
				new DynamicIterativeSequentialFibonacciCalculator());

		List<Object[]> list = new LinkedList<>();
		for (FibonacciCalculator fibonacciCalculator : fibonacciCalculators) {
			OrderOfGrowth orderOfGrowth = fibonacciCalculator.getOrderOfGrowth();
			if(orderOfGrowth.ordinal() <= OrderOfGrowth.LINEAR.ordinal()) {
				list.add(new Object[] { fibonacciCalculator });
			}
		}
		return list;
	}
	
	public IntInsteadOfLongTest(FibonacciCalculator fibonacciCalculator) {
		this.fibonacciCalculator = fibonacciCalculator;
	}

	@Test
	public void test() {
		long expected46 = FibonacciTest.truthAndBeautyFibonacci(46); // just below Integer.MAX_VALUE
		long expected47 = FibonacciTest.truthAndBeautyFibonacci(47); // just above Integer.MAX_VALUE
		launchHabaneroApp(() -> {
			long actual46 = this.fibonacciCalculator.fibonacci(46);
			long actual47 = this.fibonacciCalculator.fibonacci(47);
			
			Assert.assertEquals(expected46, actual46);
			Assert.assertNotEquals("mistakenly using int (32 bits) instead of long (64 bits) in calculation?  answer equals the bottom 32 bits of the expected answer", (int)expected47, actual47);
			Assert.assertEquals(expected47, actual47);
		});
	}

	private final FibonacciCalculator fibonacciCalculator;
}
