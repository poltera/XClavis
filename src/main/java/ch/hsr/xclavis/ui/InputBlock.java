/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.xclavis.ui;

import ch.hsr.xclavis.crypto.Checksum;
import java.awt.Toolkit;
import javafx.scene.control.TextField;

/**
 *
 * @author Gian
 */
public class InputBlock extends TextField {

    private final int blockLength;
    private final int checksumLength;
    private final String pattern;

    public InputBlock(int blockLength, int checksumLength, String pattern) {
        super();
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
    
    public String getChecksum() {
        String checksum = "";
        if (isComplete()) {
            checksum = super.getText().substring(blockLength - checksumLength);
        }

        return checksum;
    }

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
        super.setStyle("-fx-border-color: white");
        super.setDisable(false);
    }

    private void markCorrect() {
        super.setStyle("-fx-border-color: white");
        super.setDisable(true);
    }

    private void markIncorrect() {
        super.setStyle("-fx-border-color: red");
        super.setDisable(false);
        Toolkit.getDefaultToolkit().beep();
    }
}
