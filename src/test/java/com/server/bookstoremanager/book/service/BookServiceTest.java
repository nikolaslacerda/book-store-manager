package com.server.bookstoremanager.book.service;

import com.server.bookstoremanager.author.entity.Author;
import com.server.bookstoremanager.author.service.AuthorService;
import com.server.bookstoremanager.book.builder.BookRequestBuilder;
import com.server.bookstoremanager.book.builder.BookResponseBuilder;
import com.server.bookstoremanager.book.dto.BookRequestDTO;
import com.server.bookstoremanager.book.dto.BookResponseDTO;
import com.server.bookstoremanager.book.entity.Book;
import com.server.bookstoremanager.book.exception.BookAlreadyExistsException;
import com.server.bookstoremanager.book.exception.BookNotFoundException;
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

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

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

    @Test
    void whenExistingBookIsInformedThenABookShouldBeReturned() {
        //given
        BookRequestDTO expectedBookToFindDTO = bookRequestDTOBuilder.buildRequestBookDTO();
        BookResponseDTO expectedFoundBookDTO = bookResponseDTOBuilder.buildBookResponseDTO();
        Book expectedFoundBook = bookMapper.toModel(expectedFoundBookDTO);

        //when
        when(userService.verifyAndGetUserIfExists(authenticatedUser.getUsername())).thenReturn(new User());
        when(bookRepository.findByIdAndUser(
                eq(expectedBookToFindDTO.getId()),
                any(User.class)))
                .thenReturn(Optional.of(expectedFoundBook));
        BookResponseDTO foundBookDTO = bookService.findByIdAndUser(authenticatedUser, expectedBookToFindDTO.getId());


        //then
        assertThat(foundBookDTO, is(equalTo(expectedFoundBookDTO)));
    }

    @Test
    void whenNotExistingBookIsInformedThenAndExceptionShouldBeThrown() {
        //given
        BookRequestDTO expectedBookToFindDTO = bookRequestDTOBuilder.buildRequestBookDTO();

        //when
        when(userService.verifyAndGetUserIfExists(authenticatedUser.getUsername())).thenReturn(new User());
        when(bookRepository.findByIdAndUser(
                eq(expectedBookToFindDTO.getId()),
                any(User.class)))
                .thenReturn(Optional.empty());

        //then
        assertThrows(BookNotFoundException.class, () -> bookService.findByIdAndUser(authenticatedUser, expectedBookToFindDTO.getId()));
    }

    @Test
    void whenListBookIsCalledThenItShouldBeReturned() {
        //given
        BookResponseDTO expectedFoundBookDTO = bookResponseDTOBuilder.buildBookResponseDTO();
        Book expectedFoundBook = bookMapper.toModel(expectedFoundBookDTO);

        //when
        when(userService.verifyAndGetUserIfExists(authenticatedUser.getUsername())).thenReturn(new User());
        when(bookRepository.findAllByUser(any(User.class)))
                .thenReturn(Collections.singletonList(expectedFoundBook));
        List<BookResponseDTO> bookResponseList = bookService.findAllByUser(authenticatedUser);

        //then
        assertThat(bookResponseList.size(), is(1));
        assertThat(bookResponseList.get(0), is(equalTo(expectedFoundBookDTO)));
    }

    @Test
    void whenListBookIsCalledThenEmptyListShouldBeReturned() {
        //when
        when(userService.verifyAndGetUserIfExists(authenticatedUser.getUsername())).thenReturn(new User());
        when(bookRepository.findAllByUser(any(User.class))).thenReturn(Collections.emptyList());
        List<BookResponseDTO> bookResponseDTOListByUser = bookService.findAllByUser(authenticatedUser);

        //then
        assertThat(bookResponseDTOListByUser.size(), is(0));
    }

    @Test
    void whenExistingBookIdIsInformedThenItShouldBeUpdated() {
        //given
        BookRequestDTO expectedBookToUpdateDTO = bookRequestDTOBuilder.buildRequestBookDTO();
        BookResponseDTO expectedUpdatedBookDTO = bookResponseDTOBuilder.buildBookResponseDTO();
        Book expectedUpdatedBook = bookMapper.toModel(expectedUpdatedBookDTO);

        //when
        when(userService.verifyAndGetUserIfExists(authenticatedUser.getUsername())).thenReturn(new User());
        when(bookRepository.findByIdAndUser(eq(expectedBookToUpdateDTO.getId()), any(User.class)))
                .thenReturn(Optional.of(expectedUpdatedBook));
        when(authorService.verifyAndGetIfExists(expectedBookToUpdateDTO.getAuthorId())).thenReturn(new Author());
        when(publisherService.verifyAndGetIfExists(expectedBookToUpdateDTO.getPublisherId())).thenReturn(new Publisher());
        when(bookRepository.save(any(Book.class))).thenReturn(expectedUpdatedBook);
        BookResponseDTO updatedBookResponse = bookService.updateByUser(
                authenticatedUser,
                expectedBookToUpdateDTO.getId(),
                expectedBookToUpdateDTO);

        //then
        assertThat(updatedBookResponse, is(equalTo(expectedUpdatedBookDTO)));
    }

    @Test
    void whenNotExistingBookIdIsInformedThenAnExceptionItShouldBeThrown() {
        //given
        BookRequestDTO expectedBookToUpdateDTO = bookRequestDTOBuilder.buildRequestBookDTO();

        //when
        when(userService.verifyAndGetUserIfExists(authenticatedUser.getUsername())).thenReturn(new User());
        when(bookRepository.findByIdAndUser(
                eq(expectedBookToUpdateDTO.getId()),
                any(User.class)))
                .thenReturn(Optional.empty());

        //then
        assertThrows(BookNotFoundException.class, () -> bookService.updateByUser(
                authenticatedUser,
                expectedBookToUpdateDTO.getId(),
                expectedBookToUpdateDTO));
    }

    @Test
    void whenExistingBookIdIsInformedThenItShouldBeDeleted() {
        //given
        BookRequestDTO expectedBookToDeleteDTO = bookRequestDTOBuilder.buildRequestBookDTO();
        Book expectedBookToDelete = bookMapper.toModel(expectedBookToDeleteDTO);

        //when
        when(userService.verifyAndGetUserIfExists(authenticatedUser.getUsername())).thenReturn(new User());
        when(bookRepository.findByIdAndUser(eq(expectedBookToDeleteDTO.getId()), any(User.class)))
                .thenReturn(Optional.of(expectedBookToDelete));
        doNothing().when(bookRepository).deleteByIdAndUser(eq(expectedBookToDeleteDTO.getId()), any(User.class));
        bookService.deleteByIdAndUser(authenticatedUser, expectedBookToDeleteDTO.getId());

        //then
        verify(bookRepository, times(1)).deleteByIdAndUser(eq(expectedBookToDeleteDTO.getId()), any(User.class));
    }

    @Test
    void whenNotExistingBookIdIsInformedThenItAndExceptionShouldBeThrown() {
        //given
        BookRequestDTO expectedBookToDeleteDTO = bookRequestDTOBuilder.buildRequestBookDTO();

        //when
        when(userService.verifyAndGetUserIfExists(authenticatedUser.getUsername())).thenReturn(new User());
        when(bookRepository.findByIdAndUser(
                eq(expectedBookToDeleteDTO.getId()),
                any(User.class)))
                .thenReturn(Optional.empty());

        //then
        assertThrows(BookNotFoundException.class, () -> bookService.deleteByIdAndUser(authenticatedUser, expectedBookToDeleteDTO.getId()));
        verify(bookRepository, times(0)).deleteByIdAndUser(eq(expectedBookToDeleteDTO.getId()), any(User.class));
    }
}
