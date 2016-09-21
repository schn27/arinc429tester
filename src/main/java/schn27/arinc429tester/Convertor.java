/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schn27.arinc429tester;

/**
 *
 * @author amalikov
 */
public class Convertor {
	public Convertor(int label, Type type, int signBit, int hiBit, int loBit, double hiBitValue) {
		this.label = label;
		this.type = type;
		this.signBit = Math.max(signBit, 30);
		this.hiBit = Math.min(Math.max(hiBit, 8), 30);
		this.loBit = Math.min(Math.max(loBit, 8), this.hiBit);
		this.hiBitValue = hiBitValue;
	}
	
	public double getConverted(Arinc429Word word) {
		int data = (word.raw >> loBit) & ((1 << (hiBit - loBit + 1)) - 1);
	}

	public final int label;
	public enum Type {COMPLEMENT, DIRECT, BCD}
	public final Type type;
	public final int signBit;
	public final int hiBit;
	public final int loBit;
	public final double hiBitValue;
}
