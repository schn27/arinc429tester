/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schn27.arinc429tester;

import junit.framework.TestCase;
import org.junit.Test;

/**
 *
 * @author amalikov
 */
public class ConvertorTest extends TestCase {
	@Test
	public void testCommon() {
		Convertor conv28_11 = new Convertor(0, Convertor.Type.COMPLEMENT, 29, 28, 11, 90.0);
		assertEquals(51.1997222900390625, conv28_11.getConverted(new Arinc429Word((byte)0, (byte)0, 0x12345, (byte)0)), Double.MIN_VALUE);
		Convertor conv12_11 = new Convertor(0, Convertor.Type.COMPLEMENT, 29, 12, 11, 90.0);
		assertEquals(45.0, conv12_11.getConverted(new Arinc429Word((byte)0, (byte)0, 0x12345, (byte)0)), Double.MIN_VALUE);
	}
	
	@Test
	public void testNoSign() {
		Convertor conv = new Convertor(0, Convertor.Type.COMPLEMENT, -1, 29, 11, 90.0);
		assertEquals(118.40000152587890625, conv.getConverted(new Arinc429Word((byte)0, (byte)0, 0x54321, (byte)0)), Double.MIN_VALUE);
	}
	
	@Test
	public void testComplementNegative() {
		Convertor conv = new Convertor(0, Convertor.Type.COMPLEMENT, 29, 28, 11, 90.0);
		assertEquals(-45.0, conv.getConverted(new Arinc429Word((byte)0, (byte)0, 0x70000, (byte)0)), Double.MIN_VALUE);
	}

	@Test
	public void testDirectNegative() {
		Convertor conv = new Convertor(0, Convertor.Type.DIRECT, 29, 28, 11, 90.0);
		assertEquals(-45.0, conv.getConverted(new Arinc429Word((byte)0, (byte)0, 0x50000, (byte)0)), Double.MIN_VALUE);
	}
}
