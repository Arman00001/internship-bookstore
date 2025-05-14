package com.arman.internshipbookstore.service.mapper;

import com.arman.internshipbookstore.service.dto.book.BookDto;
import com.arman.internshipbookstore.service.exception.IncorrectPublishDateException;
import com.arman.internshipbookstore.service.exception.IncorrectRatingFormatException;
import com.arman.internshipbookstore.service.exception.IsbnDuplicationException;
import com.arman.internshipbookstore.service.exception.IsbnIncorrectFormatException;
import org.apache.commons.csv.CSVRecord;

import java.time.LocalDate;
import java.util.Set;

import static com.arman.internshipbookstore.service.util.DateUtils.setupDate;
import static com.arman.internshipbookstore.service.util.StringUtils.removeSquareBrackets;
import static com.arman.internshipbookstore.service.validation.BookValidation.*;

public class CsvBookDtoMapper {

    public static BookDto initializeBookDto(CSVRecord csvRecord, Set<Long> isbnSet) throws IsbnIncorrectFormatException, IsbnDuplicationException, IncorrectRatingFormatException {
        String isbn_ = csvRecord.get("isbn");
        Long isbn = validateISBN(isbn_, isbnSet);

        String pages_ = csvRecord.get("pages");
        Integer pages = validatePages(pages_);

        String bookTitle = csvRecord.get("title");
        String series = csvRecord.get("series");
        String rating_ = csvRecord.get("rating");

        Double rating = validateRating(rating_);

        String description = csvRecord.get("description");
        String language = csvRecord.get("language");

        String bookFormat = csvRecord.get("bookFormat");
        String edition = csvRecord.get("edition");

        String publishDate_ = csvRecord.get("publishDate");
        String firstPublishDate_ = csvRecord.get("firstPublishDate");


        LocalDate publishDate = setupDate(publishDate_);

        if (publishDate == null) throw new IncorrectPublishDateException("Publish date cannot be empty");
        LocalDate firstPublishDate = setupDate(firstPublishDate_);

        if (firstPublishDate != null) {
            if (firstPublishDate.isAfter(publishDate)) {
                assert publishDate != null;
                publishDate = publishDate.minusYears(100);
            }
        }

        String numRatings_ = csvRecord.get("numRatings");

        Integer numRatings = Integer.parseInt(numRatings_);

        String likedPercent_ = csvRecord.get("likedPercent");
        Integer likedPercent = validateLikedPercent(likedPercent_);

        String setting = csvRecord.get("setting");
        setting = removeSquareBrackets(setting);
        String bbeScore_ = csvRecord.get("bbeScore");

        Integer bbeScore = validateBbeScore(bbeScore_);

        String bbeVotes_ = csvRecord.get("bbeVotes");

        Integer bbeVotes = validateBbeVotes(bbeVotes_);

        String price_ = csvRecord.get("price");

        Double price = validatePrice(price_);

        String publisher = csvRecord.get("publisher");

        String ratingsByStars = csvRecord.get("ratingsByStars");
        ratingsByStars = removeSquareBrackets(ratingsByStars);

        BookDto bookDto = new BookDto();

        bookDto.setTitle(bookTitle);
        bookDto.setIsbn(isbn);
        bookDto.setPages(pages);
        bookDto.setSeries(series);
        bookDto.setRating(rating);
        bookDto.setDescription(description);
        bookDto.setLanguage(language);
        bookDto.setFormat(bookFormat);
        bookDto.setEdition(edition);
        bookDto.setPublishDate(publishDate);
        bookDto.setFirstPublishDate(firstPublishDate);
        bookDto.setNumRatings(numRatings);
        bookDto.setLikedPercent(likedPercent);
        bookDto.setSetting(setting);
        bookDto.setBbeScore(bbeScore);
        bookDto.setBbeVotes(bbeVotes);
        bookDto.setPrice(price);
        bookDto.setPublisherName(publisher);
        bookDto.setRatingsByStars(ratingsByStars);


        return bookDto;
    }
}
