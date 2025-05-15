package com.arman.internshipbookstore.service.mapper;

import com.arman.internshipbookstore.persistence.entity.Publisher;
import com.arman.internshipbookstore.service.dto.publisher.PublisherCreateDto;
import com.arman.internshipbookstore.service.dto.publisher.PublisherDto;
import org.springframework.stereotype.Component;

@Component
public class PublisherMapper {
    public Publisher mapDtoToPublisher(PublisherCreateDto publisherCreateDto){
        Publisher publisher = new Publisher();
        publisher.setName(publisherCreateDto.getName());

        return publisher;
    }

    public PublisherDto mapToDto(Publisher publisher){
        PublisherDto publisherDto = new PublisherDto();
        publisherDto.setName(publisher.getName());
        publisherDto.setId(publisher.getId());

        return publisherDto;
    }
}