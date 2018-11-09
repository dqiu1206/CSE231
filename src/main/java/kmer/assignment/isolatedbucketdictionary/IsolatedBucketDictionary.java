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

import static edu.wustl.cse231s.rice.habanero.Habanero.isolated;
import static edu.wustl.cse231s.rice.habanero.Habanero.isolatedWithReturn;
import static edu.wustl.cse231s.rice.habanero.Habanero.objectBased;
import static edu.wustl.cse231s.rice.habanero.Habanero.readMode;

import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.function.BiFunction;

import edu.wustl.cse231s.NotYetImplementedException;
import edu.wustl.cse231s.util.KeyMutableValuePair;
import list.assignment.SinglyLinkedList;
import net.jcip.annotations.ThreadSafe;

/**
 * @author David Qiu
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
@ThreadSafe
/* package-private */ class IsolatedBucketDictionary<K, V> {
	private final List<Entry<K, V>>[] buckets;
	@SuppressWarnings("unchecked")
	public IsolatedBucketDictionary(int bucketCount) {
		buckets = new List[bucketCount];
		for (int i = 0; i < bucketCount; ++i) {
			buckets[i] = new LinkedList<Entry<K,V>>();
		}
	}

	private List<Entry<K, V>> getBucket(K key) {
		return isolatedWithReturn(objectBased(readMode(this)),()-> {
			int hash = Math.floorMod(key.hashCode(), buckets.length);
			return buckets[hash];
		});
	}

	private static <K, V> Entry<K, V> getEntry(List<Entry<K, V>> bucket, K key) {
			if(bucket.isEmpty()) {
				return null;
			}
			for(Entry<K,V> entry:bucket) {
				if(entry.getKey().equals(key)) {
					return entry;
				}
			}
			return null;
	}

	public V get(K key) {
		List<Entry<K,V>> list = getBucket(key);
		return isolatedWithReturn(objectBased(readMode(list)),()-> {
			
			Entry<K,V> entry = getEntry(list,key);
			if(entry==null) {
				return null;
			}
			else {
				return entry.getValue();
			}
		});
		
	}

	public void put(K key, V value) {
		List<Entry<K,V>> list = getBucket(key);
		isolated(objectBased(list),()-> {
			
			Entry<K,V> entry = getEntry(list,key);
			if(entry==null) {
				Entry<K,V> add = new KeyMutableValuePair<K,V>(key,value);
				list.add(add);
			}
			else {
				entry.setValue(value);
			}
		});
	}

	public V compute(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
		List<Entry<K,V>> list = getBucket(key);
		return isolatedWithReturn(objectBased(list),()-> {
			
			Entry<K,V> entry = getEntry(list,key);
			if(entry==null) {
				V newV = null;
				V computed = remappingFunction.apply(key, newV);
				Entry<K,V> add = new KeyMutableValuePair<K,V>(key,computed);
				list.add(add);
				return computed;
			}
			else {
				V newValue = remappingFunction.apply(key, entry.getValue());
				entry.setValue(newValue);
				return newValue;
			}
		});
	}
}
