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
package kmer.warmup.bytearrayoffsetmap;

import java.util.Map;

import kmer.core.ByteArrayOffsetKMer;
import kmer.core.KMerCount;
import kmer.core.KMerUtils;

/**
 * A {@link KMerCount} implementation that wraps a {@link Map} (from k-mer as a
 * {@link ByteArrayOffsetKMer} to count).
 * 
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
/* package-private */ class ByteArrayOffsetKMerMapKMerCount implements KMerCount {

	/**
	 * Constructs a LongMapKMerCount that wraps the given Map.
	 * 
	 * @param map
	 *            a Map (from k-mer to count) to wrap
	 */
	public ByteArrayOffsetKMerMapKMerCount(Map<ByteArrayOffsetKMer, Integer> map) {
		this.map = map;
	}

	@Override
	public int getCount(byte[] kMer) {
		Integer count = this.map.get(KMerUtils.toSequenceSlice(kMer));
		return count != null ? count : 0;
	}

	private final Map<ByteArrayOffsetKMer, Integer> map;
}
