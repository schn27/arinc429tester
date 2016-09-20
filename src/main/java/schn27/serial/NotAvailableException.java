/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schn27.serial;

/**
 *
 * @author amalikov
 */
public class NotAvailableException extends Exception {
	public NotAvailableException() {}
	
	public NotAvailableException(String message) {
		super(message);
	}
}
