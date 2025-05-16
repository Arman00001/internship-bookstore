package com.arman.internshipbookstore.controller;

import com.arman.internshipbookstore.service.OrderService;
import com.arman.internshipbookstore.service.dto.order.OrderRequestDto;
import com.arman.internshipbookstore.service.dto.order.OrderResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping("/purchase")
    public ResponseEntity<OrderResponseDto> purchaseBook(@RequestBody @Valid OrderRequestDto orderRequestDto,
                                                         Authentication auth) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(orderService.purchaseBook(orderRequestDto, auth));
    }

    @PostMapping("/{orderId}/return")
    public ResponseEntity<Void> returnBook(@PathVariable("orderId") Long orderId, Authentication auth) {
        orderService.returnBook(orderId, auth);
        return ResponseEntity.noContent().build();
    }

}
