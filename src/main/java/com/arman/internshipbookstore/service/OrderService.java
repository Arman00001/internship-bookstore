package com.arman.internshipbookstore.service;

import com.arman.internshipbookstore.enums.PurchaseStatus;
import com.arman.internshipbookstore.persistence.entity.Book;
import com.arman.internshipbookstore.persistence.entity.UserOrder;
import com.arman.internshipbookstore.persistence.entity.UserProfile;
import com.arman.internshipbookstore.persistence.repository.BookRepository;
import com.arman.internshipbookstore.persistence.repository.OrderRepository;
import com.arman.internshipbookstore.persistence.repository.UserProfileRepository;
import com.arman.internshipbookstore.service.dto.order.OrderRequestDto;
import com.arman.internshipbookstore.service.dto.order.OrderResponseDto;
import com.arman.internshipbookstore.service.exception.ResourceNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class OrderService {


    private final BookRepository bookRepository;
    private final UserProfileRepository userProfileRepository;
    private final OrderRepository orderRepository;

    public OrderResponseDto purchaseBook(@Valid OrderRequestDto orderRequestDto, Authentication auth) {
        String email = auth.getName();

        UserProfile user = userProfileRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        UserOrder order = new UserOrder();
        Book book = bookRepository.getBookById(orderRequestDto.getBookId());
        order.setBook(book);
        order.setUser(user);
        order.setPurchaseStatus(PurchaseStatus.PURCHASED);
        order.setPricePaid(book.getPrice());

        return OrderResponseDto.getOrderResponse(orderRepository.save(order));
    }

    public void returnBook(Long orderId, Authentication auth) {
        String email = auth.getName();
        UserProfile user = userProfileRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        UserOrder userOrder = orderRepository.findByIdAndUser_Email(orderId, user.getEmail());

        userOrder.setPurchaseStatus(PurchaseStatus.RETURNED);
        LocalDateTime time = LocalDateTime.now();
        userOrder.setReturnedDate(time);
        orderRepository.save(userOrder);
    }


}
