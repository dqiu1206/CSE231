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
package nqueens.viz.solution;

import static edu.wustl.cse231s.rice.habanero.Habanero.launchHabaneroApp;

import java.util.Arrays;
import java.util.Collection;

import edu.wustl.cse231s.rice.habanero.options.SystemPropertiesOption;
import edu.wustl.cse231s.viz.RuntimeInterruptedException;
import edu.wustl.cse231s.viz.VizApp;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import nqueens.assignment.SequentialNQueens;
import nqueens.core.DefaultMutableQueenLocations;
import nqueens.core.MutableQueenLocations;
import nqueens.instructor.InstructorNQueensTestUtils;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
public class NQueenVizApp extends VizApp {
	private static class VizMutableQueenLocations extends DefaultMutableQueenLocations {
		public VizMutableQueenLocations(NQueenVizApp app) {
			super(8);
			this.app = app;
			this.doubleAccountedLocations = new int[this.getBoardSize()];
			Arrays.fill(this.doubleAccountedLocations, -1);

		}

		@Override
		public boolean isCandidateThreatFree(int row, int candidateColumn) {
			if(app.isToBeCanceled()) {
				throw new RuntimeInterruptedException();
			}
			boolean result = super.isCandidateThreatFree(row, candidateColumn);
			if (result) {
				// pass
			} else {
				if (this.app.isThreatFreeOnly.isSelected()) {
					// pass
				} else {
					this.doubleAccountedLocations[row] = candidateColumn;
					this.app.updateQueens(this.doubleAccountedLocations, row);
					// TODO
					this.app.chessboardPane.updateQueenArrayLabelLater(this.doubleAccountedLocations, row);
					this.doubleAccountedLocations[row] = -1;
					this.app.takeFromQueueIfNecessary();
				}
			}
			return result;
		}

		@Override
		public void setColumnOfQueenInRow(int row, int column) {
			if (this.app.isToBeCanceled()) {//Thread.interrupted()) {
				throw new RuntimeInterruptedException();
			}
			super.setColumnOfQueenInRow(row, column);
			this.doubleAccountedLocations[row] = column;
			app.updateQueens(this.doubleAccountedLocations, row);
			if (row == this.doubleAccountedLocations.length - 1) {
				this.solutionCount++;
				this.app.chessboardPane.updateSolutionCountLabelLater(this.solutionCount);
			}
			this.app.chessboardPane.updateQueenArrayLabelLater(this.doubleAccountedLocations, row);
			this.app.takeFromQueueIfNecessary();
		}

		private final NQueenVizApp app;
		private final int[] doubleAccountedLocations;
		private int solutionCount;
	}

	private static enum SolutionCounter {
		STUDENT_SEQUENTIAL("student's Sequential Solution") {
			@Override
			public int countSolutions(NQueenVizApp app) {
				MutableQueenLocations queenLocations = new VizMutableQueenLocations(app);
				return SequentialNQueens.countSolutions(queenLocations);
			}
		},
		// STUDENT_PARALLEL("student's (Serialized) Parallel Solution"){
		// @Override
		// public int countSolutions(NQueenVizApp app) {
		// throw new RuntimeException();
		// }
		// },
		INSTRUCTOR("instructor's Solution") {
			@Override
			public int countSolutions(NQueenVizApp app) {
				MutableQueenLocations queenLocations = new VizMutableQueenLocations(app);
				return InstructorNQueensTestUtils.countSolutions(queenLocations);
			}
		};

		private SolutionCounter(String repr) {
			this.repr = repr;
		}

		public abstract int countSolutions(NQueenVizApp app);

		@Override
		public String toString() {
			return this.repr;
		}

		private final String repr;
	};

	@Override
	protected void resetIfNecessary() {
		this.updateQueens(new int[8], -1);
	}

	private void updateQueens(int[] queens, int row) {
		Platform.runLater(new UpdateQueensRunnable(this.chessboardPane, queens, row + 1));
	}

	@Override
	protected void solve() throws RuntimeInterruptedException {
		SolutionCounter solutionCounter = this.solutionCounterComboBox.getValue();
		try {
			launchHabaneroApp(new SystemPropertiesOption.Builder().isSerialized(true).build(), () -> {
				int count = solutionCounter.countSolutions(this);
				// todo: clear
				System.out.println(count);
			});
		} catch (RuntimeException re) {
			if (re.getCause() instanceof RuntimeInterruptedException) {//InterruptedException) {
				System.out.println("canceled");
			} else {
				throw re;
			}
		}
	}

	@Override
	protected Collection<Node> getBonusNodesToEnableOnThreadStart() {
		return Arrays.asList();
	}

	@Override
	protected Collection<Node> getBonusNodesToDisableOnThreadStart() {
		return Arrays.asList(solutionCounterComboBox);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		super.start(primaryStage);
		this.chessboardPane = new ChessboardPane(8);

		final int PADDING = 8;
		BorderPane root = new BorderPane();
		root.setCenter(chessboardPane);
		root.setPadding(new Insets(PADDING));

		ObservableList<SolutionCounter> solutionCounterOptions = FXCollections
				.observableArrayList(SolutionCounter.values());
		this.solutionCounterComboBox = new ComboBox<>(solutionCounterOptions);
		this.solutionCounterComboBox.setValue(SolutionCounter.STUDENT_SEQUENTIAL);

		this.isThreatFreeOnly = new CheckBox("only show threat free moves?");

		Region spacerA = new Region();
		spacerA.setMinWidth(10.0);
		Region spacerB = new Region();
		spacerB.setMinWidth(10.0);
		Region spacerC = new Region();
		spacerC.setMinWidth(10.0);
		FlowPane controls = new FlowPane();
		controls.setHgap(2.0);
		controls.getChildren().addAll(this.solutionCounterComboBox, this.isThreatFreeOnly, spacerA, getSolveButton(),
				getStepButton(), spacerB, getPauseButton(), getResumeButton(), spacerC, getCancelButton());

		root.setTop(controls);

		Scene scene = new Scene(root);

		primaryStage.sizeToScene();
		primaryStage.setTitle("N-Queens");
		primaryStage.setScene(scene);
		primaryStage.show();

		primaryStage.setOnCloseRequest(e -> {
			Platform.exit();
			System.exit(0);
		});
	}

	private ChessboardPane chessboardPane;
	private ComboBox<SolutionCounter> solutionCounterComboBox;
	private CheckBox isThreatFreeOnly;

	public static void main(String[] args) throws Exception {
		checkHabaneroJavaagentArg();
		launch(args);
	}
}
