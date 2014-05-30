package com.transas.serialport;

import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import gnu.io.UnsupportedCommOperationException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.TooManyListenersException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author amalikov
 */
public class ComPort implements SerialPort, SerialPortEventListener {
	public static SerialPort create(String portName, int baudRate) {
		return portName.equals("Fake") ? new FakePort() : new ComPort(portName, baudRate);
	}
	
	private ComPort(String portName, int baudRate) {
		this.portName = portName;
		this.baudRate = baudRate;
		this.queue = new ArrayBlockingQueue<>(256);
	}
	
	public static List<String> getList() {
		List<String> list = new ArrayList<>();
		@SuppressWarnings("unchecked")
		List<CommPortIdentifier> ids = Collections.list((Enumeration<CommPortIdentifier>)CommPortIdentifier.getPortIdentifiers());
		for (CommPortIdentifier id : ids) {
			if (id.getPortType() == CommPortIdentifier.PORT_SERIAL)
				list.add(id.getName());
		}
		list.add("Fake");
		return list;
	}
	
	@Override
	public void open() throws IOException {
		close();
		
		try {
			port = openSerialPort();
			port.setSerialPortParams(baudRate, gnu.io.SerialPort.DATABITS_8, gnu.io.SerialPort.STOPBITS_1, gnu.io.SerialPort.PARITY_NONE);
			port.addEventListener(this);
			port.notifyOnDataAvailable(true);
		} catch (UnsupportedCommOperationException ex) {
			close();
			throw new IOException("UnsupportedCommOperationException: " + ex.getMessage());
		} catch (TooManyListenersException ex) {
			close();
			throw new IOException("TooManyListenersException: " + ex.getMessage());
		} catch (PortInUseException ex) {
			throw new IOException("PortInUseException: " + ex.getMessage());
		}
	}
	
	@Override
	public void close() throws IOException {
		if (port != null) {
			port.removeEventListener();
			port.close();
		}
		port = null;
	}
	
	@Override
	public int read(byte[] buffer, int ofs, int size, int timeout) {
		for (int i = 0; i < size; ++i) {
			try {
				Byte b = queue.poll(timeout, TimeUnit.MILLISECONDS);
				if (b == null)
					return i;
				buffer[i + ofs] = b;
			} catch (InterruptedException ex) {
				return i;
			}
		}
		return size;
	}
	
	@Override
	public void write(byte[] buffer, int ofs, int size) {
		try {
			OutputStream ostream = port.getOutputStream();
			ostream.write(buffer, ofs, size);
		} catch (IOException ex) {
		}
	}

	@Override
	public void serialEvent(SerialPortEvent spe) {
		if (spe.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
			try {
				InputStream istream = port.getInputStream();
				byte[] buf = new byte[istream.available()];
				istream.read(buf, 0, buf.length);
				for (int i = 0; i < buf.length; ++i)
					queue.add(buf[i]);				
			} catch (IOException ex) {
			}
        }		
	}	
	
	private gnu.io.SerialPort openSerialPort() throws PortInUseException {
		@SuppressWarnings("unchecked")
		List<CommPortIdentifier> ids = Collections.list((Enumeration<CommPortIdentifier>)CommPortIdentifier.getPortIdentifiers());
		for (CommPortIdentifier id : ids) {
			if (id.getPortType() == CommPortIdentifier.PORT_SERIAL &&
					(id.getName().equals(portName) || id.getName().startsWith(portName))) {
				return (gnu.io.SerialPort)id.open("Arinc429Tester", 1000);
			}
		}
		return null;
	}
	
	private final String portName;
	private final int baudRate;
	private final BlockingQueue<Byte> queue;
	private gnu.io.SerialPort port;
}
