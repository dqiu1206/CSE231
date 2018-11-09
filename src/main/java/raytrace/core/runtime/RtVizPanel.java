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
package raytrace.core.runtime;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import org.sunflow.core.Display;
import org.sunflow.image.Color;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
public class RtVizPanel extends JPanel implements Display {
	// chosen from conservative 7-color palette: http://mkweb.bcgsc.ca/colorblind/
	private static java.awt.Color[] colors = { new java.awt.Color(230, 159, 0), new java.awt.Color(86, 180, 233), new java.awt.Color(0, 158, 115), new java.awt.Color(240, 228, 66) };
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (this.rgb != null) {
			g.drawImage(this.rgb, 0, 0, this);
		}
		if (this.id != null) {
			g.drawImage(this.id, 640, 0, this);
		}
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(1280, 480);
	}

	@Override
	public void imageBegin(int w, int h, int bucketSize) {
		this.rgb = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		this.id = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		repaint();
	}

	@Override
	public void imagePrepare(int x, int y, int w, int h, int id) {
		Graphics g = this.id.getGraphics();
		g.setColor((id >=0 && id<colors.length) ? colors[id] : java.awt.Color.LIGHT_GRAY);
		g.fillRect(x, y, w, h);
		g.dispose();
		repaint(640 + x, y, w, h);
	}

	@Override
	public void imageUpdate(int x, int y, int w, int h, Color[] data) {
		for (int j = 0, index = 0; j < h; j++) {
			for (int i = 0; i < w; i++, index++) {
				this.rgb.setRGB(x + i, y + j, data[index].copy().toNonLinear().toRGB());
			}
		}
		repaint(x, y, w, h);
	}

	@Override
	public void imageFill(int x, int y, int w, int h, Color c) {
		int rgb = c.copy().toNonLinear().toRGB();
		for (int j = 0; j < h; j++) {
			for (int i = 0; i < w; i++) {
				this.rgb.setRGB(x + i, y + j, rgb);
			}
		}
	}

	@Override
	public void imageEnd() {
		repaint();
	}

	private BufferedImage rgb;
	private BufferedImage id;

}
