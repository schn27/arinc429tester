package schn27.arinc429tester.bl;

import schn27.serial.Com;
import schn27.serial.Serial;
import java.io.IOException;
import java.util.concurrent.TimeoutException;
import schn27.serial.FakePort;

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
 */

public class Reader implements Runnable {
	public Reader(String portName, Arinc429WordConsumer consumer) {
		comPort = portName.equals("Fake") ? new FakePort() : new Com(portName, 230400);
		this.consumer = consumer;
		isRunning = true;
		opened = false;
		frame = new byte[5];
	}
	
	@Override
	public void run() {
		while (isRunning) {
			try {
				open();
				consumer.consume(new Arinc429Word(readWord()));
			} catch(TimeoutException | InterruptedException ex) {
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
		if (opened) {
			return;
		}
		
		comPort.open();
		opened = true;
	}
	
	private void close() {
		if (!opened) {
			return;
		}
		
		if (comPort != null) {
			try {
				comPort.close();
			} catch (IOException ex) {
			}
		}
		
		opened = false;
	}
	
	private int readWord() throws TimeoutException, InterruptedException {
		receiveFrame();
		return reverseBytes(getFramePayload());
	}

	private void receiveFrame() throws InterruptedException, TimeoutException {
		int pos = 0;
		while (pos < frame.length) {
			if  (comPort.read(frame, pos, 1, 1000) != 1) {
				throw new TimeoutException();
			}
			
			if (((frame[pos] & 0x80) == 0) ^ (pos == 0)) {
				++pos;
			} else {
				pos = 0;
			}
		}
	}

	private int getFramePayload() {
		long res = 0;
		
		for (byte b : frame) {
			res = (res << 7) | ((int)b & 0x7f);
		}
		
		return (int)(res >> 3);
	}	
	
	private int reverseBytes(int value) {
		int res = 0;
		
		for (int i = 0; i < 4; ++i) {
			res <<= 8;
			res |= (value & 0xFF);
			value >>= 8;
		}
		
		return res;
	}

	private final Arinc429WordConsumer consumer;
	private volatile boolean isRunning;
	private final Serial comPort;
	private boolean opened;
	private final byte[] frame;
}
