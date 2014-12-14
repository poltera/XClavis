/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.xclavis.helpers;

import ch.hsr.xclavis.commons.ECDHKey;
import ch.hsr.xclavis.commons.Keys;
import ch.hsr.xclavis.commons.SessionID;
import ch.hsr.xclavis.commons.SessionKey;
import ch.hsr.xclavis.crypto.Checksum;

/**
 *
 * @author Gian
 */
public class QRModel {

    private final static int BLOCK_LENGTH = 5;
    private final static int BLOCK_CHECKSUM = 1;
    private final static String TITLE = "XClavis";
    private final static String NEWLINE = "\n";
    //private final static String DELIMITER = "\u007c";
    private final static String DELIMITER = "-";
    private final static String UNICODE_SPACE = "\u0020";
    private final static String UNICODE_LOGO = "\ud83d\udd10";
    private final static String UNICODE_ID = "\ud83c\udd94";
    private final static String UNICODE_KEY = "\uD83D\uDD11";
    private int numKeys;
    private String model;

    /**
     * Helper Class for creating the specific QR-Model for the QR-Output.
     */
    public QRModel() {
        // Title
        model = UNICODE_LOGO + UNICODE_SPACE + TITLE + UNICODE_SPACE;
        numKeys = 0;
    }

    /**
     * Add a SessionKey to the QR-Model.
     *
     * @param sessionKey
     */
    public void addSessionKey(SessionKey sessionKey) {
        addModell(sessionKey.getID(), sessionKey.getKey());
    }

    /**
     * Add a ECDHKey to the QR-Model
     *
     * @param ecdhKey
     */
    public void addECDHKey(ECDHKey ecdhKey) {
        addModell(ecdhKey.getID(), ecdhKey.getPublicKey());
    }

    /**
     * Returns the finished QR-Model.
     *
     * @return QRModell as String
     */
    public String getModell() {
        System.out.println(model);

        return model;
    }

    private void addModell(String id, byte[] key) {
        numKeys++;
        // Info Block, TYP 1 Character, ID 3 Character, CHECKSUM 1 Character
        String infoBlock = id;
        String infoChecksum = Checksum.get(infoBlock, BLOCK_CHECKSUM);

        if (numKeys > 1) {
            model += NEWLINE;
        }
        model += UNICODE_ID + UNICODE_SPACE + infoBlock + infoChecksum + NEWLINE;

        // Key Blocks
        model += UNICODE_KEY + UNICODE_SPACE;
        String[] keyBlocks = KeySeparator.getSeparated(Base32.byteToBase32(key), BLOCK_LENGTH - BLOCK_CHECKSUM);
        String lastBlock = "";
        for (String keyBlock : keyBlocks) {
            if (keyBlock.length() == BLOCK_LENGTH - BLOCK_CHECKSUM) {
                String blockChecksum = Checksum.get(keyBlock, BLOCK_CHECKSUM);
                model += keyBlock + blockChecksum + DELIMITER;
            } else {
                lastBlock = keyBlock;
            }
        }
        int overallChecksumLength = BLOCK_LENGTH - BLOCK_CHECKSUM - lastBlock.length();
        String overallChecksum = Checksum.get(Base32.byteToBase32(key), overallChecksumLength);
        String blockChecksum = Checksum.get(lastBlock + overallChecksum, BLOCK_CHECKSUM);
        model += lastBlock + overallChecksum + blockChecksum;
    }

    public Keys getKeys(String test) {
        Keys keys = new Keys();
        String[] splittedKeys = test.split(UNICODE_ID);

        for (int i = 1; i < splittedKeys.length; i++) {
            String id = splittedKeys[i].substring(1, 1 + BLOCK_LENGTH);
            String key = splittedKeys[i].substring(10);
            String type = id.substring(0, 1);
            String random = id.substring(1, BLOCK_LENGTH - BLOCK_CHECKSUM);
            SessionID sessionID = new SessionID(type, random);

            // Remove the delemiters and checksums from the key
            String[] blocks = key.split(DELIMITER);
            key = "";
            for (String block : blocks) {
                key += block.substring(0, BLOCK_LENGTH - BLOCK_CHECKSUM);
            }
            key = key.substring(0, (sessionID.getKeyLength() + sessionID.getAddCordLength()) / Base32.SIZE + 1);

            if (sessionID.isSessionKey()) {
                SessionKey sessionKey = new SessionKey(key, sessionID);
                keys.addKey(sessionKey);

            } else if (sessionID.isECDH()) {
                ECDHKey ecdhKey = new ECDHKey(sessionID);
                keys.addKey(ecdhKey);
                SessionKey sessionKey = ecdhKey.getSessionKey(key);
                keys.addKey(sessionKey);
            }
        }

        return keys;
    }
}
