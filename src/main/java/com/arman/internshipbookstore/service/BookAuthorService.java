package com.arman.internshipbookstore.service;

import com.arman.internshipbookstore.persistence.entity.Author;
import com.arman.internshipbookstore.persistence.entity.Book;
import com.arman.internshipbookstore.persistence.entity.BookAuthor;
import com.arman.internshipbookstore.persistence.repository.BookAuthorRepository;
import com.arman.internshipbookstore.service.dto.AuthorDto;
import com.arman.internshipbookstore.service.mapper.BookAuthorMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class BookAuthorService {
    private final AuthorService authorService;
    private final BookAuthorRepository bookAuthorRepository;
    private final BookAuthorMapper bookAuthorMapper;

    public void save(Book book, AuthorDto authorDto){
        String authorDtoName = authorDto.getName();

        List<String> roles = new ArrayList<>();

        Matcher matcher = Pattern.compile("\\(([^)]+)\\)").matcher(authorDtoName);
        while (matcher.find()) {
            roles.add(matcher.group(1).trim());
        }

        // Remove all role parts from name
        String author_name = authorDtoName.replaceAll("\\([^)]*\\)", "").trim();

        authorDto.setName(author_name);

        Author author = authorService.save(authorDto);
        if(roles.isEmpty()) roles.add("Author");

        for(String role : roles) {
            BookAuthor bookAuthor = new BookAuthor();
            bookAuthor.setBook(book);
            bookAuthor.setAuthor(author);
            bookAuthor.setRole(role);

            bookAuthorRepository.save(bookAuthor);
        }
    }

}
