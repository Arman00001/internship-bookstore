package com.arman.internshipbookstore.service;

import com.arman.internshipbookstore.persistence.entity.*;
import com.arman.internshipbookstore.service.dto.book.BookDto;
import com.arman.internshipbookstore.service.mapper.BookMapper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

import static com.arman.internshipbookstore.service.mapper.CsvBookDtoMapper.initializeBookDto;
import static com.arman.internshipbookstore.service.mapper.CsvBookEntityMapper.*;
import static com.arman.internshipbookstore.service.util.StringUtils.*;

@Service
@RequiredArgsConstructor
public class CsvUploadService {
    private final AuthorService authorService;
    private final AwardService awardService;
    private final BookService bookService;
    private final PublisherService publisherService;
    private final CharacterService characterService;

    private final BookMapper bookMapper;


    public void uploadFile(MultipartFile file) {
        try (BufferedReader bf = new BufferedReader(new InputStreamReader(file.getInputStream(), "UTF-8"))) {
            CSVFormat format = CSVFormat.Builder.create().setHeader().setSkipHeaderRecord(true).get();
            CSVParser csvParser = CSVParser.parse(bf, format);
            Iterable<CSVRecord> csvRecords = csvParser.getRecords();

            List<Author> authors = authorService.findAll();
            List<Award> awardList = awardService.findAll();
            List<Characters> charactersList = characterService.findAll();
            List<Publisher> publisherList = publisherService.findAll();
            Set<Book> books = new HashSet<>();

            Set<Long> isbnSet = bookService.findAllIsbn();
            Map<String, Author> authorMap = new HashMap<>();
            Map<String, Award> awardMap = new HashMap<>();
            Map<String, Characters> charactersMap = new HashMap<>();
            Map<String, Publisher> publisherMap = new HashMap<>();


            for (Author author : authors) {
                authorMap.put(author.getName(), author);
            }

            for (Award award : awardList) {
                awardMap.put(award.getName(), award);
            }

            for (Characters character : charactersList) {
                charactersMap.put(character.getName(), character);
            }

            for (Publisher publisher : publisherList) {
                publisherMap.put(publisher.getName(), publisher);
            }


            for (CSVRecord csvRecord : csvRecords) {

                try {
                    BookDto bookDto = initializeBookDto(csvRecord, isbnSet);

                    String authorName = csvRecord.get("author");
                    String genres_ = csvRecord.get("genres");
                    String characters_ = csvRecord.get("characters");
                    String publisher_name = csvRecord.get("publisher");
                    String awards_ = csvRecord.get("awards");
                    String coverImg = csvRecord.get("coverImg");

                    Book book = bookMapper.mapDtoToBook(bookDto);

                    setBookPublisher(book, publisherMap, publisher_name);
                    setBookAuthors(book, authorMap, authorName);
                    setBookCharacters(book, charactersMap, removeSquareBrackets(characters_));
                    setBookAwards(book, awardMap, awards_);
                    setBookGenres(book, genres_);

                    if (!coverImg.isBlank()) {
                        book.setImagePath("Download " + coverImg);
                    }

                    books.add(book);

                } catch (RuntimeException e) {
                    System.out.println(e.getMessage());
                }


            }
            bookService.saveAll(books);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}