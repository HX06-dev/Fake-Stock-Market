package com.hx06.stockmarket.repository;

import com.hx06.stockmarket.model.Transaction;
import com.hx06.stockmarket.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByAccountOrderByCreatedAtDesc(Account account);
}
