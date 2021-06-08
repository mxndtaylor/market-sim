package com.dkkm.marketsim.controller;

import com.dkkm.marketsim.model.dto.Portfolio;
import com.dkkm.marketsim.service.PortfolioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/portfolios")
public class PortfolioController {

    @Autowired
    private PortfolioService portfolioService;

    @GetMapping("/")
    @ResponseStatus(HttpStatus.OK)
    public List<Portfolio> fetchPortfolioList() {
        return null;
    }

    @PostMapping("/member")
    public ResponseEntity<Portfolio> createPortfolio(Portfolio portfolio) {
        // TODO: add validator check
        portfolio = portfolioService.addPortfolio(portfolio);
        return new ResponseEntity<>(portfolio, HttpStatus.CREATED);
    }

    @GetMapping("/member/{memberId}")
    public ResponseEntity<Portfolio> fetchPortfolio(@PathVariable int memberId) {
        Portfolio portfolio = portfolioService.getPortfolioById(memberId);
        // TODO: add validator check
        return ResponseEntity.ok(portfolio);
    }

    @PostMapping("/member/{memberId}/{ticker}")
    public ResponseEntity<Integer> sellPortfolioShares(
            @PathVariable int memberId, @PathVariable String ticker,
            @RequestBody int quantity) {
        // TODO: add validator check
        int quantitySold = portfolioService.sellTickerQuantityFromPortfolio(
                memberId, ticker, quantity);

        return ResponseEntity.ok(quantitySold);
    }

}