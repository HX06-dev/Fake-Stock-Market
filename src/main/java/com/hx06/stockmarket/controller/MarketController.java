package com.hx06.stockmarket.controller;

import com.hx06.stockmarket.service.MarketDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequiredArgsConstructor
public class MarketController {

    private final MarketDataService marketDataService;

    @GetMapping("/api/market/quote/{ticker}")
    public BigDecimal getQuote(@PathVariable String ticker) {
        return marketDataService.getCurrentPrice(ticker);
    }
}
