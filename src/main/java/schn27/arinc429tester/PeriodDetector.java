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
		if (time.equals(Instant.MIN)) {
			return;
		}
		
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
