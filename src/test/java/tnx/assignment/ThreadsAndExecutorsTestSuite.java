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
package tnx.assignment;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import tnx.assignment.executor.NucleobaseCount2WayParallelCorrectnessTest;
import tnx.assignment.executor.NucleobaseCountDivideAndConquerParallelCorrectnessTest;
import tnx.assignment.executor.NucleobaseCountNWayParallelCorrectnessTest;
import tnx.assignment.executor.NucleobaseCountSequentialCorrectnessTest;
import tnx.assignment.executor.QuicksortFinishedTest;
import tnx.assignment.executor.QuicksortParallelAtAllTest;
import tnx.assignment.executor.QuicksortParallelCorrectnessTest;
import tnx.assignment.executor.QuicksortParallelOnlyOneLevelDeepTest;
import tnx.assignment.executor.QuicksortSequentialCorrectnessTest;
import tnx.assignment.executor.QuicksortSuitableForDebuggingTest;
import tnx.assignment.executor.QuicksortTaskCountTest;
import tnx.assignment.thread.AgeSumTest;
import tnx.assignment.thread.SimpleThreadFactoryTest;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({ SimpleThreadFactoryTest.class, AgeSumTest.class, NucleobaseCountSequentialCorrectnessTest.class,
		NucleobaseCount2WayParallelCorrectnessTest.class, NucleobaseCountNWayParallelCorrectnessTest.class,
		NucleobaseCountDivideAndConquerParallelCorrectnessTest.class, QuicksortSequentialCorrectnessTest.class,
		QuicksortParallelCorrectnessTest.class, QuicksortSuitableForDebuggingTest.class,
		QuicksortParallelAtAllTest.class, QuicksortParallelOnlyOneLevelDeepTest.class, QuicksortTaskCountTest.class,
		QuicksortFinishedTest.class })
public class ThreadsAndExecutorsTestSuite {

}
