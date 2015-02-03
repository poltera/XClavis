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
package ch.hsr.xclavis.commons;

import ch.hsr.xclavis.crypto.Checksum;
import java.awt.Toolkit;
import javafx.geometry.Pos;
import javafx.scene.control.TextField;

/**
 * The class is used to create input block's for the manual input of a key.
 * 
 * @author Gian Poltéra
 */
public class InputBlock extends TextField {

    private final int blockLength;
    private final int checksumLength;
    private final String pattern;

    public InputBlock(int blockLength, int checksumLength, String pattern) {
        super();
        super.setAlignment(Pos.CENTER);
        super.setMaxWidth(blockLength * 11.56);
        //super.setWidth(blockLength);
        this.blockLength = blockLength;
        this.pattern = pattern;
        this.checksumLength = checksumLength;
    }

    @Override
    public void replaceText(int start, int end, String text) {
        // Backspace or Delete
        if (text.equals("")) {
            super.replaceText(start, end, text);
        }
        // Pattern & Length
        if (text.matches(pattern) && end < blockLength && getText().length() < blockLength) {
            super.replaceText(start, end, text.toUpperCase());
            // Replace
        } else if (text.matches(pattern) && start != end) {
            super.replaceText(start, end, text.toUpperCase());
        }
    }

    @Override
    public void replaceSelection(String text) {
        // Backspace or Delete
        if (text.equals("")) {
            super.replaceSelection(text);
        }
        // Length
        if (getText().length() + text.length() <= blockLength) {
            boolean patternResult = false;
            // Check each Symbol in the String with the pattern
            for (char symbol : text.toCharArray()) {
                if (String.valueOf(symbol).matches(pattern)) {
                    patternResult = true;
                } else {
                    patternResult = false;
                    break;
                }
            }
            // Pattern
            if (patternResult) {
                super.replaceSelection(text.toUpperCase());
            }
        }
    }

    /**
     * Check if the block is valid.
     * 
     * @return true if the block is valid and false otherwise
     */
    public boolean isValid() {
        if (isComplete()) {
            if (Checksum.verify(getValue(), getChecksum())) {
                markCorrect();
                return true;
            } else {
                markIncorrect();
            }
        } else {
            markEditable();
        }
        
        return false;
    }
    
    /**
     * Gets the checksum of a block.
     * 
     * @return the checksum as a String
     */
    public String getChecksum() {
        String checksum = "";
        if (isComplete()) {
            checksum = super.getText().substring(blockLength - checksumLength);
        }

        return checksum;
    }

    /**
     * Gets the value of a block.
     * 
     * @return the value as a String
     */
    public String getValue() {
        String value = "";
        if (isComplete()) {
            value = super.getText().substring(0, blockLength - checksumLength);
        }

        return value;
    }
    
    private boolean isComplete() {
        if (super.getText().length() == blockLength) {
            return true;
        } 
        
        return false;
    }

    private void markEditable() {
        super.setStyle("-fx-border-color: none");
        super.setDisable(false);
    }

    private void markCorrect() {
        super.setStyle("-fx-border-color: none");
        super.setDisable(true);
    }

    private void markIncorrect() {
        super.setStyle("-fx-border-color: red");
        super.setDisable(false);
        Toolkit.getDefaultToolkit().beep();
    }
}
