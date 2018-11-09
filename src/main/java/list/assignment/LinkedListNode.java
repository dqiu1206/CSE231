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
package list.assignment;

import net.jcip.annotations.NotThreadSafe;

/**
 * @author Ben Choi (benjaminchoi@wustl.edu)
 */

@NotThreadSafe
/*package-private*/ class LinkedListNode<E> {
	
	private final E value;
	private LinkedListNode<E> next;
	
	/**
	 * The list nodes used for StudentSimpleSinglyLinkedList.
	 * Note: the following class is not thread-safe.
	 * @param value, the item of type E that you wish to add to the list
	 * @param next, a reference to the next node of the list
	 */
	public LinkedListNode(E value, LinkedListNode<E> next) {
		this.value = value;
		this.next = next;
	}

	/**
	 * Gets the node the tail reference is pointing to
	 * @return next node in the list
	 */
	public LinkedListNode<E> getNext() {
		return next;
	}

	/**
	 * Sets the value of the tail reference of the given node
	 * @param next
	 */
	public void setNext(LinkedListNode<E> next) {
		this.next = next;
	}

	/**
	 * Gets the value of the given node
	 * @return the value associated with the node
	 */
	public E getValue() {
		return value;
	}
	
}
