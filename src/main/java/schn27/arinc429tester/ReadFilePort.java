/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schn27.arinc429tester;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import schn27.serial.NotAvailableException;
import schn27.serial.Serial;

/**
 *
 * @author amalikov
 */
public class ReadFilePort implements Serial {
	public ReadFilePort(String name) {
		this.name = name;
	}

	@Override
	public void open() throws IOException, NotAvailableException {
		try {
			in = new BufferedInputStream(new FileInputStream(name));
		} catch (FileNotFoundException ex) {
			throw new NotAvailableException("File not found");
		}
	}

	@Override
	public void close() throws IOException {
		if (in != null) {
			in.close();
			in = null;
		}
	}

	@Override
	public int read(byte[] buffer, int ofs, int size, int timeout) throws InterruptedException, NotAvailableException {
		try {
			if (in == null || in.available() <= 0) {
				throw new NotAvailableException();
			}
			return in.read(buffer, ofs, size);
		} catch (IOException ex) {
			throw new NotAvailableException();
		}
	}

	@Override
	public void write(byte[] buffer, int ofs, int size) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void clean() {
	}
	
	private final String name;
	private InputStream in;
}
