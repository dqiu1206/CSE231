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
package edu.wustl.cse231s.bioinformatics;

/**
 * An enum representation of a nucleobase, with the ability to represent all of
 * the bases in DNA and RNA, as well as a nonspecific (unknown) base.
 * 
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
public enum Nucleobase {
	ADENINE('A'), THYMINE('T'), CYTOSINE('C'), GUANINE('G'), URACIL('U'), NONSPECIFIC('N');

	private Nucleobase(char charRepresentation) {
		this.byteRepresentation = (byte) charRepresentation;
	}

	/**
	 * Gets the byte representation of this nucleobase. This is the numerical
	 * value (ASCII) of the char representation.
	 * 
	 * @return the byte representation of this nucleobase
	 */
	public byte toByte() {
		return this.byteRepresentation;
	}

	/**
	 * Gets the char representation of this nucleobase. This will be A, T, C, G,
	 * U, or N.
	 * 
	 * @return the letter that represents this nucleobase
	 */
	public char toChar() {
		return (char) this.byteRepresentation;
	}

	private final byte byteRepresentation;

}
