package com.arman.internshipbookstore.controller.converter;

import com.arman.internshipbookstore.enums.Genre;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class StringToGenreSetConverter implements Converter<String, Set<Genre>> {

    @Override
    public Set<Genre> convert(String source) {
        return getGenreSet(source);
    }

    public static Set<Genre> getGenreSet(String source) {
        Set<Genre> genreSet = new HashSet<>();

        for (String genreString : source.replace("[","").replace("]","").split(",")) {
            Genre genre = Genre.fromString(genreString.trim());
            genreSet.add(genre);
        }
        return genreSet;
    }
}