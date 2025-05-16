package com.arman.internshipbookstore.persistence.repository;

import com.arman.internshipbookstore.persistence.entity.Author;
import com.arman.internshipbookstore.service.dto.author.AuthorResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {

    @Query("""
            SELECT new com.arman.internshipbookstore.service.dto.author.AuthorResponseDto(
                a.id,
                a.name
            )
            FROM Author a
            WHERE a.name LIKE CONCAT('%',:name,'%')
""")
    Page<AuthorResponseDto> getAuthorByName(String name, Pageable pageable);

    Author getAuthorById(Long id);

    boolean existsAuthorByName(String name);
}