/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
