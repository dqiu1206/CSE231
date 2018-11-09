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
package opencv.dataflow.studio;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.mutable.MutableObject;
import org.junit.Test;
import org.opencv.objdetect.CascadeClassifier;

import edu.rice.hj.runtime.baseruntime.BaseRuntime;
import edu.rice.hj.runtime.baseruntime.HabaneroActivity;
import edu.wustl.cse231s.facesinthewild.FacesInTheWildUtils;
import edu.wustl.cse231s.image.ImageUtls;
import edu.wustl.cse231s.rice.habanero.Habanero;
import javafx.scene.image.Image;
import opencv.core.DetectedEye;
import opencv.core.DetectedFace;
import opencv.core.OpenCVUtils;
import opencv.core.PixelMatrix;
import opencv.dataflow.core.DataFlowFaceDetectionProducer;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 * 
 *         {@link ParallelDataFlowFaceDetectionProducer#produce(File, CascadeClassifier, CascadeClassifier, java.util.function.Consumer, java.util.function.Consumer, java.util.function.Consumer)}
 */
public class DataFlowFaceDetectionTest {
	@Test
	public void test() {
		OpenCVUtils.loadLibrary();

		File file = FacesInTheWildUtils.getFile("2002/07/19/big/img_576.jpg");

		CascadeClassifier faceCascadeClassifier = OpenCVUtils
				.newCascadeClassifier("haarcascades/haarcascade_frontalface_default.xml");
		CascadeClassifier eyeCascadeClassifier = OpenCVUtils.newCascadeClassifier("haarcascades/haarcascade_eye.xml");

		DataFlowFaceDetectionProducer dataFlow = new ParallelDataFlowFaceDetectionProducer();

		Set<Long> activityIds = ConcurrentHashMap.newKeySet();

		Set<String> futureSuspAndAsyncSusp = new HashSet<>(Arrays.asList("futureSusp", "asyncSusp"));

		Set<String> futureAwaitAndAsyncSuspAwait = new HashSet<>(Arrays.asList("futureAwait", "asyncSuspAwait"));

		MutableObject<Image> mutableDisplayImage = new MutableObject<>();
		MutableObject<DetectedFace[]> mutableFaces = new MutableObject<>();
		MutableObject<DetectedEye[][]> mutableEyes = new MutableObject<>();
		dataFlow.produce(file, faceCascadeClassifier, eyeCascadeClassifier, (Image displayImage) -> {
			assertNull(mutableDisplayImage.getValue());
			assertNotNull(displayImage);
			mutableDisplayImage.setValue(displayImage);
			assertTrue(Habanero.isLaunched());
			HabaneroActivity habaneroActivity = BaseRuntime.currentHabaneroActivity();
			assertNotNull(habaneroActivity);
			assertThat(activityIds, not(contains(habaneroActivity.id())));
			activityIds.add(habaneroActivity.id());
			assertNotEquals("init_activity", habaneroActivity.name());

			// check for await()
			assertThat(futureSuspAndAsyncSusp, not(hasItem(habaneroActivity.name())));

			assertThat(futureAwaitAndAsyncSuspAwait, hasItem(habaneroActivity.name()));
		}, (DetectedFace[] faces) -> {
			assertNull(mutableFaces.getValue());
			assertNotNull(faces);
			mutableFaces.setValue(faces);
			assertTrue(Habanero.isLaunched());
			HabaneroActivity habaneroActivity = BaseRuntime.currentHabaneroActivity();
			assertNotNull(habaneroActivity);
			assertThat(activityIds, not(contains(habaneroActivity.id())));
			activityIds.add(habaneroActivity.id());
			assertNotEquals("init_activity", habaneroActivity.name());

			// check for await()
			assertNotEquals("futureSusp", habaneroActivity.name());

			assertEquals("futureAwait", habaneroActivity.name());
		}, (DetectedEye[][] eyes) -> {
			assertNull(mutableEyes.getValue());
			assertNotNull(eyes);
			mutableEyes.setValue(eyes);
			assertTrue(Habanero.isLaunched());
			HabaneroActivity habaneroActivity = BaseRuntime.currentHabaneroActivity();
			assertNotNull(habaneroActivity);
			assertThat(activityIds, not(contains(habaneroActivity.id())));
			activityIds.add(habaneroActivity.id());
			assertNotEquals("init_activity", habaneroActivity.name());

			// check for await()
			assertThat(futureSuspAndAsyncSusp, not(hasItem(habaneroActivity.name())));

			assertThat(futureAwaitAndAsyncSuspAwait, hasItem(habaneroActivity.name()));
		});

		Image actualImage = mutableDisplayImage.getValue();
		DetectedFace[] actualFaces = mutableFaces.getValue();
		DetectedEye[][] actualEyes = mutableEyes.getValue();
		assertNotNull(actualImage);
		assertNotNull(actualFaces);
		assertNotNull(actualEyes);

		PixelMatrix color = OpenCVUtils.loadImage(file);
		Image expectedImage = color.toImage();
		assertTrue(ImageUtls.areEquals(expectedImage, actualImage));

		PixelMatrix gray = color.toGray();

		int NUM_ATTEMPTS = 5;
		for (int attemptIndex = 0; attemptIndex < NUM_ATTEMPTS; attemptIndex++) {
			DetectedFace[] expectedFaces = gray.detectFaces(faceCascadeClassifier);

			boolean isSuccessful = true;
			if (actualFaces.length == expectedFaces.length) {
				List<DetectedFace> list = Arrays.asList(expectedFaces);
				for (DetectedFace actualFace : actualFaces) {
					if (list.contains(actualFace)) {
						// pass
					} else {
						isSuccessful = false;
						break;
					}
				}
			} else {
				isSuccessful = false;
			}
			if (isSuccessful) {
				break;
			} else {
				if (attemptIndex == NUM_ATTEMPTS - 1) {
					StringBuilder sb = new StringBuilder();
					sb.append("todo for instructors: handle OpenCV non-determinism").append("\n");
					sb.append(Arrays.toString(expectedFaces)).append("\n");
					sb.append(Arrays.toString(actualFaces));
					assertTrue(sb.toString(), false);
				}
			}
		}
		final boolean IS_READY_TO_TEST_EYES = false;
		if (IS_READY_TO_TEST_EYES) {
			for (int attemptIndex = 0; attemptIndex < NUM_ATTEMPTS; attemptIndex++) {
				DetectedFace[] expectedFaces = gray.detectFaces(faceCascadeClassifier);
				DetectedEye[][] expectedEyes = gray.detectEyes(eyeCascadeClassifier, expectedFaces);
				boolean isSuccessful = true;
				if (expectedEyes.length == actualEyes.length) {
					for (int i = 0; i < expectedEyes.length; i++) {
						if (expectedEyes[i].length == actualEyes[i].length) {
							// pass
						} else {
							isSuccessful = false;
							break;
						}
					}
				} else {
					isSuccessful = false;
				}
				if (isSuccessful) {
					break;
				} else {
					if (attemptIndex == NUM_ATTEMPTS - 1) {
						StringBuilder sb = new StringBuilder();
						sb.append("todo for instructors: handle OpenCV non-determinism").append("\n");
						for (int i = 0; i < expectedEyes.length; i++) {
							sb.append(Arrays.toString(expectedEyes[i])).append(",");
						}
						sb.append("\n");
						for (int i = 0; i < actualEyes.length; i++) {
							sb.append(Arrays.toString(actualEyes[i])).append(",");
						}
						sb.append("\n");

						// System.err.println(sb);
						assertTrue(sb.toString(), false);
					}
				}
			}
		}
	}
}
