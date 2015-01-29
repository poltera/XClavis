/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.xclavis.keys;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

    public Key(SessionID sessionID) {
        this.sessionID = sessionID;
        this.id = new SimpleStringProperty(this.sessionID.getID());
        this.partner = new SimpleStringProperty("self");
        LocalDateTime now = LocalDateTime.now();
        this.date = new SimpleStringProperty(now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        this.state = new SimpleStringProperty("0");
    }

    public SessionID getSessionID() {
        return sessionID;
    }

    public String getID() {
        return sessionID.getID();
    }

    public StringProperty idProperty() {
        return id;
    }

    public String getPartner() {
        return partner.get();
    }

    public StringProperty partnerProperty() {
        return partner;
    }

    public void setPartner(String partner) {
        this.partner.set(partner);
    }

    public String getDate() {
        return date.get();
    }

    public StringProperty dateProperty() {
        return date;
    }
    
    public void setDate(String date) {
        this.date.set(date);
    }

    public String getState() {
        return state.get();
    }

    public StringProperty stateProperty() {
        return state;
    }
    
    public void setState(String state) {
        this.state.set(state);
    }

    public void changeTypeToSessionKey() {
        String type = sessionID.getType();
        switch (type) {
            case SessionID.ECDH_REQ_256:
            case SessionID.ECDH_RES_256:
                sessionID.setType(SessionID.SESSION_KEY_128);
                break;
            case SessionID.ECDH_REQ_512:
            case SessionID.ECDH_RES_512:
                sessionID.setType(SessionID.SESSION_KEY_256);
                break;
        }
    }

    public void changeTypeToECDHRes() {
        String type = sessionID.getType();
        switch (type) {
            case SessionID.ECDH_REQ_256:
                sessionID.setType(SessionID.ECDH_RES_256);
                break;
            case SessionID.ECDH_REQ_512:
                sessionID.setType(SessionID.ECDH_RES_512);
                break;
        }
    }
}
