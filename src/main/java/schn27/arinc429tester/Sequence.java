/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schn27.arinc429tester;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 *
 * @author amalikov
 */
public class Sequence {
	public Sequence() {
		this(null);
	}
	
	public Sequence(PeriodDetector periodDetector) {
		data = Collections.synchronizedList(new ArrayList<>());
		listeners = new CopyOnWriteArrayList<>();
		this.periodDetector = periodDetector;
	}
	
	public TimeMarkedArinc429Word put(Arinc429Word word) {
		return put(Instant.now(), word);
	}
	
	public TimeMarkedArinc429Word put(Instant timemark, Arinc429Word word) {
		int period = -1;
		int minPeriod = -1;
		int maxPeriod = -1;
		
		if (periodDetector != null) {
			periodDetector.put(word.getLabel() & 0xFF, timemark);
			period = periodDetector.get(word.getLabel() & 0xFF);
			minPeriod = periodDetector.getMin(word.getLabel() & 0xFF);
			maxPeriod = periodDetector.getMax(word.getLabel() & 0xFF);
		}
		
		return put(new TimeMarkedArinc429Word(timemark, word, period, minPeriod, maxPeriod));
	}
	
	public TimeMarkedArinc429Word put(TimeMarkedArinc429Word value) {
		data.add(value);
		for (SequenceChangedListener l : listeners) {
			l.onSequenceAdded(data.size());
		}
		
		return value;
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
	private final PeriodDetector periodDetector;
}
