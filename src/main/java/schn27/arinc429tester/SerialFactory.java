/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schn27.arinc429tester;

import java.util.List;
import schn27.serial.Com;
import schn27.serial.Serial;

/**
 *
 * @author amalikov
 */
public final class SerialFactory {
	private final static String fakeName = "Fake";
	
	public final static Serial create(String name) {
		return name.equals(fakeName) ? new FakePort() : new Com(name, 230400);
	}
	
	public final static List<String> getList() {
		List<String> list = Com.getList();
		list.add(fakeName);
		return list;
	}
}
