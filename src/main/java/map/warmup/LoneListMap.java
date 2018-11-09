/*******************************************************************************
 * Copyright (C) 2016-2017 Dennis Cosgrove, Ben Choi
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
package map.warmup;

import java.util.function.BiFunction;

import edu.wustl.cse231s.NotYetImplementedException;
import edu.wustl.cse231s.util.KeyMutableValuePair;
import list.assignment.SinglyLinkedList;
import map.core.SimpleMap;
import net.jcip.annotations.NotThreadSafe;

/**
 * @author David Qiu
 * @author Ben Choi (benjaminchoi@wustl.edu)
 */

@NotThreadSafe
public class LoneListMap<K, V> implements SimpleMap<K, V> {
	
	private SinglyLinkedList<KeyMutableValuePair<K, V>> entries;
	private int size;
	/**
	 * A simple map created using your implementation of a singly linked list to keep
	 * track of key/value pair entries. It takes in a key of type K and a value of type
	 * V. It initializes as an empty map with a size of 0. It only uses one bucket.
	 * Note: this class is not thread-safe.
	 */
	public LoneListMap() {
		entries = new SinglyLinkedList<KeyMutableValuePair<K, V>>();
	}
	
	@Override
	public boolean isEmpty() {
		if(size>0) {
			return false;
		}
		return true;
	}
	
	@Override
	public int size() {
		return this.size;
	}
	
	@Override
	public V put(K key, V value) {
		V oldValue;
		KeyMutableValuePair<K, V> newKeyPair = new KeyMutableValuePair<>(key,value);
		for(int i =0;i<entries.size();i++) {
			if(entries.get(i).equals(key)) {
				oldValue = entries.get(i).getValue();
				entries.get(i).setValue(value);
				return oldValue;
			}
		}
		
		entries.addFirst(newKeyPair);
		size++;
		return null;
	}
	
	@Override
	public V remove(K key) {
		V oldValue = null;
		for(int i=0;i<size();i++) {
			if(key==entries.get(i).getKey()) {
				KeyMutableValuePair<K, V> oldPair = new KeyMutableValuePair<>(key,get(key));
				oldValue = get(key);
				entries.remove(oldPair);
				size--;
				return oldValue;
				
			}
		}
		return oldValue;
	}
	
	@Override
	public V get(K key) {
		
		for(int i =0;i<size();i++) {
			if(key==entries.get(i).getKey()) {
				return entries.get(i).getValue();
			}
		}
		return null;
	}

	@Override
	public V compute(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
		if(get(key)==null) {
			return null;
		}
		return remappingFunction.apply(key, get(key));
	}
	
}
