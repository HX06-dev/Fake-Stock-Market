package com.hx06.stockmarket.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class PortfolioResponse {
    private BigDecimal cashBalance;
    private List<PositionView> positions;
    private BigDecimal totalMarketValue;
    private BigDecimal totalValue;
}
