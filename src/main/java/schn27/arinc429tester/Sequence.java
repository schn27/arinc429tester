/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schn27.arinc429tester;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author amalikov
 */
public class Sequence {
	public Sequence() {
		items = Collections.synchronizedList(new ArrayList<>());
	}
	
	public void put(SequenceItem item) {
		items.add(item);
	}
	
	public void clear() {
		items.clear();
	}
	
	public int size() {
		return items.size();
	}
	
	public SequenceItem get(int index) {
		return items.get(index);
	}
	
	public List<SequenceItem> getList() {
		return Collections.unmodifiableList(items);
	}
	
	private final List<SequenceItem> items;
}
