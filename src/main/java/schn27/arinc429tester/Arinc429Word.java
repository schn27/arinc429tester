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

public class Arinc429Word {
	public Arinc429Word(int raw) {
		this.raw = raw;
	}
	
	public Arinc429Word(int label, byte sdi, int data, byte ssm) {
		int v = (label | 
				((sdi & 3) << 8) | 
				((data & ((1 << 19) - 1)) << 10) | 
				((ssm & 3) << 29)) & 0x7FFFFFFF;
		
		if (!getWordParity(v)) {
			v |= (1 << 31);
		}
		
		raw = v;
	}
	
	public boolean isParityCorrect(boolean odd) {
		return getWordParity(raw) ^ !odd;
	}
	
	public byte getParity() {
		return (byte)((raw >> 31) & 1);
	}
	
	public byte getSsm() {
		return (byte)((raw >> 29) & 3);
	}
	
	public int getData() {
		return (raw >> 10) & ((1 << 19) - 1);
	}
	
	public byte getSdi() {
		return (byte)((raw >> 8) & 3);
	}
	
	public int getLabel() {
		return raw & 0xFF;
	}
	
	private boolean getWordParity(int value) {
		int x = (value ^ (value >> 16)) & 0xFFFF;
		x = (x ^ (x >> 8)) & 0xFF;
		x = (x ^ (x >> 4)) & 0x0F;
		x = (x ^ (x >> 2)) & 0x03;
		return ((x ^ (x >> 1)) & 1) == 1;
	}

	public final int raw;
}
