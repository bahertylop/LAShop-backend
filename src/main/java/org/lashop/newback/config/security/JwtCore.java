package org.lashop.newback.config.security;


import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtCore {

    @Value("${newback.jwt.secret}")
    private String secret;

    @Value("${newback.jwt.expirationMs}")
    private int lifetime;

    public String generateToken(Authentication authentication) {
        AccountUserDetails userDetails = (AccountUserDetails) authentication.getPrincipal();
        return Jwts.builder().setSubject(userDetails.getUsername()).setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + lifetime))
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    // не рабаотает какогото хуя

    public String getNameFromJwt(String token) {
        String s = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().getSubject();
        return s;
    }
}
