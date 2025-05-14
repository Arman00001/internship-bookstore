package com.arman.internshipbookstore.service.validation;

import com.arman.internshipbookstore.service.exception.*;
import org.apache.commons.csv.CSVRecord;

import java.util.Set;

public class BookValidation {

    public static void validateBook(CSVRecord csvRecord, Set<Long> isbnSet){
        validateISBN(csvRecord.get("isbn"), isbnSet);
        validatePages(csvRecord.get("pages"));
        validateRating(csvRecord.get("rating"));
    }


    public static Double validatePrice(String price_) {
        if (price_.isEmpty()) return null;
        Double price;
        try {
            price = Double.parseDouble(price_);
            if (price < 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            throw new IncorrectPriceFormatException("Price should be of positive numeric value");
        }

        return price;
    }

    public static Integer validateBbeVotes(String bbeVotes_) {
        Integer bbeVotes;
        try {
            bbeVotes = Integer.parseInt(bbeVotes_);
            if (bbeVotes < 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            throw new IncorrectBbeVotesFormatException("Bbe Votes should be of positive numeric value");
        }

        return bbeVotes;
    }

    public static Integer validateBbeScore(String bbeScore_) {
        Integer bbeScore;
        try {
            bbeScore = Integer.parseInt(bbeScore_);
            if (bbeScore < 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            throw new IncorrectBbeScoreFormatException("Bbe Score should be of positive numeric value");
        }

        return bbeScore;
    }

    public static Integer validateLikedPercent(String likedPercent_) {
        Integer likedPercent;
        try {
            likedPercent = Integer.parseInt(likedPercent_);
            if (likedPercent < 0 || likedPercent > 100) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            throw new IncorrectPercentageFormatException("The percentage should be of numeric value between 0 and 100");
        }

        return likedPercent;
    }

    public static Double validateRating(String rating_) throws IncorrectRatingFormatException {
        Double rating;
        try {
            rating = Double.parseDouble(rating_);
            if (rating < 0 || rating > 5) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            throw new IncorrectRatingFormatException("Rating should be a numeric value and be between 0 and 5");
        }
        return rating;
    }

    public static Integer validatePages(String pages_) {
        Integer pages;
        try {
            pages = Integer.parseInt(pages_);
            if (pages <= 0) throw new NumberFormatException();

        } catch (NumberFormatException e) {
            throw new IncorrectPageFormatException("Page count should contain numbers only and be greater than 0");
        }
        return pages;
    }

    public static Long validateISBN(String isbn, Set<Long> isbnSet) throws IsbnIncorrectFormatException, IsbnDuplicationException {
        Long isbnLong;

        if (isbn.length() == 13 && isbn.replaceAll("\\d", "").isEmpty())
            isbnLong = Long.valueOf(isbn);
        else throw new IsbnIncorrectFormatException("ISBN should contain numeric values only and be of length 13.");

        if (!isbnSet.contains(isbnLong)) {
            isbnSet.add(isbnLong);
            return isbnLong;
        } else throw new IsbnDuplicationException("Book with the following ISBN already exists: " + isbn);
    }
}
