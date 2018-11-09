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
package kmer.assignment.isolatedbucketdictionary;

import static edu.wustl.cse231s.rice.habanero.Habanero.launchHabaneroApp;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Field;

import org.junit.Test;

import edu.rice.hj.api.HjRuntime;
import edu.rice.hj.runtime.baseruntime.BaseRuntime;
import edu.rice.hj.runtime.baseruntime.HabaneroActivity;
import edu.rice.hj.runtime.baseruntime.IsolationManager;
import edu.rice.hj.runtime.config.HjConfiguration;
import edu.wustl.cse231s.junit.NonStudentTestRuntimeException;
import kmer.assignment.rubric.KMerRubric;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 * 
 *         {@link IsolatedBucketDictionary#compute(Object, java.util.function.BiFunction)}
 */
@KMerRubric(KMerRubric.Category.BUCKET_DICTIONARY)
public class ComputeIsIsolatedTest {
	@Test
	public void test() {
		launchHabaneroApp(() -> {
			IsolatedBucketDictionary<String, Integer> dictionary = new IsolatedBucketDictionary<>(1024);
			dictionary.compute("score", (k, v) -> {
				HabaneroActivity activity = BaseRuntime.currentHabaneroActivity();
				assertEquals("init_activity", activity.name());

				IsolationManager isolationManager = getIsolationManager();
				assertNotNull(NonStudentTestRuntimeException.MESSAGE, isolationManager);
				assertTrue(isolationManager.isInsideIsolatedBlock());

				return 71;
			});
		});
	}

	private static IsolationManager getIsolationManager() {
		HjRuntime runtime = HjConfiguration.runtime();
		assertNotNull(NonStudentTestRuntimeException.MESSAGE, runtime);
		if (runtime instanceof BaseRuntime) {
			BaseRuntime baseRuntime = (BaseRuntime) runtime;
			Field field;
			try {
				field = BaseRuntime.class.getDeclaredField("isolationManager");
				assertNotNull(NonStudentTestRuntimeException.MESSAGE, field);
				try {
					field.setAccessible(true);
				} catch (SecurityException e) {
					throw new NonStudentTestRuntimeException(e);
				}
				try {
					return (IsolationManager) field.get(baseRuntime);
				} catch (IllegalArgumentException | IllegalAccessException e) {
					throw new NonStudentTestRuntimeException(e);
				}
			} catch (NoSuchFieldException | SecurityException e) {
				throw new NonStudentTestRuntimeException(e);
			}
		} else {
			throw new NonStudentTestRuntimeException(
					"runtime is not an instance of BaseRuntime: " + runtime.getClass().toString());
		}
	}
}
