/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schn27.arinc429tester.bl;

/**
 *
 * @author amalikov
 */
public interface SequenceChangedListener {
	void onSequenceAdded(int size);
	void onSequenceCleared();
}
