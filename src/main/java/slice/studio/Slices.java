/*******************************************************************************
 * Copyright (C) 2016-2017 Dennis Cosgrove, Finn Voichick
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import edu.wustl.cse231s.IntendedForStaticAccessOnlyError;
import edu.wustl.cse231s.NotYetImplementedException;
import slice.core.Slice;

/**
 * @author David Qiu
 * @author Finn Voichick
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
public class Slices {
	private Slices() {
		throw new IntendedForStaticAccessOnlyError();
	}

	/**
	 * Should create a list of {@link Slice} objects of length numSlices. Each slice
	 * in the returned result is composed of roughly a 1/numSlices amount of the
	 * given data, but the last slice can contain any leftover data if the length of
	 * the data is not evenly divisible by numSlices. The ID of each slice should be
	 * its index in the returned list.
	 * 
	 * @param data
	 *            the data to be broken up into a list of slices
	 * @param numSlices
	 *            the number of slices to divide data into
	 * @return the created list of slices
	 */
	public static <T> List<Slice<T[]>> createNSlices(T[] data, int numSlices) {
		ArrayList<Slice<T[]>> result = new ArrayList<Slice<T[]>>();
		int totalLength = data.length;
		int sliceLength = totalLength/numSlices;
		for(int i=0;i<numSlices-1;++i) {
			
			Slice<T[]> newSlice = new Slice<T[]>(data,i,sliceLength*i,sliceLength*(i+1));
			result.add(newSlice);
		}
		Slice<T[]> lastSlice = new Slice<T[]>(data,numSlices-1,sliceLength*(numSlices-1),data.length);
		result.add(lastSlice);
		return result;
		
		
		
		
		
		
		
	}
}
