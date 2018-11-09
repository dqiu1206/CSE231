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
package edu.wustl.cse231s.timing;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
public final class ImmutableTimer {
	public ImmutableTimer(String prefix) {
		this.prefix = prefix;
		this.t0 = System.nanoTime();
	}

	public long mark() {
		long dt = System.nanoTime() - this.t0;
		return dt;
	}

	public void printResults(long dt, Object... args) {
		System.out.printf("%s; time:%10.3f msec;", this.prefix, dt * 0.000001);
		for (Object arg : args) {
			System.out.print(arg);
			System.out.print(" ");
		}
		System.out.println();
	}
	
	public long markAndPrintResults(Object... args) {
		long dt = System.nanoTime() - this.t0;
		this.printResults(dt, args);
		return dt;
	}

	private final String prefix;
	private final long t0;
}
