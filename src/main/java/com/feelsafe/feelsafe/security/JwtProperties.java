package com.feelsafe.feelsafe.security;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {

    private String jwtSecret = "secretKey";
    private String jwtExpirationMs = "86400000";
    private String jwtRefreshExpirationSecond = "25920000";
}
