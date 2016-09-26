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

import java.util.Arrays;
import java.util.BitSet;

/**
 *
 * @author amalikov
 */
public class LabelFilter {
	public LabelFilter() {
		includeMode = false;
		numberSystem = NumberSystem.OCT;
		labels = new int[16];
		Arrays.fill(labels, -1);
		labelMask = new BitSet(256);
	}
	
	public LabelFilter(LabelFilter other) {
		includeMode = other.includeMode;
		numberSystem = other.numberSystem;
		labels = other.getLabels();
		updateLabelMask();
	}
	
	public int[] getLabels() {
		return Arrays.copyOf(labels, labels.length);
	}
	
	public void setLabels(int[] labels) {
		this.labels = labels.clone();
		updateLabelMask();
	}
	
	public boolean isAccepted(int label) {
		return labelMask.get(label) ^ !includeMode;
	}
	
	@Override
	public String toString() {
		StringBuilder list = new StringBuilder();
		for (int label : labels) {
			if (label != -1) {
				if (list.length() > 0) {
					list.append(", ");
				}
				list.append(numberSystem.integerToString(label, 8));
			}
		}
		
		return String.format("%s (%s): %s", includeMode ? "Include" : "Exclude", numberSystem, list.length() > 0 ? list : "none");
	}
	
	private void updateLabelMask() {
		BitSet mask = new BitSet(256);
		for (int label : labels) {
			if (label >= 0 && label < 255) {
				mask.set(label);
			}
		}
		
		labelMask = mask;
	}

	public boolean includeMode;
	public NumberSystem numberSystem;
	private int[] labels;
	private BitSet labelMask;
}
