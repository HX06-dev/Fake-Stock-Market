package com.hx06.stockmarket.service;

import com.hx06.stockmarket.model.*;
import com.hx06.stockmarket.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final PositionRepository positionRepository;
    private final TransactionRepository transactionRepository;
    private final MarketDataService marketDataService;

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public Transaction executeOrder(String email, String ticker, String side, int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity should be greater than 0");
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Account account = accountRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        BigDecimal price = marketDataService.getCurrentPrice(ticker);
        BigDecimal total = price.multiply(BigDecimal.valueOf(quantity));

        if (side.equalsIgnoreCase("BUY")) {
            executeBuy(account, ticker, quantity, price, total);
        } else if (side.equalsIgnoreCase("SELL")) {
            executeSell(account, ticker, quantity, price, total);
        } else {
            throw new IllegalArgumentException("Side must be either BUY or SELL");
        }

        accountRepository.save(account);

        Transaction transaction = new Transaction();
        transaction.setAccount(account);
        transaction.setTicker(ticker.toUpperCase());
        transaction.setSide(side.toUpperCase());
        transaction.setQuantity(quantity);
        transaction.setPrice(price);
        transaction.setTotalPrice(total);

        return transactionRepository.save(transaction);
    }

    private void executeBuy(Account account, String ticker, int quantity, BigDecimal price, BigDecimal total) {
        if(account.getBalance().compareTo(total) <= 0) {
            throw new RuntimeException("Insufficient funds");
        }

        account.setBalance(account.getBalance().subtract(total));

        Optional<Position> existing = positionRepository.findByAccountAndTicker(account, ticker);

        if (existing.isPresent()) {
            Position pos =  existing.get();
            BigDecimal currentTotalCost = pos.getAvgCost().multiply(BigDecimal.valueOf(pos.getQuantity()));
            BigDecimal newTotalCost = currentTotalCost.add(total);
            int newQuantity = pos.getQuantity() + quantity;

            pos.setQuantity(newQuantity);
            pos.setAvgCost(newTotalCost.divide(BigDecimal.valueOf(newQuantity), 4, java.math.RoundingMode.HALF_UP));
            positionRepository.save(pos);
        } else {
            Position pos = new Position();
            pos.setAccount(account);
            pos.setTicker(ticker);
            pos.setQuantity(quantity);
            pos.setAvgCost(price);
            positionRepository.save(pos);
        }
    }

    private void executeSell(Account account, String ticker, int quantity, BigDecimal price, BigDecimal total) {
        Position pos = positionRepository.findByAccountAndTicker(account, ticker.toUpperCase())
                .orElseThrow(() -> new RuntimeException("No position in " + ticker));

        if (pos.getQuantity() < quantity) {
            throw new RuntimeException("Insufficient shares to sell");
        }

        account.setBalance(account.getBalance().add(total));

        int remaining = pos.getQuantity() - quantity;

        if (remaining == 0) {
            positionRepository.delete(pos);
        } else {
            pos.setQuantity(remaining);
            positionRepository.save(pos);
        }
    }
}
