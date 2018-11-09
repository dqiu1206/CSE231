/*******************************************************************************
 * Copyright (C) 2016-2017 Dennis Cosgrove
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
package count.assignment;

import static edu.wustl.cse231s.rice.habanero.Habanero.launchHabaneroApp;
import static org.junit.Assert.*;

import org.junit.Test;

import count.assignment.NucleobaseCounting;
import count.assignment.rubric.CountRubric;
import edu.rice.hj.api.SuspendableException;
import edu.wustl.cse231s.bioinformatics.Nucleobase;

public class IsolatedNucleobaseTest {
	@Test
	@CountRubric(CountRubric.Category.SEQUENTIAL)
	public void justASequential() {
		byte[] b = new byte[100];
		for (int i = 0; i < 100; ++i) {
			b[i] = Nucleobase.ADENINE.toByte();
		}
		for (Nucleobase n : Nucleobase.values()) {
			int count = NucleobaseCounting.countSequential(b, n);
			if (n == Nucleobase.ADENINE) {
				assertEquals("Your sequential method is not counting the correct number of nucleobases", 100, count);
			} else {
				assertEquals("Your sequential method is not counting the correct number of nucleobaases", 0, count);
			}
		}
	}
	
	@Test
	@CountRubric(CountRubric.Category.UPPER_LOWER)
	public void justAUpperLowerSplit() throws SuspendableException {
		byte[] b = new byte[100];
		for (int i = 0; i < 100; ++i) {
			b[i] = Nucleobase.ADENINE.toByte();
		}
		launchHabaneroApp(() -> {
			for (Nucleobase n : Nucleobase.values()) {
				int count = NucleobaseCounting.countParallelUpperLowerSplit(b, n);
				if (n == Nucleobase.ADENINE) {
					assertEquals("Your upper/lower split method is not counting the correct number of nucleobases", 100, count);
				} else {
					assertEquals("Your upper/lower split method is not counting the correct number of nucleobaases", 0, count);
				}
			}
		} );
	}
	
	@Test
	@CountRubric(CountRubric.Category.NWAY)
	public void justANWaySplit() throws SuspendableException {
		byte[] b = new byte[100];
		for (int i = 0; i < 100; ++i) {
			b[i] = Nucleobase.ADENINE.toByte();
		}
		launchHabaneroApp(() -> {
			for (Nucleobase n : Nucleobase.values()) {
				int count = NucleobaseCounting.countParallelNWaySplit(b, n, 4);
				if (n == Nucleobase.ADENINE) {
					assertEquals("Your n-way split method is not counting the correct number of nucleobases", 100, count);
				} else {
					assertEquals("Your n-way split method is not counting the correct number of nucleobaases", 0, count);
				}
			}
		} );
	}

}
