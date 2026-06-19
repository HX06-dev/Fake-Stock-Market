package com.hx06.stockmarket.service;

import com.hx06.stockmarket.dto.QuoteResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

@Service
public class MarketDataService {

    @Value("${alphavantage.api.key}")
    private String apiKey;

    private final RestClient restClient = RestClient.create();

    public BigDecimal getCurrentPrice(String ticker) {
        String url = String.format(
            "https://www.alphavantage.co/query?function=GLOBAL_QUOTE&symbol=%s&apikey=%s",
            ticker, apiKey
        );

        QuoteResponse response = restClient.get()
                .uri(url)
                .retrieve()
                .body(QuoteResponse.class);

        if (response == null || response.getGlobalQuote() == null) {
            throw new RuntimeException("Could not fetch price for ticker: " + ticker);
        }

        String priceStr = response.getGlobalQuote().get("05. price");

        if (priceStr == null) {
            throw new RuntimeException("Price field missing for ticker: " + ticker);
        }

        return new BigDecimal(priceStr);
    }
}
