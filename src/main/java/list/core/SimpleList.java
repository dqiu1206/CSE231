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
package list.core;

/**
 * @author Ben Choi (benjaminchoi@wustl.edu)
 */

public interface SimpleList<E> extends Iterable<E> {
	
	/**
	 * Adds an item of type E to the list, at the head	
	 * @param item
	 * @return true if the user attempts to put in a valid item,
	 * 			false if the user attempts to put in a null item
	 *
	 * @see java.util.Deque#addFirst(Object)
	 */
	public boolean addFirst(E item);
	
	/**
	 * Removes an item of type E from the list
	 * @param item
	 * @return true if the remove is successful, false otherwise
	 *
	 * @see java.util.List#remove(Object)
	 */
	public boolean remove(E item);
	
	/**
	 * Tells the user whether the list is empty
	 * @return true if the list is empty, false otherwise
	 *
	 * @see java.util.List#isEmpty()
	 */
	public boolean isEmpty();
	
	/**
	 * Searches the list by index value
	 * @param i
	 * @return the item of type E associated with the given index i,
	 * 			null if the index is out of bounds
	 *
	 * @see java.util.List#get(int)
	 */
	public E get(int i);
	
	/**
	 * Searches the list by its contents
	 * @param item
	 * @return the index of the first occurrence of the item if it exists, -1 otherwise
	 *
	 * @see java.util.List#indexOf(Object)
	 */
	public int indexOf(E item);
	
	/**
	 * Tells the user the number of items in a list
	 * @return the size of a list as an int
	 *
	 * @see java.util.List#size()
	 */
	public int size();
}
