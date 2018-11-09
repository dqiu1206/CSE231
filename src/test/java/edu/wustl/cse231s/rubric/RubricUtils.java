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
package edu.wustl.cse231s.rubric;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;

import javax.xml.bind.JAXBException;

import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import edu.wustl.cse231s.jaxb.JaxbUtils;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
public class RubricUtils {
	private static interface CheckedConsumer<T> {
		void accept(T value) throws NoSuchMethodException, IllegalAccessException, IllegalArgumentException,
				InvocationTargetException;
	}

	public static <A extends Annotation> void process(Class<?> suiteCls, Class<A> rubricCls,
			CheckedConsumer<Class<?>> consumer)
			throws NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		SuiteClasses childClses = suiteCls.getAnnotation(Suite.SuiteClasses.class);
		for (Class<?> childCls : childClses.value()) {
			if (childCls.isAnnotationPresent(Suite.SuiteClasses.class)) {
				process(childCls, rubricCls, consumer);
			} else {
				consumer.accept(childCls);
			}
		}
	}

	public static <A extends Annotation> TestSuiteJaxbElement process(Class<?> suiteCls, Class<A> rubricCls)
			throws NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		TestSuiteJaxbElement jaxbElement = new TestSuiteJaxbElement();
		process(suiteCls, rubricCls, (cls) -> {
			jaxbElement.addTestClass(cls, rubricCls);
		});
		return jaxbElement;
	}

	public static <A extends Annotation> void encode(Class<?> suiteCls, Class<A> rubricCls, File output)
			throws NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
			JAXBException {
		TestSuiteJaxbElement jaxbElement = process(suiteCls, rubricCls);
		JaxbUtils.encode(TestSuiteJaxbElement.class, jaxbElement, output);
	}
}
