package com.arman.internshipbookstore.service;

import com.arman.internshipbookstore.persistence.entity.*;
import com.arman.internshipbookstore.service.dto.*;
import com.arman.internshipbookstore.service.exception.*;
import com.arman.internshipbookstore.enums.*;
import com.arman.internshipbookstore.service.mapper.AsyncImageDownloaderService;
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
import java.time.LocalDate;
import java.time.Year;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class CsvUploadService {
    private final AuthorService authorService;
    private final AwardService awardService;
    private final BookService bookService;
    private final PublisherService publisherService;
    private final CharacterService characterService;

    private final BookMapper bookMapper;
    private final AsyncImageDownloaderService asyncImageDownloaderService;

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

            List<Author> authors = authorService.findAll();
            List<Award> awardList = awardService.findAll();
            List<Characters> charactersList = characterService.findAll();
            List<Publisher> publisherList = publisherService.findAll();
            List<Book> books = new ArrayList<>();

            Set<Long> isbnSet = bookService.findAllIsbn();
            Map<String, Author> authorMap = new HashMap<>();
            Map<String, Award> awardMap = new HashMap<>();
            Map<String, Characters> charactersMap = new HashMap<>();
            Map<String, Publisher> publisherMap = new HashMap<>();


            for (Author author : authors) {
                authorMap.put(author.getName(),author);
            }

            for (Award award : awardList) {
                awardMap.put(award.getName(),award);
            }

            for (Characters character : charactersList) {
                charactersMap.put(character.getName(), character);
            }

            for(Publisher publisher : publisherList){
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

                    if(!coverImg.isBlank())
                        book.setImagePath("Download " + coverImg);

                    books.add(book);

                } catch (RuntimeException e) {
                    System.out.println(e.getMessage());
                }


            }
            bookService.saveAll(books);

            asyncImageDownloaderService.scheduleImageDownloads();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void setBookPublisher(Book book, Map<String,Publisher> publisherMap, String publisher_name){
        Publisher publisher = publisherMap.get(publisher_name);
        if(publisher==null) {
            publisher = new Publisher();
            publisher.setName(publisher_name);
            publisherMap.put(publisher_name,publisher);
        }

        publisher.addBook(book);
    }

    private void setBookGenres(Book book, String genres_) {
        List<Genre> genres = createOrGetGenresList(genres_);
        Set<Genre> genreSet = book.getGenres();
        if(genreSet==null) genreSet = new HashSet<>();
        genreSet.addAll(genres);

        book.setGenres(genreSet);
    }

    private void setBookAwards(Book book, Map<String, Award> awardMap, String award_) {
        Map<Award, List<Integer>> awardYearsMap = getAwardsMap(awardMap, award_);

        for(Award award : awardYearsMap.keySet()){
            for (Integer year : awardYearsMap.get(award)) {
                book.addAward(award, year);
            }
        }
    }

    private void setBookCharacters(Book book, Map<String,Characters> charactersMap, String characters_) {
        List<Characters> charactersList = createOrGetCharacters(charactersMap,removeSquareBrackets(characters_));

        for (Characters character : charactersList) {
            book.addCharacter(character);
        }
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

    private Map<Award, List<Integer>> getAwardsMap(Map<String, Award> awardMap, String awardString) {
        Map<Award, List<Integer>> awardYearMap = new HashMap<>();

        awardString = removeSquareBrackets(awardString.trim());


        Pattern quotePattern = Pattern.compile("(['\"])(.*?)\\1");
        Matcher matcher = quotePattern.matcher(awardString);

        while (matcher.find()) {
            String awardName = matcher.group(2);

            List<String> awardsList = splitAwards(awardName);
            for (String awardToken : awardsList) {
                List<Integer> years = extractAwardYears(awardToken);

                String cleanAwardName = removeYearInfo(awardToken).trim();

                Award award = awardMap.get(cleanAwardName);
                if(award==null) {
                    award = new Award();
                    award.setName(cleanAwardName);
                    awardMap.put(cleanAwardName,award);
                }

                if (awardYearMap.containsKey(award)) {
                    awardYearMap.get(award).addAll(years);
                } else {
                    awardYearMap.put(award, new ArrayList<>(years));
                }
            }
        }

        return awardYearMap;
    }

    private List<String> splitAwards(String awardNames) {
        List<String> awards = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        int parenthesesLevel = 0;
        int i = 0;
        while (i < awardNames.length()) {
            char c = awardNames.charAt(i);
            if (c == '(') {
                parenthesesLevel++;
            } else if (c == ')') {
                parenthesesLevel--;
            }

            if (parenthesesLevel == 0 && i + 5 <= awardNames.length() && awardNames.substring(i, i + 5).equals(" and ")) {
                awards.add(sb.toString().trim());
                sb.setLength(0);
                i += 5;
                continue;
            }
            sb.append(c);
            i++;
        }
        if (sb.length() > 0) {
            awards.add(sb.toString().trim());
        }
        return awards;
    }

    private List<Integer> extractAwardYears(String input) {
        List<Integer> years = new ArrayList<>();
        Pattern yearPattern = Pattern.compile("\\((\\d{4})\\)");
        Matcher matcher = yearPattern.matcher(input);
        while (matcher.find()) {
            years.add(Integer.parseInt(matcher.group(1)));
        }

        return years;
    }

    private String removeYearInfo(String input) {
        return input.replaceAll("\\(\\d{4}\\)", "").trim();
    }

    private List<Characters> createOrGetCharacters(Map<String, Characters> characterMap, String characterNames) {
        List<Characters> characterList = new ArrayList<>();

        String[] characters = characterNames.split(", ");

        for (String characterName : characters) {
            String name = removeSingleQuotes(characterName);
            Characters character = characterMap.get(name);
            if(character==null){
                character = new Characters();
                character.setName(name);
                characterMap.put(name, character);
            }
            characterList.add(character);
        }

        return characterList;
    }

    private void setBookAuthors(Book book, Map<String, Author> authorMap, String authorName) {
        List<String> authorNameRoles = splitAuthors(authorName);

        for (String author_ : authorNameRoles) {
            String name = extractAuthorName(author_);
            Author author = authorMap.get(name);

            if(author == null) {
                author = new Author();
                author.setName(name);
                authorMap.put(name,author);
            }
            List<String> roles = extractRoles(author_);
            if(roles.isEmpty()) book.addBookAuthor(author,"Author");
            else {
                for (String role : roles) {
                    book.addBookAuthor(author, role);
                }
            }
        }
    }

    private List<String> splitAuthors(String authorName) {
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

    private List<String> extractRoles(String authorToken) {
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

    private String extractAuthorName(String authorToken) {
        return authorToken.replaceAll("\\([^)]*\\)", "").trim();
    }


    private BookDto initializeBookDto(CSVRecord csvRecord, Set<Long> isbnSet) throws IsbnIncorrectFormatException, IsbnDuplicationException, IncorrectRatingFormatException {
        String isbn_ = csvRecord.get("isbn");
        Long isbn = validateISBN(isbn_, isbnSet);

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



        LocalDate publishDate = setupDate(publishDate_);

        if(publishDate==null) throw new IncorrectPublishDateException("Publish date cannot be empty");
        LocalDate firstPublishDate = setupDate(firstPublishDate_);

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

    private LocalDate setupDate(String publishDate) {
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

    private Long validateISBN(String isbn, Set<Long> isbnSet) throws IsbnIncorrectFormatException, IsbnDuplicationException {
        Long isbnLong;

        if(isbn.length()==13 && isbn.replaceAll("\\d","").isEmpty())
            isbnLong = Long.valueOf(isbn);
        else throw new IsbnIncorrectFormatException("ISBN should contain numeric values only and be of length 13.");

        if(!isbnSet.contains(isbnLong)) {
            isbnSet.add(isbnLong);
            return isbnLong;
        }
        else throw new IsbnDuplicationException("Book with the following ISBN already exists: "+isbn);
    }

}