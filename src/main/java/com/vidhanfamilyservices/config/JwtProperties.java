package com.vidhanfamilyservices.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

import static java.time.temporal.ChronoUnit.MINUTES;

@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {

    private String configLocation;

    private String alg;

    private Duration expire = Duration.of(20, MINUTES);

    public String getConfigLocation() {
        return configLocation;
    }

    public void setConfigLocation(String configLocation) {
        this.configLocation = configLocation;
    }

    public String getAlg() {
        return alg;
    }

    public void setAlg(String alg) {
        this.alg = alg;
    }

    public Duration getExpire() {
        return expire;
    }

    public void setExpire(Duration expire) {
        this.expire = expire;
    }
}
