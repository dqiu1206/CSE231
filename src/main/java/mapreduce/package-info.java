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

/**
 * MapReduce
 * <ol type="a">
 * <li>CardApp Studio: JUnit Test Suite {@link mapreduce.CardMapReduceStudioTestSuite}</li>
 * <ol>
 * <li>{@link mapreduce.apps.cards.studio.CardMapper}</li>
 * </ol>
 * <li>(Optional Encouraged) Word Count Warm-Up: JUnit Test Suite {@link mapreduce.WordCountMapReduceWarmUpTestSuite}</li>
 * <ol>
 * <li>{@link mapreduce.warmup.wordcount.WordCountConcreteStaticMapReduce}</li>
 * </ol>
 * <li>(Optional Encouraged) Mutual Friends Warm-Up: JUnit Test Suite {@link mapreduce.MutualFriendsMapReduceWarmUpTestSuite}</li>
 * <ol>
 * <li>{@link mapreduce.warmup.friends.MutualFriendsConcreteStaticMapReduce}</li>
 * </ol>
 * <li>MapReduce Assignment: test with {@link mapreduce.MapReduceAssignmentTestSuite}</li>
 * <ol>
 * <li>{@link mapreduce.apps.wordcount.assignment.WordCountMapper}</li>
 * <li>{@link mapreduce.apps.wordcount.assignment.IntegerSumClassicReducer}</li>
 * <li>{@link mapreduce.apps.friends.assignment.IntegerSumClassicReducer}</li>
 * <li>{@link mapreduce.apps.friends.assignment.MutualFriendsMapper}</li>
 * <li>{@link mapreduce.framework.assignment.ClassicReducer}</li>
 * <li>{@link mapreduce.framework.simple.assignment.SimpleMapReduceFramework}</li>
 * <li>{@link mapreduce.framework.matrix.assignment.MatrixMapReduceFramework}</li>
 * </ol>
 * <li>CholeraApp Studio: JUnit Test Suite {@link mapreduce.CholeraMapReduceStudioTestSuite}</li>
 * <ol>
 * <li>{@link mapreduce.apps.cholera.studio.CholeraMapper}</li>
 * </ol>
 * <li>(Optional Fun) One-Concurrent-Hash-Map-To-Rule-Them-All Framework: JUnit Test Suite {@link mapreduce.OneConcurrentHashMapToRuleThemAllMapReduceTestSuite}</li>
 * <ol>
 * <li>{@link mapreduce.framework.single.fun.OneConcurrentHashMapToRuleThemAllMapReduceFramework}</li>
 * </ol>
 * <li>(Optional Fun) Streams Framework: JUnit Test Suite {@link mapreduce.OneConcurrentHashMapToRuleThemAllMapReduceTestSuite}</li>
 * <ol>
 * <li>{@link mapreduce.framework.stream.fun.StreamMapReduceFramework}</li>
 * </ol>
 * </ol>
 * 
 * @author Finn Voichick
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
package mapreduce;
