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
package schn27.utils;

/**
 *
 * @author amalikov
 */
public final class BitReverser {
	public static final byte reverse(byte x) {
		x = (byte)(((x >> 1) & 0x55) | ((x & 0x55) << 1));
		x = (byte)(((x >> 2) & 0x33) | ((x & 0x33) << 2));
		x = (byte)(((x >> 4) & 0x0F) | ((x & 0x0F) << 4));
		return x;
	}
	
	public static final short reverse(short x) {
		x = (short)(((x >> 1) & 0x5555) | ((x & 0x5555) << 1));
		x = (short)(((x >> 2) & 0x3333) | ((x & 0x3333) << 2));
		x = (short)(((x >> 4) & 0x0F0F) | ((x & 0x0F0F) << 4));
		x = (short)(((x >> 8) & 0x00FF) | ((x & 0x00FF) << 8));
		return x;
	}
	
	public static final int reverse(int x) {
		x = (((x >> 1)  & 0x55555555) | ((x & 0x55555555) << 1));
		x = (((x >> 2)  & 0x33333333) | ((x & 0x33333333) << 2));
		x = (((x >> 4)  & 0x0F0F0F0F) | ((x & 0x0F0F0F0F) << 4));
		x = (((x >> 8)  & 0x00FF00FF) | ((x & 0x00FF00FF) << 8));
		x = (((x >> 16) & 0x0000FFFF) | ((x & 0x0000FFFF) << 16));
		return x;
	}	
}	

