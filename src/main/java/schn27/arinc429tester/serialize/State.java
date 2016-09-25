/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schn27.arinc429tester.serialize;

import schn27.arinc429tester.Sequence;

/**
 *
 * @author AVIA
 */
public class State {
	public State(Sequence sequence, Config config) {
		this.sequence = sequence;
		this.config = config;
	}
	
	public Sequence sequence;
	public Config config;
}
