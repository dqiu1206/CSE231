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
@ListMapRubric(ListMapRubric.Category.LIST_GET)
public class GetValueTest {

	@Test
	public void test() {
		SinglyLinkedList<Integer> list = new SinglyLinkedList<Integer>();
		for (int i = 1; i < 4; ++i) {
			list.addFirst(i);
		}
		Integer count = 3;
		for (int i = 0; i < 3; ++i) {
			assertEquals("Your list is not finding the correct item values for indexes, check that you're working"
					+ "with the right get method", count, list.get(i));
			--count;
		}
	}

}
