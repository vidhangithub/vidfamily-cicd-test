package com.vidhanfamilyservices.config;

import com.nimbusds.jose.JWSAlgorithm;
import com.vidhanfamilyservices.jwthelper.JwtHelper;
import com.vidhanfamilyservices.jwthelper.JwtSignerImpl;
import com.vidhanfamilyservices.keys.AllKeys;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class JWTConfig {

    private final JwtProperties jwtProperties;

    public JWTConfig(final JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    }

    @Bean
    @Scope("prototype")
    JwtHelper jwtHelper() {
        return  new JwtHelper(jwtProperties.getConfigLocation());
    }

    @Bean
    public AllKeys allKeys(final ObjectFactory<JwtHelper> jwtHelperObjectFactory) {
        final AllKeys allKeys = new AllKeys(jwtHelperObjectFactory);
        return allKeys;
    }

    @Bean
    public JwtSignerImpl jwtSigner(final AllKeys allKeys) {
        return new JwtSignerImpl(JWSAlgorithm.parse(jwtProperties.getAlg()), allKeys);
    }
}
