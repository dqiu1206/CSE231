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
package tnx.assignment.executor;

import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import edu.wustl.cse231s.bioinformatics.Nucleobase;
import edu.wustl.cse231s.executors.NucleobaseCountTestUtils;
import tnx.assignment.rubric.TnXRubric;

/**
 * A unit test for {@link XNucleobaseCount}.
 * 
 * @author Finn Voichick
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
@RunWith(Parameterized.class)
@TnXRubric(TnXRubric.Category.EXECUTOR_COUNT_NWAY)
public class NucleobaseCountNWayParallelCorrectnessTest {
	private final byte[] chromosome;
	private final Nucleobase nucleobase;
	private final int nWaySplitCount;
	private final int truthAndBeautyCount;

	public NucleobaseCountNWayParallelCorrectnessTest(Nucleobase nucleobase, int nWaySplitCount) throws IOException {
		this.chromosome = NucleobaseCountTestUtils.loadHomoSapiensMitochondrion();
		this.nucleobase = nucleobase;
		this.nWaySplitCount = nWaySplitCount;
		this.truthAndBeautyCount = NucleobaseCountTestUtils.truthAndBeautyCount(chromosome, nucleobase);
	}

	@Parameters(name = "{0}, nway={1}")
	public static Collection<Object[]> getConstructorArguments() {
		int n = Runtime.getRuntime().availableProcessors();

		int[] nWays = { n, n * 2, n * 11 };

		List<Object[]> list = new LinkedList<>();
		for (Nucleobase nucleobase : Nucleobase.values()) {
			for (int nWay : nWays) {
				list.add(new Object[] { nucleobase, nWay });
			}
		}
		return list;
	}

	@Test
	public void testNWaySplit() throws InterruptedException, ExecutionException {
		ExecutorService executor = Executors.newCachedThreadPool();
		try {
			int count = XNucleobaseCount.countNWaySplit(executor, chromosome, nucleobase, this.nWaySplitCount);
			Assert.assertEquals(truthAndBeautyCount, count);
		} finally {
			executor.shutdown();
		}
	}
}
