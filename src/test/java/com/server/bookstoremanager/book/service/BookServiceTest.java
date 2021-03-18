package com.server.bookstoremanager.book.service;

import com.server.bookstoremanager.author.service.AuthorService;
import com.server.bookstoremanager.book.builder.BookRequestBuilder;
import com.server.bookstoremanager.book.builder.BookResponseBuilder;
import com.server.bookstoremanager.book.mapper.BookMapper;
import com.server.bookstoremanager.book.repository.BookRepository;
import com.server.bookstoremanager.publisher.service.PublisherService;
import com.server.bookstoremanager.user.dto.AuthenticatedUser;
import com.server.bookstoremanager.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
}
