package com.arman.internshipbookstore.service;

import com.arman.internshipbookstore.persistence.entity.Publisher;
import com.arman.internshipbookstore.persistence.repository.PublisherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PublisherService {

    private final PublisherRepository publisherRepository;

    public Publisher getPublisherByName(String name){
        return publisherRepository.getPublisherByName(name);
    }

    public Publisher save(Publisher publisher){
        publisherRepository.save(publisher);

        Publisher publisher1 = publisherRepository.getPublisherByName(publisher.getName());

        return publisher1;
    }
}