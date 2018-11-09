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
package edu.wustl.cse231s.rice.habanero.implementation;

import java.io.PrintStream;
import java.util.List;

import edu.rice.hj.api.HjCallable;
import edu.rice.hj.api.HjDataDrivenFuture;
import edu.rice.hj.api.HjFuture;
import edu.rice.hj.api.HjMetrics;
import edu.rice.hj.api.HjPhaser;
import edu.rice.hj.api.HjPhaserMode;
import edu.rice.hj.api.HjPoint;
import edu.rice.hj.api.HjRegion.HjRegion1D;
import edu.rice.hj.api.HjRegion.HjRegion2D;
import edu.rice.hj.api.HjRunnable;
import edu.rice.hj.api.HjSuspendable;
import edu.rice.hj.api.HjSuspendingCallable;
import edu.rice.hj.api.HjSuspendingProcedure;
import edu.rice.hj.api.HjSuspendingProcedureInt1D;
import edu.rice.hj.api.HjSuspendingProcedureInt2D;
import edu.rice.hj.api.SuspendableException;
import edu.rice.hj.runtime.region.RectangularRegion2D;
import edu.wustl.cse231s.rice.habanero.contrib.api.AccumulatorReducer;
import edu.wustl.cse231s.rice.habanero.contrib.api.ContentionLevel;
import edu.wustl.cse231s.rice.habanero.contrib.api.DoubleAccumulationDeterminismPolicy;
import edu.wustl.cse231s.rice.habanero.contrib.api.FinishAccumulator;
import edu.wustl.cse231s.rice.habanero.contrib.api.NumberReductionOperator;
import edu.wustl.cse231s.rice.habanero.options.ChunkedOption;
import edu.wustl.cse231s.rice.habanero.options.PhasedEmptyOption;
import edu.wustl.cse231s.rice.habanero.options.PhasedOption;
import edu.wustl.cse231s.rice.habanero.options.SingleOption;
import edu.wustl.cse231s.rice.habanero.options.SystemPropertiesOption;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
public interface HabaneroImplementation {
	// launch
	int numWorkerThreads();

	boolean isLaunched();

	void launchHabaneroApp(HjSuspendable body, Runnable preizeCallback);

	void launchHabaneroApp(SystemPropertiesOption systemPropertiesOption, HjSuspendable body,
			Runnable preFinalizeCallback);

	// statistics
	void doWork(long n);

	HjMetrics abstractMetrics();

	void dumpStatistics(HjMetrics metrics);

	void dumpStatistics(HjMetrics metrics, PrintStream ps);

	// finish, async
	void finish(HjSuspendable body) throws SuspendableException;

	void async(HjSuspendable body);

	// regions and groups
	HjRegion1D newRectangularRegion1D(int pMinInc, int pMaxInc);

	HjRegion2D newRectangularRegion2D(int pMinInc0, int pMaxInc0, int pMinInc1, int pMaxInc1);

	List<HjRegion1D> group(HjRegion1D hjRegion, int processorGrid);

	HjRegion1D myGroup(int groupId, HjRegion1D hjRegion, int groupSize);

	List<HjRegion2D> group(HjRegion2D hjRegion, int processorGrid0, int processorGrid1);

	RectangularRegion2D myGroup(int groupId0, int groupId1, HjRegion2D hjRegion, int groupSize0, int groupSize1);

	// loop 1d
	void forseq(HjRegion1D hjRegion, HjSuspendingProcedureInt1D body) throws SuspendableException;

	void forasync(HjRegion1D hjRegion, HjSuspendingProcedureInt1D body) throws SuspendableException;

	void forasync(ChunkedOption chunkedOption, HjRegion1D hjRegion, HjSuspendingProcedureInt1D body)
			throws SuspendableException;

	<T> void forseq(Iterable<T> iterable, HjSuspendingProcedure<T> body) throws SuspendableException;

	<T> void forasync(Iterable<T> iterable, HjSuspendingProcedure<T> body) throws SuspendableException;

	<T> void forseq(T[] array, HjSuspendingProcedure<T> body) throws SuspendableException;

	<T> void forasync(T[] array, HjSuspendingProcedure<T> body) throws SuspendableException;

	// loop 2d
	void forseq2d(HjRegion2D hjRegion, HjSuspendingProcedureInt2D body) throws SuspendableException;

	void forasync2d(HjRegion2D hjRegion, HjSuspendingProcedureInt2D body) throws SuspendableException;

	void forasync2d(ChunkedOption chunkedOption, HjRegion2D hjRegion, HjSuspendingProcedureInt2D body)
			throws SuspendableException;

	// phasers
	HjPhaser newPhaser(HjPhaserMode phaserMode);

	void async(PhasedOption phasedOption, HjSuspendable body);

	void forasync(PhasedOption phasedOption, int start, int endInclusive, HjSuspendingProcedureInt1D body)
			throws SuspendableException;

	void forasync(PhasedEmptyOption phasedEmptyOption, int start, int endInclusive, HjSuspendingProcedureInt1D body)
			throws SuspendableException;

	void phaserNext() throws SuspendableException;

	void phaserNext(SingleOption singleOption) throws SuspendableException;

	void phaserWait() throws SuspendableException;

	void phaserSignal() throws SuspendableException;

	// future
	<R> HjFuture<R> future(HjSuspendingCallable<R> body);

	// await
	void async(List<HjFuture<?>> awaitFutures, HjSuspendable body);

	<R> HjFuture<R> future(List<HjFuture<?>> awaitFutures, HjSuspendingCallable<R> body);

	// accumulators
	void finish(FinishAccumulator<?>[] accumulators, HjSuspendable body) throws SuspendableException;

	FinishAccumulator<Integer> newIntegerFinishAccumulator(NumberReductionOperator operator,
			ContentionLevel contentionLevel);

	FinishAccumulator<Double> newDoubleFinishAccumulator(NumberReductionOperator operator,
			ContentionLevel contentionLevel, DoubleAccumulationDeterminismPolicy determinismPolicy);

	<T> FinishAccumulator<T> newReducerFinishAccumulator(AccumulatorReducer<T> reducer,
			ContentionLevel contentionLevel);

	// isolated
	void isolated(HjRunnable body);

	void isolated(Object[] participants, HjRunnable body);

	<V> V isolatedWithReturn(HjCallable<V> callable);

	<V> V isolatedWithReturn(Object[] participants, HjCallable<V> callable);

	Object readMode(Object wrappee);

	Object writeMode(Object wrappee);

	<V> HjDataDrivenFuture<V> newDataDrivenFuture();

	HjPoint newPoint(int... values);
}
