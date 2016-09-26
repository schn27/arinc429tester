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
