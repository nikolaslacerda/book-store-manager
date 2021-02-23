package com.server.bookstoremanager.publisher.mapper;

import com.server.bookstoremanager.publisher.dto.PublisherDTO;
import com.server.bookstoremanager.publisher.entity.Publisher;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PublisherMapper {

    PublisherMapper INSTANCE = Mappers.getMapper(PublisherMapper.class);

    Publisher toModel(PublisherDTO publisherDTO);

    PublisherDTO toDTO(Publisher publisher);
}
