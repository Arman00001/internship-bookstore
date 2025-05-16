package com.arman.internshipbookstore.persistence.entity;

import com.arman.internshipbookstore.enums.PurchaseStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "user_order")
public class UserOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_profile_id")
    private UserProfile user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "book_id")
    private Book book;

    @CreationTimestamp
    @Column(name = "purchase_date")
    private LocalDateTime purchaseTime;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "order_status")
    private PurchaseStatus purchaseStatus;

    @Column(name = "return_date")
    private LocalDateTime returnedDate;

    @Column(name = "price_paid")
    private Double pricePaid;
}
