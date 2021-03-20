package com.server.bookstoremanager.book.service;

import com.server.bookstoremanager.author.entity.Author;
import com.server.bookstoremanager.author.service.AuthorService;
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
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class BookService {

    private final BookMapper bookMapper = BookMapper.INSTANCE;

    private BookRepository bookRepository;

    private UserService userService;

    private AuthorService authorService;

    private PublisherService publisherService;

    public BookResponseDTO create(AuthenticatedUser authenticatedUser, BookRequestDTO bookRequestDTO) {
        User foundAuthenticatedUser = userService.verifyAndGetUserIfExists(authenticatedUser.getUsername());
        verifyIfIsAlreadyRegistered(bookRequestDTO, foundAuthenticatedUser);
        Author foundAuthor = authorService.verifyAndGetIfExists(bookRequestDTO.getAuthorId());
        Publisher foundPublisher = publisherService.verifyAndGetIfExists(bookRequestDTO.getPublisherId());

        Book bookToSave = bookMapper.toModel(bookRequestDTO);
        bookToSave.setUser(foundAuthenticatedUser);
        bookToSave.setAuthor(foundAuthor);
        bookToSave.setPublisher(foundPublisher);
        Book savedBook = bookRepository.save(bookToSave);
        return bookMapper.toDTO(savedBook);
    }

    public BookResponseDTO findByIdAndUser(AuthenticatedUser authenticatedUser, Long bookId) {
        User foundAuthenticatedUser = userService.verifyAndGetUserIfExists(authenticatedUser.getUsername());
        return bookRepository.findByIdAndUser(bookId, foundAuthenticatedUser)
                .map(bookMapper::toDTO)
                .orElseThrow(() -> new BookNotFoundException(bookId));
    }

    public List<BookResponseDTO> findAllByUser(AuthenticatedUser authenticatedUser) {
        User foundAuthenticatedUser = userService.verifyAndGetUserIfExists(authenticatedUser.getUsername());
        return bookRepository.findAllByUser(foundAuthenticatedUser)
                .stream()
                .map(bookMapper::toDTO)
                .collect(Collectors.toList());
    }

    private void verifyIfIsAlreadyRegistered(BookRequestDTO bookRequestDTO, User foundAuthenticatedUser) {
        bookRepository.findByNameAndIsbnAndUser(bookRequestDTO.getName(), bookRequestDTO.getIsbn(), foundAuthenticatedUser)
                .ifPresent(duplicatedBook -> {
                    throw new BookAlreadyExistsException(bookRequestDTO.getName(), bookRequestDTO.getIsbn(), foundAuthenticatedUser.getUsername());
                });
    }
}
