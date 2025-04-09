package com.arman.internshipbookstore.service;

import com.arman.internshipbookstore.persistence.entity.*;
import com.arman.internshipbookstore.service.dto.*;
import com.arman.internshipbookstore.service.exception.*;
import com.arman.internshipbookstore.enums.*;
import lombok.RequiredArgsConstructor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.Year;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

@Service
@RequiredArgsConstructor
public class CsvUploadService {
    private final AuthorService authorService;
    private final AwardService awardService;
    private final BookService bookService;
    private final BookAuthorService bookAuthorService;
    private final PublisherService publisherService;
    private final CharacterService characterService;

    private static final List<DateTimeFormatter> DATE_FORMATTERS = List.of(
            DateTimeFormatter.ofPattern("M/d/yy"),
            DateTimeFormatter.ofPattern("MMMM d yyyy", Locale.ENGLISH),
            DateTimeFormatter.ofPattern("MMMM yyyy", Locale.ENGLISH),
            DateTimeFormatter.ofPattern("yyyy")
    );



    public void uploadFile(MultipartFile file) {
        try (BufferedReader bf = new BufferedReader(new InputStreamReader(file.getInputStream(), "UTF-8"))){
            CSVFormat format = CSVFormat.Builder.create().setHeader().setSkipHeaderRecord(true).get();
            CSVParser csvParser = CSVParser.parse(bf, format);
            Iterable<CSVRecord> csvRecords = csvParser.getRecords();

            for (CSVRecord csvRecord : csvRecords) {

                try {
                    BookDto bookDto = bookDtoInitializer(csvRecord);

                    String authorName = csvRecord.get("author");
                    String genres_ = csvRecord.get("genres");
                    String characters_ = csvRecord.get("characters");
                    String publisher_name = csvRecord.get("publisher");
                    String awards_ = csvRecord.get("awards");
                    String coverImg = csvRecord.get("coverImg");


                    Publisher publisher = createOrGetPublisher(publisher_name);

                    Book book = bookService.save(bookDto);

                    List<AuthorDto> authorDtos = createAuthorDtosList(authorName);
                    createBookAuthor(book, authorDtos);

                    List<Characters> characters = createOrGetCharacters(removeSquareBrackets(characters_));
                    setBookCharacters(book, characters);

                    List<Award> awards = createOrGetAwards(awards_);
                    setBookAwards(book, awards);

                    List<Genre> genres = createOrGetGenresList(genres_);
                    setBookGenres(book, genres);
                } catch (RuntimeException e) {
                    System.out.println(e.getMessage());
                }

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void setBookGenres(Book book, List<Genre> genres) {
        Set<Genre> genreSet = book.getGenres();
        if(genreSet==null) genreSet = new HashSet<>();
        genreSet.addAll(genres);

        book.setGenres(genreSet);
    }

    private void setBookAwards(Book book, List<Award> awards) {
        Set<Award> awardsSet = book.getAwards();
        if(awardsSet==null) awardsSet=new HashSet<>();
        awardsSet.addAll(awards);

        book.setAwards(awardsSet);
    }

    private void setBookCharacters(Book book, List<Characters> characters) {
        Set<Characters> charactersSet = book.getCharacters();
        if(charactersSet==null) charactersSet = new HashSet<>();
        charactersSet.addAll(characters);

        book.setCharacters(charactersSet);
    }

    private List<Genre> createOrGetGenresList(String genres) {
        List<Genre> genreList = new ArrayList<>();

        genres = removeSquareBrackets(genres);

        String[] genre = genres.split(", ");

        for (String genreName : genre) {
            Genre genre_ = Genre.fromString(removeSingleQuotes(genreName));

            genreList.add(genre_);
        }

        return genreList;
    }

    private String removeSingleQuotes(String str){
        String result = str;
        if(result.startsWith("'")) result = result.substring(1);
        if(result.endsWith("'")) result = result.substring(0,result.length()-1);

        return result;
    }

    private String removeSquareBrackets(String str) {
        return str.replace("[","").replace("]","");
    }

    private List<Award> createOrGetAwards(String awards) {
        List<Award> awardList = new ArrayList<>();

        awards = removeSquareBrackets(awards);

        String[] award = awards.split(", ");

        for (String awardName : award) {
            AwardDto awardDto = new AwardDto();
            String aw = removeSingleQuotes(awardName);
            if(aw.isEmpty()) continue;

            awardDto.setName(aw);

            awardList.add(awardService.save(awardDto));
        }

        return awardList;
    }

    private List<Characters> createOrGetCharacters(String characters) {
        List<Characters> characterList = new ArrayList<>();

        String[] character = characters.split(", ");

        for (String characterName : character) {
            CharacterDto characterDto = new CharacterDto();
            characterDto.setName(removeSingleQuotes(characterName));

            characterList.add(characterService.save(characterDto));
        }
        return characterList;
    }

    private void createBookAuthor(Book book, List<AuthorDto> authorDtos) {
        for (AuthorDto authorDto : authorDtos) {
            bookAuthorService.save(book,authorDto);
        }
    }

    private Publisher createOrGetPublisher(String publisher_name) {
        PublisherDto publisherDto = new PublisherDto();
        publisherDto.setName(publisher_name);

        return publisherService.save(publisherDto);
    }


    private BookDto bookDtoInitializer(CSVRecord csvRecord) throws IsbnIncorrectFormatException, IsbnDuplicationException, IncorrectRatingFormatException {
        String bookId = csvRecord.get("bookId");

        if(bookService.getBookByBookId(bookId)!=null)
            throw new BookAlreadyExistsException("Book with the given id already exists");
        String isbn_ = csvRecord.get("isbn");
        Long isbn = validateISBN(isbn_);

        String pages_ = csvRecord.get("pages");
        Integer pages = validatePages(pages_);

        String bookTitle = csvRecord.get("title");
        String series = csvRecord.get("series");
        String rating_ = csvRecord.get("rating");

        Double rating = validateRating(rating_);

        String description = csvRecord.get("description");
        String language = csvRecord.get("language");

        String bookFormat = csvRecord.get("bookFormat");
        String edition = csvRecord.get("edition");

        String publishDate_ = csvRecord.get("publishDate");
        String firstPublishDate_ = csvRecord.get("firstPublishDate");



        LocalDate publishDate = dateSetup(publishDate_);
        LocalDate firstPublishDate = dateSetup(firstPublishDate_);

        if(firstPublishDate!=null){
            if(firstPublishDate.isAfter(publishDate)) {
                assert publishDate != null;
                publishDate = publishDate.minusYears(100);
            }
        }

        String numRatings_ = csvRecord.get("numRatings");

        Integer numRatings = Integer.parseInt(numRatings_);

        String likedPercent_ = csvRecord.get("likedPercent");
        Integer likedPercent = validateLikedPercent(likedPercent_);

        String setting = csvRecord.get("setting");
        setting = removeSquareBrackets(setting);
        String bbeScore_ = csvRecord.get("bbeScore");

        Integer bbeScore = validateBbeScore(bbeScore_);

        String bbeVotes_ = csvRecord.get("bbeVotes");

        Integer bbeVotes = validateBbeVotes(bbeVotes_);

        String price_ = csvRecord.get("price");

        Double price = validatePrice(price_);

        String publisher = csvRecord.get("publisher");

        String ratingsByStars = csvRecord.get("ratingsByStars");
        ratingsByStars = removeSquareBrackets(ratingsByStars);

        BookDto bookDto = new BookDto();

        bookDto.setBookId(bookId);
        bookDto.setTitle(bookTitle);
        bookDto.setIsbn(isbn);
        bookDto.setPages(pages);
        bookDto.setSeries(series);
        bookDto.setRating(rating);
        bookDto.setDescription(description);
        bookDto.setLanguage(language);
        bookDto.setFormat(bookFormat);
        bookDto.setEdition(edition);
        bookDto.setPublishDate(publishDate);
        bookDto.setFirstPublishDate(firstPublishDate);
        bookDto.setNumRatings(numRatings);
        bookDto.setLikedPercent(likedPercent);
        bookDto.setSetting(setting);
        bookDto.setBbeScore(bbeScore);
        bookDto.setBbeVotes(bbeVotes);
        bookDto.setPrice(price);
        bookDto.setPublisherName(publisher);
        bookDto.setRatingsByStars(ratingsByStars);


        return bookDto;
    }

    private LocalDate dateSetup(String publishDate) {
        if (publishDate == null || publishDate.isBlank()) return null;

        publishDate = publishDate.replaceAll("(?<=\\d)(st|nd|rd|th)", "").trim();

        for (DateTimeFormatter formatter : DATE_FORMATTERS) {
            try {
                if (formatter.toString().equals(DateTimeFormatter.ofPattern("yyyy").toString())) {
                    Year year = Year.parse(publishDate,formatter);
                    return year.atDay(1);
                } else if (formatter.toString().equals(DateTimeFormatter.ofPattern("MMMM yyyy", Locale.ENGLISH).toString())) {
                    YearMonth yearMonth = YearMonth.parse(publishDate, formatter);
                    return yearMonth.atDay(1);
                } else {
                    LocalDate date = LocalDate.parse(publishDate, formatter);
                    if (date.getYear() > LocalDate.now().getYear()) {
                        return date.minusYears(100);
                    }
                    return date;
                }
            } catch (DateTimeParseException ignored) {
            }
        }

        throw new InvalidPublicationDateException("Book must have a publication date specified.");
    }

    private Double validatePrice(String price_) {
        if(price_.isEmpty()) return null;
        Double price;
        try {
            price = Double.parseDouble(price_);
            if(price<0) throw new NumberFormatException();
        } catch (NumberFormatException e){
            throw new IncorrectPriceFormatException("Price should be of positive numeric value");
        }

        return price;
    }

    private Integer validateBbeVotes(String bbeVotes_) {
        Integer bbeVotes;
        try {
            bbeVotes = Integer.parseInt(bbeVotes_);
            if(bbeVotes<0) throw new NumberFormatException();
        } catch (NumberFormatException e){
            throw new IncorrectBbeVotesFormatException("Bbe Votes should be of positive numeric value");
        }

        return bbeVotes;
    }

    private Integer validateBbeScore(String bbeScore_) {
        Integer bbeScore;
        try {
            bbeScore = Integer.parseInt(bbeScore_);
            if(bbeScore<0) throw new NumberFormatException();
        } catch (NumberFormatException e){
            throw new IncorrectBbeScoreFormatException("Bbe Score should be of positive numeric value");
        }

        return bbeScore;
    }

    private Integer validateLikedPercent(String likedPercent_){
        Integer likedPercent;
        try{
            likedPercent = Integer.parseInt(likedPercent_);
            if(likedPercent<0 || likedPercent > 100) throw new NumberFormatException();
        } catch (NumberFormatException e){
            throw new IncorrectPercentageFormatException("The percentage should be of numeric value between 0 and 100");
        }

        return likedPercent;
    }

    private Double validateRating(String rating_) throws IncorrectRatingFormatException {
        Double rating;
        try{
            rating = Double.parseDouble(rating_);
            if(rating<0 || rating>5) throw new NumberFormatException();
        } catch (NumberFormatException e){
            throw new IncorrectRatingFormatException("Rating should be a numeric value and be between 0 and 5");
        }
        return rating;
    }

    private Integer validatePages(String pages_) {
        Integer pages;
        try{
            pages = Integer.parseInt(pages_);
            if(pages<=0) throw new NumberFormatException();

        } catch (NumberFormatException e){
            throw new IncorrectPageFormatException("Page count should contain numbers only and be greater than 0");
        }
        return pages;
    }

    private List<AuthorDto> createAuthorDtosList(String authorName) {
        List<AuthorDto> authorDtosList = new ArrayList<>();
        String[] authors = authorName.split(", ");

        for (String author_name : authors) {
            AuthorDto authorDto = new AuthorDto();

            authorDto.setName(author_name);

            authorDtosList.add(authorDto);
        }

        return authorDtosList;
    }


    private Long validateISBN(String isbn) throws IsbnIncorrectFormatException, IsbnDuplicationException {
        Long isbnLong;

        if(isbn.length()==13 && isbn.replaceAll("\\d","").isEmpty())
            isbnLong = Long.valueOf(isbn);
        else throw new IsbnIncorrectFormatException("ISBN should contain numeric values only and be of length 13.");

        if(bookService.getBookByIsbn(isbnLong)==null) return isbnLong;
        else throw new IsbnDuplicationException("Book with the following ISBN already exists: "+isbn);
    }

}