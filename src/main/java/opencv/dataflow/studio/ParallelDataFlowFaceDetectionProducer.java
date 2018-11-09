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

import static edu.wustl.cse231s.rice.habanero.Habanero.async;
import static edu.wustl.cse231s.rice.habanero.Habanero.await;
import static edu.wustl.cse231s.rice.habanero.Habanero.finish;
import static edu.wustl.cse231s.rice.habanero.Habanero.future;
import static edu.wustl.cse231s.rice.habanero.Habanero.launchHabaneroApp;

import java.io.File;
import java.util.function.Consumer;

import org.opencv.objdetect.CascadeClassifier;

import edu.rice.hj.api.HjFuture;
import edu.rice.hj.api.HjMetrics;
import edu.rice.hj.runtime.config.HjSystemProperty;
import edu.wustl.cse231s.NotYetImplementedException;
import javafx.scene.image.Image;
import opencv.core.DetectedEye;
import opencv.core.DetectedFace;
import opencv.core.OpenCVUtils;
import opencv.core.PixelMatrix;
import opencv.dataflow.core.DataFlowFaceDetectionProducer;

/**
 * @author __STUDENT_NAME__
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
public class ParallelDataFlowFaceDetectionProducer implements DataFlowFaceDetectionProducer {
	@Override
	public void produce(File file, CascadeClassifier faceCascadeClassifier, CascadeClassifier eyeCascadeClassifier,
			Consumer<Image> displayImageConsumer, Consumer<DetectedFace[]> detectedFacesConsumer,
			Consumer<DetectedEye[][]> detectedEyesConsumer) {
		HjSystemProperty.abstractMetrics.set(true);
		launchHabaneroApp(() -> {
			finish(() -> {
				final HjFuture<PixelMatrix> loadColorMat = future(() -> {
					PixelMatrix color = OpenCVUtils.loadImage(file);
					return color;
                });
				final HjFuture<Integer> toDisplayImage = future(await(loadColorMat),() -> {
					Image displayImage = loadColorMat.safeGet().toImage();
					displayImageConsumer.accept(displayImage);
					return 1;
                });
				final HjFuture<PixelMatrix> toGrayMat = future(await(loadColorMat),() -> {
					PixelMatrix gray = loadColorMat.safeGet().toGray();
					return gray;
                });
				final HjFuture<DetectedFace[]> detectFaces = future(await(toGrayMat),() -> {
					DetectedFace[] faces = toGrayMat.safeGet().detectFaces(faceCascadeClassifier);
					detectedFacesConsumer.accept(faces);
					return faces;
                });
				final HjFuture<Integer> detectEyes = future(await(detectFaces),() -> {
					DetectedEye[][] eyes = toGrayMat.safeGet().detectEyes(eyeCascadeClassifier, detectFaces.safeGet());
					detectedEyesConsumer.accept(eyes);
					return 1;
                });
				
			});
		});
	}
}
