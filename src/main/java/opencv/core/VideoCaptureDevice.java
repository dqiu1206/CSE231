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

import org.apache.commons.lang3.SystemUtils;
import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.Videoio;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
public final class VideoCaptureDevice {
	private final VideoCapture videoCapture;

	public VideoCaptureDevice() {
		this.videoCapture = new VideoCapture(0);
	}

	public double getWidth() {
		return this.videoCapture.get(Videoio.CV_CAP_PROP_FRAME_WIDTH);
	}

	public double getHeight() {
		return this.videoCapture.get(Videoio.CV_CAP_PROP_FRAME_HEIGHT);
	}

	public boolean read(Mat mat) {
		// boolean b = videoCapture.retrieve(colorMat);
		return this.videoCapture.read(mat);
	}

	public void releaseIfNecessary() {
		if (SystemUtils.IS_OS_MAC_OSX) {
			// pass
		} else {
			videoCapture.release();
		}
	}
	
	@Override
	public String toString() {
		return this.getClass().getSimpleName() + "[" + this.videoCapture + "]";
	}
}
