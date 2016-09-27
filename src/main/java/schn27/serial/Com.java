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
package schn27.serial;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import jssc.SerialPort;
import jssc.SerialPortException;
import jssc.SerialPortList;
import jssc.SerialPortTimeoutException;

/**
 *
 * @author amalikov
 */
public class Com implements Serial {
	public Com(String portName, int baudRate) {
		this.portName = portName;
		this.baudRate = baudRate;
		rwLock = new ReentrantReadWriteLock();
	}
	
	public static List<String> getList() {
		return Arrays.stream(SerialPortList.getPortNames()).collect(Collectors.toList());
	}
	
	@Override
	public void open() {
		try {
			rwLock.writeLock().lock();
			openImpl();
		} finally {
			rwLock.writeLock().unlock();
		}
	}
	
	@Override
	public void close() {
		try {
			rwLock.writeLock().lock();
			closeImpl();
		} finally {
			rwLock.writeLock().unlock();
		}
	}
	
	@Override
	public int read(byte[] buffer, int ofs, int size, int timeout) throws InterruptedException, NotAvailableException {
		try {
			rwLock.readLock().lock();
			return readImpl(buffer, ofs, size, timeout);
		} finally {
			rwLock.readLock().unlock();
		}
	}
	
	@Override
	public void write(byte[] buffer, int ofs, int size) {
		try {
			rwLock.writeLock().lock();
			writeImpl(buffer, ofs, size);
		} finally {
			rwLock.writeLock().unlock();
		}
	}
	
	@Override
	public void clean() {
		try {
			rwLock.readLock().lock();
			cleanImpl();
		} finally {
			rwLock.readLock().unlock();
		}
	}
	
	private void openImpl() {
		if (port != null) {
			return;
		}

		clean();

		log.log(Level.INFO, "Opening port {0}", portName);

		try {
			port = openSerialPort();
			port.openPort();
			port.setParams(baudRate, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
			port.setFlowControlMode(SerialPort.FLOWCONTROL_NONE);
		} catch (SerialPortException ex) {
			log.log(Level.SEVERE, "exception", ex);
			close();
		}
		
		log.log(Level.INFO, "Opening port {0} done", portName);
	}
	
	private void closeImpl() {
		if (port == null) {
			return;
		}

		log.log(Level.INFO, "Closing port {0}", port.getPortName());

		clean();

		try {
			port.closePort();
		} catch (SerialPortException ex) {
			log.log(Level.SEVERE, "exception", ex);
		}

		log.log(Level.INFO, "Closing port {0} done", port.getPortName());

		port = null;
	}

	private int readImpl(byte[] buffer, int ofs, int size, int timeout) throws InterruptedException, NotAvailableException {
		try {
			byte[] read = port.readBytes(size, timeout);

			for (int i = 0; i < size; ++i) {	
				buffer[i + ofs] = read[i];
			}
			
			return size;
		} catch (SerialPortException ex) {
			throw new NotAvailableException(ex.getMessage());
		} catch (SerialPortTimeoutException ex) {
		}

		return 0;
	}
	
	private void writeImpl(byte[] buffer, int ofs, int size) {
		try {
			if (port != null && buffer != null) {
				port.writeBytes(Arrays.copyOfRange(buffer, ofs, ofs + size));
			}
		} catch (SerialPortException ex) {
			log.log(Level.SEVERE, "exception", ex);
		}
	}
	
	private void cleanImpl() {
		if (port != null) {
			try {
				port.purgePort(
						SerialPort.PURGE_RXABORT | SerialPort.PURGE_TXABORT |
						SerialPort.PURGE_RXCLEAR | SerialPort.PURGE_TXCLEAR);
			} catch (SerialPortException ex) {
				log.log(Level.SEVERE, "exception", ex);
			}
		}
	}
	
	private SerialPort openSerialPort() {
		return new SerialPort(portName);
	}
	
	private final String portName;
	private final int baudRate;
	private SerialPort port;
	
	private final ReadWriteLock rwLock;
	
	private static final Logger log = Logger.getLogger(Com.class.getName());
}
