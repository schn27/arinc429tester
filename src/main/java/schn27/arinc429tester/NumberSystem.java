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
 *
 * @author amalikov
 */
public enum NumberSystem {
	BIN("BIN"), OCT("OCT"), DEC("DEC"), HEX("HEX");
	
	NumberSystem(String title) {
		this.title = title;
	}
	
	@Override
	public String toString() {
		return title;
	}
	
	public String integerToString(int value) {
		return integerToString(value, 32);
	}
	
	public String integerToString(int value, int bitLength) {
		switch (this) {
		case BIN:
			return String.format(String.format("%%%ds", bitLength), Integer.toBinaryString(value)).replace(' ', '0');
		case OCT:
			return String.format(String.format("%%0%do", (int)(bitLength / 3.0 + 0.5)), value);
		case DEC:
			return String.format("%d", value);
		case HEX:
			return String.format(String.format("%%0%dX", (int)(bitLength / 4.0 + 0.5)), value);
		}
		
		return null;
	}
	
	public int parseInteger(String str) throws NumberFormatException {
		switch (this) {
		case BIN:
			return Integer.parseInt(str, 2);
		case OCT:
			return Integer.parseInt(str, 8);
		case DEC:
			return Integer.parseInt(str, 10);
		case HEX:
			return Integer.parseInt(str, 16);
		}
		
		return -1;
	}
	
	private final String title;
}
