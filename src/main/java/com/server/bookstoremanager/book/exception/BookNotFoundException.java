package com.server.bookstoremanager.book.exception;

import javax.persistence.EntityNotFoundException;

public class BookNotFoundException extends EntityNotFoundException {

    public BookNotFoundException(Long bookId) {
        super(String.format("Book with id %s not exists!", bookId));
    }
}
