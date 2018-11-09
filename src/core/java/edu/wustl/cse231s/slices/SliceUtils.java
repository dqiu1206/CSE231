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
package edu.wustl.cse231s.slices;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import edu.wustl.cse231s.IntendedForStaticAccessOnlyError;
import slice.core.Slice;

/**
 * @author Finn Voichick
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
public class SliceUtils {
	private SliceUtils() {
		throw new IntendedForStaticAccessOnlyError();
	}

	private static <T> List<Slice<T>> createNSlicesForArrayObject(T data, int numSlices) {
		int dataLength = Array.getLength(data);
		List<Slice<T>> result = new ArrayList<>(numSlices);

		int min = 0;
		int max = dataLength;

		int range = max - min;
		int indicesPerSlice = range / numSlices;
		int remainder = range % numSlices;

		int sliceMin = min;

		for (int sliceId = 0; sliceId < numSlices; sliceId++) {

			int sliceMax = sliceMin + indicesPerSlice;

			if (sliceId < remainder) {
				sliceMax ++;
			}

			result.add(new Slice<>(data, sliceId, sliceMin, sliceMax));

			sliceMin = sliceMax;
		}

		return Collections.unmodifiableList(result);
	}

	public static <E> List<Slice<E[]>> createNSlices(E[] data, int numSlices) {
		return createNSlicesForArrayObject(data, numSlices);
	}

	public static List<Slice<byte[]>> createNSlices(byte[] data, int numSlices) {
		return createNSlicesForArrayObject(data, numSlices);
	}

	public static List<Slice<char[]>> createNSlices(char[] data, int numSlices) {
		return createNSlicesForArrayObject(data, numSlices);
	}

	public static List<Slice<short[]>> createNSlices(short[] data, int numSlices) {
		return createNSlicesForArrayObject(data, numSlices);
	}

	public static List<Slice<int[]>> createNSlices(int[] data, int numSlices) {
		return createNSlicesForArrayObject(data, numSlices);
	}

	public static List<Slice<long[]>> createNSlices(long[] data, int numSlices) {
		return createNSlicesForArrayObject(data, numSlices);
	}

	public static List<Slice<float[]>> createNSlices(float[] data, int numSlices) {
		return createNSlicesForArrayObject(data, numSlices);
	}

	public static List<Slice<double[]>> createNSlices(double[] data, int numSlices) {
		return createNSlicesForArrayObject(data, numSlices);
	}
}
