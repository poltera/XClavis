/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.xclavis.crypto;

import java.math.BigInteger;
import java.security.SecureRandom;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.generators.ECKeyPairGenerator;
import org.bouncycastle.crypto.agreement.ECDHCBasicAgreement;
import org.bouncycastle.crypto.params.ECDomainParameters;
import org.bouncycastle.crypto.params.ECKeyGenerationParameters;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.bouncycastle.jce.ECNamedCurveTable;
import org.bouncycastle.jce.spec.ECParameterSpec;
import org.bouncycastle.math.ec.ECPoint;

/**
 *
 * @author Gian
 */
public class ECDH {

    private final ECParameterSpec ecParameterSpec;
    private final ECDomainParameters ecDomainParameters;
    private ECPrivateKeyParameters ecPrivateKeyParameters;
    private ECPublicKeyParameters ecPublicKeyParameters;

    /**
     * ECDH with new KeyPair
     * 
     * @param ellipticCurve
     */
    public ECDH(String ellipticCurve) {
        // Init the curve
        this.ecParameterSpec = ECNamedCurveTable.getParameterSpec(ellipticCurve);
        this.ecDomainParameters = new ECDomainParameters(this.ecParameterSpec.getCurve(), this.ecParameterSpec.getG(), this.ecParameterSpec.getN(), this.ecParameterSpec.getH());

        // Generating a new KeyPair
        generateKeyPair();
    }

    /**
     * ECDH with existing Private and PublicKey
     * 
     * @param ellipticCurve
     * @param privateKey
     * @param publicKey
     */
    public ECDH(String ellipticCurve, byte[] privateKey, byte[] publicKey) {
        // Init the curve
        this.ecParameterSpec = ECNamedCurveTable.getParameterSpec(ellipticCurve);
        this.ecDomainParameters = new ECDomainParameters(this.ecParameterSpec.getCurve(), this.ecParameterSpec.getG(), this.ecParameterSpec.getN(), this.ecParameterSpec.getH());

        // Set the KeyPair
        this.ecPrivateKeyParameters = byteToECPrivateKeyParam(privateKey);
        this.ecPublicKeyParameters = byteToECPublicKeyParam(publicKey);
    }

    public byte[] getPrivateKey() {

        return ecPrivateKeyParameters.getD().toByteArray();
    }

    public byte[] getPublicKey() {

        return ecPublicKeyParameters.getQ().getEncoded(true);
    }

    public byte[] getAgreedKey(byte[] publicKey) {
        ECPublicKeyParameters remotePublicKey = byteToECPublicKeyParam(publicKey);

        ECDHCBasicAgreement basicAgreement = new ECDHCBasicAgreement();
        basicAgreement.init(ecPrivateKeyParameters);
        byte[] agreedKey = basicAgreement.calculateAgreement(remotePublicKey).toByteArray();

        return agreedKey;
    }

    private void generateKeyPair() {
        ECKeyGenerationParameters keyGenerationParameters = new ECKeyGenerationParameters(ecDomainParameters, new SecureRandom());
        ECKeyPairGenerator keyPairGenerator = new ECKeyPairGenerator();
        keyPairGenerator.init(keyGenerationParameters);
        AsymmetricCipherKeyPair keyPair = keyPairGenerator.generateKeyPair();
        
        ecPrivateKeyParameters = (ECPrivateKeyParameters) keyPair.getPrivate();
        ecPublicKeyParameters = (ECPublicKeyParameters) keyPair.getPublic();
        
        // Check if size of the PrivateKey is correct, 25% is the Length to long
        if (ecPrivateKeyParameters.getD().toByteArray().length != ecParameterSpec.getCurve().getFieldSize() / Byte.SIZE) {
            generateKeyPair();
        }
    }

    private ECPublicKeyParameters byteToECPublicKeyParam(byte[] publicKey) {
        ECPoint ecPoint = ecParameterSpec.getCurve().decodePoint(publicKey);
        ECPublicKeyParameters ecPublicKey = new ECPublicKeyParameters(ecPoint, ecDomainParameters);

        return ecPublicKey;
    }
    
    private ECPrivateKeyParameters byteToECPrivateKeyParam(byte[] privateKey) {
        BigInteger d = new BigInteger(1, privateKey);
        ECPrivateKeyParameters ecPrivateKey = new ECPrivateKeyParameters(d, ecDomainParameters);
        
        return ecPrivateKey;
    }
}
