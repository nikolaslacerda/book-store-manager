package com.server.bookstoremanager.book.builder;

import com.server.bookstoremanager.author.builder.AuthorBuilder;
import com.server.bookstoremanager.author.dto.AuthorDTO;
import com.server.bookstoremanager.book.dto.BookResponseDTO;
import com.server.bookstoremanager.publisher.builder.PublisherBuilder;
import com.server.bookstoremanager.publisher.dto.PublisherDTO;
import com.server.bookstoremanager.user.builder.UserBuilder;
import com.server.bookstoremanager.user.dto.UserDTO;
import lombok.Builder;

@Builder
public class BookResponseBuilder {

    @Builder.Default
    private final Long id = 1L;

    @Builder.Default
    private final String name = "Spring Boot Pro";

    @Builder.Default
    private final String isbn = "978-3-16-148410-0";

    @Builder.Default
    private final PublisherDTO publisher = PublisherBuilder.builder().build().buildPublisherDTO();

    @Builder.Default
    private final AuthorDTO author = AuthorBuilder.builder().build().buildAuthorDTO();

    @Builder.Default
    private final Integer pages = 200;

    @Builder.Default
    private final Integer chapters = 10;

    private final UserDTO userDTO = UserBuilder.builder().build().buildUserDTO();

    public BookResponseDTO buildBookResponseDTO() {
        return new BookResponseDTO(id,
                name,
                isbn,
                pages,
                chapters,
                author,
                publisher);
    }
}
