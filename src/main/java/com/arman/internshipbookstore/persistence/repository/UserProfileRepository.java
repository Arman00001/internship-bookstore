package com.arman.internshipbookstore.persistence.repository;

import com.arman.internshipbookstore.persistence.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {

    boolean existsByEmail(String email);

    Optional<UserProfile> findByEmail(String email);
}
