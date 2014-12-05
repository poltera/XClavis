/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.xclavis.ui;

import javafx.scene.control.TextField;

/**
 *
 * @author Gian
 */
public class InputBlock extends TextField {

    int maxLength = 5;
    String pattern = "[23456789abcdefghjklmnpqrstuvwxyzABCDEFGHJKLMNPQRSTUVWXYZ]";

    public InputBlock() {
        super();
    }

    @Override
    public void replaceText(int start, int end, String text) {
        // Backspace or Delete
        if (text.equals("")) {
            super.replaceText(start, end, text);
        }
        // Pattern & Length
        if (text.matches(pattern) && end < maxLength && getText().length() < maxLength) {
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
        if (getText().length() + text.length() <= maxLength) {
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
}
