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
package opencv.core;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.SystemUtils;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.objdetect.CascadeClassifier;

import edu.wustl.cse231s.download.DownloadUtils;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
public class OpenCVUtils {
	private static final int MAJOR_VERSION = 3;
	private static final int MINOR_VERSION = 3;
	private static final int MICRO_VERSION = 1;

	private OpenCVUtils() {
		throw new Error();
	}

	public static CascadeClassifier newCascadeClassifier(String subpath) {
		File faceFile = new File(OpenCVUtils.getDataDirectory(), subpath);
		if (faceFile.exists()) {
			return new CascadeClassifier(faceFile.getAbsolutePath());
		} else {
			throw new IllegalArgumentException(subpath);
		}
	}

	public static PixelMatrix loadImage(File file) {
		Mat mat = Imgcodecs.imread(file.getAbsolutePath());
		return new PixelMatrix(mat);
	}

	private static String getFirstOpenCVJarPathFromClassPath() {
		String classPath = System.getProperty("java.class.path");
		String separator = System.getProperty("path.separator");

		String baseName = String.format("opencv-%d%d%d.jar", MAJOR_VERSION, MINOR_VERSION, MICRO_VERSION);

		for (String subPath : classPath.split(separator)) {
			if (subPath.endsWith(baseName)) {
				return subPath;
			}
		}
		throw new Error("path to " + baseName + " not found in classpath");
	}

	public static File getDataDirectory() {
		String pathFromClassPath = getFirstOpenCVJarPathFromClassPath();
		File fileFromClassPath = new File(pathFromClassPath);
		return new File(fileFromClassPath.getParentFile().getParentFile(), "etc");
	}

	private static File getHomeBrewFile() {
		File cellarDirectory = new File("/usr/local/Cellar");
		if (cellarDirectory.isDirectory()) {
			File opencvDirectory = new File(cellarDirectory, "opencv");
			if (opencvDirectory.isDirectory()) {
				String baseName = String.format("libopencv_java%d%d%d", MAJOR_VERSION, MINOR_VERSION, MICRO_VERSION);
				List<String> versionTexts = new LinkedList<>();
				String MAC_VERSION_POSTFIX = "_1";
				versionTexts.add(
						String.format("%d.%d.%d%s", MAJOR_VERSION, MINOR_VERSION, MICRO_VERSION, MAC_VERSION_POSTFIX));
				for (String versionText : versionTexts) {
					File versionDirectory = new File(opencvDirectory, versionText);
					if (versionDirectory.isDirectory()) {
						File directory = new File(versionDirectory, "share/OpenCV/java/");
						if (directory.exists()) {
							File file = new File(directory, baseName + ".so");
							if (file.exists()) {
								// pass
							} else {
								file = new File(directory, baseName + ".dylib");
							}
							if (file.exists()) {
								System.out.println("found: " + file);
								return file;
							} else {
								System.err.println("neither " + baseName + ".so nor .dylib found in "
										+ directory.getAbsolutePath());
							}
						} else {
							System.err.println(directory + " does not exist.");
						}
					}
				}
				System.err.println("share/OpenCV/java/" + baseName + ".so nor .dylib found in "
						+ opencvDirectory.getAbsolutePath() + " " + versionTexts);
			} else {
				System.err.println("install opencv3");
			}
		} else {
			System.err.println("install homebrew");
		}
		return null;
	}

	private static File getDownloadedFile() throws IOException {
		String semester = "f17";
		long fileSize;
		String prefix;
		String postfix;
		if (SystemUtils.IS_OS_WINDOWS) {
			StringBuilder sbPrefix = new StringBuilder();
			if (System.getProperty("sun.arch.data.model").contains("64")) {
				sbPrefix.append("x64");
				fileSize = 45_385_728L;
			} else {
				sbPrefix.append("x86");
				fileSize = 28_284_416L;
			}
			sbPrefix.append("/");
			prefix = sbPrefix.toString();
			postfix = ".dll";
		} else if (SystemUtils.IS_OS_MAC_OSX) {
			fileSize = -1L;
			prefix = "osx/lib";
			postfix = ".dylib";
		} else {
			throw new RuntimeException("todo: handle " + System.getProperty("sun.arch.data.model"));
		}
		StringBuilder sb = new StringBuilder();
		sb.append("http://www.cse.wustl.edu/~cosgroved/courses/cse231/");
		sb.append(semester);
		sb.append("/opencv/build/java/");
		sb.append(prefix);
		sb.append("opencv_java");
		sb.append(MAJOR_VERSION);
		sb.append(MINOR_VERSION);
		sb.append(MICRO_VERSION);
		sb.append(postfix);
		URL url = new URL(sb.toString());

		System.out.println("Downloading " + url + ".  This might take a while...");

		return DownloadUtils.getDownloadedFile(url, fileSize);
	}

	public static void loadLibrary() {
		String libraryPath = System.getProperty("java.library.path");
		if (libraryPath.contains("opencv/build/java")) {
			System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		} else {
			String path = System.getProperty("opencv.path");
			if (path != null) {
				System.load(path);
			} else {
				File file;
				if(SystemUtils.IS_OS_MAC) {
					file = getHomeBrewFile();
				} else {
					file = null;
				}
				if( file != null ) {
					//pass
				} else {
					try {
						file = getDownloadedFile();
					} catch(IOException ioe) {
						throw new RuntimeException(ioe);
					}
				}
				if( file != null ) {
					System.load(file.getAbsolutePath());
				} else {
					throw new RuntimeException("opencv not found");
				}
			}
		}
	}
}
