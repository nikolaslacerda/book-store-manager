package com.server.bookstoremanager.user.builder;

import com.server.bookstoremanager.user.dto.JwtRequest;
import lombok.Builder;

@Builder
public class JwtRequestBuilder {

    @Builder.Default
    private String username = "nikolas";

    @Builder.Default
    private String password = "123456";

    public JwtRequest buildJwtRequest() {
        return new JwtRequest(username, password);
    }
}
