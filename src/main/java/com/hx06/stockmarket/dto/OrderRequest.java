package com.hx06.stockmarket.dto;

import lombok.Data;

@Data
public class OrderRequest {
    private String ticker;
    private String side;
    private int quantity;
}
