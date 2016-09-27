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

import java.io.IOException;
import schn27.serial.Serial;
import schn27.utils.BitReverser;


/**
 *
 * @author amalikov
 */
public class FakePort implements Serial {

	public FakePort() {
		this(true);
	}
	
	public FakePort(boolean workaround) {
		this.workaround = workaround;
		buffer = new byte[5];
		pos = buffer.length;
	}
	
	@Override
	public void open() throws IOException {}

	@Override
	public void close() throws IOException {}

	@Override
	public int read(byte[] buffer, int ofs, int size, int timeout) throws InterruptedException {
		for (int i = 0; i < size; ++i) {
			if (pos >= this.buffer.length) {
				fillBuffer();
			}

			buffer[ofs + i] = this.buffer[pos++];
		}
		return size;
	}

	@Override
	public void write(byte[] buffer, int ofs, int size) {}
	
	@Override
	public void clean() {}
	
	private void fillBuffer() {
		try {
			Thread.sleep(100);
		} catch (InterruptedException ex) {
		}		
		
		Arinc429Word word = new Arinc429Word(
				(int)(Math.random() * (1 << 8)),
				(byte)(Math.random() * (1 << 2)),
				(int)(Math.random() * (1 << 19)),
				(byte)(Math.random() * (1 << 2)));
		
		int w = BitReverser.reverse(workaround ? doWorkaround(word.raw) : word.raw);
		for (int i = 0; i < buffer.length; ++i) {
			buffer[i] = (byte)(((i == 0) ? (byte)0x80 : 0) | (byte)((w >> 25) & 0x7f));
			w <<= 7;
		}
		pos = 0;
	}
	
	private int doWorkaround(int x) {
		return 
				(x & 0xFF) |
				((BitReverser.reverse((byte)(x >> 8)) & 0xFF) << 8) | 
				((BitReverser.reverse((byte)(x >> 16)) & 0xFF) << 16) | 
				((BitReverser.reverse((byte)(x >> 24)) & 0xFF) << 24);
	}
	
	private final boolean workaround;
	private final byte[] buffer;
	private int pos;
}
