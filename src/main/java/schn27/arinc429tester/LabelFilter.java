/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
