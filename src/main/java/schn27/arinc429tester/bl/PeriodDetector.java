/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schn27.arinc429tester.bl;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author amalikov
 */
public class PeriodDetector {
	public PeriodDetector() {
		current = new TreeMap<>();
		min = new TreeMap<>();
		max = new TreeMap<>();
		time = new TreeMap<>();
	}
	
	public void clear() {
		current.clear();
		min.clear();
		max.clear();
		time.clear();
	}
	
	public void put(int id, Instant time) {
		if (this.time.containsKey(id)) {
			long delta = Duration.between(this.time.get(id), time).toMillis();
			
			current.put(id, (int)delta);
			
			if (!min.containsKey(id) || delta < min.get(id)) {
				min.put(id, (int)delta);
			}
			
			if (!max.containsKey(id) || delta > max.get(id)) {
				max.put(id, (int)delta);
			}			
		}
		
		this.time.put(id, time);
	}
	
	public int get(int id) {
		return get(id, current);
	}
	
	public int getMin(int id) {
		return get(id, min);
	}
	
	public int getMax(int id) {
		return get(id, max);
	}
	
	private int get(int id, Map<Integer, Integer> src) {
		return src.getOrDefault(id, -1);
	}
	
	private final Map<Integer, Integer> current;
	private final Map<Integer, Integer> min;
	private final Map<Integer, Integer> max;
	private final Map<Integer, Instant> time;
}
