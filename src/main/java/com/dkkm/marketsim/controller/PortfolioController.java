package com.dkkm.marketsim.controller;

import com.dkkm.marketsim.model.dto.Holding;
import com.dkkm.marketsim.model.dto.Portfolio;
import com.dkkm.marketsim.service.PortfolioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.relational.core.sql.In;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/portfolios")
public class PortfolioController {

    @Autowired
    private PortfolioService portfolioService;

    @GetMapping("/")
    @ResponseStatus(HttpStatus.OK)
    public List<Portfolio> fetchPortfolioList() {
        return portfolioService.getMembers();
    }

    @PostMapping("/member")
    public ResponseEntity<Portfolio> createPortfolio(@RequestBody Portfolio portfolio) {
        // TODO: add validator check
        portfolio = portfolioService.addMember(portfolio);
        return new ResponseEntity<>(portfolio, HttpStatus.CREATED);
    }

    @GetMapping("/member/{memberId}")
    public ResponseEntity<Portfolio> fetchPortfolio(@PathVariable int memberId) {
        Portfolio portfolio = portfolioService.getMemberByKey(memberId);
        // TODO: add validator check
        return ResponseEntity.ok(portfolio);
    }

    @PostMapping("/member/{memberId}/holdings")
    public ResponseEntity<Integer> buyPortfolioShares(
            @PathVariable int memberId,
            @RequestBody Holding holding) {
        String ticker = holding.getTicker();
        int buyQuantity = holding.getShareQuantity();
        LocalDate date = holding.getPurchaseDate();
        int boughtQuantity = portfolioService.buyTickerQuantityForPortfolio(memberId, ticker, date, buyQuantity);
        return new ResponseEntity<>(boughtQuantity, HttpStatus.OK);
    }

    @PostMapping("/member/{memberId}/{ticker}/{quantity}")
    public ResponseEntity<Integer> sellPortfolioShares(
            @PathVariable int memberId, @PathVariable String ticker,
            @PathVariable Integer quantity) {
        // TODO: add validator check
        int quantitySold = portfolioService.sellTickerQuantityFromPortfolio(memberId, ticker, quantity);

        return ResponseEntity.ok(quantitySold);
    }

    @PutMapping("/member")
    public ResponseEntity updatePortfolio(@RequestBody Portfolio portfolio) {
        boolean succeeded = portfolioService.updateMember(portfolio);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/member/{memberId}")
    public ResponseEntity deletePortfolio(@PathVariable int memberId) {
        boolean succeeded = portfolioService.deleteMemberByKey(memberId);
        return ResponseEntity.noContent().build();
    }
}
