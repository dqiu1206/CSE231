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

package opencv.viz;

import org.opencv.core.Rect;

import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import opencv.core.DetectedEye;
import opencv.core.DetectedFace;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
public class FaceDetectionPane extends Pane {
	private final ImageView imageView = new ImageView();
	private final Rectangle[] faceRectangleArray = createRectangleArray(5, Color.YELLOW);
	private final Rectangle[][] eyeRectangleMatrix = createRectangleMatrix(5, 4, Color.BLUE);
	private final double xScale;
	private final double yScale;
	
	public FaceDetectionPane(double xScale, double yScale) {
		this.getChildren().add(imageView);
		this.getChildren().addAll(faceRectangleArray);
		for (Rectangle[] row : eyeRectangleMatrix) {
			this.getChildren().addAll(row);
		}
		this.xScale = xScale;
		this.yScale = yScale;
	}
	public FaceDetectionPane() {
		this(1.0, 1.0);
	}

	public void setImage(Image image) {
		if (Platform.isFxApplicationThread()) {
			this.imageView.setImage(image);
			this.setFitWidth(image.getWidth());
			this.setFitHeight(image.getHeight());
		} else {
			Platform.runLater(() -> {
				setImage(image);
			});
		}
	}

	public void setFitWidth(double width) {
		this.imageView.setFitWidth(width*this.xScale);
	}

	public void setFitHeight(double height) {
		this.imageView.setFitHeight(height*this.yScale);
	}

	public void setDetectedFaces(DetectedFace[] detectedFaces) {
		if (Platform.isFxApplicationThread()) {
			for (int i = 0; i < faceRectangleArray.length; i++) {
				setRectangle(faceRectangleArray[i], (detectedFaces.length > i) ? detectedFaces[i].getRect() : null);
			}
		} else {
			Platform.runLater(() -> {
				setDetectedFaces(detectedFaces);
			});
		}
	}

	public void setDetectedEyes(DetectedEye[][] detectedEyes) {
		if (Platform.isFxApplicationThread()) {
			for (int i = 0; i < eyeRectangleMatrix.length; i++) {
				for (int j = 0; j < eyeRectangleMatrix[i].length; j++) {
					setRectangle(eyeRectangleMatrix[i][j],
							(detectedEyes.length > i && detectedEyes[i].length > j) ? detectedEyes[i][j].getRect() : null);
				}
			}
		} else {
			Platform.runLater(() -> {
				setDetectedEyes(detectedEyes);
			});
		}
	}

	private void setRectangle(Rectangle rectangle, Rect rect) {
		if (rect != null) {
			rectangle.setX(rect.x*this.xScale);
			rectangle.setY(rect.y*this.yScale);
			rectangle.setWidth(rect.width*this.xScale);
			rectangle.setHeight(rect.height*this.yScale);
		} else {
			rectangle.setWidth(0);
			rectangle.setHeight(0);
		}
	}

	private static Rectangle[] createRectangleArray(int n, Color strokeColor) {
		Rectangle[] result = new Rectangle[n];
		for (int i = 0; i < result.length; i++) {
			result[i] = new Rectangle();
			result[i].setFill(Color.TRANSPARENT);
			result[i].setStroke(strokeColor);
		}
		return result;
	}

	private static Rectangle[][] createRectangleMatrix(int n, int m, Color strokeColor) {
		Rectangle[][] result = new Rectangle[n][m];
		for (int i = 0; i < result.length; i++) {
			for (int j = 0; j < result[i].length; j++) {
				result[i][j] = new Rectangle();
				result[i][j].setFill(Color.TRANSPARENT);
				result[i][j].setStroke(strokeColor);
			}
		}
		return result;
	}
}
