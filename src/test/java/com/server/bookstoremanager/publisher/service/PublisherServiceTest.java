package com.server.bookstoremanager.publisher.service;

import com.server.bookstoremanager.publisher.builder.PublisherBuilder;
import com.server.bookstoremanager.publisher.mapper.PublisherMapper;
import com.server.bookstoremanager.publisher.repository.PublisherRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class PublisherServiceTest {

    private final PublisherMapper publisherMapper = PublisherMapper.INSTANCE;

    @Mock
    private PublisherRepository publisherRepository;

    @InjectMocks
    private PublisherService publisherService;

    private PublisherBuilder publisherBuilder;

    @BeforeEach
    void setUp() {
        publisherBuilder = PublisherBuilder.builder().build();
    }
}
