package com.vidhanfamilyservices.keys;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.vidhanfamilyservices.jwthelper.JwtHelper;
import jakarta.annotation.PostConstruct;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.servlet.JwkSetUriJwtDecoderBuilderCustomizer;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

public class AllKeys {

    private JWKSet jwkSet;

    private String signKey;

    private ObjectFactory<JwtHelper> jwtHelperObjectFactory;

    @Autowired
    public AllKeys(final ObjectFactory<JwtHelper> jwtHelperObjectFactory) {
        this.jwtHelperObjectFactory = jwtHelperObjectFactory;
    }

    @PostConstruct
    public void init() {
        refresh();
    }

    public String getSignKid() {
        return this.signKey;
    }

    public RSASSASigner getSigner() throws JOSEException {
        final JWK keyByKeyId = jwkSet.getKeyByKeyId(signKey);
        return new RSASSASigner((RSAKey) keyByKeyId);
    }
    public Map<String, Object> toJsonObject() {
        return jwkSet.toJSONObject();
    }

    public void refresh() {
        final JwtHelper helper = jwtHelperObjectFactory.getObject();
        final String newAlias = helper.getAlias();
        if (!StringUtils.equals(newAlias, this.signKey)) {
            final JWK newKey = helper.getJwk();
            if (this.signKey == null) {
                System.out.println("Loading Key for the first time " + newAlias);
                this.jwkSet = new JWKSet(newKey);
            } else {
                final JWK oldKey = this.jwkSet.getKeyByKeyId(this.signKey);
                this.jwkSet = new JWKSet(List.of(oldKey, newKey));
            }
            this.signKey = newAlias;
            System.out.println("Key refresh completed");
        }
         else {
            System.out.println("key alias " + newAlias + " already loaded");
        }
    }
}
