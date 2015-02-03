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
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Gian
 */
public class InputBlocks {

    private List<InputBlock> inputBlocks;
    private final int numBlocks;
    private final int checksumSize;

    public InputBlocks(int numBlocks, int checksumSize) {
        this.inputBlocks = new ArrayList<>();
        this.numBlocks = numBlocks;
        this.checksumSize = checksumSize;
    }

    public void addBlock(InputBlock inputBlock) {
        inputBlocks.add(inputBlock);
    }

    public boolean areValid() {
        if (areComplete()) {
            if (Checksum.verify(getValue(), getChecksum())) {
                markCorrect();
                return true;
            } else {
                markIncorrect();
            }
        }
        
        return false;
    }

    public String getValue() {
        String value = "";

        if (areComplete()) {
            value = inputBlocks.stream().map((inputBlock) -> inputBlock.getValue()).reduce(value, String::concat);
            value = value.substring(0, value.length() - checksumSize);
        }

        return value;
    }

    public String getChecksum() {
        String checksum = "";

        if (areComplete()) {
            InputBlock lastInputBlock = inputBlocks.get(inputBlocks.size() - 1);
            checksum = lastInputBlock.getValue().substring(lastInputBlock.getValue().length() - checksumSize);
        }

        return checksum;
    }

    private boolean areComplete() {
        if (inputBlocks.size() == numBlocks) {
            for (InputBlock inputBlock : inputBlocks) {
                if (!inputBlock.isValid()) {
                    return false;
                }
            }
            return true;
        }

        return false;
    }

    private void markIncorrect() {
        inputBlocks.stream().forEach((inputBlock) -> {
            inputBlock.setStyle("-fx-border-color: red");
            inputBlock.setDisable(false);
            Toolkit.getDefaultToolkit().beep();
        });
    }

    private void markCorrect() {
        inputBlocks.forEach((inputBlock) -> {
            inputBlock.setStyle("-fx-border-color: green");
            inputBlock.setDisable(true);
        });
    }
}
