package com.server.bookstoremanager.book.service;

import com.server.bookstoremanager.author.entity.Author;
import com.server.bookstoremanager.author.service.AuthorService;
import com.server.bookstoremanager.book.builder.BookRequestBuilder;
import com.server.bookstoremanager.book.builder.BookResponseBuilder;
import com.server.bookstoremanager.book.dto.BookRequestDTO;
import com.server.bookstoremanager.book.dto.BookResponseDTO;
import com.server.bookstoremanager.book.entity.Book;
import com.server.bookstoremanager.book.exception.BookAlreadyExistsException;
import com.server.bookstoremanager.book.mapper.BookMapper;
import com.server.bookstoremanager.book.repository.BookRepository;
import com.server.bookstoremanager.publisher.entity.Publisher;
import com.server.bookstoremanager.publisher.service.PublisherService;
import com.server.bookstoremanager.user.dto.AuthenticatedUser;
import com.server.bookstoremanager.user.entity.User;
import com.server.bookstoremanager.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {

    private final BookMapper bookMapper = BookMapper.INSTANCE;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private UserService userService;

    @Mock
    private AuthorService authorService;

    @Mock
    private PublisherService publisherService;

    @InjectMocks
    private BookService bookService;

    private BookRequestBuilder bookRequestDTOBuilder;

    private BookResponseBuilder bookResponseDTOBuilder;

    private AuthenticatedUser authenticatedUser;

    @BeforeEach
    void setUp() {
        bookRequestDTOBuilder = BookRequestBuilder.builder().build();
        bookResponseDTOBuilder = BookResponseBuilder.builder().build();
        authenticatedUser = new AuthenticatedUser("nikolas", "123456", "ADMIN");
    }

    @Test
    void whenNewBookIsInformedThenItShouldBeCreated() {
        //given
        BookRequestDTO expectedBookToCreateDTO = bookRequestDTOBuilder.buildRequestBookDTO();
        BookResponseDTO expectedCreatedBookDTO = bookResponseDTOBuilder.buildBookResponseDTO();
        Book expectedCreatedBook = bookMapper.toModel(expectedCreatedBookDTO);

        //when
        when(userService.verifyAndGetUserIfExists(authenticatedUser.getUsername())).thenReturn(new User());
        when(bookRepository.findByNameAndIsbnAndUser(
                eq(expectedBookToCreateDTO.getName()),
                eq(expectedBookToCreateDTO.getIsbn()),
                any(User.class)))
                .thenReturn(Optional.empty());
        when(authorService.verifyAndGetIfExists(expectedBookToCreateDTO.getAuthorId())).thenReturn(new Author());
        when(publisherService.verifyAndGetIfExists(expectedBookToCreateDTO.getPublisherId())).thenReturn(new Publisher());
        when(bookRepository.save(any(Book.class))).thenReturn(expectedCreatedBook);
        BookResponseDTO createdBookResponseDTO = bookService.create(authenticatedUser, expectedBookToCreateDTO);

        //then
        assertThat(createdBookResponseDTO, is(equalTo(expectedCreatedBookDTO)));
    }

    @Test
    void whenExistingBookIsInformedThenAnExceptionShouldBeThrown() {
        //given
        BookRequestDTO expectedBookToCreateDTO = bookRequestDTOBuilder.buildRequestBookDTO();
        Book expectedDuplicatedBook = bookMapper.toModel(expectedBookToCreateDTO);

        //when
        when(userService.verifyAndGetUserIfExists(authenticatedUser.getUsername())).thenReturn(new User());
        when(bookRepository.findByNameAndIsbnAndUser(
                eq(expectedBookToCreateDTO.getName()),
                eq(expectedBookToCreateDTO.getIsbn()),
                any(User.class)))
                .thenReturn(Optional.of(expectedDuplicatedBook));

        //then
        assertThrows(BookAlreadyExistsException.class, () -> bookService.create(authenticatedUser, expectedBookToCreateDTO));
    }
}
