package schn27.arinc429tester.bl;

public class Arinc429Word {
	public Arinc429Word(int raw) {
		this.raw = raw;
	}
	
	public Arinc429Word(byte label, byte sdi, int pad, byte ssm) {
		int v = ((reverseByte(label) & 0xFF) | 
				((sdi & 3) << 8) | 
				((pad & ((1 << 18) - 1)) << 10) | 
				((ssm & 3) << 29)) & 0x7FFFFFFF;
		
		if (!getWordParity(v)) {
			v |= (1 << 31);
		}
		
		raw = v;
	}
	
	public boolean isParityCorrect() {
		return getWordParity(raw);
	}
	
	public byte getParity() {
		return (byte)((raw >> 31) & 1);
	}
	
	public byte getSSM() {
		return (byte)((raw >> 29) & 3);
	}
	
	public int getPad() {
		return (raw >> 10) & ((1 << 18) - 1);
	}
	
	public byte getSDI() {
		return (byte)((raw >> 8) & 3);
	}
	
	public byte getLabel() {
		return reverseByte((byte)(raw & 0xFF));
	}
	
	private boolean getWordParity(int value) {
		int x = (value ^ (value >> 16)) & 0xFFFF;
		x = (x ^ (x >> 8)) & 0xFF;
		x = (x ^ (x >> 4)) & 0x0F;
		x = (x ^ (x >> 2)) & 0x03;
		return ((x ^ (x >> 1)) & 1) == 1;
	}

	private byte reverseByte(byte x) {
		x = (byte)(((x >> 1) & 0x55) | ((x & 0x55) << 1));
		x = (byte)(((x >> 2) & 0x33) | ((x & 0x33) << 2));
		return (byte)(((x >> 4) & 0x0F) | (x << 4));
	}	
	
	public final int raw;
}
