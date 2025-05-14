package com.arman.internshipbookstore.service.util;

import com.arman.internshipbookstore.service.exception.InvalidPublicationDateException;

import java.time.LocalDate;
import java.time.Year;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Locale;

public class DateUtils {

    private static final List<DateTimeFormatter> DATE_FORMATTERS = List.of(
            DateTimeFormatter.ofPattern("M/d/yy"),
            DateTimeFormatter.ofPattern("MMMM d yyyy", Locale.ENGLISH),
            DateTimeFormatter.ofPattern("MMMM yyyy", Locale.ENGLISH),
            DateTimeFormatter.ofPattern("yyyy")
    );


    public static LocalDate setupDate(String publishDate) {
        if (publishDate == null || publishDate.isBlank()) return null;

        publishDate = publishDate.replaceAll("(?<=\\d)(st|nd|rd|th)", "").trim();

        for (DateTimeFormatter formatter : DATE_FORMATTERS) {
            try {
                if (formatter.toString().equals(DateTimeFormatter.ofPattern("yyyy").toString())) {
                    Year year = Year.parse(publishDate, formatter);
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
}
