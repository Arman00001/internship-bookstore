package com.arman.internshipbookstore.service;

import com.arman.internshipbookstore.persistence.entity.Book;
import com.arman.internshipbookstore.persistence.entity.Publisher;
import com.arman.internshipbookstore.persistence.repository.PublisherRepository;
import com.arman.internshipbookstore.service.dto.publisher.PublisherCreateDto;
import com.arman.internshipbookstore.service.dto.publisher.PublisherResponseDto;
import com.arman.internshipbookstore.service.dto.publisher.PublisherUpdateDto;
import com.arman.internshipbookstore.service.exception.PublisherNotFoundException;
import com.arman.internshipbookstore.service.exception.ResourceAlreadyUsedException;
import com.arman.internshipbookstore.service.mapper.PublisherMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PublisherService {

    private final PublisherMapper publisherMapper;
    private final PublisherRepository publisherRepository;

    public PublisherResponseDto getPublisherById(Long id) {
        Publisher publisher = publisherRepository.findById(id)
                .orElseThrow(() ->
                        new PublisherNotFoundException("Publisher with the following id not found: " + id));

        return PublisherResponseDto.getPublisherResponse(publisher);
    }

    public PublisherResponseDto getPublisherByName(String name) {
        Publisher publisher = publisherRepository.getPublisherByName(name);

        return PublisherResponseDto.getPublisherResponse(publisher);
    }

    public List<Publisher> findAll() {
        return publisherRepository.findAll();
    }

    public PublisherResponseDto addPublisher(PublisherCreateDto publisherCreateDto) {
        Publisher pub = publisherRepository.getPublisherByName(publisherCreateDto.getName());

        if (pub == null) {
            pub = publisherMapper.mapDtoToPublisher(publisherCreateDto);

            Publisher publisher = publisherRepository.save(pub);

            return PublisherResponseDto.getPublisherResponse(publisher);
        }

        throw new ResourceAlreadyUsedException("Publisher with following name already exists: " + publisherCreateDto.getName());
    }

    public void deletePublisher(Long id) {
        Publisher publisher = publisherRepository.findById(id)
                .orElseThrow(() ->
                        new PublisherNotFoundException("Publisher with the following id does not exist: " + id));

        if (!publisher.getBooks().isEmpty()) {
            throw new IllegalStateException("Cannot delete publisher: it still has books associated.");
        }

        publisherRepository.delete(publisher);
    }

    public PublisherResponseDto updatePublisher(Long id, @Valid PublisherUpdateDto publisherUpdateDto) {
        Publisher publisher = publisherRepository.findById(id)
                .orElseThrow(() ->
                        new PublisherNotFoundException("Publisher with the following id does not exist: " + id));

        if(publisherRepository.existsPublisherByName(publisherUpdateDto.getName())) {
            throw new IllegalArgumentException("Publisher with the following name already exists: " + publisher.getName());
        }
        publisher.setName(publisherUpdateDto.getName());

        return PublisherResponseDto.getPublisherResponse(publisherRepository.save(publisher));
    }


    public void assignPublisherToBook(Book book, Long publisherId) {
        Publisher publisher = publisherRepository.findById(publisherId)
                .orElseThrow(() ->
                        new PublisherNotFoundException("Publisher with the following id not found: " + publisherId));

        publisher.addBook(book);
    }
}