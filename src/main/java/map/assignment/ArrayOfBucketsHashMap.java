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
package map.assignment;

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
public class ArrayOfBucketsHashMap<K, V> implements SimpleMap<K, V> {

	private final SinglyLinkedList<KeyMutableValuePair<K, V>>[] buckets;
	private int size=0;
	/**
	 * A simple HashMap which extends the simple map you implemented earlier.
	 * This implementation will make use of buckets based on an object's hash in
	 * order to cut down on the runtime of finding entries. See the two given
	 * methods in order to find out which bucket an entry should go in depending
	 * on the hashcode of the entry's key. Note: this class is not thread-safe.
	 */
	public ArrayOfBucketsHashMap() {
		buckets = new SinglyLinkedList[1024];
		for (int i = 0; i < 1024; ++i) {
			buckets[i] = new SinglyLinkedList<KeyMutableValuePair<K, V>>();
		}
	}

	/**
	 * Uses the key's hashcode and the length of the number of buckets to
	 * determine which bucket the entry should go into
	 * 
	 * @param key
	 * @return the index of the bucket the entry should go into
	 */
	private int hash(K key) {
		return Math.floorMod(key.hashCode(), buckets.length);
	}

	/**
	 * Uses the key's hash to create a reference to the bucket the key is in
	 * 
	 * @param key
	 * @return the bucket the key is in
	 */
	private SinglyLinkedList<KeyMutableValuePair<K, V>> getListFor(K key) {
		return buckets[hash(key)];
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
		
		V oldValue=null;
		KeyMutableValuePair<K, V> newKeyPair = new KeyMutableValuePair<>(key,value);
		
		for(int i =0;i<getListFor(key).size();i++) {
			if(key.equals(getListFor(key).get(i).getKey())) {
				oldValue = getListFor(key).get(i).getValue();
				getListFor(key).get(i).setValue(value);
				
				return oldValue;
			}
		}
		
		
		getListFor(key).addFirst(newKeyPair);
		size++;
		return oldValue;
	}
	

	@Override
	public V remove(K key) {
		 
		V value=null;
		for(int i = 0; i<getListFor(key).size();i++) {
			
			if(key.equals(getListFor(key).get(i).getKey())) {
				value = getListFor(key).get(i).getValue();
				
				KeyMutableValuePair<K, V> old = new KeyMutableValuePair<>(key,get(key));
				getListFor(key).remove(old);
				size--;
				return value;
			}
		}
		return value;
	}

	@Override
	public V get(K key) {
		
		V value=null;
		for(int i = 0;i<getListFor(key).size();i++) {
			if(key.equals(getListFor(key).get(i).getKey())) {
				value = getListFor(key).get(i).getValue();
				return value;
			}
		}
		return value;
	}
	

	@Override
	public V compute(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
		
		V newValue = remappingFunction.apply(key,get(key));
		if(newValue!=null) {
			remove(key);
			put(key,newValue);
		}
		else {
			remove(key);
		}
		
		return newValue;
	}

}
