package com.hx06.stockmarket.controller;

import com.hx06.stockmarket.dto.OrderRequest;
import com.hx06.stockmarket.model.Transaction;
import com.hx06.stockmarket.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<Transaction> placeOrder(Authentication auth,
                                                  @RequestBody OrderRequest req) {
        String email = auth.getName();
        Transaction tx = orderService.executeOrder(
                email, req.getTicker(), req.getSide(), req.getQuantity()
        );
        return ResponseEntity.ok(tx);
    }
}
