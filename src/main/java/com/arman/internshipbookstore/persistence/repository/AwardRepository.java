package com.arman.internshipbookstore.persistence.repository;

import com.arman.internshipbookstore.persistence.entity.Award;
import com.arman.internshipbookstore.service.dto.award.AwardResponseDto;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface AwardRepository extends JpaRepository<Award, Long> {
    @Query("""
            SELECT new com.arman.internshipbookstore.service.dto.award.AwardResponseDto(
                aw.id,
                aw.name
            )
            FROM Award aw
            WHERE aw.name LIKE CONCAT('%',:name,'%')
""")
    Page<AwardResponseDto> findAwardsByName(String name, Pageable pageable);

    Award getAwardByName(String name);

    @Query("SELECT aw.name FROM Award aw")
    Set<String> findAllAwardNames();

    Award getAwardById(Long id);

    boolean existsAwardByName(String name);
}
