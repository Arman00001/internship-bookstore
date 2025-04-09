package com.arman.internshipbookstore.service.mapper;

import com.arman.internshipbookstore.persistence.entity.BookAuthor;
import com.arman.internshipbookstore.service.dto.BookAuthorDto;
import org.springframework.stereotype.Component;

@Component
public class BookAuthorMapper {

//    public BookAuthor mapDtoToBookAuthor(BookAuthorDto bookAuthorDto){
//        BookAuthor bookAuthor = new BookAuthorDto();
//
//        bookAuthor.setRole(bookAuthor.getRole());
//
//        return bookAuthor;
//    }

    public BookAuthorDto mapToDto(BookAuthor bookAuthor){
        BookAuthorDto bookAuthorDto = new BookAuthorDto();

        bookAuthorDto.setId(bookAuthor.getId());
        bookAuthorDto.setAuthorId(bookAuthor.getId());
        bookAuthorDto.setBookId(bookAuthor.getBook().getBookId());
        bookAuthorDto.setRole(bookAuthor.getRole());

        return bookAuthorDto;
    }
}
