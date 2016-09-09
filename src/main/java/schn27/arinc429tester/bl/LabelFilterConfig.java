/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schn27.arinc429tester.bl;

import java.util.Arrays;

/**
 *
 * @author amalikov
 */
public class LabelFilterConfig {
	public LabelFilterConfig() {
		includeMode = false;
		numberSystem = NumberSystem.OCT;
		labels = new int[16];
		Arrays.fill(labels, -1);
	}
	
	public boolean includeMode;
	public NumberSystem numberSystem;
	public int[] labels;
	
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
}
