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
package edu.wustl.cse231s.rice.habanero;

import java.io.PrintStream;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import edu.rice.hj.api.HjCallable;
import edu.rice.hj.api.HjDataDrivenFuture;
import edu.rice.hj.api.HjFuture;
import edu.rice.hj.api.HjMetrics;
import edu.rice.hj.api.HjPhaser;
import edu.rice.hj.api.HjPhaserMode;
import edu.rice.hj.api.HjPhaserPair;
import edu.rice.hj.api.HjPoint;
import edu.rice.hj.api.HjRegion;
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
import edu.wustl.cse231s.rice.habanero.implementation.HabaneroImplementation;
import edu.wustl.cse231s.rice.habanero.implementation.classic.ClassicHabaneroImplementation;
import edu.wustl.cse231s.rice.habanero.options.AwaitFuturesOption;
import edu.wustl.cse231s.rice.habanero.options.ChunkedOption;
import edu.wustl.cse231s.rice.habanero.options.ObjectBasedIsolationOption;
import edu.wustl.cse231s.rice.habanero.options.PhasedEmptyOption;
import edu.wustl.cse231s.rice.habanero.options.PhasedOption;
import edu.wustl.cse231s.rice.habanero.options.RegisterAccumulatorsOption;
import edu.wustl.cse231s.rice.habanero.options.SingleOption;
import edu.wustl.cse231s.rice.habanero.options.SystemPropertiesOption;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
public class Habanero {
	private Habanero() {
		throw new Error();
	}

	private static AtomicReference<HabaneroImplementation> implementationReference = new AtomicReference<>(
			new ClassicHabaneroImplementation());

	private static HabaneroImplementation getImplementation() {
		return implementationReference.get();
	}

	/**
	 * @see edu.rice.hj.Module0#numWorkerThreads()
	 */
	public static int numWorkerThreads() {
		return getImplementation().numWorkerThreads();
	}

	public static boolean isLaunched() {
		return getImplementation().isLaunched();
	}

	/**
	 * @see edu.rice.hj.Module0#launchHabaneroApp(HjSuspendable)
	 */
	public static void launchHabaneroApp(HjSuspendable body) {
		launchHabaneroApp(body, null);
	}

	public static void launchHabaneroApp(HabaneroImplementation implementation, HjSuspendable body) {
		HabaneroImplementation prevImplementation = getImplementation();
		implementationReference.set(implementation);
		try {
			launchHabaneroApp(body, null);
		} finally {
			implementationReference.set(prevImplementation);
		}
	}

	public static void launchHabaneroApp(HjSuspendable body, Runnable preFinalizeCallback) {
		getImplementation().launchHabaneroApp(body, preFinalizeCallback);
	}

	public static void launchHabaneroApp(SystemPropertiesOption systemPropertiesOption, HjSuspendable body) {
		launchHabaneroApp(systemPropertiesOption, body, null);
	}

	public static void launchHabaneroApp(SystemPropertiesOption systemPropertiesOption, HjSuspendable body, Runnable preFinalizeCallback) {
		getImplementation().launchHabaneroApp(systemPropertiesOption, body, preFinalizeCallback);
	}

	/**
	 * @see edu.rice.hj.Module0#finish(HjSuspendable)
	 */
	public static void finish(HjSuspendable body) throws SuspendableException {
		getImplementation().finish(body);
	}

	/**
	 * @see edu.rice.hj.Module1#async(HjSuspendable)
	 */
	public static void async(HjSuspendable body) {
		getImplementation().async(body);
	}

	/**
	 * @see edu.rice.hj.Module1#future(HjSuspendingCallable)
	 */
	public static <R> HjFuture<R> future(HjSuspendingCallable<R> body) {
		return getImplementation().future(body);
	}

	/**
	 * @see edu.rice.hj.Module1#asyncAwait(HjFuture,HjSuspendable)
	 * @see edu.rice.hj.Module1#asyncAwait(List,HjSuspendable)
	 * @see edu.rice.hj.Module1#asyncAwait(HjFuture,HjFuture,HjSuspendable)
	 * @see edu.rice.hj.Module1#asyncAwait(HjFuture,HjFuture,HjFuture,HjSuspendable)
	 */
	public static void async(AwaitFuturesOption awaitOption, HjSuspendable body) {
		getImplementation().async(awaitOption.getFutures(), body);
	}

