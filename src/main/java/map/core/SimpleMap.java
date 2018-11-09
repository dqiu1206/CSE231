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
package map.core;

import java.util.function.BiFunction;

/**
 * @author Ben Choi (benjaminchoi@wustl.edu)
 */

public interface SimpleMap<K, V> {
	
	/**
	 * Checks to see if the map is empty
	 * @return true if the map is empty, false otherwise
	 *
	 * @see java.util.Map#isEmpty()
	 */
	public boolean isEmpty();
	
	/**
	 * Checks the size of the map by counting the number of entries in it
	 * @return size of the map as an int
	 *
	 * @see java.util.Map#size()
	 */
	public int size();
	
	/**
	 * Inserts a StudentMapEntry with the given key and value pairs into the map
	 * @param key
	 * @param value
	 * @return the old value associated with the given key if it existed, null otherwise
	 *
	 * @see java.util.Map#put(Object,Object)
	 */
	public V put(K key, V value);
	
	/**
	 * Removes the StudentMapEntry associated with the given key value from the map
	 * @param key
	 * @return the old value associated with the given key if it existed, null otherwise
	 *
	 * @see java.util.Map#remove(Object)
	 */
	public V remove(K key);
	
	/**
	 * Gets the value associated with the given key
	 * @param key
	 * @return the value associated with the key if it exists, null otherwise
	 *
	 * @see java.util.Map#get(Object)
	 */
	public V get(K key);
	
	/**
	 * @param key
	 * @param remappingFunction
	 * @return the value (null or not) returned from the remappingFunction
	 *
	 * @see java.util.Map#compute(Object,java.util.function.BiFunction)
	 */
	public V compute(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction);
}
