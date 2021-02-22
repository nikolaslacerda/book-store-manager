package com.server.bookstoremanager.author.service;


import com.server.bookstoremanager.author.builder.AuthorBuilder;
import com.server.bookstoremanager.author.dto.AuthorDTO;
import com.server.bookstoremanager.author.entity.Author;
import com.server.bookstoremanager.author.exception.AuthorAlreadyExistsException;
import com.server.bookstoremanager.author.exception.AuthorNotFoundException;
import com.server.bookstoremanager.author.mapper.AuthorMapper;
import com.server.bookstoremanager.author.repository.AuthorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;
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

    @Test
    void whenValidIdIsGivenThenAnAuthorShouldBeReturned() {
        //given
        AuthorDTO expectedFoundAuthorDTO = authorBuilder.buildAuthorDTO();
        Author expectedFoundAuthor = authorMapper.toModel(expectedFoundAuthorDTO);

        //when
        when(authorRepository.findById(expectedFoundAuthor.getId())).thenReturn(Optional.of(expectedFoundAuthor));
        AuthorDTO foundAuthorDTO = authorService.findById(expectedFoundAuthorDTO.getId());

        //then
        assertThat(foundAuthorDTO.getId(), is(equalTo(expectedFoundAuthorDTO.getId())));
    }

    @Test
    void whenInvalidIdIsGivenThenAnExceptionShouldBeThrown() {
        //given
        AuthorDTO expectedFoundAuthorDTO = authorBuilder.buildAuthorDTO();

        //when
        when(authorRepository.findById(expectedFoundAuthorDTO.getId())).thenReturn(Optional.empty());

        //then
        assertThrows(AuthorNotFoundException.class, () -> authorService.findById(expectedFoundAuthorDTO.getId()));
    }

    @Test
    void whenListAuthorsIsCalledThenItShouldBeReturned() {
        //given
        AuthorDTO expectedFoundAuthorDTO = authorBuilder.buildAuthorDTO();
        Author expectedFoundAuthor = authorMapper.toModel(expectedFoundAuthorDTO);

        //when
        when(authorRepository.findAll()).thenReturn(Collections.singletonList(expectedFoundAuthor));
        List<AuthorDTO> foundAuthorsDTO = authorService.findAll();

        //then
        assertThat(foundAuthorsDTO.size(), is(1));
        assertThat(foundAuthorsDTO.get(0), is(equalTo(expectedFoundAuthorDTO)));
    }

    @Test
    void whenListAuthorsIsCalledThenEmptyListShouldBeReturned() {
        //when
        when(authorRepository.findAll()).thenReturn(Collections.EMPTY_LIST);
        List<AuthorDTO> foundAuthorsDTO = authorService.findAll();

        //then
        assertThat(foundAuthorsDTO.size(), is(0));
    }
}
