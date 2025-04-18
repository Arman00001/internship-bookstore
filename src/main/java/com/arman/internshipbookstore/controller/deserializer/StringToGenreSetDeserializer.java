package com.arman.internshipbookstore.controller.deserializer;

import com.arman.internshipbookstore.enums.Genre;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.util.Set;

import static com.arman.internshipbookstore.controller.converter.StringToGenreSetConverter.getGenreSet;

public class StringToGenreSetDeserializer extends JsonDeserializer<Set<Genre>> {
    @Override
    public Set<Genre> deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {
        String source = jsonParser.getText();
        return getGenreSet(source);
    }
}
