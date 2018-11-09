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

import org.apache.commons.lang3.ArrayUtils;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Deque;
import java.util.LinkedList;

import list.core.SimpleList;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
public abstract class AbstractComprehensiveRemoveTest<T> {
	private static <T> String toString(SimpleList<T> list) {
		StringBuilder sb = new StringBuilder();
		String prefix = "[";
		
		for(T item : list ) {
			sb.append(prefix);
			sb.append(item);
			prefix = ", ";
		}

		sb.append("]");
		return sb.toString();
	}
	
	protected abstract T createItem(char c);

	private static <T> void assertEquivalent(Deque<T> truthAndBeauty, SimpleList<T> studentList) {
		assertEquals(truthAndBeauty.isEmpty(), studentList.isEmpty());
		assertEquals(truthAndBeauty.size(), studentList.size());
		int i = 0;
		for(T item : truthAndBeauty) {
			assertEquals(item, studentList.get(i));
			i++;
		}
	}
	
	private static <T> void pushToBoth(Deque<T> truthAndBeauty, SimpleList<T> studentList, T item) {
		truthAndBeauty.push(item);
		studentList.addFirst(item);
		assertEquivalent(truthAndBeauty, studentList);
	}
	
	private static <T> void removeFromBoth(Deque<T> truthAndBeauty, SimpleList<T> studentList, T item) {
		boolean expectedSuccess = truthAndBeauty.remove(item);
		boolean actualSuccess = studentList.remove(item);
		assertEquals(expectedSuccess, actualSuccess);
		assertEquivalent(truthAndBeauty, studentList);
	}
	
	private SimpleList<T> createList(char[] data, Deque<T> truthAndBeauty) {
		SimpleList<T> studentList = new SinglyLinkedList<>();
		assertTrue(studentList.isEmpty());
		assertEquals(0, studentList.size());
		for (char c : data) {
			T item = createItem(c);
			assertEquals(-1, studentList.indexOf(item));
		}
		for (char c : data) {
			T item = createItem(c);
			pushToBoth(truthAndBeauty, studentList, item);
		}
		return studentList;
	}
	
	private static char[] createReversed(String s) {
		char[] data = s.toCharArray();
		ArrayUtils.reverse(data);
		return data;
	}
	
	@Test
	public void testUnique() {
		Deque<T> truthAndBeauty = new LinkedList<>();
		char[] data = createReversed("ABCDE");
		SimpleList<T> studentList = createList(data, truthAndBeauty);
		System.out.println(toString(studentList));
		
		T first = this.createItem('A');
		T middle = this.createItem('C');
		T last = this.createItem('E');
		T nonExistent = this.createItem('F');
		for( int i=0; i<2; i++ ) {
			removeFromBoth(truthAndBeauty, studentList, nonExistent);
			removeFromBoth(truthAndBeauty, studentList, first);
			removeFromBoth(truthAndBeauty, studentList, middle);
			removeFromBoth(truthAndBeauty, studentList, last);
		}
	}

	@Test
	public void testDuplicates() {
		Deque<T> truthAndBeauty = new LinkedList<>();
		char[] data = createReversed("abracadabra");
		SimpleList<T> studentList = createList(data, truthAndBeauty);
		System.out.println(toString(studentList));

		while(truthAndBeauty.isEmpty()==false) {
			for(char c : "abcdr".toCharArray()) {
				T item = this.createItem(c);
				removeFromBoth(truthAndBeauty, studentList, item);
			}
		}
		
		System.out.println(truthAndBeauty);
	}
}
