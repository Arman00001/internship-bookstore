package com.arman.internshipbookstore.service.mapper;

import com.arman.internshipbookstore.persistence.entity.Author;
import com.arman.internshipbookstore.persistence.entity.Award;
import com.arman.internshipbookstore.persistence.entity.Book;
import com.arman.internshipbookstore.service.dto.author.AuthorOfBookResponseDto;
import com.arman.internshipbookstore.service.dto.award.AwardOfBookResponseDto;
import com.arman.internshipbookstore.service.dto.book.BookCreateDto;
import com.arman.internshipbookstore.service.dto.book.BookDto;
import com.arman.internshipbookstore.service.dto.book.BookResponseDto;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static com.arman.internshipbookstore.service.mapper.CsvBookEntityMapper.setBookRatingsByStars;

@Component
public class BookMapper {

    public Book mapDtoToBook(BookDto bookDto){
        Book book = new Book();

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
        setBookRatingsByStars(book, bookDto.getRatingsByStars());

        book.setImagePath(bookDto.getImageUrl());

        return book;
    }

    public Book mapCreateDtoToBook(BookCreateDto bookCreateDto) {
        Book book = new Book();

        book.setTitle(bookCreateDto.getTitle());
        book.setDescription(bookCreateDto.getDescription());
        book.setSeries(bookCreateDto.getSeries());
        book.setRating(bookCreateDto.getRating());
        book.setPages(bookCreateDto.getPages());
        book.setIsbn(bookCreateDto.getIsbn());
        book.setFormat(bookCreateDto.getFormat());
        book.setPublishDate(bookCreateDto.getPublishDate());
        book.setFirstPublishDate(bookCreateDto.getFirstPublishDate());
        book.setLanguage(bookCreateDto.getLanguage());
        book.setEdition(bookCreateDto.getEdition());
        book.setNumRatings(bookCreateDto.getNumRatings());
        book.setLikedPercent(bookCreateDto.getLikedPercent());
        book.setSetting(bookCreateDto.getSetting());
        book.setBbeScore(bookCreateDto.getBbeScore());
        book.setBbeVotes(bookCreateDto.getBbeVotes());
        book.setPrice(bookCreateDto.getPrice());
        setBookRatingsByStars(book, bookCreateDto.getRatingsByStars());

        book.setImagePath(bookCreateDto.getImageUrl());

        return book;
    }

    public BookResponseDto mapToResponseDto(Book book) {
        BookResponseDto bookResponseDto = new BookResponseDto(book);
        bookResponseDto.setAuthors(book.getBookAuthors().stream()
                .map(bookAuthor -> {
                    Author author = bookAuthor.getAuthor();
                    return new AuthorOfBookResponseDto(author.getId(), author.getName(), bookAuthor.getRole());
                })
                .toList());

        bookResponseDto.setAwards(book.getBookAwards().stream().map(bookAward -> {
                    Award award = bookAward.getAward();
                    return new AwardOfBookResponseDto(award.getId(), award.getName(), bookAward.getYear());
                })
                .toList());

        return bookResponseDto;
    }

    public BookDto mapToDto(Book book) {
        BookDto bookDto = new BookDto();

        bookDto.setId(book.getId());
        bookDto.setTitle(book.getTitle());
        bookDto.setDescription(book.getDescription());
        bookDto.setSeries(book.getSeries());
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
        book.getBookAuthors().forEach(bookAuthor -> stringBuilder.
                append(bookAuthor.getAuthor().getName()).append(" (").
                append(bookAuthor.getRole()).append("), "));

        if (stringBuilder.length() > 2)
            bookDto.setAuthorNames(stringBuilder.substring(0, stringBuilder.length() - 2));


        stringBuilder.setLength(0);
        book.getBookAwards().forEach(bookAward -> stringBuilder.
                append(bookAward.getAward().getName()).append("(").
                append(bookAward.getYear()).append(")").append(", "));

        if (stringBuilder.length() > 2)
            bookDto.setAwards(stringBuilder.substring(0, stringBuilder.length() - 2));


        stringBuilder.setLength(0);
        book.getCharacters().forEach(character -> stringBuilder.
                append(character.getName()).append(", "));

        if (stringBuilder.length() > 2)
            bookDto.setCharacters(stringBuilder.substring(0, stringBuilder.length() - 2));


        String imageUrl = book.getImagePath();
        if (imageUrl != null && !imageUrl.startsWith("Download"))
            bookDto.setImageUrl(imageUrl);

        return bookDto;
    }

    public List<BookDto> mapToDtos(List<Book> books) {
        List<BookDto> bookDtos = new ArrayList<>();
        for (Book book : books) {
            bookDtos.add(mapToDto(book));
        }

        return bookDtos;
    }
}