	/**
	 * @see edu.rice.hj.Module1#futureAwait(HjFuture,HjSuspendingCallable)
	 * @see edu.rice.hj.Module1#futureAwait(List,HjSuspendingCallable)
	 * @see edu.rice.hj.Module1#futureAwait(HjFuture,HjFuture,HjSuspendingCallable)
	 * @see edu.rice.hj.Module1#futureAwait(HjFuture,HjFuture,HjFuture,HjSuspendingCallable)
	 */
	public static <R> HjFuture<R> future(AwaitFuturesOption awaitOption, HjSuspendingCallable<R> body) {
		return getImplementation().future(awaitOption.getFutures(), body);
	}

	/**
	 * @see edu.rice.hj.Module0#newRectangularRegion1D(int,int)
	 */
	public static HjRegion1D newRectangularRegion1D(int min, int maxExclusive) {
		return getImplementation().newRectangularRegion1D(min, maxExclusive-1);
	}

	/**
	 * @see edu.rice.hj.Module0#group(HjRegion.HjRegion1D,int)
	 */
	public static List<HjRegion1D> group(HjRegion1D hjRegion, int processorGrid) {
		return getImplementation().group(hjRegion, processorGrid);
	}

	/**
	 * @see edu.rice.hj.Module0#myGroup(int,HjRegion.HjRegion1D,int)
	 */
	public static HjRegion1D myGroup(int groupId, HjRegion1D hjRegion, int groupSize) {
		return getImplementation().myGroup(groupId, hjRegion, groupSize);
	}

	/**
	 * @see edu.rice.hj.Module1#forseq(HjRegion.HjRegion1D,HjSuspendingProcedureInt1D)
	 */
	public static void forseq(HjRegion1D hjRegion, HjSuspendingProcedureInt1D body) throws SuspendableException {
		getImplementation().forseq(hjRegion, body);
	}

	/**
	 * @see edu.rice.hj.Module1#forasync(HjRegion.HjRegion1D,HjSuspendingProcedureInt1D)
	 */
	public static void forasync(HjRegion1D hjRegion, HjSuspendingProcedureInt1D body) throws SuspendableException {
		getImplementation().forasync(hjRegion, body);
	}

	/**
	 * @see edu.rice.hj.Module1#forall(HjRegion.HjRegion1D,HjSuspendingProcedureInt1D)
	 */
	public static void forall(HjRegion1D hjRegion, HjSuspendingProcedureInt1D body) throws SuspendableException {
		finish(() -> {
			forasync(hjRegion, body);
		});
	}

	/**
	 * @see edu.rice.hj.Module1#forasyncChunked(HjRegion.HjRegion1D,HjSuspendingProcedureInt1D)
	 */
	public static void forasync(ChunkedOption chunkedOption, HjRegion1D hjRegion, HjSuspendingProcedureInt1D body)
			throws SuspendableException {
		getImplementation().forasync(chunkedOption, hjRegion, body);
	}

	/**
	 * @see edu.rice.hj.Module1#forallChunked(HjRegion.HjRegion1D,HjSuspendingProcedureInt1D)
	 */
	public static void forall(ChunkedOption chunkedOption, HjRegion1D hjRegion, HjSuspendingProcedureInt1D body)
			throws SuspendableException {
		finish(() -> {
			forasync(chunkedOption, hjRegion, body);
		});
	}

	/**
	 * @see edu.rice.hj.Module1#forseq(int,int,HjSuspendingProcedureInt1D)
	 */
	public static void forseq(int start, int endExclusive, HjSuspendingProcedureInt1D body)
			throws SuspendableException {
		forseq(newRectangularRegion1D(start, endExclusive), body);
	}

	/**
	 * @see edu.rice.hj.Module1#forasync(int,int,HjSuspendingProcedureInt1D)
	 */
	public static void forasync(int start, int endExclusive, HjSuspendingProcedureInt1D body)
			throws SuspendableException {
		forasync(newRectangularRegion1D(start, endExclusive), body);
	}

