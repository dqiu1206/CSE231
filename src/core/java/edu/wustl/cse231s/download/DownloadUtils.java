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
package edu.wustl.cse231s.download;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
public enum DownloadUtils {
	;
	private static final long DO_NOT_CHECK_FILE_LENGTH = -1;
	public static File getDownloadsDirectory() {
		File homeDirectory = new File(System.getProperty("user.home"));
		File downloadsDirectory = new File( homeDirectory, "Downloads" );
		if( downloadsDirectory.exists() ) {
			if( downloadsDirectory.isDirectory() ) {
				return downloadsDirectory;
			} else {
				throw new RuntimeException( downloadsDirectory.getAbsolutePath() + " is not a directory." );
			}
		} else {
			throw new RuntimeException( downloadsDirectory.getAbsolutePath() + " does not exist." );
		}
	}
	public static File getDownloadsDirectory( String downloadsSubpath ) {
		File directory = getDownloadsDirectory();
		if( downloadsSubpath != null ) {
			directory = new File(directory, downloadsSubpath );
		}
		return directory;
	}
	public static File getDownloadedFile( URL url, long fileLengthCheck, String downloadsSubpath ) throws IOException {
		String name = FilenameUtils.getName(url.getFile());
		File directory = getDownloadsDirectory(downloadsSubpath);
		File file = new File( directory, name );
		boolean isAlreadyDownloaded = false;
		if( file.exists() ) {
			if( fileLengthCheck != DO_NOT_CHECK_FILE_LENGTH ) {
				long fileLength = file.length();
				if( fileLength == fileLengthCheck ) {
					isAlreadyDownloaded = true;
				} else {
					System.err.println();
					System.err.println();
					System.err.println();
					System.err.println( file + " file length check failed.  expecting: " + fileLengthCheck + "; found: " + fileLength + ".  Re-downloading." );
					System.err.println();
					System.err.println();
					System.err.println();
				}
			} else {
				isAlreadyDownloaded = true;
			}
		}
		
		if( isAlreadyDownloaded ) {
			//pas
		} else {
			FileUtils.copyURLToFile(url, file);
			if( fileLengthCheck != DO_NOT_CHECK_FILE_LENGTH ) {
				long fileLength = file.length();
				if( fileLength == fileLengthCheck ) {
					//pass 
				} else {
					throw new IOException( file + " file length check failed.  expecting: " + fileLengthCheck + "; found: " + fileLength );
				}
			}
		}
		return file;
	}
	public static File getDownloadedFile( URL url, long fileLengthCheck ) throws IOException {
		return getDownloadedFile(url, fileLengthCheck, null);
	}
	public static File getDownloadedFile( URL url ) throws IOException {
		return getDownloadedFile(url, DO_NOT_CHECK_FILE_LENGTH);
	}

	private static List<URL> listAll(URL urlDirectory) throws IOException {
		String directoryPath = urlDirectory.toExternalForm();
		List<URL> result = new LinkedList<>();
		Document doc = Jsoup.connect(directoryPath).get();
		for (Element file : doc.select("a[href]")) {
			String href = file.attr("href");
			String hrefAbs = file.attr("abs:href");
			if (href.startsWith("?")) {
				// pass
				//System.out.println("skipping: " + hrefAbs);
			} else {
				if (hrefAbs.startsWith(directoryPath)) {
					try {
						URL uri = new URL(hrefAbs);
						result.add(uri);
					} catch (MalformedURLException murle) {
						throw new RuntimeException(murle);
					}
				} else {
					//System.out.println("skipping: " + hrefAbs);
				}
			}
		}
		return result;
	}

	public static List<File> getDownloadedFiles( URL urlDirectory, String downloadsSubpath ) throws IOException {
		List<URL> urls = listAll(urlDirectory);
		List<File> result = new ArrayList<>(urls.size());
		for( URL url : urls ) {
			result.add(getDownloadedFile(url, DO_NOT_CHECK_FILE_LENGTH, downloadsSubpath));
		}
		return result;
	}
}
