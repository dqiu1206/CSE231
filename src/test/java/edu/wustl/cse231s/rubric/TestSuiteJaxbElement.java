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

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.junit.Test;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
@XmlRootElement(name = "testsuite")
public class TestSuiteJaxbElement {
	@XmlElement(name = "testclass")
	private List<TestClass> testClasses;

	public TestSuiteJaxbElement() {
	}
	
	public List<TestClass> getTestClasses() {
		return testClasses;
	}

	public <A extends Annotation> void addTestClass(Class<?> cls, Class<A> rubricCls) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		if (testClasses != null) {
			// pass
		} else {
			testClasses = new LinkedList<>();
		}
		TestClass testClass = new TestClass(cls, rubricCls);
		testClasses.add(testClass);
	}

	public static class TestClass {
		@XmlAttribute
		private String name;
		@XmlAttribute
		private String rubric;
		@XmlElement(name = "testmethod")
		private List<TestMethod> testMethods;

		public TestClass() {
		}
		public <A extends Annotation> TestClass(Class<?> cls, Class<A> rubricCls) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
			this.name = cls.getName();
			A annotation = cls.getAnnotation(rubricCls);
			if (annotation != null) {
				Method valueMthd = rubricCls.getMethod("value");
				Object a = valueMthd.invoke(annotation);
				Objects.requireNonNull(a);
				rubric = Arrays.toString((Object[])a);
			}
			for (Method mthd : cls.getMethods()) {
				Test test = mthd.getAnnotation(Test.class);
				if (test != null) {
					this.addTestMethod(mthd, rubricCls);
				}
			}
		}

		private <A extends Annotation> void addTestMethod(Method mthd, Class<A> rubricCls) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
			if (testMethods != null) {
				// pass
			} else {
				testMethods = new LinkedList<>();
			}
			TestMethod testMethod = new TestMethod(mthd, rubricCls);
			testMethods.add(testMethod);
		}

		@Override
		public String toString() {
			return "TestClass [name=" + name + ", rubric=" + rubric + ", testMethods=" + testMethods + "]";
		}
	}

	public static class TestMethod {
		@XmlAttribute
		private String name;
		@XmlAttribute
		private String rubric;

		public TestMethod() {
		}
		public <A extends Annotation> TestMethod(Method mthd, Class<A> rubricCls) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
			this.name = mthd.getName();
			A annotation = mthd.getAnnotation(rubricCls);
			if (annotation != null) {
				Method valueMthd = rubricCls.getMethod("value");
				Object a = valueMthd.invoke(annotation);
				Objects.requireNonNull(a);
				rubric = Arrays.toString((Object[])a);
			}
		}
		
		@Override
		public String toString() {
			return "TestMethod [name=" + name + ", rubric=" + rubric + "]";
		}

	}

	@Override
	public String toString() {
		return "TestSuiteJaxbElement [testClasses=" + testClasses + "]";
	}
}
