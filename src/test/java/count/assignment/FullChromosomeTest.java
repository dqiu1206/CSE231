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
package count.assignment;

import static edu.wustl.cse231s.rice.habanero.Habanero.launchHabaneroApp;
import static edu.wustl.cse231s.rice.habanero.Habanero.numWorkerThreads;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import count.assignment.NucleobaseCounting;
import count.assignment.rubric.CountRubric;
import count.core.NucleobaseCountUtils;
import edu.wustl.cse231s.bioinformatics.Nucleobase;
import edu.wustl.cse231s.bioinformatics.io.ChromosomeResource;
import edu.wustl.cse231s.bioinformatics.io.FastaUtils;
import edu.wustl.cse231s.download.DownloadUtils;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
public class FullChromosomeTest {
	private static List<byte[]> chromosomes;
	static {
		try {
			chromosomes = new ArrayList<>(ChromosomeResource.values().length);
			for (ChromosomeResource chromosomeResource : ChromosomeResource.values()) {
				File file = DownloadUtils.getDownloadedFile(chromosomeResource.getUrl(),
						chromosomeResource.getFileLength());
				byte[] chromosome = FastaUtils.read(file);
				chromosomes.add(chromosome);
			}
		} catch (IOException ioe) {
			throw new RuntimeException(ioe);
		}
	}

	private void testUpperLowerNucleobase(Nucleobase nucleobase) {
		launchHabaneroApp(() -> {
			for (byte[] chromosome : chromosomes) {
				int expectedCount = NucleobaseCountUtils.countSequential(chromosome, nucleobase);
				int actualCount = NucleobaseCounting.countParallelUpperLowerSplit(chromosome, nucleobase);
				Assert.assertEquals(expectedCount, actualCount);
			}
		});
	}

	@Test
	@CountRubric(CountRubric.Category.UPPER_LOWER)
	public void testUpperLower() {
		for (Nucleobase nucleobase : Nucleobase.values()) {
			if (nucleobase == Nucleobase.URACIL) {
				// pass
			} else {
				this.testUpperLowerNucleobase(nucleobase);
			}
		}
	}

	@Test
	@CountRubric(CountRubric.Category.UPPER_LOWER)
	public void testUpperLowerUracil() {
		this.testUpperLowerNucleobase(Nucleobase.URACIL);
	}

	private void testNWayNucleobase(Nucleobase nucleobase) {
		launchHabaneroApp(() -> {
			int numWorkerThreads = numWorkerThreads();
			for( int numTasks : new int[] {numWorkerThreads, numWorkerThreads*2, numWorkerThreads*10} ) {
				for (byte[] chromosome : chromosomes) {
					int expectedCount = NucleobaseCountUtils.countSequential(chromosome, nucleobase);
					int actualCount = NucleobaseCounting.countParallelNWaySplit(chromosome, nucleobase, numTasks);
					Assert.assertEquals(expectedCount, actualCount);
				}
			}
		});
	}

	@Test
	@CountRubric(CountRubric.Category.NWAY)
	public void testNWay() {
		for (Nucleobase nucleobase : Nucleobase.values()) {
			if (nucleobase == Nucleobase.URACIL) {
				// pass
			} else {
				this.testNWayNucleobase(nucleobase);
			}
		}
	}

	@Test
	@CountRubric(CountRubric.Category.NWAY)
	public void testNWayUracil() {
		this.testNWayNucleobase(Nucleobase.URACIL);
	}
}
