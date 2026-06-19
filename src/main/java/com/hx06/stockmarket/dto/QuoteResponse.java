package com.hx06.stockmarket.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.Map;

@Data
public class QuoteResponse {

    @JsonProperty("Global Quote")
    private Map<String, String> globalQuote;
}
