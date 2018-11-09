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
package opencv.pipeline.viz;

import java.io.File;

import org.opencv.objdetect.CascadeClassifier;

import edu.wustl.cse231s.facesinthewild.FacesInTheWildUtils;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import opencv.core.DetectedEye;
import opencv.core.DetectedFace;
import opencv.core.OpenCVUtils;
import opencv.pipeline.core.PipelineFaceDetectionProducer;
import opencv.pipeline.demo.SequentialPipelineFaceDetectionProducer;
import opencv.pipeline.studio.ParallelPipelineFaceDetectionProducer;
import opencv.viz.FaceDetectionPane;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
public class PipelineFaceDetectionApp extends Application {
	@Override
	public void start(Stage primaryStage) throws Exception {
		OpenCVUtils.loadLibrary();

		FlowPane pane = new FlowPane();

		File[] files = FacesInTheWildUtils.getFiles("2002/07/19/big");
		final int N = files.length;

		FaceDetectionPane[] faceDetectionPanes = new FaceDetectionPane[N];

		final double scale = 0.5;
		for (int i = 0; i < N; i++) {
			faceDetectionPanes[i] = new FaceDetectionPane(scale, scale);

			// TODO: fix
			// load image just to get the size for the component
			Image image = OpenCVUtils.loadImage(files[i]).toImage();
			faceDetectionPanes[i].setPrefWidth(image.getWidth()*scale);
			faceDetectionPanes[i].setPrefHeight(image.getHeight()*scale);
		}

		ScrollPane scrollPane = new ScrollPane();
		scrollPane.setContent(pane);
		scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
		scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);

		pane.prefWrapLengthProperty().bind(Bindings.subtract(scrollPane.widthProperty(), 50));

		pane.getChildren().addAll(faceDetectionPanes);

		BorderPane borderPane = new BorderPane();

		Button sequentialButton = new Button("sequential");
		Button parallelButton = new Button("parallel");

		CascadeClassifier faceCascadeClassifier = OpenCVUtils
				.newCascadeClassifier("haarcascades/haarcascade_frontalface_alt.xml");
		CascadeClassifier eyeCascadeClassifier = OpenCVUtils.newCascadeClassifier("haarcascades/haarcascade_eye.xml");

		if (faceCascadeClassifier != null && eyeCascadeClassifier != null) {
			class ProduceThread extends Thread {
				private final PipelineFaceDetectionProducer pipeline;

				public ProduceThread(PipelineFaceDetectionProducer pipeline) {
					this.pipeline = pipeline;
				}

				@Override
				public void run() {
					pipeline.produce(files, faceCascadeClassifier, eyeCascadeClassifier,
							(Integer i, Image displayImage) -> {
								faceDetectionPanes[i].setImage(displayImage);
							}, (Integer i, DetectedFace[] faces) -> {
								faceDetectionPanes[i].setDetectedFaces(faces);
							}, (Integer i, DetectedEye[][] eyes) -> {
								faceDetectionPanes[i].setDetectedEyes(eyes);
							});
				}
			}
			sequentialButton.setOnAction((event) -> {
				sequentialButton.setDisable(true);
				parallelButton.setDisable(true);
				new ProduceThread(new SequentialPipelineFaceDetectionProducer()).start();
			});
			parallelButton.setOnAction((event) -> {
				sequentialButton.setDisable(true);
				parallelButton.setDisable(true);
				new ProduceThread(new ParallelPipelineFaceDetectionProducer()).start();
			});
		} else {
			sequentialButton.setDisable(true);
			parallelButton.setDisable(true);
		}

		HBox buttonBox = new HBox(sequentialButton, parallelButton);
		borderPane.setTop(buttonBox);

		borderPane.setCenter(scrollPane);
		Scene scene = new Scene(borderPane);
		primaryStage.setTitle("Pipeline");
		primaryStage.setScene(scene);

		Screen screen = Screen.getPrimary();
		Rectangle2D bounds = screen.getVisualBounds();

		primaryStage.setX(bounds.getMinX());
		primaryStage.setY(bounds.getMinY());
		primaryStage.setWidth(bounds.getWidth());
		primaryStage.setHeight(bounds.getHeight());
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
