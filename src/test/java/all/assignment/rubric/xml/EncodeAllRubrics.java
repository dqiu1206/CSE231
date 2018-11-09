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
import java.lang.reflect.InvocationTargetException;

import javax.xml.bind.JAXBException;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
public class EncodeAllRubrics {
	public static void main(String[] args) throws NoSuchMethodException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException, JAXBException {
		String pathToDirectory;
		if (args.length > 0) {
			pathToDirectory = args[0];
		} else {
			pathToDirectory = System.getProperty("user.home") + System.getProperty("file.separator") + "Documents";
		}

		File directory = new File(pathToDirectory);

		if (directory.exists()) {
			for (RubricTask rubric : RubricTask.values()) {
				rubric.encodeToDirectory(directory);
				System.out.println(rubric);
			}
		} else {
			throw new RuntimeException(pathToDirectory);
		}
	}
}
