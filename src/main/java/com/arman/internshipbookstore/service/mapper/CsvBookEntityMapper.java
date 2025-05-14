package com.arman.internshipbookstore.service.mapper;

import com.arman.internshipbookstore.enums.Genre;
import com.arman.internshipbookstore.persistence.entity.*;
import com.arman.internshipbookstore.service.AuthorService;

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
}
