package com.server.bookstoremanager.publisher.service;

import com.server.bookstoremanager.publisher.dto.PublisherDTO;
import com.server.bookstoremanager.publisher.entity.Publisher;
import com.server.bookstoremanager.publisher.exception.PublisherAlreadyExistsException;
import com.server.bookstoremanager.publisher.mapper.PublisherMapper;
import com.server.bookstoremanager.publisher.repository.PublisherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PublisherService {

    private final static PublisherMapper publisherMapper = PublisherMapper.INSTANCE;

    private PublisherRepository publisherRepository;

    @Autowired
    public PublisherService(PublisherRepository publisherRepository) {
        this.publisherRepository = publisherRepository;
    }

    public PublisherDTO create(PublisherDTO publisherDTO) {
        verifyIfExists(publisherDTO.getName(), publisherDTO.getCode());

        Publisher publisherToCreate = publisherMapper.toModel(publisherDTO);
        Publisher createdPublisher = publisherRepository.save(publisherToCreate);
        return publisherMapper.toDTO(createdPublisher);
    }

    private void verifyIfExists(String name, String code) throws PublisherAlreadyExistsException {
        Optional<Publisher> duplicatedPublisher = publisherRepository.findByNameOrCode(name, code);
        if (duplicatedPublisher.isPresent()) {
            throw new PublisherAlreadyExistsException(name, code);
        }
    }
}
