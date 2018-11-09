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
package kmer.assignment.longconcurrenthashmap;

import static edu.wustl.cse231s.rice.habanero.Habanero.async;
import static edu.wustl.cse231s.rice.habanero.Habanero.finish;
import static edu.wustl.cse231s.rice.habanero.Habanero.forall;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;

import edu.rice.hj.api.SuspendableException;
import edu.wustl.cse231s.NotYetImplementedException;
import kmer.assignment.util.ThresholdSlices;
import kmer.core.KMerCount;
import kmer.core.KMerCounter;
import kmer.core.KMerUtils;
import slice.core.Slice;

/**
 * A parallel implementation of {@link KMerCounter} that uses a
 * {@link ConcurrentHashMap}, where each k-mer is represented as a Long.
 * 
 * @author David Qiu
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
public class LongConcurrentHashMapKMerCounter implements KMerCounter {

	@Override
	public KMerCount parse(List<byte[]> sequences, int kMerLength) throws SuspendableException {
		Map<Long,Integer> map = new ConcurrentHashMap<Long,Integer>();
		List<Slice<byte[]>> slices = ThresholdSlices.createSlicesBelowReasonableThreshold(sequences, kMerLength);
		forall(0,slices.size(),(i)->{
			for(int j=slices.get(i).getMinInclusive();j<slices.get(i).getMaxExclusive();j++) {
				Long kMer = KMerUtils.toPackedLong(slices.get(i).getData(),j,kMerLength);
				map.compute(kMer, (k,v)->{
					if(v==null) {
						v = 1;
						return v;
					}
					else {
						v++;
						return v;
					}
					
				});
				
			}
		});
		LongMapKMerCount kmer = new LongMapKMerCount(map);
		return kmer;
	}

}