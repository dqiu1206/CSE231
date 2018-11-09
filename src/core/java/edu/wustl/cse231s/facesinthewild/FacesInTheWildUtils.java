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
package edu.wustl.cse231s.facesinthewild;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;

import edu.wustl.cse231s.IntendedForStaticAccessOnlyError;
import edu.wustl.cse231s.download.DownloadUtils;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
public class FacesInTheWildUtils {
	private FacesInTheWildUtils() {
		throw new IntendedForStaticAccessOnlyError();
	}
	

	public static File getFile(String subPath) {
		try {
			String subDirectoryPath = subPath.substring(0, subPath.lastIndexOf('/'));
			return DownloadUtils.getDownloadedFile(new URL("http://www.cse.wustl.edu/~cosgroved/courses/cse231/s17/facesInTheWild/"+subPath), -1, "facesInTheWild/"+subDirectoryPath);
		} catch( IOException e ) {
			throw new RuntimeException(e);
		}
	}
	public static File[] getFiles( String subpath ) {
		try {
			List<File> fileList = DownloadUtils.getDownloadedFiles(new URL("http://www.cse.wustl.edu/~cosgroved/courses/cse231/s17/facesInTheWild/2002/07/19/big/"), "facesInTheWild/2002/07/19/big/");
			return fileList.toArray(new File[fileList.size()]);
		} catch( IOException ioe ) {
			throw new RuntimeException( ioe );
		}
	}
}
