package com.arman.internshipbookstore.service;

import com.arman.internshipbookstore.persistence.entity.*;
import com.arman.internshipbookstore.persistence.repository.BookAuthorRepository;
import com.arman.internshipbookstore.persistence.repository.BookAwardRepository;
import com.arman.internshipbookstore.persistence.repository.BookRepository;
import com.arman.internshipbookstore.service.dto.author.AuthorOfBookResponseDto;
import com.arman.internshipbookstore.service.dto.PageResponseDto;
import com.arman.internshipbookstore.service.dto.award.AwardOfBookResponseDto;
import com.arman.internshipbookstore.service.dto.book.*;
import com.arman.internshipbookstore.service.criteria.BookSearchCriteria;
import com.arman.internshipbookstore.service.dto.character.CharacterDto;
import com.arman.internshipbookstore.service.exception.*;
import com.arman.internshipbookstore.service.mapper.BookMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static com.arman.internshipbookstore.service.util.StringUtils.removeSingleQuotes;

@Service
@RequiredArgsConstructor
public class BookService {
    private final AuthorService authorService;
    private final AwardService awardService;
    private final BookRepository bookRepository;
    private final BookAuthorRepository bookAuthorRepository;
    private final BookMapper bookMapper;
    private final CharacterService characterService;
    private final PublisherService publisherService;
    private final BookAwardRepository bookAwardRepository;


    public BookDto addBook(BookCreateDto bookCreateDto) {
        validateIsbn(bookCreateDto.getIsbn());

        Book book = bookMapper.mapCreateDtoToBook(bookCreateDto);

        setBookPublisher(book, bookCreateDto.getPublisherId());
        setBookAuthors(book, bookCreateDto.getAuthorIdRoles());
        setBookAwards(book, bookCreateDto.getAwards());
        setBookCharacters(book, bookCreateDto.getCharacters());
        book.setGenres(bookCreateDto.getGenres());

        Book book1 = bookRepository.save(book);

        return bookMapper.mapToDto(book1);
    }

    public PageResponseDto<BookSummaryResponseDto> searchBooks(BookSearchCriteria criteria) {
        List<Long> ids = bookRepository.findAllCriteriaIds(criteria);
        Page<BookSummaryResponseDto> page = bookRepository.findAllById(ids, criteria.buildPageRequest());

        page.getContent().forEach(bookSummaryResponseDto -> {
            List<String> authorNames = setBookResponseAuthors(bookSummaryResponseDto.getId()).stream()
                    .map(AuthorOfBookResponseDto::getName).toList();
            bookSummaryResponseDto.setAuthorNames(authorNames);
        });

        return PageResponseDto.from(page);
    }


    @Transactional
    public BookDto updateBook(BookUpdateDto bookUpdateDto) {
        Book book = bookRepository.findById(bookUpdateDto.getId()).
                orElseThrow(() -> new BookNotFoundException("Book with the following id does not exist: " + bookUpdateDto.getId()));

        String description = bookUpdateDto.getDescription();
        Integer starRating = bookUpdateDto.getStarRating();
        String characterName = bookUpdateDto.getCharacter();

        if (description != null && !description.isBlank()) {
            book.setDescription(description);
        }
        if (starRating != null) {
            if (starRating > 0 && starRating <= 5) {
                Integer[] stars = new Integer[5];
                Arrays.fill(stars, 0);
                stars[starRating - 1] = 1;
                book.addStarRatings(stars);
            } else throw new IncorrectStarRatingsFormat("Star rating value should be between 1 and 5");
        }
        setBookAwards(book, bookUpdateDto.getAward());

        if (characterName != null && !characterName.isBlank()) {
            characterService.assignCharactersOfBook(book,characterName);
        }

        Book book1 = bookRepository.save(book);

        return bookMapper.mapToDto(book1);
    }

    @Transactional
    public void deleteBook(Long id) {

        Book book = bookRepository.getBookById(id);

        if (book == null)
            throw new BookNotFoundException("Book with the following id does not exist: " + id);

        Set<Characters> characters = book.getCharacters();
        for (Characters character : characters) {
            Set<Book> books = character.getBooks();
            if (books.size() > 1) {
                books.remove(book);
                continue;
            }
            characterService.deleteCharacterOfBook(character);
        }

        book.getCharacters().clear();


        for (BookAuthor bookAuthor : book.getBookAuthors()) {
            bookAuthor.getAuthor().getBooks().remove(bookAuthor);
        }

        book.getBookAuthors().clear();

        for (BookAward bookAward : book.getBookAwards()) {
            bookAward.getAward().getBookAwards().remove(bookAward);
        }

        book.getBookAwards().clear();

        book.getGenres().clear();

        bookRepository.delete(book);
    }


    public BookResponseDto getBookById(Long bookId) {

        BookResponseDto bookResponseDto = bookRepository.getBookResponseById(bookId);

        List<AuthorOfBookResponseDto> authorOfBookResponseDtos = setBookResponseAuthors(bookId);
        List<AwardOfBookResponseDto> awardOfBookResponseDtos = setBookResponseAwards(bookId);

        bookResponseDto.setAuthors(authorOfBookResponseDtos);
        bookResponseDto.setAwards(awardOfBookResponseDtos);

        return bookResponseDto;
    }


    private List<AwardOfBookResponseDto> setBookResponseAwards(Long bookId) {

        List<AwardOfBookResponseDto> awardOfBookResponseDtos = bookAwardRepository.getBookAwardsByBook_Id(bookId).stream()
                .map(bookAward -> {
                    Award award = bookAward.getAward();
                    return new AwardOfBookResponseDto(award.getId(), award.getName(), bookAward.getYear());
                })
                .toList();

        return awardOfBookResponseDtos;
    }

    private List<AuthorOfBookResponseDto> setBookResponseAuthors(Long bookId) {

        List<AuthorOfBookResponseDto> authorOfBookResponseDtos = bookAuthorRepository.getBookAuthorsByBook_Id(bookId).stream()
                .map(bookAuthor -> {
                    Author author = bookAuthor.getAuthor();
                    return new AuthorOfBookResponseDto(author.getId(), author.getName(), bookAuthor.getRole());
                })
                .toList();

        return authorOfBookResponseDtos;
    }


    public Set<Book> findAllWithoutImageDownloaded() {
        return bookRepository.findAllWithoutImageDownloaded();
    }

    public Set<Long> findAllIsbn() {
        return bookRepository.findAllIsbn();
    }

    public void saveAll(Set<Book> books) {
        bookRepository.saveAll(books);
    }


    private void setBookCharacters(Book book, String characters) {
        if (characters != null && !characters.isBlank()) {
            characterService.assignCharactersOfBook(book, characters);
        }
    }

    private void setBookPublisher(Book book, Long publisherId) {
        publisherService.assignPublisherToBook(book, publisherId);
    }

    private void setBookAuthors(Book book, Map<String, List<String>> authorIdRoles) {
        authorIdRoles.forEach((key, value) -> {
            Long id = Long.valueOf(key);
            authorService.assignAuthorsOfBook(book, id, value);
        });
    }

    private void setBookAwards(Book book, String awards) {
        if (awards != null && !awards.isBlank()) {
            awardService.assignAwardsOfBook(book, awards);
        }
    }


    private void validateIsbn(Long isbn) {
        if (isbn == null || Long.toString(isbn).length() != 13 || isbn <= 0)
            throw new IsbnIncorrectFormatException("ISBN should contain numeric values only and be of length 13.");

        else if (bookRepository.getBookByIsbn(isbn) != null)
            throw new IsbnDuplicationException("Book with the following ISBN already exists: " + isbn);
    }

}