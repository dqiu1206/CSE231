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
package datarace.demo.wordscore;

import static edu.wustl.cse231s.rice.habanero.Habanero.launchHabaneroApp;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Test;

import datarace.core.wordscore.DataCleaningUtils;
import datarace.core.wordscore.WordScoreUtils;
import edu.wustl.cse231s.download.DownloadUtils;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
public class SuspectWordScoreTest {
	public SuspectWordScoreTest() throws IOException {
		URL wordsUrl = new URL("https://users.cs.duke.edu/~ola/ap/linuxwords");
		//System.out.print("get/download: \"" + wordsUrl + "\"... ");
		File file = DownloadUtils.getDownloadedFile(wordsUrl, 409048L);
		//System.out.println(" done.");

		//System.out.print("reading: " + file.getAbsolutePath() + "... ");
		List<String> lines = FileUtils.readLines(file, StandardCharsets.UTF_8);
		//System.out.println(" done.");

		//System.out.print("toUnique: ");
		this.sourceLines = DataCleaningUtils.toUnique(lines);
		//System.out.println(" done.");
	}

	@Test
	public void testClean() {
		launchHabaneroApp(() -> {
			List<String> cleanedWords = SuspectWordScore.toCleanedWordsViaArray(this.sourceLines);
			for(String cleanedWord : cleanedWords ) {
				Assert.assertTrue(WordScoreUtils.isCleanedWord(cleanedWord));
			}
		});
	}

	@Test
	public void testSuspectClean() {
		launchHabaneroApp(() -> {
			//List<String> expected = SuspectWordScore.toCleanedWordsViaArray(this.sourceLines);
			List<String> actual = SuspectWordScore.suspectToCleanedWordsViaArray(this.sourceLines);
			//Assert.assertEquals(expected, actual);
			for(String cleanedWord : actual ) {
				Assert.assertTrue(WordScoreUtils.isCleanedWord(cleanedWord));
			}
		});
	}

	private final List<String> sourceLines;
}