	/**
	 * @see edu.rice.hj.Module1#forasyncChunked(int,int,HjSuspendingProcedureInt1D)
	 * @see edu.rice.hj.Module1#forasyncChunked(int,int,int,HjSuspendingProcedureInt1D)
	 */
	public static void forasync(ChunkedOption chunkedOption, int start, int endExclusive,
			HjSuspendingProcedureInt1D body) throws SuspendableException {
		forasync(chunkedOption, newRectangularRegion1D(start, endExclusive), body);
	}

	/**
	 * @see edu.rice.hj.Module1#forall(int,int,HjSuspendingProcedureInt1D)
	 */
	public static void forall(int start, int endExclusive, HjSuspendingProcedureInt1D body)
			throws SuspendableException {
		forall(newRectangularRegion1D(start, endExclusive), body);
	}

	/**
	 * @see edu.rice.hj.Module1#forallChunked(int,int,HjSuspendingProcedureInt1D)
	 * @see edu.rice.hj.Module1#forallChunked(int,int,int,HjSuspendingProcedureInt1D)
	 */
	public static void forall(ChunkedOption chunkedOption, int start, int endExclusive, HjSuspendingProcedureInt1D body)
			throws SuspendableException {
		forall(chunkedOption, newRectangularRegion1D(start, endExclusive), body);
	}

	/**
	 * @see edu.rice.hj.Module1#forseq(Iterable,HjSuspendingProcedure)
	 */
	public static <T> void forseq(Iterable<T> iterable, HjSuspendingProcedure<T> body) throws SuspendableException {
		getImplementation().forseq(iterable, body);
	}

	/**
	 * @see edu.rice.hj.Module1#forasync(Iterable,HjSuspendingProcedure)
	 */
	public static <T> void forasync(Iterable<T> iterable, HjSuspendingProcedure<T> body) throws SuspendableException {
		getImplementation().forasync(iterable, body);
	}

	/**
	 * @see edu.rice.hj.Module1#forall(Iterable,HjSuspendingProcedure)
	 */
	public static <T> void forall(Iterable<T> iterable, HjSuspendingProcedure<T> body) throws SuspendableException {
		finish(() -> {
			forasync(iterable, body);
		});
	}


	public static <T> void forseq(T[] array, HjSuspendingProcedure<T> body) throws SuspendableException {
		getImplementation().forseq(array, body);
	}

	public static <T> void forasync(T[] array, HjSuspendingProcedure<T> body) throws SuspendableException {
		getImplementation().forasync(array, body);
	}

	public static <T> void forall(T[] array, HjSuspendingProcedure<T> body) throws SuspendableException {
		finish(() -> {
			forasync(array, body);
		});
	}

	/**
	 * @see edu.rice.hj.Module0#newRectangularRegion2D(int,int,int,int)
	 */
	public static HjRegion2D newRectangularRegion2D(int aMin, int aMaxExclusive, int bMin, int bMaxExclusive) {
		return getImplementation().newRectangularRegion2D(aMin, aMaxExclusive-1, bMin, bMaxExclusive-1);
	}

	/**
	 * @see edu.rice.hj.Module0#group(HjRegion.HjRegion2D,int,int)
	 */
	public static List<HjRegion2D> group(HjRegion2D hjRegion, int processorGrid0, int processorGrid1) {
		return getImplementation().group(hjRegion, processorGrid0, processorGrid1);
	}

	/**
	 * @see edu.rice.hj.Module0#myGroup(int,int,HjRegion.HjRegion2D,int,int)
	 */
	public static RectangularRegion2D myGroup(int groupId0, int groupId1, HjRegion2D hjRegion, int groupSize0,
			int groupSize1) {
		return getImplementation().myGroup(groupId0, groupId1, hjRegion, groupSize0, groupSize1);
	}

	/**
	 * @see edu.rice.hj.Module1#forseq(HjRegion.HjRegion2D,HjSuspendingProcedureInt2D)
	 */
	public static void forseq2d(HjRegion2D hjRegion, HjSuspendingProcedureInt2D body) throws SuspendableException {
		getImplementation().forseq2d(hjRegion, body);
	}

