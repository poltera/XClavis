/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.xclavis.crypto;

import ch.hsr.xclavis.helpers.FormatTransformer;
import ch.hsr.xclavis.helpers.KeySeparator;
import java.io.IOException;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.generators.ECKeyPairGenerator;
import org.bouncycastle.crypto.agreement.ECDHBasicAgreement;
import org.bouncycastle.crypto.agreement.ECDHCBasicAgreement;
import org.bouncycastle.crypto.params.ECDomainParameters;
import org.bouncycastle.crypto.params.ECKeyGenerationParameters;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.bouncycastle.crypto.util.PublicKeyFactory;
import org.bouncycastle.jce.ECNamedCurveTable;
import org.bouncycastle.jce.interfaces.ECPublicKey;
import org.bouncycastle.jce.spec.ECParameterSpec;
import org.bouncycastle.jce.spec.ECPublicKeySpec;
import org.bouncycastle.math.ec.ECPoint;

/**
 *
 * @author Gian
 */
public class ECDH {

    private final ECParameterSpec ecParameterSpec;
    private final ECDomainParameters ecDomainParameters;
    private AsymmetricCipherKeyPair ecKeyPair;

    public ECDH(String ellipticCurve) {
        // Init the curve
        this.ecParameterSpec = ECNamedCurveTable.getParameterSpec(ellipticCurve);
        this.ecDomainParameters = new ECDomainParameters(this.ecParameterSpec.getCurve(), this.ecParameterSpec.getG(), this.ecParameterSpec.getN(), this.ecParameterSpec.getH());
        
        // Generating a new KeyPair
        this.ecKeyPair = getKeyPair();
    }

    public byte[] getPublicKey() {
        ECPublicKeyParameters publicKeyParameters = (ECPublicKeyParameters) ecKeyPair.getPublic();
        
        return publicKeyParameters.getQ().getEncoded(true);
    }

    public byte[] getAgreedKey(byte[] remotePublicKey) {
        ECPrivateKeyParameters privateKey = (ECPrivateKeyParameters) ecKeyPair.getPrivate();
        ECPublicKeyParameters publicKey = byteToECPublicKeyParam(remotePublicKey);

        ECDHCBasicAgreement basicAgreement = new ECDHCBasicAgreement();
        basicAgreement.init(privateKey);
        byte[] agreedKey = basicAgreement.calculateAgreement(publicKey).toByteArray();

        //if first byte zero
        
        return agreedKey;
    }

    private AsymmetricCipherKeyPair getKeyPair() {
        ECKeyGenerationParameters keyGenerationParameters = new ECKeyGenerationParameters(ecDomainParameters, new SecureRandom());
        ECKeyPairGenerator keyPairGenerator = new ECKeyPairGenerator();
        keyPairGenerator.init(keyGenerationParameters);
        AsymmetricCipherKeyPair keyPair = keyPairGenerator.generateKeyPair();

        return keyPair;
    }

    private ECPublicKeyParameters byteToECPublicKeyParam(byte[] publicKey) {    
        ECPoint ecPoint = ecParameterSpec.getCurve().decodePoint(publicKey);
        ECPublicKeyParameters ecPublicKey = new ECPublicKeyParameters(ecPoint, ecDomainParameters);
        
        return ecPublicKey;
    }
}
