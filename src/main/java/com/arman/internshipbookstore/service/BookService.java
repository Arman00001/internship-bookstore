package com.arman.internshipbookstore.service;

import com.arman.internshipbookstore.persistence.entity.*;
import com.arman.internshipbookstore.persistence.repository.BookAuthorRepository;
import com.arman.internshipbookstore.persistence.repository.BookAwardRepository;
import com.arman.internshipbookstore.persistence.repository.BookRepository;
import com.arman.internshipbookstore.service.dto.AuthorResponseDto;
import com.arman.internshipbookstore.service.dto.PageResponseDto;
import com.arman.internshipbookstore.service.dto.award.AwardResponseDto;
import com.arman.internshipbookstore.service.dto.book.BookDto;
import com.arman.internshipbookstore.service.criteria.BookSearchCriteria;
import com.arman.internshipbookstore.service.dto.book.BookResponseDto;
import com.arman.internshipbookstore.service.dto.book.BookSummaryResponseDto;
import com.arman.internshipbookstore.service.dto.book.BookUpdateDto;
import com.arman.internshipbookstore.service.dto.character.CharacterDto;
import com.arman.internshipbookstore.service.dto.character.CharacterResponseDto;
import com.arman.internshipbookstore.service.dto.publisher.PublisherResponseDto;
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


    public BookDto addBook(BookDto bookDto) {
        validateIsbn(bookDto.getIsbn());

        Book book = bookMapper.mapDtoToBook(bookDto);

        setBookPublisher(book, bookDto.getPublisherName());
        setBookAuthors(book, bookDto.getAuthorNames());
        if(bookDto.getAwards()!=null && !bookDto.getAwards().isBlank())
            setBookAwards(book, bookDto.getAwards());
        if(bookDto.getCharacters()!=null && !bookDto.getCharacters().isBlank())
            setBookCharacters(book, bookDto.getCharacters());
        book.setGenres(bookDto.getGenres());


        Book book1 = bookRepository.save(book);

        return bookMapper.mapToDto(book1);
    }

    public PageResponseDto<BookSummaryResponseDto> searchBooks(BookSearchCriteria criteria){
        List<Long> ids = bookRepository.findAllCriteriaIds(criteria);
        Page<BookSummaryResponseDto> page = bookRepository.findAllById(ids, criteria.buildPageRequest());

        page.getContent().forEach(bookSummaryResponseDto -> {
            List<String> authorNames = setBookResponseAuthors(bookSummaryResponseDto.getId()).stream()
                    .map(AuthorResponseDto::getName).toList();
            bookSummaryResponseDto.setAuthorNames(authorNames);
        });

        return PageResponseDto.from(page);
    }


    @Transactional
    public BookDto updateBook(BookUpdateDto bookUpdateDto) {
        Book book = bookRepository.findById(bookUpdateDto.getId()).
                orElseThrow(()->new BookNotFoundException("Book with the following id does not exist: "+ bookUpdateDto.getId()));

        String description = bookUpdateDto.getDescription();
        Integer starRating = bookUpdateDto.getStarRating();
        String award = bookUpdateDto.getAward();
        String characterName = bookUpdateDto.getCharacter();

        if(description!=null && !description.isBlank()){
            book.setDescription(description);
        }
        if(starRating!=null){
            if(starRating>0 && starRating<=5) {
                Integer[] stars = new Integer[5];
                Arrays.fill(stars,0);
                stars[starRating - 1] = 1;
                book.addStarRatings(stars);
            } else throw new IncorrectStarRatingsFormat("Star rating value should be between 1 and 5");
        }
        if(award!=null && !award.isBlank()){
            setBookAwards(book,award);
        }
        if(characterName!=null && !characterName.isBlank()){
            Characters character = characterService.getCharacterByName(characterName);
            if(character==null){
                CharacterDto characterDto = new CharacterDto();
                characterDto.setName(characterName);

                character =characterService.save(characterDto);
            }
            book.addCharacter(character);
        }
        Book book1 = bookRepository.save(book);

        return bookMapper.mapToDto(book1);
    }

    @Transactional
    public void deleteBook(Long id) {

        Book book = bookRepository.getBookById(id);

        if(book==null)
            throw new BookNotFoundException("Book with the following id does not exist: "+id);

        Set<Characters> characters = book.getCharacters();
        for (Characters character : characters) {
            Set<Book> books = character.getBooks();
            if(books.size()>1){
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

        for(BookAward bookAward : book.getBookAwards()){
            bookAward.getAward().getBookAwards().remove(bookAward);
        }

        book.getBookAwards().clear();

        book.getGenres().clear();

        bookRepository.delete(book);
    }



    public BookResponseDto getBookById(Long bookId){

        BookResponseDto bookResponseDto = bookRepository.getBookResponseById(bookId);

        List<AuthorResponseDto> authorResponseDtos = setBookResponseAuthors(bookId);
        List<AwardResponseDto> awardResponseDtos = setBookResponseAwards(bookId);

        bookResponseDto.setAuthors(authorResponseDtos);
        bookResponseDto.setAwards(awardResponseDtos);

        return bookResponseDto;
    }


    private List<AwardResponseDto> setBookResponseAwards(Long bookId) {

        List<AwardResponseDto> awardResponseDtos = bookAwardRepository.getBookAwardsByBook_Id(bookId).stream()
                .map(bookAward -> {
                    Award award = bookAward.getAward();
                    return new AwardResponseDto(award.getId(), award.getName(), bookAward.getYear());
                })
                .toList();

        return awardResponseDtos;
    }

    private List<AuthorResponseDto> setBookResponseAuthors(Long bookId) {

        List<AuthorResponseDto> authorResponseDtos = bookAuthorRepository.getBookAuthorsByBook_Id(bookId).stream()
                .map(bookAuthor -> {
                    Author author = bookAuthor.getAuthor();
                    return new AuthorResponseDto(author.getId(), author.getName(), bookAuthor.getRole());
                })
                .toList();

        return authorResponseDtos;
    }


    public Set<Book> findAllWithoutImageDownloaded(){
        return bookRepository.findAllWithoutImageDownloaded();
    }

    public Set<Long> findAllIsbn(){
        return bookRepository.findAllIsbn();
    }

    public void saveAll(Set<Book> books) {
        bookRepository.saveAll(books);
    }


    private void setBookCharacters(Book book, String characters) {
        List<Characters> charactersList = createOrGetCharacters(characters);

        for (Characters character : charactersList) {
            book.addCharacter(character);
        }
    }

    private void setBookPublisher(Book book, String publisherName) {
        Publisher publisher = publisherService.getPublisherByName(publisherName);
        if (publisher == null)
            throw new PublisherNotFoundException("Publisher with the following name is not found: "+publisherName);

        publisher.addBook(book);
    }

    private void setBookAuthors(Book book, String authorNames) {
        List<String> authorNameRoles = AuthorService.splitAuthors(authorNames);

        for (String author_ : authorNameRoles) {
            String name = AuthorService.extractAuthorName(author_);
            Author author = authorService.getAuthorByName(name);

            if (author == null)
                throw new AuthorNotFoundException("Author with the following name is not found: "+name);

            List<String> roles = AuthorService.extractRoles(author_);
            if (roles.isEmpty()) book.addBookAuthor(author, "Author");
            else {
                for (String role : roles) {
                    book.addBookAuthor(author, role);
                }
            }
        }
    }

    private void setBookAwards(Book book, String awards) {
        Map<Award, List<Integer>> awardYearsMap = getAwardsMap(awards);

        for(Award award : awardYearsMap.keySet()){
            for (Integer year : awardYearsMap.get(award)) {
                book.addAward(award, year);
            }
        }
    }


    private Map<Award, List<Integer>> getAwardsMap(String awardString) {
        Map<Award, List<Integer>> awardYearMap = new HashMap<>();

        awardString = awardString.trim().replace("[","").replace("]","");

        for (String awardName : awardString.split(",")) {
            List<String> awardsList = AwardService.splitAwards(awardName.trim());
            for (String awardToken : awardsList) {
                List<Integer> years = AwardService.extractAwardYears(awardToken);

                String cleanAwardName = AwardService.removeYearInfo(awardToken).trim();

                Award award = awardService.getAwardByName(cleanAwardName);
                if (award == null)
                    throw new AwardNotFoundException("Award with the following name is not found: " + cleanAwardName);

                if (awardYearMap.containsKey(award)) {
                    awardYearMap.get(award).addAll(years);
                } else {
                    awardYearMap.put(award, new ArrayList<>(years));
                }
            }
        }

        return awardYearMap;
    }

    private List<Characters> createOrGetCharacters(String characterNames) {
        List<Characters> characterList = new ArrayList<>();

        String[] characters = characterNames.split(", ");

        for (String characterName : characters) {
            String name = removeSingleQuotes(characterName);
            Characters character = characterService.getCharacterByName(name);
            if(character==null){
                character = new Characters();
                character.setName(name);
            }
            characterList.add(character);
        }

        return characterList;
    }

    private void validateIsbn(Long isbn){
        if(isbn==null || Long.toString(isbn).length()!=13 || isbn<=0)
            throw new IsbnIncorrectFormatException("ISBN should contain numeric values only and be of length 13.");

        else if(bookRepository.getBookByIsbn(isbn)!=null)
            throw new IsbnDuplicationException("Book with the following ISBN already exists: "+isbn);
    }

}