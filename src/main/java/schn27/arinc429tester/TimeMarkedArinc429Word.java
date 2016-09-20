/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schn27.arinc429tester;

import java.time.Instant;

/**
 *
 * @author amalikov
 */
public class TimeMarkedArinc429Word {
	public TimeMarkedArinc429Word(Instant timemark, Arinc429Word word) {
		this(timemark, word, -1, -1, -1);
	}
	
	public TimeMarkedArinc429Word(Instant timemark, Arinc429Word word, int period, int minPeriod, int maxPeriod) {
		this.timemark = timemark;
		this.word = word;
		this.period = period;
		this.minPeriod = minPeriod;
		this.maxPeriod = maxPeriod;
	}
	
	public final Instant timemark;
	public final Arinc429Word word;
	public final int period;
	public final int minPeriod;
	public final int maxPeriod;
}
