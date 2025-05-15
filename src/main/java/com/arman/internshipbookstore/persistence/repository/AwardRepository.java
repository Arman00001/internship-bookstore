package com.arman.internshipbookstore.persistence.repository;

import com.arman.internshipbookstore.persistence.entity.Award;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface AwardRepository extends JpaRepository<Award, Long> {
    Award getAwardByName(String name);

    @Query("SELECT aw.name FROM Award aw")
    Set<String> findAllAwardNames();

    Award getAwardById(Long id);

    boolean existsAwardByName(String name);
}