	/**
	 * @see edu.rice.hj.Module1#forasync(HjRegion.HjRegion2D,HjSuspendingProcedureInt2D)
	 */
	public static void forasync2d(HjRegion2D hjRegion, HjSuspendingProcedureInt2D body) throws SuspendableException {
		getImplementation().forasync2d(hjRegion, body);
	}

	/**
	 * @see edu.rice.hj.Module1#forall(HjRegion.HjRegion2D,HjSuspendingProcedureInt2D)
	 */
	public static void forall2d(HjRegion2D hjRegion, HjSuspendingProcedureInt2D body) throws SuspendableException {
		finish(() -> {
			forasync2d(hjRegion, body);
		});
	}

	/**
	 * @see edu.rice.hj.Module1#forasyncChunked(HjRegion.HjRegion2D,HjSuspendingProcedureInt2D)
	 */
	public static void forasync2d(ChunkedOption chunkedOption, HjRegion2D hjRegion, HjSuspendingProcedureInt2D body)
			throws SuspendableException {
		getImplementation().forasync2d(chunkedOption, hjRegion, body);
	}

	/**
	 * @see edu.rice.hj.Module1#forallChunked(HjRegion.HjRegion2D,HjSuspendingProcedureInt2D)
	 */
	public static void forall2d(ChunkedOption chunkedOption, HjRegion2D hjRegion, HjSuspendingProcedureInt2D body)
			throws SuspendableException {
		finish(() -> {
			forasync2d(chunkedOption, hjRegion, body);
		});
	}

	/**
	 * @see edu.rice.hj.Module1#forseq(int,int,int,int,HjSuspendingProcedureInt2D)
	 */
	public static void forseq2d(int aMin, int aMaxExclusive, int bMin, int bMaxExclusive,
			HjSuspendingProcedureInt2D body) throws SuspendableException {
		forseq2d(newRectangularRegion2D(aMin, aMaxExclusive, bMin, bMaxExclusive), body);
	}

	/**
	 * @see edu.rice.hj.Module1#forasync(int,int,int,int,HjSuspendingProcedureInt2D)
	 */
	public static void forasync2d(int aMin, int aMaxExclusive, int bMin, int bMaxExclusive,
			HjSuspendingProcedureInt2D body) throws SuspendableException {
		forasync2d(newRectangularRegion2D(aMin, aMaxExclusive, bMin, bMaxExclusive), body);
	}

	/**
	 * @see edu.rice.hj.Module1#forasyncChunked(HjRegion.HjRegion2D,HjSuspendingProcedureInt2D)
	 */
	public static void forasync2d(ChunkedOption chunkedOption, int aMin, int aMaxExclusive, int bMin, int bMaxExclusive, HjSuspendingProcedureInt2D body) throws SuspendableException {
		forasync2d(chunkedOption, newRectangularRegion2D(aMin, aMaxExclusive, bMin, bMaxExclusive), body);
	}

	/**
	 * @see edu.rice.hj.Module1#forall(int,int,int,int,HjSuspendingProcedureInt2D)
	 */
	public static void forall2d(int aMin, int aMaxExclusive, int bMin, int bMaxExclusive,
			HjSuspendingProcedureInt2D body) throws SuspendableException {
		forall2d(newRectangularRegion2D(aMin, aMaxExclusive, bMin, bMaxExclusive), body);
	}

	/**
	 * @see edu.rice.hj.Module1#forallChunked(HjRegion.HjRegion2D,HjSuspendingProcedureInt2D)
	 */
	public static void forall2d(ChunkedOption chunkedOption, int aMin, int aMaxExclusive, int bMin, int bMaxExclusive, HjSuspendingProcedureInt2D body) throws SuspendableException {
		forall2d(chunkedOption, newRectangularRegion2D(aMin, aMaxExclusive, bMin, bMaxExclusive), body);
	}

	/**
	 * @see edu.rice.hj.Module0#newPhaser(edu.rice.hj.api.HjPhaserMode)
	 */
	public static HjPhaser newPhaser(HjPhaserMode phaserMode) {
		return getImplementation().newPhaser(phaserMode);
	}

