package com.arman.internshipbookstore.service.mapper;

import com.arman.internshipbookstore.enums.Genre;
import com.arman.internshipbookstore.persistence.entity.Award;
import com.arman.internshipbookstore.persistence.entity.Book;
import com.arman.internshipbookstore.persistence.entity.BookAuthor;
import com.arman.internshipbookstore.service.dto.BookDto;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
public class BookMapper {
    public Book mapDtoToBook(BookDto bookDto){
        Book book = new Book();

        book.setBookId(bookDto.getBookId());
        book.setTitle(bookDto.getTitle());
        book.setDescription(bookDto.getDescription());
        book.setSeries(bookDto.getSeries());
        book.setRating(bookDto.getRating());
        book.setPages(bookDto.getPages());
        book.setIsbn(bookDto.getIsbn());
        book.setFormat(bookDto.getFormat());
        book.setPublishDate(bookDto.getPublishDate());
        book.setFirstPublishDate(bookDto.getFirstPublishDate());
        book.setLanguage(bookDto.getLanguage());
        book.setEdition(bookDto.getEdition());
        book.setNumRatings(bookDto.getNumRatings());
        book.setLikedPercent(bookDto.getLikedPercent());
        book.setSetting(bookDto.getSetting());
        book.setBbeScore(bookDto.getBbeScore());
        book.setBbeVotes(bookDto.getBbeVotes());
        book.setPrice(bookDto.getPrice());
        book.setRatingsByStars(bookDto.getRatingsByStars());

        return book;
    }

    public BookDto mapToDto(Book book){
        BookDto bookDto = new BookDto();

        bookDto.setBookId(book.getBookId());
        bookDto.setTitle(book.getTitle());
        bookDto.setDescription(book.getDescription());
        bookDto.setSeries(bookDto.getSeries());
        bookDto.setRating(book.getRating());
        bookDto.setPages(book.getPages());
        bookDto.setIsbn(book.getIsbn());
        bookDto.setFormat(book.getFormat());
        bookDto.setPublishDate(book.getPublishDate());
        bookDto.setFirstPublishDate(book.getFirstPublishDate());
        bookDto.setLanguage(book.getLanguage());
        bookDto.setEdition(book.getEdition());
        bookDto.setNumRatings(book.getNumRatings());
        bookDto.setLikedPercent(book.getLikedPercent());
        bookDto.setSetting(book.getSetting());
        bookDto.setBbeScore(book.getBbeScore());
        bookDto.setBbeVotes(book.getBbeVotes());
        bookDto.setPrice(book.getPrice());
        bookDto.setPublisherName(book.getPublisher().getName());
        bookDto.setRatingsByStars(book.getRatingsByStars());
        bookDto.setGenres(book.getGenres());

        StringBuilder stringBuilder = new StringBuilder();
        List<BookAuthor> authors = book.getAuthors();

        for (int i = 0; i < authors.size(); i++) {
            stringBuilder.append(authors.get(i).getAuthor().getName());
            if(i+1<authors.size()) stringBuilder.append(", ");
        }

        bookDto.setAuthorNames(stringBuilder.toString());

        StringBuilder sb1 = new StringBuilder();

        book.getAwards().forEach(award -> sb1.append(award.getName()+", "));
        if(sb1.length()>2)
            bookDto.setAwards(sb1.substring(0,sb1.length()-2));

        return bookDto;
    }

    public List<BookDto> mapToDtos(List<Book> books){
        List<BookDto> bookDtos = new ArrayList<>();
        for (Book book : books) {
            bookDtos.add(mapToDto(book));
        }

        return bookDtos;
    }
}
