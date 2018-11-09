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

import static org.junit.Assert.*;

import org.junit.Test;

import list.assignment.SinglyLinkedList;
import util.assignment.rubric.ListMapRubric;

/**
 * @author Ben Choi (benjaminchoi@wustl.edu)
 */
@ListMapRubric(ListMapRubric.Category.LIST_UNCATEGORIZED)
public class StudentListTest {

	@Test
	public void test() {
		SinglyLinkedList<Integer> list = new SinglyLinkedList<>();

		assertEquals("Your list is not initializing with a size of 0", 0, list.size());
		assertTrue("Your list is not returning isEmpty correctly", list.isEmpty());

		for (int i = 0; i < 100; ++i) {
			list.addFirst(i);
		}

		assertEquals("Your list does not update size correctly", 100, list.size());
		assertFalse("Your list is not returning isEmpty correctly", list.isEmpty());

		int count = 99;
		for (Integer num : list) {
			assertEquals("Your list is not iterating properly, check your put method", (Integer) count, num);
			--count;
		}

		assertTrue("Your list is not removing properly, check your returns", list.remove(0));
		assertEquals("Your get method does not return -1 if item is not found", -1, list.indexOf(0));
		assertNull("Your list is not removing properly, check your next references", list.get(99));

		assertEquals("Your list does not update size correctly", 99, list.size());
		assertFalse("Your list is not returning isEmpty correctly", list.isEmpty());
	}

}
