package com.arman.internshipbookstore.service.dto.order;

import com.arman.internshipbookstore.enums.PurchaseStatus;
import com.arman.internshipbookstore.persistence.entity.UserOrder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class OrderResponseDto {
    private Long id;
    private Long userId;
    private Long bookId;
    private LocalDateTime purchaseDate;
    private PurchaseStatus purchaseStatus;
    private Double pricePaid;

    public OrderResponseDto(Long id, Long userId, Long bookId, LocalDateTime purchaseDate, PurchaseStatus purchaseStatus, Double pricePaid) {
        this.id = id;
        this.userId = userId;
        this.bookId = bookId;
        this.purchaseDate = purchaseDate;
        this.purchaseStatus = purchaseStatus;
        this.pricePaid = pricePaid;
    }

    public static OrderResponseDto getOrderResponse(UserOrder order) {
        return new OrderResponseDto(order.getId(),
                order.getUser().getId(),
                order.getBook().getId(),
                order.getPurchaseTime(),
                order.getPurchaseStatus(),
                order.getPricePaid());
    }
}
