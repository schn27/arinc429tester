/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schn27.arinc429tester.bl;

/**
 *
 * @author amalikov
 */
public enum NumberSystem {
	BIN("BIN"), OCT("OCT"), DEC("DEC"), HEX("HEX");
	
	NumberSystem(String title) {
		this.title = title;
	}
	
	@Override
	public String toString() {
		return title;
	}
	
	public String integerToString(int value) {
		return integerToString(value, 32);
	}
	
	public String integerToString(int value, int bitLength) {
		switch (this) {
		case BIN:
			return String.format(String.format("%%%ds", bitLength), Integer.toBinaryString(value)).replace(' ', '0');
		case OCT:
			return String.format(String.format("%%0%do", (int)(bitLength / 3.0 + 0.5)), value);
		case DEC:
			return String.format("%d", value);
		case HEX:
			return String.format(String.format("%%0%dX", (int)(bitLength / 4.0 + 0.5)), value);
		}
		
		return null;
	}
	
	public int parseInteger(String str) throws NumberFormatException {
		switch (this) {
		case BIN:
			return Integer.parseInt(str, 2);
		case OCT:
			return Integer.parseInt(str, 8);
		case DEC:
			return Integer.parseInt(str, 10);
		case HEX:
			return Integer.parseInt(str, 16);
		}
		
		return -1;
	}
	
	private final String title;
}
