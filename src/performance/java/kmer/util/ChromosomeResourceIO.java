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

package kmer.util;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FilenameUtils;

import edu.wustl.cse231s.bioinformatics.io.ChromosomeResource;
import edu.wustl.cse231s.bioinformatics.io.FastaSplitUtils;
import edu.wustl.cse231s.bioinformatics.io.FastaUtils;
import edu.wustl.cse231s.download.DownloadUtils;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
public class ChromosomeResourceIO {
	public static List<byte[]> readChromosomeSequences(ChromosomeResource chromosomeResource) throws IOException {
		File downloadsDirectory = DownloadUtils.getDownloadsDirectory();

		String filename = FilenameUtils.getName(chromosomeResource.getUrl().getPath());
		File sequencesFile = new File(downloadsDirectory, filename.replaceAll(".fna.gz", ".sequences.zip"));
		List<byte[]> originalSequences;
		if (sequencesFile.exists()) {
			originalSequences = FastaSplitUtils.readSequences(sequencesFile);
		} else {
			System.out.print("get/download: \"" + chromosomeResource.getUrl() + "\"... ");
			File chromosomeFile = DownloadUtils.getDownloadedFile(chromosomeResource.getUrl(),
					chromosomeResource.getFileLength());
			System.out.println(" done.");

			System.out.print("reading: " + chromosomeFile.getAbsolutePath() + "... ");
			byte[] chromosome = FastaUtils.read(chromosomeFile);
			System.out.println(" done.");
			originalSequences = FastaSplitUtils.splitIntoSequences(chromosome);
			FastaSplitUtils.writeSequences(sequencesFile, originalSequences);
		}
		return originalSequences;
	}
}
