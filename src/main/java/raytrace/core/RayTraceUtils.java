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
package raytrace.core;

import java.awt.BorderLayout;
import java.io.File;
import java.net.URL;
import java.util.concurrent.ConcurrentLinkedDeque;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.sunflow.SunflowAPI;

import raytrace.core.runtime.RtImageSampler;
import raytrace.core.runtime.RtVizPanel;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
public class RayTraceUtils {
	private static void launch(RayTracer rayTracer, URL url) {
		SwingUtilities.invokeLater(() -> {
			RtVizPanel panel = new RtVizPanel();
			RtImageSampler imageSampler = new RtImageSampler(rayTracer);

			JFrame frame = new JFrame("raytrace");
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.getContentPane().add(panel, BorderLayout.CENTER);
			frame.pack();
			frame.setVisible(true);

			String path = url.getFile();
			File file = new File(path);
			if (file.exists()) {
				new Thread(() -> {
					SunflowAPI api = new SunflowAPI();
					api.parse(path);
					api.build();
					api.options(SunflowAPI.DEFAULT_OPTIONS);
					api.render(SunflowAPI.DEFAULT_OPTIONS, panel, imageSampler);
				}).start();
			}
		});
	}

	public static void launchBunnyScene(RayTracer rayTracer) {
		launch(rayTracer, RayTraceUtils.class.getResource("../bunny.sc"));
	}

	public static ConcurrentLinkedDeque<Section> createSections(int xMin, int yMin, int xMaxExclusive,
			int yMaxExclusive, int size) {
		ConcurrentLinkedDeque<Section> queue = new ConcurrentLinkedDeque<>();
		for (int y = yMin; y < yMaxExclusive; y += size) {
			for (int x = xMin; x < xMaxExclusive; x += size) {
				queue.offerFirst(
						new Section(x, y, Math.min(x + size, xMaxExclusive), Math.min(y + size, yMaxExclusive)));
			}
		}
		return queue;
	}
}
