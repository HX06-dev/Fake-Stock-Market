package com.hx06.stockmarket.service;

import com.hx06.stockmarket.model.Account;
import com.hx06.stockmarket.model.Position;
import com.hx06.stockmarket.model.User;
import com.hx06.stockmarket.repository.AccountRepository;
import com.hx06.stockmarket.repository.PositionRepository;
import com.hx06.stockmarket.repository.UserRepository;
import com.hx06.stockmarket.dto.PortfolioResponse;
import com.hx06.stockmarket.dto.PositionView;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PortfolioService {

    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final PositionRepository positionRepository;
    private final MarketDataService marketDataService;

    public PortfolioResponse getPortfolio(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Account account = accountRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        List<Position> positions = positionRepository.findByAccount(account);

        List<PositionView> views = positions.stream().map(pos -> {
            BigDecimal currentPrice = marketDataService.getCurrentPrice(pos.getTicker());
            BigDecimal marketValue = currentPrice.multiply(BigDecimal.valueOf(pos.getQuantity()));
            BigDecimal costBasis = pos.getAvgCost().multiply(BigDecimal.valueOf(pos.getQuantity()));
            BigDecimal unrealizedPL = marketValue.subtract(costBasis);

            PositionView view = new PositionView();
            view.setTicker(pos.getTicker());
            view.setQuantity(pos.getQuantity());
            view.setAvgCost(pos.getAvgCost());
            view.setCurrentPrice(currentPrice);
            view.setMarketValue(marketValue.setScale(2, RoundingMode.HALF_UP));
            view.setUnrealizedPL(unrealizedPL.setScale(2, RoundingMode.HALF_UP));
            return view;
        }).toList();

        BigDecimal totalMarketValue = views.stream()
                .map(PositionView::getMarketValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalValue = account.getBalance().add(totalMarketValue);

        PortfolioResponse response = new PortfolioResponse();
        response.setCashBalance(account.getBalance());
        response.setPositions(views);
        response.setTotalMarketValue(totalMarketValue.setScale(2, RoundingMode.HALF_UP));
        response.setTotalValue(totalValue.setScale(2, RoundingMode.HALF_UP));

        return response;
    }

}
