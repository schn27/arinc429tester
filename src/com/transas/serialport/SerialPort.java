package com.transas.serialport;

import java.io.IOException;

/**
 *
 * @author amalikov
 */
public interface SerialPort {
	void open() throws IOException;
	void close() throws IOException;
	int read(byte[] buffer, int ofs, int size, int timeout);	
	void write(byte[] buffer, int ofs, int size);
}
