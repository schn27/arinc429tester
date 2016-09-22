/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schn27.arinc429tester;

/**
 * All bit numbers are in ARINC429 notation: 1..32
 * @author amalikov
 */
public class Convertor {
	public Convertor(int label) {
		this(label, Type.NULL, -1, -1, -1, Double.NaN);
	}
	
	public Convertor(int label, Type type, int signBit, int hiBit, int loBit, double hiBitValue) {
		this.label = label;
		this.type = type;
		this.signBit = Math.min(signBit, 31);
		this.hiBit = Math.min(Math.max(hiBit, 9), 31);
		this.loBit = Math.min(Math.max(loBit, 9), this.hiBit);
		this.hiBitValue = hiBitValue;
	}
	
	public double getConverted(Arinc429Word word) {
		if (type == Type.NULL) {
			return Double.NaN;
		}
		
		final int mantissaBits = hiBit - loBit + 1;
		final int mantissaMask = (1 << mantissaBits) - 1;
		int data = (word.raw >> (loBit - 1)) & mantissaMask;
		
		if (signBit >= 8 && ((word.raw >> (signBit - 1)) & 1) == 1) {
			if (type == Type.COMPLEMENT) {
				data |= ~mantissaMask;
			} else if (type == Type.DIRECT) {
				data = -data;
			}
		}
		
		return data * (hiBitValue / (1 << (mantissaBits - 1)));
	}

	public final int label;
	public enum Type {NULL, COMPLEMENT, DIRECT, BCD}
	public final Type type;
	public final int signBit;
	public final int hiBit;
	public final int loBit;
	public final double hiBitValue;
}
