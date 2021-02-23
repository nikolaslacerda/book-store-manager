package com.server.bookstoremanager.publisher.builder;

import com.server.bookstoremanager.publisher.dto.PublisherDTO;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public class PublisherBuilder {

    @Builder.Default
    private final Long id = 1L;

    @Builder.Default
    private final String name = "Publisher";

    @Builder.Default
    private final String code = "1234";

    @Builder.Default
    private final LocalDate foundationDate = LocalDate.of(2020, 6, 1);

    public PublisherDTO buildPublisherDTO() {
        return new PublisherDTO(id, name, code, foundationDate);
    }

}
