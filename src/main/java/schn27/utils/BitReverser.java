/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schn27.utils;

/**
 *
 * @author amalikov
 */
public final class BitReverser {
	public static final byte reverse(byte x) {
		x = (byte)(((x >> 1) & 0x55) | ((x & 0x55) << 1));
		x = (byte)(((x >> 2) & 0x33) | ((x & 0x33) << 2));
		x = (byte)(((x >> 4) & 0x0F) | ((x & 0x0F) << 4));
		return x;
	}
	
	public static final short reverse(short x) {
		x = (short)(((x >> 1) & 0x5555) | ((x & 0x5555) << 1));
		x = (short)(((x >> 2) & 0x3333) | ((x & 0x3333) << 2));
		x = (short)(((x >> 4) & 0x0F0F) | ((x & 0x0F0F) << 4));
		x = (short)(((x >> 8) & 0x00FF) | ((x & 0x00FF) << 8));
		return x;
	}
	
	public static final int reverse(int x) {
		x = (((x >> 1)  & 0x55555555) | ((x & 0x55555555) << 1));
		x = (((x >> 2)  & 0x33333333) | ((x & 0x33333333) << 2));
		x = (((x >> 4)  & 0x0F0F0F0F) | ((x & 0x0F0F0F0F) << 4));
		x = (((x >> 8)  & 0x00FF00FF) | ((x & 0x00FF00FF) << 8));
		x = (((x >> 16) & 0x0000FFFF) | ((x & 0x0000FFFF) << 16));
		return x;
	}	
}	

