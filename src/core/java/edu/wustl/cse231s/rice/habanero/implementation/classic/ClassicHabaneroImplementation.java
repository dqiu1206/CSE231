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
package edu.wustl.cse231s.rice.habanero.implementation.classic;

import java.io.File;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import edu.rice.hj.Module0;
import edu.rice.hj.Module1;
import edu.rice.hj.Module2;
import edu.rice.hj.ModuleEventLogging;
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
import edu.rice.hj.runtime.config.HjConfiguration;
import edu.rice.hj.runtime.config.HjSystemProperty;
import edu.rice.hj.runtime.metrics.AbstractMetricsManager;
import edu.rice.hj.runtime.region.RectangularRegion2D;
import edu.rice.hj.runtime.util.MultipleExceptions;
import edu.wustl.cse231s.rice.habanero.contrib.api.AccumulatorReducer;
import edu.wustl.cse231s.rice.habanero.contrib.api.ContentionLevel;
import edu.wustl.cse231s.rice.habanero.contrib.api.DoubleAccumulationDeterminismPolicy;
import edu.wustl.cse231s.rice.habanero.contrib.api.FinishAccumulator;
import edu.wustl.cse231s.rice.habanero.contrib.api.NumberReductionOperator;
import edu.wustl.cse231s.rice.habanero.implementation.HabaneroImplementation;
import edu.wustl.cse231s.rice.habanero.implementation.classic.contrib.runtime.AccumulatorRuntime;
import edu.wustl.cse231s.rice.habanero.options.ChunkedOption;
import edu.wustl.cse231s.rice.habanero.options.PhasedEmptyOption;
import edu.wustl.cse231s.rice.habanero.options.PhasedOption;
import edu.wustl.cse231s.rice.habanero.options.SingleOption;
import edu.wustl.cse231s.rice.habanero.options.SystemPropertiesOption;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
public class ClassicHabaneroImplementation implements HabaneroImplementation {

	@Override
	public int numWorkerThreads() {
		return Module1.numWorkerThreads();
	}

	@Override
	public HjPhaser newPhaser(HjPhaserMode phaserMode) {
		return Module0.newPhaser(phaserMode);
	}

	@Override
	public boolean isLaunched() {
		boolean result = true;
		try {
			HjConfiguration.runtime();
		} catch (IllegalStateException ise) {
			result = false;
		}
		return result;
	}

