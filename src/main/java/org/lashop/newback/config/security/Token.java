package org.lashop.newback.config.security;

import lombok.Data;

@Data
public class Token {

    private final String token;

    public Token(String token) {
        this.token = token;
    }
}
