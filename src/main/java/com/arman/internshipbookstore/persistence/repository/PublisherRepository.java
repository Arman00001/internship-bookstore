package com.arman.internshipbookstore.persistence.repository;

import com.arman.internshipbookstore.persistence.entity.Publisher;
import com.arman.internshipbookstore.service.dto.publisher.PublisherResponseDto;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PublisherRepository extends JpaRepository<Publisher, Long> {

    @Query("""
            SELECT new com.arman.internshipbookstore.service.dto.publisher.PublisherResponseDto(
                p.id,
                p.name
            )
            FROM Publisher p
            WHERE p.name LIKE CONCAT('%',:name,'%')
""")
    Page<PublisherResponseDto> findPublisherByName(String name, Pageable pageable);

    boolean existsPublisherByName(@NotBlank String name);
}
