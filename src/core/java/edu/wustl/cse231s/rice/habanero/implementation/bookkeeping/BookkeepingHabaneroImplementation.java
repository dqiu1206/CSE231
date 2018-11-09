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
package edu.wustl.cse231s.rice.habanero.implementation.bookkeeping;

import java.util.Iterator;
import java.util.concurrent.atomic.AtomicInteger;

import edu.rice.hj.api.HjSuspendable;
import edu.rice.hj.api.HjSuspendingProcedure;
import edu.rice.hj.api.HjSuspendingProcedureInt1D;
import edu.rice.hj.api.HjSuspendingProcedureInt2D;
import edu.rice.hj.api.SuspendableException;
import edu.rice.hj.api.HjRegion.HjRegion1D;
import edu.rice.hj.api.HjRegion.HjRegion2D;
import edu.wustl.cse231s.rice.habanero.implementation.classic.ClassicHabaneroImplementation;
import edu.wustl.cse231s.rice.habanero.options.ChunkedOption;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
public class BookkeepingHabaneroImplementation extends ClassicHabaneroImplementation {
	@Override
	public void async(HjSuspendable body) {
		super.async(body);
		asyncInvocationCount.incrementAndGet();
	}
	
	@Override
	public void finish(HjSuspendable body) throws SuspendableException {
		super.finish(body);
		finishInvocationCount.incrementAndGet();
	}
	
	@Override
	public void forasync(HjRegion1D hjRegion, HjSuspendingProcedureInt1D body) throws SuspendableException {
		super.forasync(hjRegion, body);
		forasyncRegionInvovationCount.incrementAndGet();
		asyncViaForasyncRegionCount.addAndGet(hjRegion.numElements());
	}

	@Override
	protected <T> void forasync(Iterator<T> iterator, HjSuspendingProcedure<T> body) throws SuspendableException {
		if(iterator.hasNext()) {
			asyncAvoidedInContinuationCount.incrementAndGet();		
		}
		super.forasync(iterator, body);
	}
	
	@Override
	public <T> void forasync(T[] array, HjSuspendingProcedure<T> body) throws SuspendableException {
		if( array.length  > 0 ) {
			asyncAvoidedInContinuationCount.incrementAndGet();		
		}
		super.forasync(array, body);
	}
	
	@Override
	public void forasync2d(HjRegion2D hjRegion, HjSuspendingProcedureInt2D body) {
		super.forasync2d(hjRegion, body);
		forasync2dRegionInvocationCount.incrementAndGet();
	}
	
	@Override
	public void forasync2d(ChunkedOption chunkedOption, HjRegion2D hjRegion, HjSuspendingProcedureInt2D body)
			throws SuspendableException {
		super.forasync2d(chunkedOption, hjRegion, body);
		forasync2dRegionChunkedInvocationCount.incrementAndGet();
	}
	
	public int getAsyncInvocationCount() {
		return asyncInvocationCount.get();
	}
	public int getFinishInvocationCount() {
		return finishInvocationCount.get();
	}
	public int getForasyncRegionInvocationCount() {
		return forasyncRegionInvovationCount.get();
	}
	
	public int getForasync2dRegionInvocationCount() {
		return forasync2dRegionInvocationCount.get();
	}

	public int getForasync2dRegionChunkedInvocationCount() {
		return forasync2dRegionChunkedInvocationCount.get();
	}

	public int getAsyncViaForasyncRegionCount() {
		return asyncViaForasyncRegionCount.get();
	}

	public int getTaskCount() {
		return asyncInvocationCount.get() + asyncViaForasyncRegionCount.get() + asyncAvoidedInContinuationCount.get();
	}
	
	public void resetAllInvocationCounts() {
		asyncInvocationCount.set(0);
		finishInvocationCount.set(0);
		forasyncRegionInvovationCount.set(0);
		asyncViaForasyncRegionCount.set(0);
	}

	private final AtomicInteger asyncInvocationCount = new AtomicInteger(0);
	private final AtomicInteger finishInvocationCount = new AtomicInteger(0);
	private final AtomicInteger forasyncRegionInvovationCount = new AtomicInteger(0);
	private final AtomicInteger asyncViaForasyncRegionCount = new AtomicInteger(0);
	private final AtomicInteger asyncAvoidedInContinuationCount = new AtomicInteger(0);

	private final AtomicInteger forasync2dRegionInvocationCount = new AtomicInteger(0);
	private final AtomicInteger forasync2dRegionChunkedInvocationCount = new AtomicInteger(0);
}
