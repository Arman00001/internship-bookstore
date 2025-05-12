package com.arman.internshipbookstore.persistence.repository;

import com.arman.internshipbookstore.persistence.entity.UserCredentials;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserCredentialRepository extends JpaRepository<UserCredentials,Long> {

    Optional<UserCredentials> findByUserProfileId(Long id);

    Optional<UserCredentials> findByUsername(String username);
}
