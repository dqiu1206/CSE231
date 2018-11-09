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
package opencv.dataflow.viz;

import java.io.File;

import org.opencv.objdetect.CascadeClassifier;

import edu.wustl.cse231s.facesinthewild.FacesInTheWildUtils;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import opencv.core.DetectedEye;
import opencv.core.DetectedFace;
import opencv.core.OpenCVUtils;
import opencv.dataflow.core.DataFlowFaceDetectionProducer;
import opencv.dataflow.demo.SequentialDataFlowFaceDetectionProducer;
import opencv.dataflow.studio.ParallelDataFlowFaceDetectionProducer;
import opencv.viz.FaceDetectionPane;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
public class DataFlowFaceDetectionApp extends Application {
	@Override
	public void start(Stage primaryStage) throws Exception {
		OpenCVUtils.loadLibrary();
		File file = FacesInTheWildUtils.getFile("2002/07/19/big/img_576.jpg");

		FaceDetectionPane faceDetectionPane = new FaceDetectionPane();

		// TODO: fix
		// load image just to get the size for the component
		Image image = OpenCVUtils.loadImage(file).toImage();
		faceDetectionPane.setPrefWidth(image.getWidth());
		faceDetectionPane.setPrefHeight(image.getHeight());
		
		CascadeClassifier faceCascadeClassifier = OpenCVUtils
				.newCascadeClassifier("haarcascades/haarcascade_frontalface_default.xml");
		CascadeClassifier eyeCascadeClassifier = OpenCVUtils.newCascadeClassifier("haarcascades/haarcascade_eye.xml");

		BorderPane root = new BorderPane();

		Button sequentialButton = new Button("sequential");
		Button parallelButton = new Button("parallel");

		if (faceCascadeClassifier != null && eyeCascadeClassifier != null) {
			class ProduceThread extends Thread {
				private final DataFlowFaceDetectionProducer dataFlow;

				public ProduceThread(DataFlowFaceDetectionProducer dataFlow) {
					this.dataFlow = dataFlow;
				}

				@Override
				public void run() {
					dataFlow.produce(file, faceCascadeClassifier, eyeCascadeClassifier, (Image displayImage) -> {
						faceDetectionPane.setImage(displayImage);
					}, (DetectedFace[] faces) -> {
						faceDetectionPane.setDetectedFaces(faces);
					}, (DetectedEye[][] eyes) -> {
						faceDetectionPane.setDetectedEyes(eyes);
					});
				}
			}
			sequentialButton.setOnAction((event) -> {
				sequentialButton.setDisable(true);
				parallelButton.setDisable(true);
				new ProduceThread(new SequentialDataFlowFaceDetectionProducer()).start();
			});
			parallelButton.setOnAction((event) -> {
				sequentialButton.setDisable(true);
				parallelButton.setDisable(true);
				new ProduceThread(new ParallelDataFlowFaceDetectionProducer()).start();
			});
		} else {
			sequentialButton.setDisable(true);
			parallelButton.setDisable(true);
		}

		HBox buttonBox = new HBox(sequentialButton, parallelButton);
		root.setTop(buttonBox);

		Scene scene = new Scene(root);
		root.setCenter(faceDetectionPane);

		primaryStage.setTitle("DataFlow");
		primaryStage.setScene(scene);
		primaryStage.show();

		Platform.runLater(() -> primaryStage.sizeToScene());

		primaryStage.setOnCloseRequest(e -> {
			Platform.exit();
			System.exit(0);
		});
	}

	public static void main(String[] args) throws Exception {
		launch(args);
	}
}
