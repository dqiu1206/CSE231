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
import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections4.iterators.LazyIteratorChain;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
public class MultiWrapMap<K, V> extends AbstractMap<K, V> {
	public MultiWrapMap(Map<K, V>[] maps) {
		this.maps = maps;
	}

	private int getReduceIndex(Object key) {
		return Math.floorMod(key.hashCode(), this.maps.length);
	}

	private Map<K, V> getMap(Object key) {
		return this.maps[this.getReduceIndex(key)];
	}

	@Override
	public boolean containsKey(Object key) {
		return this.getMap(key).containsKey(key);
	}

	@Override
	public V get(Object key) {
		return this.getMap(key).get(key);
	}

	@Override
	public V put(K key, V value) {
		return this.getMap(key).put(key, value);
	}

	@Override
	public V remove(Object key) {
		return this.getMap(key).remove(key);
	}

	@Override
	public void clear() {
		for (Map<K, V> map : this.maps) {
			map.clear();
		}
	}

	@Override
	public int size() {
		int result = 0;
		for (Map<K, V> map : this.maps) {
			result += map.size();
		}
		return result;
	}

	@Override
	public boolean isEmpty() {
		for (Map<K, V> map : this.maps) {
			if (map.isEmpty()) {
				// pass
			} else {
				return false;
			}
		}
		return true;
	}

	@Override
	public Set<java.util.Map.Entry<K, V>> entrySet() {
		return new AbstractSet<java.util.Map.Entry<K, V>>() {
			@Override
			public Iterator<Entry<K, V>> iterator() {
				return new LazyIteratorChain<Map.Entry<K, V>>() {
					@Override
					protected Iterator<? extends java.util.Map.Entry<K, V>> nextIterator(int count) {
						int index = count - 1;
						return index < MultiWrapMap.this.maps.length
								? MultiWrapMap.this.maps[index].entrySet().iterator() 
								: null;
					}
				};
			}

			@Override
			public int size() {
				return MultiWrapMap.this.size();
			}
		};
	}

	private final Map<K, V>[] maps;
}
