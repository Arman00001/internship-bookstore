package com.arman.internshipbookstore.service.mapper;

import com.arman.internshipbookstore.persistence.entity.Publisher;
import com.arman.internshipbookstore.service.dto.publisher.PublisherDto;
import org.springframework.stereotype.Component;

@Component
public class PublisherMapper {
    public Publisher mapDtoToPublisher(PublisherDto publisherDto){
        Publisher publisher = new Publisher();
        publisher.setName(publisherDto.getName());

        return publisher;
    }

    public PublisherDto mapToDto(Publisher publisher){
        PublisherDto publisherDto = new PublisherDto();
        publisherDto.setName(publisher.getName());
        publisherDto.setId(publisher.getId());

        return publisherDto;
    }
}