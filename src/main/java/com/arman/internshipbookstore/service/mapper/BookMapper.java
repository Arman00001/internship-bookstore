package com.arman.internshipbookstore.service.mapper;

import com.arman.internshipbookstore.persistence.entity.Book;
import com.arman.internshipbookstore.service.dto.BookDto;
import com.arman.internshipbookstore.service.exception.IncorrectStarRatingsFormat;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

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

        String[] stars_ = bookDto.getRatingsByStars().replace("[","").
                replace("]","").split(",");
        if(stars_.length==0)
            book.addStarRatings(new Integer[0]);
        else {
            Integer[] stars = new Integer[5];
            try {
                for (int i = 0; i < 5; i++) {
                    Integer val = Integer.parseInt(stars_[i].replace("'","").trim());
                    if(val<0) throw new NumberFormatException();
                    stars[i] = val;
                }
            } catch (NumberFormatException e){
                throw new IncorrectStarRatingsFormat("Star ratings should be positive integer numbers only!");
            }
            book.addStarRatings(stars);
        }

        book.setImagePath(bookDto.getImageUrl());

        return book;
    }

//    private void setRatingsByStars(Book book, String ratings)

    public BookDto mapToDto(Book book){
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

        if(stringBuilder.length()>2)
            bookDto.setAuthorNames(stringBuilder.substring(0,stringBuilder.length()-2));


        StringBuilder sb1 = new StringBuilder();

        book.getBookAwards().forEach(bookAward -> sb1.
                append(bookAward.getAward().getName()).append("(").
                append(bookAward.getYear()).append(")").append(", "));

        if(sb1.length()>2)
            bookDto.setAwards(sb1.substring(0,sb1.length()-2));

        String imageUrl = book.getImagePath();
        if(!imageUrl.startsWith("Download"))
            bookDto.setImageUrl(imageUrl);

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