	/**
	 * @see edu.rice.hj.Module0#asyncPhased(HjPhaserPair,HjSuspendable)
	 * @see edu.rice.hj.Module0#asyncPhased(HjPhaserPair,HjPhaserPair,HjSuspendable)
	 * @see edu.rice.hj.Module0#asyncPhased(List,HjSuspendable)
	 */
	public static void async(PhasedOption phasedOption, HjSuspendable body) {
		getImplementation().async(phasedOption, body);
	}

	/**
	 * @see edu.rice.hj.Module0#forasyncPhased(int,int,List,HjSuspendingProcedureInt1D)
	 */
	public static void forasync(PhasedOption phasedOption, int start, int endExclusive, HjSuspendingProcedureInt1D body)
			throws SuspendableException {
		getImplementation().forasync(phasedOption, start, endExclusive-1, body);
	}

	/**
	 * @see edu.rice.hj.Module0#forasyncPhased(int,int,HjSuspendingProcedureInt1D)
	 */
	public static void forasync(PhasedEmptyOption phasedEmptyOption, int start, int endExclusive,
			HjSuspendingProcedureInt1D body) throws SuspendableException {
		getImplementation().forasync(phasedEmptyOption, start, endExclusive-1, body);
	}

	// TODO? forall( PhasedOption phasedOption
	
	/**
	 * @see edu.rice.hj.Module0#forallPhased(int,int,HjSuspendingProcedureInt1D)
	 */
	public static void forall(PhasedEmptyOption phasedEmptyOption, int start, int endExclusive,
			HjSuspendingProcedureInt1D body) throws SuspendableException {
		finish(() -> {
			forasync(phasedEmptyOption, start, endExclusive, body);
		});
	}

	/**
	 * @see edu.rice.hj.Module0#next()
	 */
	public static void phaserNext() throws SuspendableException {
		getImplementation().phaserNext();
	}

	public static void phaserNext(SingleOption singleOption) throws SuspendableException {
		getImplementation().phaserNext(singleOption);
	}

	/**
	 * @see edu.rice.hj.Module0#doWait()
	 */
	public static void phaserWait() throws SuspendableException {
		getImplementation().phaserWait();
	}

	/**
	 * @see edu.rice.hj.Module0#signal()
	 */
	public static void phaserSignal() throws SuspendableException {
		getImplementation().phaserSignal();
	}

	/**
	 * @see edu.rice.hj.Module0#doWork(long)
	 */
	public static void doWork(long n) {
		getImplementation().doWork(n);
	}

	/**
	 * @see edu.rice.hj.Module0#abstractMetrics()
	 */
	public static HjMetrics abstractMetrics() {
		return getImplementation().abstractMetrics();
	}

	public static void dumpStatistics() {
		dumpStatistics(abstractMetrics());
	}

	public static void dumpStatistics(HjMetrics metrics) {
		getImplementation().dumpStatistics(metrics);
	}

	public static void dumpStatistics(HjMetrics metrics, PrintStream ps) {
		getImplementation().dumpStatistics(metrics, ps);
	}

	public static void finish(RegisterAccumulatorsOption registerAccumulatorsOption, HjSuspendable body)
			throws SuspendableException {
		getImplementation().finish(registerAccumulatorsOption.getAccumulators(), body);
	}

	public static FinishAccumulator<Integer> newIntegerFinishAccumulator(NumberReductionOperator operator,
			ContentionLevel contentionLevel) {
		return getImplementation().newIntegerFinishAccumulator(operator, contentionLevel);
	}

	public static FinishAccumulator<Integer> newIntegerFinishAccumulator(NumberReductionOperator operator) {
		return newIntegerFinishAccumulator(operator, ContentionLevel.HIGH);
	}

	public static FinishAccumulator<Double> newDoubleFinishAccumulator(NumberReductionOperator operator,
			ContentionLevel contentionLevel, DoubleAccumulationDeterminismPolicy determinismPolicy) {
		return getImplementation().newDoubleFinishAccumulator(operator, contentionLevel, determinismPolicy);
	}

