package com.arman.internshipbookstore.service;

import com.arman.internshipbookstore.service.dto.author.AuthorCreateDto;
import com.arman.internshipbookstore.persistence.entity.Author;
import com.arman.internshipbookstore.persistence.repository.AuthorRepository;
import com.arman.internshipbookstore.service.dto.author.AuthorResponseDto;
import com.arman.internshipbookstore.service.exception.AuthorAlreadyExistsException;
import com.arman.internshipbookstore.service.exception.AuthorNotFoundException;
import com.arman.internshipbookstore.service.mapper.AuthorMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class AuthorService {

    private final AuthorRepository authorRepository;
    private final AuthorMapper authorMapper;

    public Author getAuthorByName(String name) {

        return authorRepository.getAuthorByName(name);
    }

    public List<Author> findAll() {
        return authorRepository.findAll();
    }

    public AuthorResponseDto addAuthor(AuthorCreateDto authorCreateDto) {
        Author author = authorRepository.getAuthorByName(authorCreateDto.getName());
        if (author != null) {
            throw new AuthorAlreadyExistsException("Author with the following name already exists: " + authorCreateDto.getName());
        }

        author = authorMapper.mapDtoToAuthor(authorCreateDto);

        Author author1 = authorRepository.save(author);

        return new AuthorResponseDto(author1.getId(),author1.getName());
    }


    public static List<String> splitAuthors(String authorName) {
        List<String> authors = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        int parenthesesLevel = 0;

        for (char c : authorName.toCharArray()) {
            if (c == '(') {
                parenthesesLevel++;
            } else if (c == ')') {
                parenthesesLevel--;
            }

            if (c == ',' && parenthesesLevel == 0) {
                authors.add(sb.toString().trim());
                sb.setLength(0);
            } else {
                sb.append(c);
            }
        }

        if (!sb.isEmpty()) {
            authors.add(sb.toString().trim());
        }
        return authors;
    }


    public static List<String> extractRoles(String authorToken) {
        List<String> roles = new ArrayList<>();
        Pattern pattern = Pattern.compile("\\(([^)]+)\\)");
        Matcher matcher = pattern.matcher(authorToken);

        while (matcher.find()) {
            String roleGroup = matcher.group(1);
            for (String role : roleGroup.split(",")) {
                roles.add(role.trim());
            }
        }
        return roles;
    }

    public static String extractAuthorName(String authorToken) {
        return authorToken.replaceAll("\\([^)]*\\)", "").trim();
    }

    public void deleteAuthor(Long id) {
        Author author = authorRepository.getAuthorById(id);
        if (author == null) {
            throw new AuthorNotFoundException("Author with the following id does not exist: " + id);
        }

        if (!author.getBooks().isEmpty())
            throw new IllegalStateException("Cannot delete author: it still has books associated.");

        authorRepository.delete(author);
    }

    public Author getAuthorById(Long id) {
        return authorRepository.getAuthorById(id);
    }

    public AuthorResponseDto getAuthorResponseById(Long id) {
        Author author = authorRepository.getAuthorById(id);

        if (author == null) {
            throw new AuthorNotFoundException("Author with the following id does not exist: " + id);
        }

        return new AuthorResponseDto(author.getId(), author.getName());
    }
}
