package com.dkkm.marketsim.controller;

import com.dkkm.marketsim.model.dao.PortfolioDao;
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

    @GetMapping("")
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

    @PostMapping("/member/{memberId}/holdings/member")
    public ResponseEntity<Integer> buyPortfolioShares(
            @PathVariable int memberId,
            @RequestBody Holding buyOrder) {
        String ticker = buyOrder.getTicker();
        LocalDate date = buyOrder.getPurchaseDate();
        int buyQuantity = buyOrder.getShareQuantity();
        Holding bought = portfolioService.buyTickerQuantityForPortfolio(memberId, ticker, date, buyQuantity);
        return new ResponseEntity<>(bought.getShareQuantity(), HttpStatus.OK);
    }

    @PatchMapping("/member/{memberId}/holdings/member")
    public ResponseEntity<Integer> sellPortfolioShares(
            @PathVariable int memberId,
            @RequestBody Holding sellOrder) {
        // TODO: add validator check
        int quantity = sellOrder.getShareQuantity();
        String ticker = sellOrder.getTicker();
        Holding sold = portfolioService.sellTickerQuantityFromPortfolio(memberId, ticker, quantity);

        return new ResponseEntity<>(sold.getShareQuantity(), HttpStatus.OK);
    }

    @PutMapping("/member")
    public ResponseEntity updatePortfolio(@RequestBody Portfolio portfolio) {
        boolean succeeded = portfolioService.updateMember(portfolio);
        // TODO: add exception on false
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/member")
    public ResponseEntity nextMarketDay(@RequestBody Portfolio portfolio) {
        boolean succeeded = portfolioService.nextMarketDay(portfolio);
        // TODO: add exception on false
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/member/{memberId}")
    public ResponseEntity deletePortfolio(@PathVariable int memberId) {
        boolean succeeded = portfolioService.deleteMemberByKey(memberId);
        // TODO: add exception on false
        return ResponseEntity.noContent().build();
    }
}
