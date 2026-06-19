package com.hx06.stockmarket.controller;

import com.hx06.stockmarket.dto.PortfolioResponse;
import com.hx06.stockmarket.service.PortfolioService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PortfolioController {

    private final PortfolioService portfolioService;

    @GetMapping("/api/portfolio")
    public PortfolioResponse getPortfolio(Authentication auth) {
        String email = auth.getName();
        return portfolioService.getPortfolio(email);
    }
}
