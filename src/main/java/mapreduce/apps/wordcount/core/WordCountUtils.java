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
package mapreduce.apps.wordcount.core;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import edu.wustl.cse231s.IntendedForStaticAccessOnlyError;
import edu.wustl.cse231s.download.DownloadUtils;
import mapreduce.apps.wordcount.core.io.WordsResource;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
public class WordCountUtils {
	private WordCountUtils() {
		throw new IntendedForStaticAccessOnlyError();
	}
	
	public static TextSection[] toTextSections(String... strings) {
		TextSection[] result = new TextSection[strings.length];
		int i = 0;
		for (String string : strings) {
			result[i] = new TextSection(string);
			i++;
		}
		return result;
	}
	
	public static TextSection[] readAllLinesOfWords( WordsResource wordsResource ) throws IOException {
		//System.out.print("get/download: " + wordsResource + "\"" + wordsResource.getUrl() + "\"... ");
		File file = DownloadUtils.getDownloadedFile(wordsResource.getUrl(), wordsResource.getFileLength());
		//System.out.println(" done.");
		
		byte[] bytes = Files.readAllBytes(file.toPath());
		String s = new String(bytes, StandardCharsets.UTF_8);
				
		String[] sections = s.split(wordsResource.getDelimiter());

		TextSection[] textSections = new TextSection[ sections.length ];
		for( int i=0; i<sections.length; i++ ) {
			textSections[i] = new TextSection(sections[i]);
		}
		return textSections;
	}
	
	public static void printWordsToCountsSortedByCounts(Map<String, Integer> mapWordToCount) {
		List<Entry<String, Integer>> list = new ArrayList<>(mapWordToCount.entrySet());
		list.sort((a, b) -> {
			return a.getValue() - b.getValue();
		});

		for (Entry<String, Integer> entry : list) {
			System.out.println(entry);
		}
	}
}
