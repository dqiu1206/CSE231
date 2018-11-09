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

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ThreadLocalRandom;

import org.junit.Assert;
import org.junit.Test;

import edu.wustl.cse231s.executors.TestExecutor;
import tnx.assignment.rubric.TnXRubric;

/**
 * @author Finn Voichick
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
@TnXRubric(TnXRubric.Category.EXECUTOR_QUICKSORT)
public class QuicksortParallelAtAllTest {

	@Test
	public void test() throws InterruptedException, ExecutionException {
		int size = 1_000;
		int[] array = ThreadLocalRandom.current().ints(size, 0, size).toArray();

		int spawnLimit = size; // todo
		TestExecutor executor = new TestExecutor.Builder(ForkJoinPool.commonPool(), spawnLimit).build();
		int threshold = 50;

		XQuicksort.parallelQuicksort(executor, array, threshold);

		int taskCount = executor.getSpawnCount();
		Assert.assertNotEquals("parallelism not added at all.", taskCount, 0);

		// Assert.assertThat("copy and paste from sequential error?", taskCount);

	}

}
