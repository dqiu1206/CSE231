/*******************************************************************************
 * Copyright (C) 2016-2017 Dennis Cosgrove, Ben Choi
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
package kmer.assignment.isolatedbucketdictionary;

import static edu.wustl.cse231s.rice.habanero.Habanero.launchHabaneroApp;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import kmer.assignment.rubric.KMerRubric;

/**
 * @author Ben Choi (benjaminchoi@wustl.edu)
 * 
 *         {@link IsolatedBucketDictionary#compute(Object, java.util.function.BiFunction)}
 */
@KMerRubric(KMerRubric.Category.BUCKET_DICTIONARY)
public class ComputeTest extends AbstractDictionaryTest {
	@Test
	public void test() {
		launchHabaneroApp(() -> {
			IsolatedBucketDictionary<Integer, Double> map = createMap();
			for (int i = 0; i < 10; ++i) {
				double originalValue = i * 0.1;
				map.put(i, originalValue);
				assertEquals(originalValue, map.get(i), /* epsilon= */0.0);

				double remappedValue = i * 0.01;

				Double computeReturnValue = map.compute(i, (key, value) -> {
					return remappedValue;
				});

				assertNotNull("Your compute method should be returning " + remappedValue + ", but is returning null",
						computeReturnValue);
				assertEquals(remappedValue, computeReturnValue, /* epsilon= */0.0);

				double actualValue = map.get(i);
				if (i != 0) {
					assertNotEquals(originalValue, actualValue, /* epsilon= */0.0);
				}
				assertEquals(remappedValue, actualValue, /* epsilon= */0.0);
			}

			assertEquals(0.11, map.compute(11, (key, value) -> {
				return 0.11;
			}).doubleValue(), /* epsilon= */0.0);
		});
	}

	@Test
	public void testReturnValueSimply() {
		launchHabaneroApp(() -> {
			IsolatedBucketDictionary<String, Integer> map = createMap();
			Integer valueShouldBe1 = map.compute("a", (k, v) -> {
				return 1;
			});
			assertEquals(1, valueShouldBe1.intValue());
		});
	}
}
