package com.transas.serialport;

import com.transas.arinc429tester.bl.Arinc429Word;
import java.io.IOException;


/**
 *
 * @author amalikov
 */
public class FakePort implements SerialPort {

	public FakePort() {
		buffer = new byte[5];
		pos = buffer.length;
	}
	
	@Override
	public void open() throws IOException {}

	@Override
	public void close() throws IOException {}

	@Override
	public int read(byte[] buffer, int ofs, int size, int timeout) {
		for (int i = 0; i < size; ++i) {
			if (pos >= this.buffer.length)
				fillBuffer();

			buffer[ofs + i] = this.buffer[pos++];
		}
		return size;
	}

	@Override
	public void write(byte[] buffer, int ofs, int size) {}
	
	private void fillBuffer() {
		try {
			Thread.sleep(100);
		} catch (InterruptedException ex) {
		}		
		
		Arinc429Word word = new Arinc429Word(
				(byte)(Math.random() * (1 << 8)),
				(byte)(Math.random() * (1 << 2)),
				(int)(Math.random() * (1 << 19)),
				(byte)(Math.random() * (1 << 2)));
		
		int w = word.raw;
		for (int i = 0; i < buffer.length; ++i) {
			buffer[i] = (byte)(((i == 0) ? (byte)0x80 : 0) | (byte)((w >> 25) & 0x7f));
			w <<= 7;
		}
		pos = 0;
	}
	
	private final byte[] buffer;
	private int pos;
}
