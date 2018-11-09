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

import java.math.BigDecimal;

import org.junit.Test;

import list.assignment.SinglyLinkedList;
import list.core.SimpleList;
import util.assignment.rubric.ListMapRubric;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
@ListMapRubric(ListMapRubric.Category.LIST_UNCATEGORIZED)
public class EqualsTest {
	private SimpleList<Integer> createBoxedPrimitiveList() {
		SimpleList<Integer> list = new SinglyLinkedList<>();
		assertTrue(list.isEmpty());
		
		list.addFirst(3);
		assertFalse(list.isEmpty());
		assertEquals(1, list.size());
		assertEquals(0, list.indexOf(3));
		assertEquals(3, list.get(0).intValue());
		
		list.addFirst(2);
		assertFalse(list.isEmpty());
		assertEquals(2, list.size());
		assertEquals(0, list.indexOf(2));
		assertEquals(1, list.indexOf(3));
		assertEquals(2, list.get(0).intValue());
		assertEquals(3, list.get(1).intValue());
		
		list.addFirst(1);
		assertFalse(list.isEmpty());
		assertEquals(3, list.size());
		assertEquals(0, list.indexOf(1));
		assertEquals(1, list.indexOf(2));
		assertEquals(2, list.indexOf(3));
		assertEquals(1, list.get(0).intValue());
		assertEquals(2, list.get(1).intValue());
		assertEquals(3, list.get(2).intValue());
		
		return list;
	}

	private SimpleList<BigDecimal> createNonPrimitiveList() {
		SimpleList<BigDecimal> list = new SinglyLinkedList<>();
		assertTrue(list.isEmpty());
		
		list.addFirst(new BigDecimal(3));
		assertFalse(list.isEmpty());
		assertEquals(1, list.size());
		assertEquals(0, list.indexOf(new BigDecimal(3)));
		assertEquals(3, list.get(0).intValue());
		
		list.addFirst(new BigDecimal(2));
		assertFalse(list.isEmpty());
		assertEquals(2, list.size());
		assertEquals(0, list.indexOf(new BigDecimal(2)));
		assertEquals(1, list.indexOf(new BigDecimal(3)));
		assertEquals(2, list.get(0).intValue());
		assertEquals(3, list.get(1).intValue());
		
		list.addFirst(new BigDecimal(1));
		assertFalse(list.isEmpty());
		assertEquals(3, list.size());
		assertEquals(0, list.indexOf(new BigDecimal(1)));
		assertEquals(1, list.indexOf(new BigDecimal(2)));
		assertEquals(2, list.indexOf(new BigDecimal(3)));
		assertEquals(1, list.get(0).intValue());
		assertEquals(2, list.get(1).intValue());
		assertEquals(3, list.get(2).intValue());
		
		return list;
	}
	
	@Test
	public void testPrimitiveRemoveFirst() {
		SimpleList<Integer> list = createBoxedPrimitiveList();
		assertEquals(3, list.size());
		list.remove(1);
		assertEquals(2, list.size());
		
		assertEquals(-1, list.indexOf(1));
		assertEquals(0, list.indexOf(2));
		assertEquals(1, list.indexOf(3));
		
		assertNull(list.get(-1));
		assertEquals(2, list.get(0).intValue());
		assertEquals(3, list.get(1).intValue());
		assertNull(list.get(2));
	}

	@Test
	public void testPrimitiveRemoveMiddle() {
		SimpleList<Integer> list = createBoxedPrimitiveList();
		assertEquals(3, list.size());
		list.remove(2);
		assertEquals(2, list.size());

		assertEquals(-1, list.indexOf(2));
		assertEquals(0, list.indexOf(1));
		assertEquals(1, list.indexOf(3));

		assertNull(list.get(-1));
		assertEquals(1, list.get(0).intValue());
		assertEquals(3, list.get(1).intValue());
		assertNull(list.get(2));
	}

	@Test
	public void testPrimitiveRemoveEnd() {
		SimpleList<Integer> list = createBoxedPrimitiveList();
		assertEquals(3, list.size());
		list.remove(3);
		assertEquals(2, list.size());

		assertEquals(-1, list.indexOf(3));
		assertEquals(0, list.indexOf(1));
		assertEquals(1, list.indexOf(2));

		assertNull(list.get(-1));
		assertEquals(1, list.get(0).intValue());
		assertEquals(2, list.get(1).intValue());
		assertNull(list.get(2));
	}

	@Test
	public void testNonPrimitiveRemoveFirst() {
		SimpleList<BigDecimal> list = createNonPrimitiveList();
		assertEquals(3, list.size());
		list.remove(new BigDecimal(1));
		assertEquals(2, list.size());
		
		assertEquals(-1, list.indexOf(new BigDecimal(1)));
		assertEquals(0, list.indexOf(new BigDecimal(2)));
		assertEquals(1, list.indexOf(new BigDecimal(3)));
		
		assertNull(list.get(-1));
		assertEquals(2, list.get(0).intValue());
		assertEquals(3, list.get(1).intValue());
		assertNull(list.get(2));
	}

	@Test
	public void testNonPrimitiveRemoveMiddle() {
		SimpleList<BigDecimal> list = createNonPrimitiveList();
		assertEquals(3, list.size());
		list.remove(new BigDecimal(2));
		assertEquals(2, list.size());

		assertEquals(-1, list.indexOf(new BigDecimal(2)));
		assertEquals(0, list.indexOf(new BigDecimal(1)));
		assertEquals(1, list.indexOf(new BigDecimal(3)));

		assertNull(list.get(-1));
		assertEquals(1, list.get(0).intValue());
		assertEquals(3, list.get(1).intValue());
		assertNull(list.get(2));
	}

	@Test
	public void testNonPrimitiveRemoveEnd() {
		SimpleList<BigDecimal> list = createNonPrimitiveList();
		assertEquals(3, list.size());
		list.remove(new BigDecimal(3));
		assertEquals(2, list.size());

		assertEquals(-1, list.indexOf(new BigDecimal(3)));
		assertEquals(0, list.indexOf(new BigDecimal(1)));
		assertEquals(1, list.indexOf(new BigDecimal(2)));

		assertNull(list.get(-1));
		assertEquals(1, list.get(0).intValue());
		assertEquals(2, list.get(1).intValue());
		assertNull(list.get(2));
	}
}
