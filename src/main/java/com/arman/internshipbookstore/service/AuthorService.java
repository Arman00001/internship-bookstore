package com.arman.internshipbookstore.service;

import com.arman.internshipbookstore.service.dto.AuthorDto;
import com.arman.internshipbookstore.persistence.entity.Author;
import com.arman.internshipbookstore.persistence.repository.AuthorRepository;
import com.arman.internshipbookstore.service.exception.AuthorAlreadyExistsException;
import com.arman.internshipbookstore.service.exception.AuthorNotFoundException;
import com.arman.internshipbookstore.service.mapper.AuthorMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class AuthorService {

    private final AuthorRepository authorRepository;
    private final AuthorMapper authorMapper;

    public Author getAuthorByName(String name) {
        Author author = authorRepository.getAuthorByName(name);

        return author;
    }

    public Set<String> findAllNames() {
        return authorRepository.findAllNames();
    }

    public List<Author> findAll() {
        return authorRepository.findAll();
    }

    public AuthorDto addAuthor(AuthorDto authorDto) {
        Author author = authorRepository.getAuthorByName(authorDto.getName());
        if (author != null) {
            throw new AuthorAlreadyExistsException("Author with the following name already exists: " + authorDto.getName());
        }

        author = authorMapper.mapDtoToAuthor(authorDto);

        Author author1 = authorRepository.save(author);

        return authorMapper.mapToDto(author1);
    }


    static List<String> splitAuthors(String authorName) {
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

        if (sb.length() > 0) {
            authors.add(sb.toString().trim());
        }
        return authors;
    }


    static List<String> extractRoles(String authorToken) {
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

    static String extractAuthorName(String authorToken) {
        return authorToken.replaceAll("\\([^)]*\\)", "").trim();
    }

    public void delete(Long id) {
        Author author = authorRepository.getAuthorById(id);
        if (author == null) {
            throw new AuthorNotFoundException("Author with the following id does not exist: " + id);
        }

        if (!author.getBooks().isEmpty())
            throw new IllegalStateException("Cannot delete author: it still has books associated.");

        authorRepository.delete(author);
    }
}
