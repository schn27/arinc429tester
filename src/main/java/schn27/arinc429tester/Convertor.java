/* 
 * The MIT License
 *
 * Copyright 2016 Aleksandr Malikov <schn27@gmail.com>.
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
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package schn27.arinc429tester;

/**
 * All bit numbers are in ARINC429 notation: 1..32
 * @author amalikov
 */
public class Convertor {
	public Convertor(int label) {
		this(label, Type.NULL, -1, -1, -1, Double.NaN);
	}
	
	public Convertor(int label, Type type, int signBit, int hiBit, int loBit, double hiBitValue) {
		this.label = label;
		this.type = type;
		this.signBit = Math.min(signBit, 31);
		this.hiBit = Math.min(Math.max(hiBit, 9), 31);
		this.loBit = Math.min(Math.max(loBit, 9), this.hiBit);
		this.hiBitValue = hiBitValue;
	}
	
	public double getConverted(Arinc429Word word) {
		if (type == Type.NULL) {
			return Double.NaN;
		}
		
		final int mantissaBits = hiBit - loBit + 1;
		final int mantissaMask = (1 << mantissaBits) - 1;
		int data = (word.raw >> (loBit - 1)) & mantissaMask;
		
		if (signBit >= 8 && ((word.raw >> (signBit - 1)) & 1) == 1) {
			if (type == Type.COMPLEMENT) {
				data |= ~mantissaMask;
			} else if (type == Type.DIRECT) {
				data = -data;
			}
		}
		
		return data * (hiBitValue / (1 << (mantissaBits - 1)));
	}

	public final int label;
	public enum Type {NULL, COMPLEMENT, DIRECT, BCD}
	public final Type type;
	public final int signBit;
	public final int hiBit;
	public final int loBit;
	public final double hiBitValue;
}
