package com.transas.arinc429tester.bl;

import com.transas.serialport.ComPort;
import com.transas.serialport.SerialPort;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Reader implements Runnable {
	public Reader(String portName, Arinc429WordConsumer consumer) {
		comPort = ComPort.create(portName, 230400);
		this.consumer = consumer;
		isRunning = true;
		opened = false;
		buffer = new byte[5];
	}
	
	@Override
	public void run() {
		while (isRunning) {
			try {
				open();
				consumer.consume(new Arinc429Word(readWord()));
			} catch(TimeoutException ex) {
			} catch (IOException ex) {
				System.err.println("IO exception: " + ex.getMessage());
			}
		}
		
		close();
	}
	
	public void stop() {
		isRunning = false;
	}
	
	private void open() throws IOException {
		if (opened)
			return;
		comPort.open();
		opened = true;
	}
	
	private void close() {
		if (!opened)
			return;
		
		if (comPort != null) {
			try {
				comPort.close();
			} catch (IOException ex) {
			}
		}
		
		opened = false;
	}
	
	private int readWord() throws TimeoutException {
		int pos = 0;
		while (pos < buffer.length) {
			if  (comPort.read(buffer, pos, 1, 1000) != 1)
				throw new TimeoutException();
			
			if (buffer[0] >= 0 || (pos != 0 && buffer[pos] < 0))
				pos = 0;
			else
				++pos;
		}
	
		long t = 0;
		for (byte b : buffer)
			t = (t << 7) | ((int)b & 0x7f);

		t >>= 3;
		
		int res = 0;
		for (int i = 0; i < 4; ++i) {
			res <<= 8;
			res |= (t & 0xFF);
			t >>= 8;
		}
		
		return res;
	}
		
	private final Arinc429WordConsumer consumer;
	private volatile boolean isRunning;
	private final SerialPort comPort;
	private boolean opened;
	private final byte[] buffer;
}
