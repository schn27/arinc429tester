/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schn27.arinc429tester;

/**
 *
 * @author amalikov
 */
public final class SequenceItem {
	public SequenceItem(TimeMarkedArinc429Word tmword, int period, int minPeriod, int maxPeriod) {
		this.tmword = tmword;
		this.period = period;
		this.minPeriod = minPeriod;
		this.maxPeriod = maxPeriod;
	}
	
	public SequenceItem(TimeMarkedArinc429Word tmword) {
		this(tmword, -1, -1, -1);
	}
	
	public final TimeMarkedArinc429Word tmword;
	public final int period;
	public final int minPeriod;
	public final int maxPeriod;
}
