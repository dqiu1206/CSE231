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
package kmer.assignment.rubric;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface KMerRubric {
	public static enum Category {
		THRESHOLD_SLICES("Correct ThresholdSlices"),
		LONG_CONCURRENT_HASH_MAP("Correct Long ConcurrentHashMap"),
		INT_ARRAY("Correct Int Array"),
		ATOMIC_INTEGER_ARRAY("Correct AtomicIntegerArray"),
		BUCKET_DICTIONARY_COUNTER("Correct BucketDictionaryCounter"),
		BUCKET_DICTIONARY(" BucketDictionary implementation");

		private final String title;

		private Category(String title) {
			this.title = title;
		}

		public String getTitle() {
			return title;
		}
	}

	Category[] value();
}
