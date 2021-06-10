package com.dkkm.marketsim.controller;

import com.dkkm.marketsim.model.dao.ClosingDao;
import com.dkkm.marketsim.model.dao.StockDBDao;
import com.dkkm.marketsim.model.dto.Closing;
import com.dkkm.marketsim.model.dto.Data;
import com.dkkm.marketsim.model.dto.Stock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/tickers")
public class MarketStackController {

    @Value("${api.key}")
    private String apiKey;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ClosingDao closingDao;

    @Autowired
    private StockDBDao stockDao;

    @RequestMapping("/initDB")
    public List<Stock> initDB() {
        String[] tickers = initStocks();

        for(int i = 0; i < tickers.length; i++) {
            setTickerData(tickers[i]);
        }
        return stockDao.getMembers();
    }

    @RequestMapping("/initStocks")
    public String[] initStocks(){
        String[] tickers = {"AAPL", "TSLA", "NFLX", "INTC", "AMD", "WMT", "F", "GME", "BB", "AMC", "HD", "BAC", "DIS", "HON", "BA", "COP"};
        for(int i = 0; i < tickers.length; i++) {
            Stock stock = new Stock();
            stock.setTicker(tickers[i]);
            stockDao.addMember(stock);
        }
        return tickers;
    }

    @RequestMapping("/addToClosings/{ticker}")
    public void setTickerData(@PathVariable("ticker") String ticker) {
        Object response = restTemplate.getForObject(
                "http://api.marketstack.com/v1/eod?access_key=" + apiKey + "&symbols=" + ticker, Object.class
        );
        Data data = new Data();
        data.setData((Map<String, Object>) response);
        data.setDataList((ArrayList<Map<String, Object>>) data.getData().get("data"));
        for(int i = 0; i < 30; i++) {
            double close = (double) data.getDataList().get(i).get("close");
            String symbol = (String) data.getDataList().get(i).get("symbol");
            DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZ");
            LocalDateTime localDateTime = LocalDateTime.parse((String)data.getDataList().get(i).get("date"),format);
            LocalDate date = localDateTime.toLocalDate();
            Closing closing = new Closing();
            closing.setTicker(symbol);
            closing.setDate(date);
            closing.setPrice(close);
            closingDao.addMember(closing);
        }
    }
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
