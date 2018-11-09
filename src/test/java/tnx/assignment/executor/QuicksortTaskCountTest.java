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
import org.junit.Before;
import org.junit.Test;

import edu.wustl.cse231s.executors.TestExecutor;
import tnx.assignment.rubric.TnXRubric;

/**
 * @author Finn Voichick
 */
@TnXRubric(TnXRubric.Category.EXECUTOR_QUICKSORT)
public class QuicksortTaskCountTest {

	private TestExecutor executor;
	private int threshold;
	private int[] array;
	private int minSpawns;

	@Before
	public void setUp() throws Exception {
		int size = 1_000_000;
		int numLeaves = 50;
		threshold = size / numLeaves;
		// spawns should be about 100
		int spawnLimit = size / 2;
		minSpawns = numLeaves;
		executor = new TestExecutor.Builder(ForkJoinPool.commonPool(), spawnLimit).build();
		this.array = ThreadLocalRandom.current().ints(size, 0, size).toArray();
	}

	@Test
	public void test() throws InterruptedException, ExecutionException {
		// the TestExecutor.submit method fails if too many tasks are spawned
		XQuicksort.parallelQuicksort(executor, array, threshold);
		
		int taskCount = executor.getSpawnCount();
		Assert.assertTrue("You must spawn a sufficient number of tasks", taskCount >= minSpawns);
	}

}
