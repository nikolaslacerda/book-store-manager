package com.server.bookstoremanager.author.service;


import com.server.bookstoremanager.author.builder.AuthorBuilder;
import com.server.bookstoremanager.author.mapper.AuthorMapper;
import com.server.bookstoremanager.author.repository.AuthorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
}
