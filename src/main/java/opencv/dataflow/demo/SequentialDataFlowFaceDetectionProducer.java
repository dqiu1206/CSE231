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
package opencv.dataflow.demo;

import java.io.File;
import java.util.function.Consumer;

import org.opencv.objdetect.CascadeClassifier;

import javafx.scene.image.Image;
import opencv.core.DetectedEye;
import opencv.core.DetectedFace;
import opencv.core.OpenCVUtils;
import opencv.core.PixelMatrix;
import opencv.dataflow.core.DataFlowFaceDetectionProducer;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
public class SequentialDataFlowFaceDetectionProducer implements DataFlowFaceDetectionProducer {
	@Override
	public void produce(File file, CascadeClassifier faceCascadeClassifier, CascadeClassifier eyeCascadeClassifier,
			Consumer<Image> displayImageConsumer, Consumer<DetectedFace[]> detectedFacesConsumer,
			Consumer<DetectedEye[][]> detectedEyesConsumer) {
		PixelMatrix color = OpenCVUtils.loadImage(file);
		Image displayImage = color.toImage();
		displayImageConsumer.accept(displayImage);
		PixelMatrix gray = color.toGray();
		DetectedFace[] faces = gray.detectFaces(faceCascadeClassifier);
		detectedFacesConsumer.accept(faces);
		DetectedEye[][] eyes = gray.detectEyes(eyeCascadeClassifier, faces);
		detectedEyesConsumer.accept(eyes);
	}
}
