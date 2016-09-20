/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schn27.serial;

import java.io.IOException;

/**
 *
 * @author amalikov
 */
public class NullSerial implements Serial {
	public NullSerial() {}

	@Override
	public void open() throws IOException {
	}

	@Override
	public void close() throws IOException {
	}

	@Override
	public int read(byte[] buffer, int ofs, int size, int timeout) throws InterruptedException, NotAvailableException {
		throw new NotAvailableException("Can't read from NullSerial");
	}

	@Override
	public void write(byte[] buffer, int ofs, int size) {
	}

	@Override
	public void clean() {
	}
}
