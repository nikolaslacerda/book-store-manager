package com.server.bookstoremanager.book.builder;

import com.server.bookstoremanager.book.dto.BookRequestDTO;
import com.server.bookstoremanager.user.builder.UserBuilder;
import com.server.bookstoremanager.user.dto.UserDTO;
import lombok.Builder;

@Builder
public class BookRequestBuilder {

    @Builder.Default
    private final Long id = 1L;

    @Builder.Default
    private final String name = "Spring Boot Pro";

    @Builder.Default
    private final String isbn = "978-3-16-148410-0";

    @Builder.Default
    private final Long publisherId = 2L;

    @Builder.Default
    private final Long authorId = 3L;

    @Builder.Default
    private final Integer pages = 200;

    @Builder.Default
    private final Integer chapters = 10;

    private final UserDTO userDTO = UserBuilder.builder().build().buildUserDTO();

    public BookRequestDTO buildRequestBookDTO() {
        return new BookRequestDTO(id,
                name,
                isbn,
                pages,
                chapters,
                publisherId,
                authorId);
    }
}
