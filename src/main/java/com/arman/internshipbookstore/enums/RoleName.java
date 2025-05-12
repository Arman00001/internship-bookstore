package com.arman.internshipbookstore.enums;

public enum RoleName {
    ROLE_GUEST, // Can search/view books,authors,etc
    ROLE_USER, // Same as GUEST, plus leave ratings, reviews
    ROLE_USER_PRIME, //Can get chosen book for free for 1 month
    ROLE_MODERATOR, // Add/delete entities
    ROLE_ADMIN, // Change roles of users
}