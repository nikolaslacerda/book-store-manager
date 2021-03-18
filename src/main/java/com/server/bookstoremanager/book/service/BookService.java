package com.server.bookstoremanager.book.service;

import com.server.bookstoremanager.author.service.AuthorService;
import com.server.bookstoremanager.book.mapper.BookMapper;
import com.server.bookstoremanager.book.repository.BookRepository;
import com.server.bookstoremanager.publisher.service.PublisherService;
import com.server.bookstoremanager.user.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class BookService {

    private final BookMapper bookMapper = BookMapper.INSTANCE;

    private BookRepository bookRepository;

    private UserService userService;

    private AuthorService authorService;

    private PublisherService publisherService;

}
