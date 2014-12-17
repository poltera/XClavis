/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.xclavis.commons;

import java.time.LocalDateTime;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Gian
 */
public class Key {

    private SessionID sessionID;
    private StringProperty partner;
    private StringProperty date;
    private StringProperty id;
    private StringProperty state;

    /**
     *
     * @param sessionID
     * @param partner
     * @param state
     */
    public Key(SessionID sessionID, String partner, int state) {
        this.sessionID = sessionID;
        this.id = new SimpleStringProperty(sessionID.getID());
        this.partner = new SimpleStringProperty(partner);
        LocalDateTime now = LocalDateTime.now();
        this.date = new SimpleStringProperty(now.getYear() + "-" + now.getMonthValue() + "-" + now.getDayOfMonth() + " " + now.getHour() + ":" + now.getMinute());
        this.state = new SimpleStringProperty("Bereit");
    }
    
    public String getPartner() {
        return partner.get();
    }
    
    public StringProperty partnerProperty() {
        return partner;
    }
    
    public String getDate() {
        return date.get();
    }
    
    public StringProperty dateProperty() {
        return date;
    }
    
    public SessionID getSessionID() {
        return sessionID;
    }
    
    public StringProperty idProperty() {
        //return sessionID.
        return id;
    }
    
    public int getState() {
        return 0;
    }
    
    public StringProperty stateProperty() {
        return state;
    }
}
