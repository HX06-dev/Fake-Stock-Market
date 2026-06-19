package com.hx06.stockmarket.service;

import com.hx06.stockmarket.model.Account;
import com.hx06.stockmarket.model.User;
import com.hx06.stockmarket.repository.AccountRepository;
import com.hx06.stockmarket.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    public Account getAccount(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        return accountRepository.findByUser(user)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));
    }

    @Transactional
    public Account deposit(String email, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Deposit amount must be greater than zero");
        }

        Account account = getAccount(email);

        account.setBalance(account.getBalance().add(amount));
        return accountRepository.save(account);
    }
}
