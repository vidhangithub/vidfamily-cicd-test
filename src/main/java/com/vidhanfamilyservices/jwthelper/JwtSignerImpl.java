package com.vidhanfamilyservices.jwthelper;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.vidhanfamilyservices.keys.AllKeys;

public class JwtSignerImpl {

    private final JWSAlgorithm algorithm;

    private final AllKeys allKeys;

    public JwtSignerImpl(JWSAlgorithm algorithm, AllKeys allKeys) {
        this.algorithm = algorithm;
        this.allKeys = allKeys;
    }

    public String generateToken(final JWTClaimsSet jwtClaimsSet) throws JOSEException {
         final JWSHeader header = new JWSHeader.Builder(algorithm).keyID(allKeys.getSignKid()).build();
         final SignedJWT signedJWT = new SignedJWT(header, jwtClaimsSet);
         signedJWT.sign(allKeys.getSigner());
         return signedJWT.serialize();
    }
}
