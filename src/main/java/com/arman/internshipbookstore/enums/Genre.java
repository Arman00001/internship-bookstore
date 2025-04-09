package com.arman.internshipbookstore.enums;

public enum Genre {
    FICTION,
    ACTION,
    NONFICTION,
    FANTASY,
    SCIENCE_FICTION,
    DYSTOPIA,
    ADVENTURE,
    ROMANCE,
    CONTEMPORARY,
    MYSTERY,
    THRILLER,
    HORROR,
    HISTORICAL_FICTION,
    YOUNG_ADULT,
    NEW_ADULT,
    CHILDRENS,
    CLASSICS,
    BIOGRAPHY,
    AUTOBIOGRAPHY,
    MEMOIR,
    SELF_HELP,
    HEALTH,
    HISTORY,
    TRAVEL,
    GUIDE,
    RELIGION,
    SCIENCE,
    PHILOSOPHY,
    POETRY,
    ART,
    COOKBOOK,
    JOURNAL,
    MATH,
    COMICS,
    GRAPHIC_NOVEL,
    SHORT_STORIES,
    ESSAYS,
    CRIME,
    TRUE_CRIME,
    HUMOR,
    SPORTS,
    MUSIC,
    TECHNOLOGY,
    BUSINESS,
    POLITICS,
    EDUCATION,
    PSYCHOLOGY,
    SPIRITUALITY,
    PARENTING,
    PICTURE_BOOKS,
    POST_APOCALYPTIC,
    TEEN,
    SCIENCE_FICTION_FANTASY,
    UNKNOWN;  // Default value for unknown genres

    public static Genre fromString(String value) {
        try {
            return Genre.valueOf(value.trim().toUpperCase().replace(" ", "_").replace("-", "_"));
        } catch (IllegalArgumentException e) {
            return Genre.UNKNOWN;  // Return UNKNOWN if genre doesn't match
        }
    }
}
