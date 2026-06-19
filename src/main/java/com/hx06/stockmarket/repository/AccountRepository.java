package com.hx06.stockmarket.repository;

import com.hx06.stockmarket.model.Account;
import com.hx06.stockmarket.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByUser(User user);
}
