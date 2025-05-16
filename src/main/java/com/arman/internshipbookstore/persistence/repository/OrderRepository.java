package com.arman.internshipbookstore.persistence.repository;

import com.arman.internshipbookstore.persistence.entity.UserOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<UserOrder, Long> {
    UserOrder findByIdAndUser_Email(Long id, String userEmail);

}
