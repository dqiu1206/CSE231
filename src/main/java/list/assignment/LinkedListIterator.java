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

import java.util.Iterator;
import net.jcip.annotations.NotThreadSafe;

/**
 * @author Ben Choi (benjaminchoi@wustl.edu)
 */

@NotThreadSafe
/*package-private*/ class LinkedListIterator<E> implements Iterator<E> {

	private LinkedListNode<E> currentNode;
	private LinkedListNode<E> prevNode;
	private int index;
	
	/**
	 * Iterator designed for StudentSimpleSinglyLinkedList. Keeps track of the index value, the current
	 * node, and the previously examined node. Starts at an index value of -1 to adjust for a sentinel
	 * node at the head, comprised of a node of value null with its next pointer indicating the true first
	 * node of the list.
	 * Note: the following class is not thread safe.
	 * @param list
	 */
	public LinkedListIterator(SinglyLinkedList<E> list) {
		index = -1;
		currentNode = list.getHead();
		prevNode = null;
	}
	
	@Override
	public boolean hasNext() {
		if (currentNode.getNext() != null) {
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public E next() {
		prevNode = currentNode;
		currentNode = currentNode.getNext();
		++index;
		return currentNode.getValue();
	}

	/**
	 * Gets the current node the iterator is examining
	 * @return the current node
	 */
	/*package-private*/ LinkedListNode<E> getCurrentNode() {
		return currentNode;
	}

	/**
	 * Gets the previous node the iterator was just examining
	 * @return the previous node
	 */
	/*package-private*/ LinkedListNode<E> getPrevNode() {
		return prevNode;
	}

	/**
	 * Gets the index of the node the iterator is examining
	 * @return the index of the current node
	 */
	/*package-private*/ int getIndex() {
		return index;
	}

}
