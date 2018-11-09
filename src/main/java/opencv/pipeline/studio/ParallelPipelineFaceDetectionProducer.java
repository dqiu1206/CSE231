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
package opencv.pipeline.studio;

import static edu.wustl.cse231s.rice.habanero.Habanero.async;
import static edu.wustl.cse231s.rice.habanero.Habanero.finish;
import static edu.wustl.cse231s.rice.habanero.Habanero.launchHabaneroApp;
import static edu.wustl.cse231s.rice.habanero.Habanero.newPhaser;
import static edu.wustl.cse231s.rice.habanero.Habanero.phased;

import java.io.File;
import java.util.function.BiConsumer;

import org.opencv.objdetect.CascadeClassifier;

import edu.rice.hj.api.HjPhaser;
import edu.rice.hj.api.HjPhaserMode;
import edu.wustl.cse231s.NotYetImplementedException;
import javafx.scene.image.Image;
import opencv.core.DetectedEye;
import opencv.core.DetectedFace;
import opencv.core.OpenCVUtils;
import opencv.core.PixelMatrix;
import opencv.pipeline.core.PipelineFaceDetectionProducer;

/**
 * @author David Qiu
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
public class ParallelPipelineFaceDetectionProducer implements PipelineFaceDetectionProducer {
	@Override
	public void produce(File[] files, CascadeClassifier faceCascadeClassifier, CascadeClassifier eyeCascadeClassifier,
			BiConsumer<Integer, Image> displayImageConsumer, BiConsumer<Integer, DetectedFace[]> detectedFacesConsumer,
			BiConsumer<Integer, DetectedEye[][]> detectedEyesConsumer) {
		launchHabaneroApp(() -> {
			finish(() -> {
				final int N = files.length;
				PixelMatrix[] colorPixelMatrices = new PixelMatrix[N];
				PixelMatrix[] grayPixelMatrices = new PixelMatrix[N];
				Image[] displayImages = new Image[N];
				DetectedFace[][] detectedFaces = new DetectedFace[N][];
				DetectedEye[][][] detectedEyesOfFaces = new DetectedEye[N][][];
				HjPhaser[] phaserArray = new HjPhaser[N];
				for(int j=0;j<N;j++) {
					phaserArray[j] = newPhaser(HjPhaserMode.DEFAULT_MODE);
				}
				for (int i = 0; i < files.length; i++) {
					final int ii = i;
					if(ii>0) {
						async(phased(phaserArray[ii-1].inMode(HjPhaserMode.WAIT),phaserArray[ii].inMode(HjPhaserMode.SIG)),()->{
							phaserArray[ii-1].doWait();
							colorPixelMatrices[ii] = OpenCVUtils.loadImage(files[ii]);
							grayPixelMatrices[ii] = colorPixelMatrices[ii].toGray();
							displayImages[ii] = colorPixelMatrices[ii].toImage();
							displayImageConsumer.accept(ii, displayImages[ii]);
							detectedFaces[ii] = grayPixelMatrices[ii].detectFaces(faceCascadeClassifier);
							detectedFacesConsumer.accept(ii, detectedFaces[ii]);
							detectedEyesOfFaces[ii] = grayPixelMatrices[ii].detectEyes(eyeCascadeClassifier, detectedFaces[ii]);
							detectedEyesConsumer.accept(ii, detectedEyesOfFaces[ii]);
							phaserArray[ii].signal();
						});
					}
					if(ii==0) {
						async(phased(phaserArray[ii].inMode(HjPhaserMode.SIG)),()->{
							colorPixelMatrices[ii] = OpenCVUtils.loadImage(files[ii]);
							grayPixelMatrices[ii] = colorPixelMatrices[ii].toGray();
							displayImages[ii] = colorPixelMatrices[ii].toImage();
							displayImageConsumer.accept(ii, displayImages[ii]);
							detectedFaces[ii] = grayPixelMatrices[ii].detectFaces(faceCascadeClassifier);
							detectedFacesConsumer.accept(ii, detectedFaces[ii]);
							detectedEyesOfFaces[ii] = grayPixelMatrices[ii].detectEyes(eyeCascadeClassifier, detectedFaces[ii]);
							detectedEyesConsumer.accept(ii, detectedEyesOfFaces[ii]);
							phaserArray[ii].signal();
						});
					}
					
					
				}
			});
		});
	}
}
