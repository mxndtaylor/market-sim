package com.dkkm.marketsim.controller;

import com.dkkm.marketsim.model.dto.Data;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/tickers")
public class MarketStackController {

    @Value("${api.key}")
    private String apiKey;

    @Autowired
    private RestTemplate restTemplate;
/*
    @RequestMapping("/{ticker}")
    public Closing getClosingInfo(@PathVariable("ticker") String ticker) {
        Data data = restTemplate.getForObject(
          "http://api.marketstack.com/v1/eod?access_key=" + apiKey + "&symbols=" + ticker, Data.class
        );
        return new Closing(data.getDate(), data.getSymbol(), data.getClose());
    }
*/
    @RequestMapping("/{ticker}")
    public Data getTickerData(@PathVariable("ticker") String ticker) {
        Object response = restTemplate.getForObject(
                "http://api.marketstack.com/v1/eod?access_key=" + apiKey + "&symbols=" + ticker, Object.class
        );
        Data data = new Data();
        data.setData((Map<String, Object>) response);
        data.setDataList((ArrayList<Map<String, Object>>) data.getData().get("data"));
        double close = (double) data.getDataList().get(0).get("close");
        String symbol = (String) data.getDataList().get(0).get("symbol");
        String date = (String) data.getDataList().get(0).get("date");
        return data;
    }
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
