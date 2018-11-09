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
package slice.studio;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import slice.core.Slice;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
public class NSlicesTest {
	private void test(Object[] data, int numSlices) {
		List<Slice<Object[]>> slices = Slices.createNSlices(data, numSlices);
		Assert.assertNotNull(slices);
		Assert.assertEquals(numSlices, slices.size());
		for (Slice<Object[]> slice : slices) {
			Assert.assertSame(data, slice.getData());
		}
		int expectedId = 0;
		for (Slice<Object[]> slice : slices) {
			Assert.assertEquals(expectedId, slice.getSliceIndexId());
			expectedId++;
		}
		if (numSlices > 0) {
			int expectedMin = 0;
			for (Slice<Object[]> slice : slices) {
				Assert.assertEquals(expectedMin, slice.getMinInclusive());
				expectedMin = slice.getMaxExclusive();
			}
			int expectedMax = expectedMin;
			Assert.assertEquals(data.length, expectedMax);
		}
	}

	@Test
	public void testDivisibleData() {
		this.test(new Object[10], 2);
	}

	@Test
	public void testNotDivisibleData() {
		this.test(new Object[11], 2);
	}
}