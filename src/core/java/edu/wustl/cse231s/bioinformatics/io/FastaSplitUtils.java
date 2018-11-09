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
package edu.wustl.cse231s.bioinformatics.io;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
public class FastaSplitUtils {
	private FastaSplitUtils() {
		throw new Error();
	}
	
	public static List<byte[]> splitIntoSequences(byte[] chromosome) {
		// holy smokes is this ridiculously unoptimized
		List<byte[]> sequences = new LinkedList<>();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		for (int i = 0; i < chromosome.length; i++) {
			byte b = chromosome[i];
			switch ((char) b) {
			case 'A':
			case 'C':
			case 'T':
			case 'G':
				baos.write(chromosome, i, 1);
				break;
			case 'U':
			case 'R':
			case 'Y':
			case 'K':
			case 'M':
			case 'S':
			case 'W':
			case 'B':
			case 'D':
			case 'H':
			case 'V':
			case 'N':
				if (baos.size() > 0) {
					sequences.add(baos.toByteArray());
					baos.reset();
				}
				break;
			default:
				throw new RuntimeException("" + (char) b);
			}
		}
		if (baos.size() > 0) {
			sequences.add(baos.toByteArray());
		}
		return sequences;
	}

	public static List<byte[]> readSequences(File file) throws IOException {
		List<byte[]> result = new LinkedList<>();
		ZipFile zipFile = new ZipFile(file);
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			for (ZipEntry zipEntry : Collections.list(zipFile.entries())) {
				InputStream is = zipFile.getInputStream(zipEntry);
				baos.reset();
				while (true) {
					int len = is.read(buffer);
					if (len > 0) {
						baos.write(buffer, 0, len);
					} else {
						break;
					}
				}
				result.add(baos.toByteArray());
			}
		} finally {
			zipFile.close();
		}
		return result;
	}

	public static void writeSequences(File file, List<byte[]> sequences) throws IOException {
		FileOutputStream fos = new FileOutputStream(file);
		try {
			ZipOutputStream zos = new ZipOutputStream(fos);
			try {
				int i = 0;
				for (byte[] sequence : sequences) {
					ZipEntry zipEntry = new ZipEntry("sequence" + i);
					zos.putNextEntry(zipEntry);
					zos.write(sequence, 0, sequence.length);
					zos.closeEntry();
					i++;
				}
			} finally {
				zos.close();
			}
		} finally {
			fos.close();
		}

	}
}
