package com.hx06.stockmarket.controller;

import com.hx06.stockmarket.dto.DepositRequest;
import com.hx06.stockmarket.model.Account;
import com.hx06.stockmarket.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/account")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @GetMapping
    public ResponseEntity<Account> getAccount(Authentication auth) {
        String email = auth.getName();
        return ResponseEntity.ok(accountService.getAccount(email));
    }

    @PostMapping("/deposit")
    public ResponseEntity<Account> deposit(@RequestBody DepositRequest req, Authentication auth) {
        String email = auth.getName();
        return ResponseEntity.ok(accountService.deposit(email, req.getAmount()));
    }
}
