package com.server.bookstoremanager.author.service;


import com.server.bookstoremanager.author.builder.AuthorBuilder;
import com.server.bookstoremanager.author.dto.AuthorDTO;
import com.server.bookstoremanager.author.entity.Author;
import com.server.bookstoremanager.author.exception.AuthorAlreadyExistsException;
import com.server.bookstoremanager.author.mapper.AuthorMapper;
import com.server.bookstoremanager.author.repository.AuthorRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthorServiceTest {

    private final AuthorMapper authorMapper = AuthorMapper.INSTANCE;

    @Mock
    private AuthorRepository authorRepository;

    @InjectMocks
    private AuthorService authorService;

    private AuthorBuilder authorBuilder;

    @BeforeEach
    void setup() {
        authorBuilder = AuthorBuilder.builder().build();
    }

    @Test
    void whenNewAuthorIsInformedThenItShouldBeCreated() {
        //given
        AuthorDTO expectedAuthorToCreatedDTO = authorBuilder.buildAuthorDTO();
        Author expectedCreatedAuthor = authorMapper.toModel(expectedAuthorToCreatedDTO);

        //when
        when(authorRepository.save(expectedCreatedAuthor)).thenReturn(expectedCreatedAuthor);
        when(authorRepository.findByName(expectedCreatedAuthor.getName())).thenReturn(Optional.empty());

        AuthorDTO createdAuthorDTO = authorService.create(expectedAuthorToCreatedDTO);

        //then
        assertThat(createdAuthorDTO, is(equalTo(expectedAuthorToCreatedDTO)));
    }

    @Test
    void whenExistingAuthorIsInformedThenAnExceptionShouldBeThrown() {
        //given
        AuthorDTO expectedAuthorToCreatedDTO = authorBuilder.buildAuthorDTO();
        Author expectedCreatedAuthor = authorMapper.toModel(expectedAuthorToCreatedDTO);

        //when
        when(authorRepository.findByName(expectedCreatedAuthor.getName())).thenReturn(Optional.of(expectedCreatedAuthor));

        //then
        assertThrows(AuthorAlreadyExistsException.class, () -> authorService.create(expectedAuthorToCreatedDTO));
    }
}
