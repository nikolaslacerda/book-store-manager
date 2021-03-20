package com.server.bookstoremanager.book.controller;

import com.server.bookstoremanager.book.dto.BookRequestDTO;
import com.server.bookstoremanager.book.dto.BookResponseDTO;
import com.server.bookstoremanager.book.service.BookService;
import com.server.bookstoremanager.user.dto.AuthenticatedUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/books")
public class BookController implements BookControllerDocs {

    private BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService     = bookService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookResponseDTO create(@AuthenticationPrincipal AuthenticatedUser authenticatedUser,
                                  @RequestBody @Valid BookRequestDTO bookRequestDTO) {
        return bookService.create(authenticatedUser, bookRequestDTO);
    }

    @GetMapping("/{bookId}")
    public BookResponseDTO findByIdAndUser(@AuthenticationPrincipal AuthenticatedUser authenticatedUser,
                                           @PathVariable Long bookId) {
        return bookService.findByIdAndUser(authenticatedUser, bookId);
    }

    @GetMapping
    public List<BookResponseDTO> findAllByUser(@AuthenticationPrincipal AuthenticatedUser authenticatedUser) {
        return bookService.findAllByUser(authenticatedUser);
    }
}
