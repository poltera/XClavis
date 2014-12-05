/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.xclavis.crypto;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.spec.InvalidKeySpecException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.KeyAgreement;
import javax.crypto.SecretKey;


import org.bouncycastle.asn1.nist.NISTObjectIdentifiers;
import org.bouncycastle.jce.ECNamedCurveTable;
import org.bouncycastle.jce.interfaces.ECPublicKey;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.jce.spec.ECParameterSpec;
import org.bouncycastle.math.ec.ECPoint;
import org.bouncycastle.jce.spec.ECPublicKeySpec;

/**
 *
 * @author Gian
 */
public class ECDH {

    private ECParameterSpec ecSpec;

    public ECDH(String ec) {
        Security.addProvider(new BouncyCastleProvider());
        this.ecSpec = ECNamedCurveTable.getParameterSpec(ec);
    }

    public KeyPair getKeyPair() {
        KeyPair keyPair = null;
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("ECDH", "BC");
            keyPairGenerator.initialize(ecSpec, new SecureRandom());
            keyPair = keyPairGenerator.generateKeyPair();
        } catch (NoSuchAlgorithmException | NoSuchProviderException | InvalidAlgorithmParameterException ex) {
            Logger.getLogger(ECDH.class.getName()).log(Level.SEVERE, null, ex);
        }

        return keyPair;
    }

    public ECParameterSpec getECParamaeterSpec() {
        return ecSpec;
    }

    public SecretKey getSessionKey(PrivateKey privateKey, PublicKey publicKey) {
        SecretKey secretKey = null;
        try {
            KeyAgreement keyAgree = KeyAgreement.getInstance("ECDH", "BC");
            keyAgree.init(privateKey);
            keyAgree.doPhase(publicKey, true);
            secretKey = keyAgree.generateSecret(NISTObjectIdentifiers.id_aes128_GCM.getId());

        } catch (NoSuchAlgorithmException | NoSuchProviderException | InvalidKeyException ex) {
            Logger.getLogger(ECDH.class.getName()).log(Level.SEVERE, null, ex);
        }

        return secretKey;
    }

    public ECPublicKey rawdataToPublicKey(byte[] publicKey) {
        ECPublicKey remotePublicKey = null;
        try {
            ECPoint point = ecSpec.getCurve().decodePoint(publicKey);
            ECPublicKeySpec publicKeySpec = new ECPublicKeySpec(point, ecSpec);
            remotePublicKey = (ECPublicKey) KeyFactory.getInstance("ECDH", "BC").generatePublic(publicKeySpec);
        } catch (NoSuchAlgorithmException | NoSuchProviderException | InvalidKeySpecException ex) {
            Logger.getLogger(ECDH.class.getName()).log(Level.SEVERE, null, ex);
        }
        return remotePublicKey;
    }
}