	public static FinishAccumulator<Double> newDoubleFinishAccumulator(NumberReductionOperator operator,
			ContentionLevel contentionLevel) {
		return newDoubleFinishAccumulator(operator, contentionLevel, DoubleAccumulationDeterminismPolicy.DETERMINISTIC);
	}

	public static FinishAccumulator<Double> newDoubleFinishAccumulator(NumberReductionOperator operator) {
		return newDoubleFinishAccumulator(operator, ContentionLevel.HIGH);
	}

	public static <T> FinishAccumulator<T> newReducerFinishAccumulator(AccumulatorReducer<T> reducer,
			ContentionLevel contentionLevel) {
		return getImplementation().newReducerFinishAccumulator(reducer, contentionLevel);
	}

	public static <T> FinishAccumulator<T> newReducerFinishAccumulator(AccumulatorReducer<T> reducer) {
		return newReducerFinishAccumulator(reducer, ContentionLevel.HIGH);
	}

	/**
	 * @see edu.rice.hj.Module2#isolated(HjRunnable)
	 */
	public static void isolated(HjRunnable body) {
		getImplementation().isolated(body);
	}

	/**
	 * @see edu.rice.hj.Module2#isolated(Object[],HjRunnable)
	 */
	public static void isolated(ObjectBasedIsolationOption objectBasedIsolationOption, HjRunnable body) {
		getImplementation().isolated(objectBasedIsolationOption.getParticipants(), body);
	}

	/**
	 * @see edu.rice.hj.Module2#isolatedWithReturn(HjCallable)
	 */
	public static <V> V isolatedWithReturn(HjCallable<V> callable) {
		return getImplementation().isolatedWithReturn(callable);
	}

	/**
	 * @see edu.rice.hj.Module2#isolatedWithReturn(Object[],HjCallable)
	 */
	public static <V> V isolatedWithReturn(ObjectBasedIsolationOption objectBasedIsolationOption,
			HjCallable<V> callable) {
		return getImplementation().isolatedWithReturn(objectBasedIsolationOption.getParticipants(), callable);
	}

	/**
	 * @see edu.rice.hj.Module2#readMode(Object)
	 */
	public static Object readMode(Object wrappee) {
		return getImplementation().readMode(wrappee);
	}

	/**
	 * @see edu.rice.hj.Module2#writeMode(Object)
	 */
	public static Object writeMode(Object wrappee) {
		return getImplementation().writeMode(wrappee);
	}

	/**
	 * @see edu.rice.hj.Module0#newDataDrivenFuture()
	 */
	public static <V> HjDataDrivenFuture<V> newDataDrivenFuture() {
		return getImplementation().newDataDrivenFuture();
	}

	/**
	 * @see edu.rice.hj.Module0#newPoint(int...)
	 */
	public static HjPoint newPoint(int... values) {
		return getImplementation().newPoint(values);
	}

	public static AwaitFuturesOption await(HjFuture<?> futureA, HjFuture<?>... futuresBtoZ) {
		return new AwaitFuturesOption(futureA, futuresBtoZ);
	}

	public static ChunkedOption chunked() {
		return new ChunkedOption();
	}

	public static ChunkedOption chunked(int size) {
		return new ChunkedOption(size);
	}

	public static RegisterAccumulatorsOption register(FinishAccumulator<?> accumulatorA,
			FinishAccumulator<?>... accumulatorBtoZ) {
		return new RegisterAccumulatorsOption(accumulatorA, accumulatorBtoZ);
	}

	public static ObjectBasedIsolationOption objectBased(Object participant) {
		return new ObjectBasedIsolationOption(participant);
	}

	public static ObjectBasedIsolationOption objectBasedAll(Object participantA, Object participantB,
			Object... participantsCtoZ) {
		return new ObjectBasedIsolationOption(participantA, participantB, participantsCtoZ);
	}

	public static PhasedOption phased(HjPhaserPair phaserPairA, HjPhaserPair... phaserPairsBtoZ) {
		return new PhasedOption(phaserPairA, phaserPairsBtoZ);
	}

	public static PhasedEmptyOption phased() {
		return new PhasedEmptyOption();
	}

	public static SingleOption single(Runnable runnable) {
		return new SingleOption(runnable);
	}
}
