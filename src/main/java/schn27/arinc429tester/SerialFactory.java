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

import java.util.List;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import schn27.serial.Com;
import schn27.serial.NullSerial;
import schn27.serial.Serial;

/**
 *
 * @author amalikov
 */
public final class SerialFactory {
	private final static String fakePortName = "Fake";
	private final static String filePortName = "File...";
	
	private SerialFactory() {}
	
	public final static Serial create(String name) {
		if (name.equals(fakePortName)) {
			return new FakePort();
		} else if (name.equals(filePortName)) {
			return createFilePort();
		} else {
			return new Com(name, 230400);
		}
	}
	
	public final static List<String> getList() {
		List<String> list = Com.getList();
		list.add(fakePortName);
		list.add(filePortName);
		return list;
	}
	
	public final static boolean isTimedSerial(String name) {
		return !name.equals(filePortName);
	}
	
	private static Serial createFilePort() {
		JFileChooser fc = new JFileChooser();
		fc.setAcceptAllFileFilterUsed(false);
		fc.addChoosableFileFilter(new FileNameExtensionFilter("Raw", "raw"));
		
		if (fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			return new ReadFilePort(fc.getSelectedFile().getPath());
		} else {
			return new NullSerial();
		}
	}
}
