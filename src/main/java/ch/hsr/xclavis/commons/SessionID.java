/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.xclavis.commons;

import ch.hsr.xclavis.crypto.RandomGenerator;
import ch.hsr.xclavis.helpers.FormatTransformer;

/**
 *
 * @author Gian
 */
public class SessionID {
    String type;
    String random;
    
    public SessionID(String type, int length) {
        this.type = type;
        this.random = FormatTransformer.bitStringToBase32(RandomGenerator.getRandomBits(length));
        //CHECK IF EXISTS TBA
    }
    
    public String getID() {
        return type + random;
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
}
