package com.vidhanfamilyservices.jwthelper;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyStore;
import java.util.Properties;

public class JwtHelper {

    private final String configLocation;
    private final Properties properties;


    public JwtHelper(final String configLocation) {
        this.configLocation = configLocation;
        this.properties = loadJwtPropertiesFile(configLocation + File.separator + "jwt.properties");
    }

    public String getAlias() {
        return properties.getProperty("alias");
    }

    public JWK getJwk() {
        final String jksFile = configLocation + File.separator + properties.getProperty("file");
        System.out.println("Loading JKS file:: "+jksFile);
        final String storePass = properties.getProperty("storepass");
        final String keyPass = properties.getProperty("keypass");
        return loadKeyStore(jksFile, storePass, getAlias(), keyPass);
    }

    private Properties loadJwtPropertiesFile(final String propertiesFile) {
        final Properties jwtProperties = new Properties();
        final File pf = new File(propertiesFile);
        try(FileInputStream inputStream = new FileInputStream(pf)) {
            System.out.println("Loading properties from-->"+pf.getAbsolutePath());
            jwtProperties.load(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jwtProperties;
    }

    private JWK loadKeyStore(final String ksFile, final String storePassWord, final String keyAlias, final String keyPassword) {

        try(final InputStream inputStream = new FileSystemResource(ksFile).getInputStream()) {
            final KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(inputStream, storePassWord.toCharArray());
            final JWKSet jwkSet = JWKSet.load(keyStore, alias -> keyPassword.toCharArray());
            return jwkSet.getKeyByKeyId(keyAlias);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  null;
    }
}
