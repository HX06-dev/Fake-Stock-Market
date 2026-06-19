package com.hx06.stockmarket.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class PositionView {
    private String ticker;
    private int quantity;
    private BigDecimal avgCost;
    private BigDecimal currentPrice;
    private BigDecimal marketValue;
    private BigDecimal unrealizedPL;
}
