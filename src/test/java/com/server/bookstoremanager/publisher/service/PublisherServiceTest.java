package com.server.bookstoremanager.publisher.service;

import com.server.bookstoremanager.publisher.builder.PublisherBuilder;
import com.server.bookstoremanager.publisher.dto.PublisherDTO;
import com.server.bookstoremanager.publisher.entity.Publisher;
import com.server.bookstoremanager.publisher.exception.PublisherAlreadyExistsException;
import com.server.bookstoremanager.publisher.exception.PublisherNotFoundException;
import com.server.bookstoremanager.publisher.mapper.PublisherMapper;
import com.server.bookstoremanager.publisher.repository.PublisherRepository;
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
import static org.mockito.Mockito.when;

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

    @Test
    void whenNewPublisherIsInformedThenItShouldBeCreated() {
        //given
        PublisherDTO expectedPublisherToCreateDTO = publisherBuilder.buildPublisherDTO();
        Publisher expectedPublisherCreated = publisherMapper.toModel(expectedPublisherToCreateDTO);

        //when
        when(publisherRepository.findByNameOrCode(expectedPublisherToCreateDTO.getName(), expectedPublisherToCreateDTO.getCode())).thenReturn(Optional.empty());
        when(publisherRepository.save(expectedPublisherCreated)).thenReturn(expectedPublisherCreated);
        PublisherDTO createdPublisherDTO = publisherService.create(expectedPublisherToCreateDTO);

        //then
        assertThat(createdPublisherDTO, is(equalTo(expectedPublisherToCreateDTO)));
    }

    @Test
    void whenExistingPublisherIsInformedThenAnExceptionShouldBeThrown() {
        //given
        PublisherDTO expectedPublisherToCreateDTO = publisherBuilder.buildPublisherDTO();
        Publisher expectedPublisherDuplicated = publisherMapper.toModel(expectedPublisherToCreateDTO);

        //when
        when(publisherRepository.findByNameOrCode(expectedPublisherToCreateDTO.getName(), expectedPublisherToCreateDTO.getCode())).thenReturn(Optional.of(expectedPublisherDuplicated));

        //then
        assertThrows(PublisherAlreadyExistsException.class, () -> publisherService.create(expectedPublisherToCreateDTO));
    }

    @Test
    void whenValidIdIsGivenThenAPublisherShouldBeReturned() {
        //given
        PublisherDTO expectedPublisherFoundDTO = publisherBuilder.buildPublisherDTO();
        Publisher expectedPublisherFound = publisherMapper.toModel(expectedPublisherFoundDTO);
        Long expectedPublisherFoundId = expectedPublisherFoundDTO.getId();

        //when
        when(publisherRepository.findById(expectedPublisherFoundId)).thenReturn(Optional.of(expectedPublisherFound));
        PublisherDTO foundPublisherDTO = publisherService.findById(expectedPublisherFoundId);

        //then
        assertThat(expectedPublisherFoundDTO, is(equalTo(foundPublisherDTO)));
    }

    @Test
    void whenInvalidIdIsGivenThenAnExceptionShouldBeThrown() {
        //given
        PublisherDTO expectedPublisherFoundDTO = publisherBuilder.buildPublisherDTO();
        Long expectedPublisherFoundId = expectedPublisherFoundDTO.getId();

        //when
        when(publisherRepository.findById(expectedPublisherFoundId)).thenReturn(Optional.empty());

        //then
        assertThrows(PublisherNotFoundException.class, () -> publisherService.findById(expectedPublisherFoundId));
    }
}
