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
package edu.wustl.cse231s.util;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
public final class OrderedPair<T extends Comparable<T>> implements Comparable<OrderedPair<T>> {
	public OrderedPair( T candidate1, T candidate2 ) {
		if( candidate1.compareTo(candidate2) < 0 ) {
			this.a = candidate1;
			this.b = candidate2;
		} else {
			this.a = candidate2;
			this.b = candidate1;
		}
	}

	public T getA() {
		return this.a;
	}

	public T getB() {
		return this.b;
	}

	@Override
	public int compareTo(OrderedPair<T> other) {
		int result = this.a.compareTo(other.getA());
		if (result != 0) {
			// pass
		} else {
			result = this.b.compareTo(other.getB());
		}
		return result;
	}
	
	@Override
	public boolean equals( Object obj ) {
		if( obj instanceof OrderedPair ) {
			OrderedPair<T> other = (OrderedPair<T>)obj;
			return this.a.equals(other.getA()) && this.b.equals(other.getB());
		} else {
			return false;
		}
	}

	@Override
	public final int hashCode() {
		int rv = 17;
		rv = ( 37 * rv ) + this.a.hashCode();
		rv = ( 37 * rv ) + this.b.hashCode();
		return rv;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("(");
		sb.append(this.a);
		sb.append(" ");
		sb.append(this.b);
		sb.append(")");
		return sb.toString();
	}
	
	private final T a;
	private final T b;
}
