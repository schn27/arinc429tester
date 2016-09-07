package schn27.serial;

import java.io.IOException;

/**
 *
 * @author amalikov
 */
public interface Serial {
	void open() throws IOException;
	void close() throws IOException;
	int read(byte[] buffer, int ofs, int size, int timeout) throws InterruptedException;
	void write(byte[] buffer, int ofs, int size);
	void clean();
}
