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
package opencv.pipeline.studio;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.junit.Test;
import org.opencv.objdetect.CascadeClassifier;

import edu.wustl.cse231s.facesinthewild.FacesInTheWildUtils;
import javafx.scene.image.Image;
import opencv.core.DetectedEye;
import opencv.core.DetectedFace;
import opencv.core.OpenCVUtils;
import opencv.pipeline.core.PipelineFaceDetectionProducer;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
public class PipelineFaceDetectionTest {
	@Test
	public void test() {
		OpenCVUtils.loadLibrary();
		File[] files = FacesInTheWildUtils.getFiles("2002/07/19/big");

		CascadeClassifier faceCascadeClassifier = OpenCVUtils
				.newCascadeClassifier("haarcascades/haarcascade_frontalface_alt.xml");
		CascadeClassifier eyeCascadeClassifier = OpenCVUtils.newCascadeClassifier("haarcascades/haarcascade_eye.xml");
		PipelineFaceDetectionProducer pipeline = new ParallelPipelineFaceDetectionProducer();
		Queue<Image> displayImageQueue = new ConcurrentLinkedQueue<>();
		Queue<DetectedFace[]> facesQueue = new ConcurrentLinkedQueue<>();
		Queue<DetectedEye[][]> eyesQueue = new ConcurrentLinkedQueue<>();

		pipeline.produce(files, faceCascadeClassifier, eyeCascadeClassifier, (Integer i, Image displayImage) -> {
			assertNotNull(displayImage);
			assertEquals(displayImageQueue.size(), i.intValue());
			displayImageQueue.add(displayImage);
		}, (Integer i, DetectedFace[] faces) -> {
			assertEquals(facesQueue.size(), i.intValue());
			assertThat(displayImageQueue.size(), greaterThanOrEqualTo(i.intValue()));
			assertNotNull(faces);
			facesQueue.add(faces);
		}, (Integer i, DetectedEye[][] eyes) -> {
			assertEquals(eyesQueue.size(), i.intValue());
			assertThat(facesQueue.size(), greaterThanOrEqualTo(i.intValue()));
			assertNotNull(eyes);
			eyesQueue.add(eyes);
		});
	}
}
