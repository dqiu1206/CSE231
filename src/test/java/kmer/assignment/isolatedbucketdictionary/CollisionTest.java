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
package kmer.assignment.isolatedbucketdictionary;

import static edu.wustl.cse231s.rice.habanero.Habanero.launchHabaneroApp;
import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

import org.junit.Test;

import kmer.assignment.rubric.KMerRubric;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 * 
 *         {@link IsolatedBucketDictionary#put(Object, Object)}
 */
@KMerRubric(KMerRubric.Category.BUCKET_DICTIONARY)
public class CollisionTest extends AbstractDictionaryTest {
	private static class ValidButPessimalHashObject {
		private final String value;

		public ValidButPessimalHashObject(String value) {
			this.value = value;
		}

		@Override
		public int hashCode() {
			return 1;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			ValidButPessimalHashObject other = (ValidButPessimalHashObject) obj;
			return Objects.equals(this.value, other.value);
		}
	}

	private static <K, V> void assertMapsAreEquivalent(Map<K, V> truthAndBeauty,
			IsolatedBucketDictionary<K, V> studentMap) {
		for (Entry<K, V> entry : truthAndBeauty.entrySet()) {
			assertEquals(entry.getValue(), studentMap.get(entry.getKey())); // TODO: check ==
		}
	}

	private static <K, V> void putOnBoth(Map<K, V> truthAndBeauty, IsolatedBucketDictionary<K, V> studentMap, K key,
			V value) {
		truthAndBeauty.put(key, value);
		studentMap.put(key, value);
		assertMapsAreEquivalent(truthAndBeauty, studentMap);
	}

	@Test
	public void testPut() {
		launchHabaneroApp(() -> {
			Map<ValidButPessimalHashObject, Integer> truthAndBeauty = new HashMap<>();
			IsolatedBucketDictionary<ValidButPessimalHashObject, Integer> studentMap = createMap();
			putOnBoth(truthAndBeauty, studentMap, new ValidButPessimalHashObject("fred"), 231);
			putOnBoth(truthAndBeauty, studentMap, new ValidButPessimalHashObject("george"), 341);
		});
	}
}
