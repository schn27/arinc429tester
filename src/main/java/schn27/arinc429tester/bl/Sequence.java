/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schn27.arinc429tester.bl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 *
 * @author amalikov
 */
public class Sequence {
	public Sequence() {
		data = Collections.synchronizedList(new ArrayList<>());
		listeners = new CopyOnWriteArrayList<>();
	}
	
	public void put(Arinc429Word word) {
		put(new Date(System.currentTimeMillis()), word);
	}
	
	public void put(Date timemark, Arinc429Word word) {
		data.add(new TimeMarkedArinc429Word(timemark, word));
		for (SequenceChangedListener l : listeners) {
			l.onSequenceAdded(data.size());
		}
	}
	
	public void clear() {
		data.clear();
		for (SequenceChangedListener l : listeners) {
			l.onSequenceCleared();
		}
	}
	
	public int size() {
		return data.size();
	}
	
	public TimeMarkedArinc429Word get(int index) {
		return data.get(index);
	}
	
	public List<TimeMarkedArinc429Word> getList() {
		return Collections.unmodifiableList(data);
	}
	
	public void addListener(SequenceChangedListener listener) {
		if (!listeners.contains(listener)) {
			listeners.add(listener);
		}
	}
	
	public void removeListener(SequenceChangedListener listener) {
		listeners.remove(listener);
	}
	
	private final List<TimeMarkedArinc429Word> data;
	private final List<SequenceChangedListener> listeners;
}
