package com.arman.internshipbookstore.service.parser.csv;

import com.arman.internshipbookstore.enums.Genre;

import java.util.ArrayList;
import java.util.List;

import static com.arman.internshipbookstore.service.util.StringUtils.removeSingleQuotes;
import static com.arman.internshipbookstore.service.util.StringUtils.removeSquareBrackets;

public class GenreParser {

    public static List<Genre> createOrGetGenresList(String genres) {
        List<Genre> genreList = new ArrayList<>();

        genres = removeSquareBrackets(genres);

        String[] genre = genres.split(", ");

        for (String genreName : genre) {
            Genre genre_ = Genre.fromString(removeSingleQuotes(genreName));

            genreList.add(genre_);
        }

        return genreList;
    }
}
