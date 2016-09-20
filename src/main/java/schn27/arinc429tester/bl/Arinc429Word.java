package schn27.arinc429tester.bl;

public class Arinc429Word {
	public Arinc429Word(int raw) {
		this.raw = raw;
	}
	
	public Arinc429Word(byte label, byte sdi, int data, byte ssm) {
		int v = ((label & 0xFF) | 
				((sdi & 3) << 8) | 
				((data & ((1 << 19) - 1)) << 10) | 
				((ssm & 3) << 29)) & 0x7FFFFFFF;
		
		if (!getWordParity(v)) {
			v |= (1 << 31);
		}
		
		raw = v;
	}
	
	public boolean isParityCorrect(boolean odd) {
		return getWordParity(raw) ^ !odd;
	}
	
	public byte getParity() {
		return (byte)((raw >> 31) & 1);
	}
	
	public byte getSsm() {
		return (byte)((raw >> 29) & 3);
	}
	
	public int getData() {
		return (raw >> 10) & ((1 << 19) - 1);
	}
	
	public byte getSdi() {
		return (byte)((raw >> 8) & 3);
	}
	
	public byte getLabel() {
		return (byte)(raw & 0xFF);
	}
	
	private boolean getWordParity(int value) {
		int x = (value ^ (value >> 16)) & 0xFFFF;
		x = (x ^ (x >> 8)) & 0xFF;
		x = (x ^ (x >> 4)) & 0x0F;
		x = (x ^ (x >> 2)) & 0x03;
		return ((x ^ (x >> 1)) & 1) == 1;
	}

	public final int raw;
}
