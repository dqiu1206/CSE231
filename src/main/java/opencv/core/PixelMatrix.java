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

import java.io.ByteArrayInputStream;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

import javafx.scene.image.Image;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
public final class PixelMatrix {
	private final Mat mat;

	public PixelMatrix(Mat mat) {
		this.mat = mat;
	}

	public int getWidth() {
		return this.mat.cols();
	}

	public int getHeight() {
		return this.mat.rows();
	}

	public PixelMatrix toGray() {
		Mat grayMat = new Mat(this.mat.rows(), this.mat.cols(), CvType.CV_8UC1);
		Imgproc.cvtColor(this.mat, grayMat, Imgproc.COLOR_RGB2GRAY);
		return new PixelMatrix(grayMat);
	}

	public Image toImage() {
		MatOfByte matOfByte = new MatOfByte();
		Imgcodecs.imencode(".bmp", this.mat, matOfByte);
		return new Image(new ByteArrayInputStream(matOfByte.toArray()));
	}

	public DetectedFace[] detectFaces(CascadeClassifier faceCascadeClassifier) {
		MatOfRect matOfRect = new MatOfRect();
		faceCascadeClassifier.detectMultiScale(this.mat, matOfRect);
		Rect[] rects = matOfRect.toArray();
		DetectedFace[] result = new DetectedFace[rects.length];
		for (int i = 0; i < result.length; i++) {
			result[i] = new DetectedFace(rects[i]);
		}
		return result;
	}

	public DetectedEye[][] detectEyes(CascadeClassifier eyeCascadeClassifier, DetectedFace[] faces) {
		DetectedEye[][] result = new DetectedEye[faces.length][];
		int i = 0;
		for (DetectedFace face : faces) {
			Rect faceRect = face.getRect();
			Mat subImage = this.mat.submat(faceRect.y, faceRect.y + faceRect.height, faceRect.x,
					faceRect.x + faceRect.width);
			MatOfRect matOfEyeRect = new MatOfRect();
			eyeCascadeClassifier.detectMultiScale(subImage, matOfEyeRect);
			int j = 0;
			result[i] = new DetectedEye[(int) matOfEyeRect.total()];
			for (Rect eyeRect : matOfEyeRect.toArray()) {
				result[i][j] = new DetectedEye(face,
						new Rect(eyeRect.x + faceRect.x, eyeRect.y + faceRect.y, eyeRect.width, eyeRect.height));
				j++;
			}
			i++;
		}
		return result;
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + "[" + this.mat + "]";
	}
}
