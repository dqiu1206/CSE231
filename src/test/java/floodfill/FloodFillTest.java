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
package floodfill;

import static edu.wustl.cse231s.rice.habanero.Habanero.launchHabaneroApp;

import java.nio.ByteBuffer;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.wustl.cse231s.color.ColorUtil;
import edu.wustl.cse231s.fx.FxImageUtils;
import edu.wustl.cse231s.pixels.MutablePixels;
import floodfill.studio.FloodFiller;
import floodfill.viz.FloodFillVizApp;
import floodfill.viz.FxMutablePixels;
import floodfill.viz.FxThreadConfinementPolicy;
import javafx.application.Application;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.WritableImage;
import javafx.scene.image.WritablePixelFormat;
import javafx.scene.paint.Color;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
public class FloodFillTest {
	@BeforeClass
	public static void initializeJavaFx() throws InterruptedException {
		// start up a JavaFx App so that Images are supported
		new Thread(() -> {
			Application.launch(FxStartLatchApp.class, new String[] {});
		}).start();
		FxStartLatchApp.awaitStarted();
	}

	@Test
	public void test() {
		WritableImage original = FloodFillVizApp.loadClampedToBlackAndWhiteWritableImage();

		int x = 100;
		int y = 100;

		Color[] colors = ColorUtil.getColorPalette();
		Color replacementColor = colors[0];

		int bytesPerPixel = 4;
		WritablePixelFormat<ByteBuffer> pixelformat = PixelFormat.getByteBgraInstance();
		int width = (int) original.getWidth();
		int height = (int) original.getHeight();

		byte[] originalBytes = FxImageUtils.getBuffer(original.getPixelReader(), bytesPerPixel, width, height,
				pixelformat);

		WritableImage studentImage = new WritableImage(width, height);
		FxImageUtils.copyBuffer(originalBytes, bytesPerPixel, width, height, pixelformat,
				studentImage.getPixelWriter());

		MutablePixels pixels = new FxMutablePixels(studentImage,
				FxThreadConfinementPolicy.NOT_CONFINED_TO_JAVAFX_THREAD);
		launchHabaneroApp(() -> {
			FloodFiller.floodFill(pixels, replacementColor, x, y);
		});
		Assert.assertTrue(InstructorSolutionUtils.isCorrect(original, studentImage, replacementColor, x, y));
	}
}
