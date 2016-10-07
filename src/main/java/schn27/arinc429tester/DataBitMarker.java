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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Aleksandr Malikov <schn27@gmail.com>
 */
public class DataBitMarker {
	public DataBitMarker() {
		this(new ArrayList<>());
	}
	
	public DataBitMarker(List<Entry> colors) {
		setColors(colors);
	}
	
	public List<Entry> getColors() {
		return colors;
	}
	
	public final void setColors(List<Entry> colors) {
		this.colors = new ArrayList<>(colors);
		colorsMap = new HashMap<>();
		colors.forEach((color) -> colorsMap.put(color.bitNumber, color.color));
	}
	
	public String getHtmlText(String text) {
		if (colorsMap.isEmpty()) {
			return text;
		}
	
		StringBuilder sb = new StringBuilder();
		sb.append("<html>");
		
		int bitNumber = 29;
		
		for (char c : text.toCharArray()) {
			if (colorsMap.containsKey(bitNumber)) {
				sb.append(String.format("<span style=\"background:#%x\">%c</span>", colorsMap.get(bitNumber), c));
			} else {
				sb.append(c);
			}
			
			--bitNumber;			
		}
		
		sb.append("</html>");
		return sb.toString();		
	}
	
	public static class Entry {
		public Entry(int bitNumber, int color) {
			this.bitNumber = bitNumber;
			this.color = color;
		}
		
		public final int bitNumber;
		public final int color;
	}
	
	private List<Entry> colors;
	private Map<Integer, Integer> colorsMap;
}
