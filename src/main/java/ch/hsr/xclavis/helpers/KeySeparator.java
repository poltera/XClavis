/*
 * Copyright (c) 2015, Gian Poltéra
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1.	Redistributions of source code must retain the above copyright notice,
 *   	this list of conditions and the following disclaimer.
 * 2.	Redistributions in binary form must reproduce the above copyright 
 *   	notice, this list of conditions and the following disclaimer in the 
 *   	documentation and/or other materials provided with the distribution.
 * 3.	Neither the name of HSR University of Applied Sciences Rapperswil nor 
 * 	the names of its contributors may be used to endorse or promote products
 * 	derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package ch.hsr.xclavis.helpers;

/**
 * This class provides the methods to separate and put together a string.
 * 
 * @author Gian Poltéra
 */
public class KeySeparator {
		
    /**
     * Gets a separate string-array of a string.
     * 
     * @param string the string to separate
     * @param length the length of each block
     * @return the separated string as a string-array
     */
    public static String[] getSeparated(String string, int length) {
		//Specifically worth
		double blockscalc = (double) string.length() / (double) length;
		//Round up
		int blocksnumber = (int) StrictMath.ceil(blockscalc);
		
		String[] blocks = new String[blocksnumber];
		
		for (int i = 0; i < blocksnumber; i++) {
			int start = i * length;
			int end = i * length + length;
			
			if (end < string.length()) {
				blocks[i] = string.substring(start, end);
			} else {
				blocks[i] = string.substring(start);
			}
		}
		
		return blocks;
	}
	
    /**
     * Gets the composited string of a string-array.
     * 
     * @param blocks the separated blocks
     * @return the composited value as a string
     */
    public static String putTogether(String[] blocks) {		
		String string = "";
		
		for (String block : blocks) {
			string += block;
		}
		
		return string;
	}
}

