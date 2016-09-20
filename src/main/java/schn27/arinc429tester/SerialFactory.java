/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
