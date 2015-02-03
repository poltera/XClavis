/*
 * Copyright (c) 2015, Gian Poltéra
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1.	Redistributions of source code must retain the above copyright notice,
 *   	this list of conditions and the following disclaimer.
 * 2.	Redistributions in binary form must reproduce the above copyright 
 *   	notice, this list of conditions and the following disclaimer in the 
 *   	documentation and/or other materials provided with the distribution.
 * 3.	Neither the name of HSR University of Applied Sciences Rapperswil nor 
 * 	the names of its contributors may be used to endorse or promote products
 * 	derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package ch.hsr.xclavis.crypto;

import ch.hsr.xclavis.helpers.Base32;
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

        //System.out.println("Test1: " + Base32.byteToBitString(ecPublicKeyParameters.getQ().getEncoded(false)));
        //System.out.println("Test2: " + Base32.byteToBitString(ecPublicKeyParameters.getQ().getEncoded(true)));
        
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
