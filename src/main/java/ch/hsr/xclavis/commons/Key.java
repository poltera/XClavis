/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.xclavis.commons;

import javafx.beans.property.StringProperty;

/**
 *
 * @author Gian
 */
public class Key {

    private StringProperty partner;
    private StringProperty date;
    private StringProperty sessionID;
    private StringProperty state;

    public Key() {
    }
    
    public String getPartner() {
        return partner.get();
    }
    
    public String getDate() {
        return date.get();
    }
    
    public String getSessionID() {
        return null;
    }
    
    public int getState() {
        return 0;
    }
}
