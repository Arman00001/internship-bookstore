package com.arman.internshipbookstore.service;

import com.arman.internshipbookstore.persistence.entity.Publisher;
import com.arman.internshipbookstore.persistence.repository.PublisherRepository;
import com.arman.internshipbookstore.service.dto.PublisherDto;
import com.arman.internshipbookstore.service.exception.PublisherNotFoundException;
import com.arman.internshipbookstore.service.mapper.PublisherMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PublisherService {
    private final PublisherMapper publisherMapper;

    private final PublisherRepository publisherRepository;

    public Publisher getPublisherById(Long id){
        return publisherRepository.getPublisherById(id);
    }

    public Publisher getPublisherByName(String name){
        Publisher publisher = publisherRepository.getPublisherByName(name);

        return publisher;
    }

    public Publisher save(PublisherDto publisherDto){
        Publisher publisher = publisherRepository.getPublisherByName(publisherDto.getName());
        if(publisher != null) return publisher;

        publisher = publisherMapper.mapDtoToPublisher(publisherDto);

        Publisher publisher1 = publisherRepository.save(publisher);

        return publisher1;
    }

    public List<Publisher> findAll() {
        return publisherRepository.findAll();
    }

    public PublisherDto addPublisher(PublisherDto publisherDto) {
        Publisher publisher = publisherRepository.getPublisherByName(publisherDto.getName());

        if(publisher==null){
            publisher = publisherMapper.mapDtoToPublisher(publisherDto);

            Publisher publisher1 = publisherRepository.save(publisher);

            return publisherMapper.mapToDto(publisher1);
        }

        return null;
    }

    public void deletePublisher(Long id) {
        Publisher publisher = publisherRepository.getPublisherById(id);
        if(publisher==null)
            throw new PublisherNotFoundException("Publisher with the following id does not exist: "+id);

        if(!publisher.getBooks().isEmpty())
            throw new IllegalStateException("Cannot delete publisher: it still has books associated.");

        publisherRepository.delete(publisher);
    }
}