	private static void initMultipleExceptionsCause(MultipleExceptions me) {
		if (me.getCause() != null) {
			// pass
		} else {
			try {
				Field field = MultipleExceptions.class.getDeclaredField("exceptions");
				field.setAccessible(true);
				List<Throwable> exceptions = (List<Throwable>) field.get(me);
				if (exceptions.size() > 0) {
					me.initCause(exceptions.get(0));
				}
			} catch (NoSuchFieldException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}

	private final AtomicInteger preFinalizeCallbackEnterVsLeaveCount = new AtomicInteger(0);

	@Override
	public void launchHabaneroApp(HjSuspendable body, Runnable preFinalizeCallback) {
		try {
			Module0.launchHabaneroApp(body, preFinalizeCallback != null ? () -> {
				preFinalizeCallbackEnterVsLeaveCount.incrementAndGet();
				try {
					preFinalizeCallback.run();
				} finally {
					preFinalizeCallbackEnterVsLeaveCount.decrementAndGet();
				}
			} : null);
		} catch (MultipleExceptions me) {
			initMultipleExceptionsCause(me);
			throw me;
		}
	}

	@Override
	public void launchHabaneroApp(SystemPropertiesOption systemPropertiesOption, HjSuspendable body, Runnable preFinalizeCallback) {
		final Boolean prevEventLogging;
		final Boolean prevAbstractMetrics;
		final Integer prevNumWorkers;

		File eventLogFile = systemPropertiesOption.getEventLogFile();
		if (eventLogFile != null) {
			prevEventLogging = Boolean.parseBoolean(HjSystemProperty.eventLogging.getPropertyValue());
			// System.out.println(prevEventLogging);
			HjSystemProperty.eventLogging.set(true);
		} else {
			prevEventLogging = null;
		}
		if (systemPropertiesOption.isAbstractMetricsDesired()) {
			prevAbstractMetrics = Boolean.parseBoolean(HjSystemProperty.abstractMetrics.getPropertyValue());
			// System.out.println(prevAbstractMetrics);
			HjSystemProperty.abstractMetrics.set(true);
		} else {
			prevAbstractMetrics = null;
		}

		Integer numWorkerThreads = systemPropertiesOption.getNumWorkerThreads();
		if(numWorkerThreads != null ) {
			prevNumWorkers = Integer.parseInt(HjSystemProperty.numWorkers.getPropertyValue());
			HjSystemProperty.numWorkers.set(numWorkerThreads.intValue());
		} else {
			if (systemPropertiesOption.isSerialized()) {
				prevNumWorkers = Integer.parseInt(HjSystemProperty.numWorkers.getPropertyValue());
				// System.out.println(prevNumWorkers);
				HjSystemProperty.numWorkers.set(1);
				// TODO
				// HjSystemProperty.runtime.set(RuntimeType.SEQUENTIAL.shortName);
			} else {
				prevNumWorkers = null;
			}
		}

		try {
			launchHabaneroApp(body, () -> {
				if (systemPropertiesOption.isDumpStatisticsDesired()) {
					dumpStatistics(abstractMetrics());
				}
				if (eventLogFile != null) {
					ModuleEventLogging.dumpEventLog(eventLogFile.getAbsolutePath());
				}
				if( preFinalizeCallback != null ) {
					preFinalizeCallback.run();
				}
			});
		} finally {
			if (prevEventLogging != null) {
				HjSystemProperty.eventLogging.set(prevEventLogging);
			}
			if (prevAbstractMetrics != null) {
				HjSystemProperty.abstractMetrics.set(prevAbstractMetrics);
			}
			if (prevNumWorkers != null) {
				HjSystemProperty.numWorkers.set(prevNumWorkers);
			}
		}
	}

	@Override
	public void finish(HjSuspendable body) throws SuspendableException {
		try {
			Module0.finish(body);
		} catch (MultipleExceptions me) {
			initMultipleExceptionsCause(me);
			throw me;
		}
	}

	@Override
	public void async(HjSuspendable body) {
		Module1.async(body);
	}

	@Override
	public void finish(FinishAccumulator<?>[] accumulators, HjSuspendable body) throws SuspendableException {
		AccumulatorRuntime.finish(accumulators, body);
	}

	@Override
	public FinishAccumulator<Integer> newIntegerFinishAccumulator(NumberReductionOperator operator,
			ContentionLevel contentionLevel) {
		return AccumulatorRuntime.newIntegerFinishAccumulator(operator, contentionLevel);
	}

	@Override
	public FinishAccumulator<Double> newDoubleFinishAccumulator(NumberReductionOperator operator,
			ContentionLevel contentionLevel, DoubleAccumulationDeterminismPolicy determinismPolicy) {
		return AccumulatorRuntime.newDoubleFinishAccumulator(operator, contentionLevel, determinismPolicy);
	}

	@Override
	public <T> FinishAccumulator<T> newReducerFinishAccumulator(AccumulatorReducer<T> reducer,
			ContentionLevel contentionLevel) {
		return AccumulatorRuntime.newReducerFinishAccumulator(reducer, contentionLevel);
	}

	@Override
	public <R> HjFuture<R> future(HjSuspendingCallable<R> body) {
		return Module1.future(body);
	}

	@Override
	public void async(List<HjFuture<?>> awaitFutures, HjSuspendable body) {
		Module1.asyncAwait(awaitFutures, body);
	}

	@Override
	public <R> HjFuture<R> future(List<HjFuture<?>> awaitFutures, HjSuspendingCallable<R> body) {
		return Module1.futureAwait(awaitFutures, body);
	}

	@Override
	public void forseq(HjRegion1D hjRegion, HjSuspendingProcedureInt1D body) throws SuspendableException {
		Module1.forseq(hjRegion, body);
	}

	@Override
	public void forasync(HjRegion1D hjRegion, HjSuspendingProcedureInt1D body) throws SuspendableException {
		Module1.forasync(hjRegion, body);
	}

	@Override
	public void forasync(ChunkedOption chunkedOption, HjRegion1D hjRegion, HjSuspendingProcedureInt1D body)
			throws SuspendableException {
		if (chunkedOption.isSizeDecidedBySystem()) {
			Module1.forasyncChunked(hjRegion, body);
		} else {
			Module1.forasyncChunked(hjRegion.lowerLimit(0), hjRegion.upperLimit(0), chunkedOption.getSize(), body);
		}
	}

	@Override
	public <T> void forseq(Iterable<T> iterable, HjSuspendingProcedure<T> body) throws SuspendableException {
		for (T item : iterable) {
			body.apply(item);
		}
	}
	
	protected <T> void forasync(Iterator<T> iterator, HjSuspendingProcedure<T> body) throws SuspendableException {
		if (iterator.hasNext()) {
			T zerothItem = iterator.next();
			while (iterator.hasNext()) {
				T item = iterator.next();
				async(() -> body.apply(item));
			}
			body.apply(zerothItem);
		}
	}

	@Override
	public final <T> void forasync(Iterable<T> iterable, HjSuspendingProcedure<T> body) throws SuspendableException {
		Iterator<T> iterator = iterable.iterator();
		forasync(iterator, body);
	}
	
	@Override
	public <T> void forseq(T[] array, HjSuspendingProcedure<T> body) throws SuspendableException {
		for (T item : array) {
			body.apply(item);
		}
	}
	
	@Override
	public <T> void forasync(T[] array, HjSuspendingProcedure<T> body) throws SuspendableException {
		if(array.length>0) {
			for(int i=1; i<array.length; i++ ) {
				final int ii = i;
				async(()-> body.apply(array[ii]));
			}
			body.apply(array[0]);
		}
	}
	
	@Override
	public void async(PhasedOption phasedOption, HjSuspendable body) {
		Module1.asyncPhased(phasedOption.getPhaserPairs(), body);
	}

	@Override
	public void forasync(PhasedOption phasedOption, int start, int endInclusive, HjSuspendingProcedureInt1D body)
			throws SuspendableException {
		Module1.forasyncPhased(start, endInclusive, phasedOption.getPhaserPairs(), body);
	}

	@Override
	public void forasync(PhasedEmptyOption phasedEmptyOption, int start, int endInclusive,
			HjSuspendingProcedureInt1D body) throws SuspendableException {
		Module1.forasyncPhased(start, endInclusive, body);
	}

	@Override
	public void phaserNext() throws SuspendableException {
		Module1.next();
	}

	@Override
	public void phaserNext(SingleOption singleOption) throws SuspendableException {
		// TODO:
		throw new RuntimeException("single option not supported");
	}

	@Override
	public void phaserWait() throws SuspendableException {
		Module1.doWait();
	}

	@Override
	public void phaserSignal() throws SuspendableException {
		Module1.signal();
	}

	@Override
	public void doWork(long n) {
		Module0.doWork(n);
	}

	@Override
	public HjMetrics abstractMetrics() {
		// NOTE: this is a crude check
		if (preFinalizeCallbackEnterVsLeaveCount.get() > 0) {
			return Module0.abstractMetrics();
		} else {
			throw new IllegalStateException(
					"abstractMetrics() should be called from within launchHabaneroApp's preFinalizeCallback.");
		}
	}

	@Override
	public void dumpStatistics(HjMetrics metrics) {
		AbstractMetricsManager.dumpStatistics(metrics);
	}

	@Override
	public void dumpStatistics(HjMetrics metrics, PrintStream ps) {
		AbstractMetricsManager.dumpStatistics(metrics, ps);
	}

	@Override
	public HjPoint newPoint(int... values) {
		return Module1.newPoint(values);
	}

	@Override
	public HjRegion1D newRectangularRegion1D(int pMinInc, int pMaxInc) {
		return Module1.newRectangularRegion1D(pMinInc, pMaxInc);
	}

	@Override
	public HjRegion2D newRectangularRegion2D(int pMinInc0, int pMaxInc0, int pMinInc1, int pMaxInc1) {
		return Module1.newRectangularRegion2D(pMinInc0, pMaxInc0, pMinInc1, pMaxInc1);
	}

	@Override
	public List<HjRegion1D> group(HjRegion1D hjRegion, int processorGrid) {
		return Module1.group(hjRegion, processorGrid);
	}

	@Override
	public HjRegion1D myGroup(int groupId, HjRegion1D hjRegion, int groupSize) {
		return Module1.myGroup(groupId, hjRegion, groupSize);
	}

	@Override
	public List<HjRegion2D> group(HjRegion2D hjRegion, int processorGrid0, int processorGrid1) {
		return Module1.group(hjRegion, processorGrid0, processorGrid1);
	}

	@Override
	public RectangularRegion2D myGroup(int groupId0, int groupId1, HjRegion2D hjRegion, int groupSize0,
			int groupSize1) {
		return Module1.myGroup(groupId0, groupId1, hjRegion, groupSize0, groupSize1);
	}

	@Override
	public void forseq2d(HjRegion2D hjRegion, HjSuspendingProcedureInt2D body) throws SuspendableException {
		Module1.forseq(hjRegion, body);
	}

	@Override
	public void forasync2d(HjRegion2D hjRegion, HjSuspendingProcedureInt2D body) {
		Module1.forasync(hjRegion, body);
	}

	@Override
	public void forasync2d(ChunkedOption chunkedOption, HjRegion2D hjRegion, HjSuspendingProcedureInt2D body)
			throws SuspendableException {
		if (chunkedOption.isSizeDecidedBySystem()) {
			Module1.forasyncChunked(hjRegion, body);
		} else {
			throw new RuntimeException("chunk size not supported");
		}
	}

	@Override
	public <V> HjDataDrivenFuture<V> newDataDrivenFuture() {
		return Module0.newDataDrivenFuture();
	}

	@Override
	public void isolated(HjRunnable body) {
		Module2.isolated(body);
	}

	@Override
	public void isolated(Object[] participants, HjRunnable body) {
		Module2.isolated(participants, body);
	}

	@Override
	public <V> V isolatedWithReturn(HjCallable<V> callable) {
		return Module2.isolatedWithReturn(callable);
	}

	@Override
	public <V> V isolatedWithReturn(Object[] participants, HjCallable<V> callable) {
		return Module2.isolatedWithReturn(participants, callable);
	}

	@Override
	public Object readMode(Object wrappee) {
		return Module2.readMode(wrappee);
	}

	@Override
	public Object writeMode(Object wrappee) {
		return Module2.writeMode(wrappee);
	}
}
