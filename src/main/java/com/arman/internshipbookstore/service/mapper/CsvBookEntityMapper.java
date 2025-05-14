package com.arman.internshipbookstore.service.mapper;

import com.arman.internshipbookstore.enums.Genre;
import com.arman.internshipbookstore.persistence.entity.*;
import com.arman.internshipbookstore.service.AuthorService;
import com.arman.internshipbookstore.service.exception.IncorrectStarRatingsFormat;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.arman.internshipbookstore.service.parser.csv.AwardParser.getAwardsMap;
import static com.arman.internshipbookstore.service.parser.csv.CharacterParser.createOrGetCharacters;
import static com.arman.internshipbookstore.service.parser.csv.GenreParser.createOrGetGenresList;
import static com.arman.internshipbookstore.service.util.StringUtils.removeSquareBrackets;

public class CsvBookEntityMapper {

    public static void setBookAuthors(Book book, Map<String, Author> authorMap, String authorName) {
        List<String> authorNameRoles = AuthorService.splitAuthors(authorName);

        for (String author_ : authorNameRoles) {
            String name = AuthorService.extractAuthorName(author_);
            Author author = authorMap.get(name);

            if (author == null) {
                author = new Author();
                author.setName(name);
                authorMap.put(name, author);
            }
            List<String> roles = AuthorService.extractRoles(author_);
            if (roles.isEmpty()) {
                book.addBookAuthor(author, "Author");
            } else {
                for (String role : roles) {
                    book.addBookAuthor(author, role);
                }
            }
        }
    }

    public static void setBookPublisher(Book book, Map<String, Publisher> publisherMap, String publisher_name) {
        Publisher publisher = publisherMap.get(publisher_name);
        if (publisher == null) {
            publisher = new Publisher();
            publisher.setName(publisher_name);
            publisherMap.put(publisher_name, publisher);
        }

        publisher.addBook(book);
    }

    public static void setBookGenres(Book book, String genres_) {
        List<Genre> genres = createOrGetGenresList(genres_);
        Set<Genre> genreSet = book.getGenres();
        if (genreSet == null) genreSet = new HashSet<>();
        genreSet.addAll(genres);

        book.setGenres(genreSet);
    }

    public static void setBookAwards(Book book, Map<String, Award> awardMap, String award_) {
        Map<Award, List<Integer>> awardYearsMap = getAwardsMap(awardMap, award_);

        for (Award award : awardYearsMap.keySet()) {
            for (Integer year : awardYearsMap.get(award)) {
                book.addAward(award, year);
            }
        }
    }

    public static void setBookCharacters(Book book, Map<String, Characters> charactersMap, String characters_) {
        List<Characters> charactersList = createOrGetCharacters(charactersMap, removeSquareBrackets(characters_));

        for (Characters character : charactersList) {
            book.addCharacter(character);
        }
    }

    public static void setBookRatingsByStars(Book book, String ratingsByStars) {
        String[] stars_ = ratingsByStars.replace("[", "").
                replace("]", "").split(",");
        if (stars_.length == 0) {
            book.addStarRatings(new Integer[0]);
        } else {
            Integer[] stars = new Integer[5];
            try {
                for (int i = 0; i < 5; i++) {
                    Integer val = Integer.parseInt(stars_[i].replace("'", "").trim());
                    if (val < 0) throw new NumberFormatException();
                    stars[i] = val;
                }
            } catch (NumberFormatException e) {
                throw new IncorrectStarRatingsFormat("Star ratings should be positive integer numbers only!");
            }
            book.addStarRatings(stars);
        }
    }
}
