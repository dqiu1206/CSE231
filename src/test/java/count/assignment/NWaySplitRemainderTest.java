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

import java.nio.charset.StandardCharsets;

import org.junit.Assert;
import org.junit.Test;

import count.assignment.rubric.CountRubric;
import edu.wustl.cse231s.bioinformatics.Nucleobase;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
@CountRubric(CountRubric.Category.NWAY)
public class NWaySplitRemainderTest {
	@Test
	public void testRemainder() throws Exception {
		launchHabaneroApp(()-> {
			byte[] chromosome = "AAA".getBytes(StandardCharsets.UTF_8);
			int actualCountA = NucleobaseCounting.countParallelNWaySplit(chromosome, Nucleobase.ADENINE, 3);
			Assert.assertEquals(3, actualCountA);

			int actualCount = NucleobaseCounting.countParallelNWaySplit(chromosome, Nucleobase.ADENINE, 2);
			Assert.assertNotEquals(2, actualCount);
			Assert.assertEquals(3, actualCount);
		});
	}
}
