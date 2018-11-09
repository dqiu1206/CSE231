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

import edu.wustl.cse231s.NotYetImplementedException;
import list.core.SimpleList;
import net.jcip.annotations.NotThreadSafe;

/**
 * @author David Qiu
 * @author Ben Choi (benjaminchoi@wustl.edu)
 */

@NotThreadSafe
public class SinglyLinkedList<E> implements SimpleList<E> {

	private int size;
	private final LinkedListNode<E> head;

	/**
	 * A simple singly linked list which starts at size zero with a sentinel head node.
	 * The sentinel node should always be maintained with a value of null, with its next
	 * pointer indicating the true first node of the list. This information will come in
	 * useful if you choose to use the StudentListIterator for your method implementations.
	 * Note: the following class is not thread safe.
	 */
	public SinglyLinkedList() {
		size = 0;
		head = new LinkedListNode<E>(null, null);
	}

	/**
	 * Gets the head (sentinel) node
	 * @return the head (sentinel) node of the list
	 */
	/*package-private*/ LinkedListNode<E> getHead() {
		return head;
	}
	
	@Override
	public LinkedListIterator<E> iterator() {
		return new LinkedListIterator<E>(this);
	}

	@Override
	public int size() {
		return this.size;
	}

	@Override
	public boolean isEmpty() {
		
		if(head.getNext()!=null) {
			return false;
		}
		return true;
	}

	@Override
	public boolean addFirst(E item) {
		
		LinkedListNode<E> curr = head;
		LinkedListNode<E> temp = head.getNext();
		if(item.equals(null)) {
			return false;
		}
		else {
			LinkedListNode<E> news = new LinkedListNode<E>(item,null);
			curr.setNext(news);
			news.setNext(temp);
			size++;
			return true;
		}
		
		
		
	}

	@Override
	public boolean remove(E item) {
		LinkedListNode<E> prev = head;
		LinkedListNode<E> curr = prev.getNext();
		while(curr!=null) {
			if(curr.getValue().equals(item)) {
				LinkedListNode<E> temp = curr.getNext();
				prev.setNext(temp);
				curr=null;
				size--;
				return true;
			}
			prev = curr;
			curr = curr.getNext();
			
		}
		return false;
		
	}

	@Override
	public E get(int index) {
		if (index<0 || index>=size) {
			return null;
		}
	
		else {
			
			LinkedListNode<E> curr = head.getNext();
			for(int i = 0;i<index;i++) {
				curr = curr.getNext();
			}
			return curr.getValue();
		}
		
	}

	@Override
	public int indexOf(E item) {
		if (item.equals(null)) {
			return 0;
		}
		else {
			LinkedListNode<E> curr = head.getNext();
			int track = 0;
			while(track<size) {
				if(curr.getValue().equals(item)) {
					return track;
				}
				else {
					curr = curr.getNext();
					track++;
				}
			}
		}
		return -1;
	}

}
