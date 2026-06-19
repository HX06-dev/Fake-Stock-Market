package com.hx06.stockmarket.repository;

import com.hx06.stockmarket.model.Position;
import com.hx06.stockmarket.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface PositionRepository extends JpaRepository<Position, Long> {
    List<Position> findByAccount(Account account);
    Optional<Position> findByAccountAndTicker(Account account, String ticker);
}
