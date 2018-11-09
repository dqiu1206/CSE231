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
package kmer.core;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
public final class ByteArrayOffsetKMer {
	public ByteArrayOffsetKMer(byte[] sequence, int minInclusive, int maxExclusive) {
		this.sequence = sequence;
		this.minInclusive = minInclusive;
		this.maxExclusive = maxExclusive;
	}

	public byte[] getSequence() {
		return this.sequence;
	}

	public int getMinInclusive() {
		return this.minInclusive;
	}

	public int getMaxExclusive() {
		return this.maxExclusive;
	}

	private int getLength() {
		return this.maxExclusive - this.minInclusive;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj instanceof ByteArrayOffsetKMer) {
			ByteArrayOffsetKMer other = (ByteArrayOffsetKMer) obj;
			int length = this.getLength();
			int otherLength = other.getLength();
			if (length == otherLength) {
				for (int i = 0; i < length; i++) {
					if (this.sequence[i + this.minInclusive] == other.sequence[i + other.minInclusive]) {
						// pass
					} else {
						return false;
					}
				}
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		int result = 17;
		final int REASONABLE_NUMBER_OF_ELEMENTS = 16;
		int max = Math.min(this.maxExclusive, this.minInclusive + REASONABLE_NUMBER_OF_ELEMENTS);
		for (int i = this.minInclusive; i < max; i++) {
			result = (37 * result) + this.sequence[i];
		}
		return result;
	}

	@Override
	public String toString() {
		return String.format("%s[sequence length: %d; range: [%d-%d)]", this.getClass().getSimpleName(),
				this.sequence.length, minInclusive, maxExclusive);
	}

	private final byte[] sequence;
	private final int minInclusive;
	private final int maxExclusive;
}
