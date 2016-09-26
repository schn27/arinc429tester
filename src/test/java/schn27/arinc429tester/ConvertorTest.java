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
