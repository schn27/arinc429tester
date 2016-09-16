/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schn27.arinc429tester.bl;

import java.time.Instant;

/**
 *
 * @author amalikov
 */
public class TimeMarkedArinc429Word {
	public TimeMarkedArinc429Word(Instant timemark, Arinc429Word word) {
		this.timemark = timemark;
		this.word = word;
	}
	
	public final Instant timemark;
	public final Arinc429Word word;
}
