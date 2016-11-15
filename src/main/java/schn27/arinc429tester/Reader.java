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
import schn27.arinc429tester.binary.RawSerialReader;
import schn27.arinc429tester.binary.SerialReader;
import schn27.serial.NotAvailableException;

/*
 * Reads Arinc429 words from the Arinc429tester device via USB.
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
		this.readerClosedListener = readerClosedListener;
		this.fakeTime = fakeTime;
		serialReader = new RawSerialReader(workaround);
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
		return new TimeMarkedArinc429Word(fakeTime ? Instant.MIN : Instant.now(), serialReader.read(serial, 1000));
	}

	private final Consumer<TimeMarkedArinc429Word> consumer;
	private volatile boolean isRunning;
	private final Serial serial;
	private boolean opened;
	private final ReaderClosedListener readerClosedListener;
	private final boolean fakeTime;
	private final SerialReader serialReader;
}
