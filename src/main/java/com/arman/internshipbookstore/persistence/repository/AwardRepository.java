package com.arman.internshipbookstore.persistence.repository;

import com.arman.internshipbookstore.persistence.entity.Award;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AwardRepository extends JpaRepository<Award, Long> {
}
