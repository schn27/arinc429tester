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

import schn27.serial.Serial;
import java.io.IOException;
import java.time.Instant;
import java.util.concurrent.TimeoutException;
import java.util.function.Consumer;
import schn27.serial.NotAvailableException;
import schn27.utils.BitReverser;

/*
 * Reads Arinc429 words from the Arinc429tester device via USB.
 *
 * Arinc429 words are transmitted as 5-bytes frames. MSB bit of each byte is the frame flag: 1 - first byte, 0 - other bytes (2-5).
 * Complete frame is on the following diagram, where a1-a32 - ARINC429 bits:
 *    7    6    5    4    3    2    1    0
 * 1 [1,  a1,  a2 , a3,  a4,  a5,  a6,  a7]
 * 2 [0,  a8,  a9, a10, a11, a12, a13, a14]
 * 3 [0, a15, a16, a17, a18, a19, a20, a21]
 * 4 [0, a22, a23, a24, a25, a26, a27, a28]
 * 5 [0, a29, a30, a31, a32,   u,   u,   u]
 *
 * WARNING! Due to the bug in the arin429tester firmware the bit order should be considered as following:
 *    7    6    5    4    3    2    1    0
 * 1 [1,  a8,  a7 , a6,  a5,  a4,  a3,  a2]
 * 2 [0,  a1, a16, a15, a14, a13, a12, a11]
 * 3 [0, a10,  a9, a24, a23, a22, a21, a20]
 * 4 [0, a19, a18, a17, a32, a31, a30, a29]
 * 5 [0, a28, a27, a26, a25,   u,   u,   u]
 */

public class Reader implements Runnable {
	public Reader(Serial serial, Consumer<TimeMarkedArinc429Word> consumer, ReaderClosedListener readerClosedListener, boolean fakeTime) {
		this(serial, consumer, readerClosedListener, fakeTime, true);
	}
	
	public Reader(Serial serial, Consumer<TimeMarkedArinc429Word> consumer, ReaderClosedListener readerClosedListener, boolean fakeTime, boolean workaround) {
		this.consumer = consumer;
		isRunning = true;
		this.serial = serial;
		opened = false;
		frame = new byte[5];
		this.readerClosedListener = readerClosedListener;
		this.fakeTime = fakeTime;
		this.workaround = workaround;
	}
	
	@FunctionalInterface
	public interface ReaderClosedListener {
		void performed();
	}	
	
	@Override
	public void run() {
		while (isRunning) {
			try {
				open();
				consumer.accept(readWord());
			} catch (TimeoutException | InterruptedException ex) {
			} catch (NotAvailableException ex) {
				break;
			} catch (IOException ex) {
				System.err.println("IO exception: " + ex.getMessage());	// TODO: use java.util.logging
			}
		}
		
		close();
	}
	
	public void stop() {
		isRunning = false;
	}
	
	private void open() throws IOException, NotAvailableException {
		if (!opened) {
			serial.open();
			opened = true;
		}
	}
	
	private void close() {
		if (opened) {
			if (serial != null) {
				try {
					serial.close();
				} catch (IOException ex) {
				}
			}

			opened = false;
		}
		
		if (readerClosedListener != null) {
			readerClosedListener.performed();
		}
	}
	
	private TimeMarkedArinc429Word readWord() throws TimeoutException, InterruptedException, NotAvailableException {
		int raw = BitReverser.reverse(receiveFrame());
		Arinc429Word word = new Arinc429Word(workaround ? doWorkaround(raw) : raw);
		return new TimeMarkedArinc429Word(fakeTime ? Instant.MIN : Instant.now(), word);
	}

	private int receiveFrame() throws InterruptedException, TimeoutException, NotAvailableException {
		int pos = 0;
		while (pos < frame.length) {
			if  (serial.read(frame, pos, 1, 1000) != 1) {
				throw new TimeoutException();
			}
			
			if (((frame[pos] & 0x80) == 0) ^ (pos == 0)) {
				++pos;
			} else {
				pos = 0;
			}
		}
		
		return getFramePayload();
	}

	private int getFramePayload() {
		long res = 0;
		
		for (byte b : frame) {
			res = (res << 7) | ((int)b & 0x7f);
		}
		
		return (int)(res >> 3);
	}
	
	private int doWorkaround(int x) {
		return 
				(x & 0xFF) |
				((BitReverser.reverse((byte)(x >> 8)) & 0xFF) << 8) | 
				((BitReverser.reverse((byte)(x >> 16)) & 0xFF) << 16) | 
				((BitReverser.reverse((byte)(x >> 24)) & 0xFF) << 24);
	}
	
	private final Consumer<TimeMarkedArinc429Word> consumer;
	private volatile boolean isRunning;
	private final Serial serial;
	private boolean opened;
	private final byte[] frame;
	private final ReaderClosedListener readerClosedListener;
	private final boolean fakeTime;
	private final boolean workaround;
}
