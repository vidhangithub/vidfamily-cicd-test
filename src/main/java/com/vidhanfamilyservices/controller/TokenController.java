package com.vidhanfamilyservices.controller;


import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.JWTClaimsSet;
import com.vidhanfamilyservices.config.JwtProperties;
import com.vidhanfamilyservices.jwthelper.JwtSignerImpl;
import com.vidhanfamilyservices.keys.AllKeys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.util.*;

@RestController
@RequestMapping("vidhanapi/v1.0")
public class TokenController {

    private static final String SECRET_KEY = "vidhan_chandra"; // Change this to a secure secret key
    private static final String keystorePassword = "chandra";
    private static final String keyAlias = "chandra";
    private static final String keyPassword = "chandra";
    private static final String keystoreFileName = "chandra-keystore.jks";

    private final AllKeys allKeys;

    @Autowired
    public TokenController(final AllKeys allKeys) {
        this.allKeys = allKeys;
    }
    @Autowired
    private JwtProperties jwtProperties;

    @Autowired
    private JwtSignerImpl jwtSigner;

    // JWKS endpoint
    @GetMapping("/jwks")
    public Map<String, Object> getPublicKeys() {
       /* Map<String, Object> jwks = new HashMap<>();
        jwks.put("keys", Collections.singletonList(generateJwk()));*/
        return allKeys.toJsonObject();
    }

    private Map<String, Object> generateJwk() {
        // In a real-world scenario, use a proper key pair
        Map<String, Object> jwk = new HashMap<>();
        jwk.put("kty", "RSA");
        jwk.put("kid", "mock_key_id");
        jwk.put("alg", "RS256");
        jwk.put("use", "sig");
        jwk.put("n", "mock_modulus");
        jwk.put("e", "AQAB");
        return jwk;
    }

    @PostMapping("/token")
    public Map<String, String> token(@RequestParam("code") String authorizationCode,
                                     @RequestParam("code_verifier") String codeVerifier,
                                     @RequestParam("grant_type") String grantType,
                                     @RequestParam("client_id") String clientId) throws IOException, JOSEException {

        // Validate authorization code and code verifier
        // For simplicity, skip validation in this example

        // Generate JWT token with custom claims
        String jwtToken = generateJwtToken();

        // Return a valid access token response
        Map<String, String> response = new HashMap<>();
        response.put("access_token", jwtToken);
        response.put("token_type", "Bearer");
        response.put("expires_in", "3600");

        return response;
    }

    private String generateJwtToken() throws IOException, JOSEException {
       /* ClassPathResource classPathResource = new ClassPathResource(keystoreFileName);
        String keystorePath = classPathResource.getFile().getAbsolutePath();
        // Mocking claims (replace with actual user data)
        Map<String, Object> claims = new HashMap<>();
        claims.put("customer_id", "123");
        claims.put("name", "John Doe");
        claims.put("age", 30);
        Map<String, Object> headers = new HashMap<>();
        headers.put("kid","mock_key_id");
        headers.put("issuer","vid-issuer");*/

        // Generate JWT token with claims
        /*return Jwts.builder()
                .setHeader(headers)
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 3600000)) // 1 hour expiration
                .signWith(SignatureAlgorithm.RS256, generatePrivateKey(keystorePath, keystorePassword, keyAlias, keyPassword))
                .compact();*/

        final Date now = new Date();
        final Date expireDate = new Date(System.currentTimeMillis() + jwtProperties.getExpire().toMillis());
        final JWTClaimsSet.Builder builder = new JWTClaimsSet.Builder()
                .issuer("vidhan:company")
                .subject("vidhan")
                .notBeforeTime(now)
                .issueTime(now)
                .expirationTime(expireDate)
                .jwtID(UUID.randomUUID().toString())
                .claim("company","vidhan-tech-world")
                .claim("Age","39")
                .claim("Sex","Male");

        return jwtSigner.generateToken(builder.build());
    }

    private PrivateKey generatePrivateKey(String keystorePath, String keystorePassword, String keyAlias, String keyPassword) {
        // In a real-world scenario, retrieve the private key from a secure storage
        // For simplicity, this example uses an empty private key
        try {
            // Load the keystore
            FileInputStream fis = new FileInputStream(keystorePath);
            KeyStore keystore = KeyStore.getInstance("JKS");
            keystore.load(fis, keystorePassword.toCharArray());
            fis.close();

            // Get the private key and certificate chain from the keystore
            KeyStore.PasswordProtection keyPasswordProtection = new KeyStore.PasswordProtection(keyPassword.toCharArray());
            KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry) keystore.getEntry(keyAlias, keyPasswordProtection);

            // Return the private key
            return privateKeyEntry.getPrivateKey();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
