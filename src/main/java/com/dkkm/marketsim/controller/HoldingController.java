package com.dkkm.marketsim.controller;

import com.dkkm.marketsim.model.dto.Holding;
import com.dkkm.marketsim.service.HoldingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/portfolios/{portfolioId}/holdings")
public class HoldingController {

    @Autowired
    private HoldingService holdingService;

    @GetMapping("/")
    public ResponseEntity getHoldingsByPortfolio(@PathVariable int portfolioId) {
        return ResponseEntity.ok(holdingService.aggregatePortfolioHoldings(portfolioId));
    }
}
