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
package edu.wustl.cse231s.util;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import edu.wustl.cse231s.IntendedForStaticAccessOnlyError;

import java.util.TreeMap;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
public class EntryUtils {
	private EntryUtils() {
		throw new IntendedForStaticAccessOnlyError();
	}
	
	public static <K, V> Entry<K, V> createEntry(K key, V value) {
		return new AbstractMap.SimpleEntry<>(key, value);
	}

	public static <K, V> Entry<K, V>[] createEntryArray(int length) {
		return new Entry[length];
	}

	private static <K, V> Map<K, V> createMapFromArrayOfEntries(Map<K, V> result, Entry<K, V>[] entries) {
		for (Entry<K, V> entry : entries) {
			result.put(entry.getKey(), entry.getValue());
		}
		return result;
	}
	public static <K, V> Map<K, V> createHashMapFromArrayOfEntries(Entry<K, V>[] entries) {
		return createMapFromArrayOfEntries( new HashMap<>(), entries );
	}
	public static <K, V> Map<K, V> createTreeMapFromArrayOfEntries(Entry<K, V>[] entries) {
		return createMapFromArrayOfEntries( new TreeMap<>(), entries );
	}
}
