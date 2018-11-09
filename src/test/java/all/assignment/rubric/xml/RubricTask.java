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
package all.assignment.rubric.xml;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.util.Objects;

import javax.xml.bind.JAXBException;

import backtrack.assignment.BacktrackTestSuite;
import backtrack.assignment.rubric.BacktrackRubric;
import count.assignment.CountTestSuite;
import count.assignment.rubric.CountRubric;
import edu.wustl.cse231s.jaxb.JaxbUtils;
import edu.wustl.cse231s.rubric.RubricUtils;
import edu.wustl.cse231s.rubric.TestSuiteJaxbElement;
import kmer.assignment.KMerTestSuite;
import kmer.assignment.rubric.KMerRubric;
import mapreduce.MapReduceAssignmentTestSuite;
import mapreduce.assignment.rubric.MapReduceRubric;
import tnx.assignment.ThreadsAndExecutorsTestSuite;
import tnx.assignment.rubric.TnXRubric;
import util.assignment.RequiredListMapTestSuite;
import util.assignment.rubric.ListMapRubric;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
public enum RubricTask {
	COUNT(CountTestSuite.class, CountRubric.class),
	LIST_AND_MAP(RequiredListMapTestSuite.class, ListMapRubric.class),
	THREADS_AND_EXECUTORS(ThreadsAndExecutorsTestSuite.class, TnXRubric.class),
	MAP_REDUCE(MapReduceAssignmentTestSuite.class, MapReduceRubric.class),
	BACKTRACK(BacktrackTestSuite.class, BacktrackRubric.class),
	K_MER(KMerTestSuite.class, KMerRubric.class);

	private final Class<?> suiteCls;
	private final Class<? extends Annotation> rubricCls;
	
	private RubricTask(Class<?> suiteCls, Class<? extends Annotation> rubricCls) {
		Objects.requireNonNull(suiteCls);
		Objects.requireNonNull(rubricCls);
		this.suiteCls = suiteCls;
		this.rubricCls = rubricCls;
	}
	
	private File getXmlFile(File directory) {
		return new File(directory, this.name() + ".xml");
	}
	
	public TestSuiteJaxbElement decodeFromDirectory(File directory) throws JAXBException {
		return JaxbUtils.decode(TestSuiteJaxbElement.class, this.getXmlFile(directory));
	}

	public void encodeToDirectory(File directory) throws NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, JAXBException {
		File outputFile = this.getXmlFile(directory);
		RubricUtils.encode(suiteCls, rubricCls, outputFile);
	}
}
