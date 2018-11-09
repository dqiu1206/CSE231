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

import java.util.Arrays;
import java.util.Objects;

import org.opencv.core.Rect;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
public final class DetectedEye {
	private final DetectedFace detectedFace;
	private final Rect rect;

	public DetectedEye(DetectedFace detectedFace, Rect rect) {
		this.detectedFace = detectedFace;
		this.rect = rect;
	}

	public DetectedFace getDetectedFace() {
		return this.detectedFace;
	}

	public Rect getRect() {
		return this.rect;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DetectedEye other = (DetectedEye) obj;
		return Objects.equals(this.rect, other.rect) && Objects.equals(this.detectedFace, other.detectedFace);
	}
	
	@Override
	public String toString() {
		return this.getClass().getSimpleName() + "[detectedFace=" +this.detectedFace + ",rect="+ this.rect + "]";
	}
}
