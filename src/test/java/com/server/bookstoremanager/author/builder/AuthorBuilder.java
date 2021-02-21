package com.server.bookstoremanager.author.builder;

import com.server.bookstoremanager.author.dto.AuthorDTO;
import lombok.Builder;

@Builder
public class AuthorBuilder {

    @Builder.Default
    private final Long id = 1L;

    @Builder.Default
    private final String name = "Nikolas Lacerda";

    @Builder.Default
    private final Integer age = 22;

    public AuthorDTO buildAuthorDTO() {
        return new AuthorDTO(id, name,age);
    }
}
