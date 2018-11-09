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
package mapreduce;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import mapreduce.apps.cards.assignment.CardMapReduceAllTest;
import mapreduce.apps.cholera.application.CholeraMapReduceAllTest;
import mapreduce.apps.friends.assignment.MutualFriendsMapReduceAllTest;
import mapreduce.apps.friends.assignment.MutualFriendsMapperTest;
import mapreduce.apps.friends.assignment.MutualFriendsReducerTest;
import mapreduce.apps.wordcount.assignment.IntegerSumClassicReducerPointedTest;
import mapreduce.apps.wordcount.assignment.WordCountCollectorStressTest;
import mapreduce.apps.wordcount.assignment.WordCountMapperLetterCaseTest;
import mapreduce.apps.wordcount.assignment.WordCountMapperStressTest;
import mapreduce.apps.wordcount.assignment.WordCountReducerStressTest;
import mapreduce.assignment.InstructorSolutionTest;
import mapreduce.framework.assignment.ClassicReducerTest;
import mapreduce.framework.simple.assignment.AccumulateAllSimpleFrameworkPointedTest;
import mapreduce.framework.simple.assignment.AccumulateAllSimpleFrameworkStressTest;
import mapreduce.framework.simple.assignment.FinishAllSimpleFrameworkPointedTest;
import mapreduce.framework.simple.assignment.FinishAllSimpleFrameworkStressTest;
import mapreduce.framework.simple.assignment.MapAllSimpleFrameworkPointedTest;
import mapreduce.framework.simple.assignment.MapAllSimpleFrameworkStressTest;
import mapreduce.framework.simple.assignment.SimpleFrameworkStressTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({ InstructorSolutionTest.class, WordCountMapperLetterCaseTest.class,
		WordCountMapperStressTest.class,

		IntegerSumClassicReducerPointedTest.class, WordCountReducerStressTest.class,

		MutualFriendsMapperTest.class, MutualFriendsReducerTest.class,

		ClassicReducerTest.class, WordCountCollectorStressTest.class,

		// Simple
		MapAllSimpleFrameworkPointedTest.class, MapAllSimpleFrameworkStressTest.class,
		AccumulateAllSimpleFrameworkPointedTest.class, AccumulateAllSimpleFrameworkStressTest.class,
		FinishAllSimpleFrameworkPointedTest.class, FinishAllSimpleFrameworkStressTest.class,
		SimpleFrameworkStressTest.class,

		// Matrix Test Suite
		MatrixMapReduceTestSuite.class,

		// All
		MutualFriendsMapReduceAllTest.class, CardMapReduceAllTest.class, CholeraMapReduceAllTest.class })
/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
public class MapReduceAssignmentTestSuite {
	// @Suite.SuiteClasses annotation defines test suite
}
