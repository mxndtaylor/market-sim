package com.dkkm.marketsim.controller;

import com.dkkm.marketsim.model.dto.Holding;
import com.dkkm.marketsim.service.HoldingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/portfolios/member/{memberId}/holdings")
public class HoldingController {

    @Autowired
    private HoldingService holdingService;

    @GetMapping("")
    public ResponseEntity getHoldingsByPortfolio(@PathVariable int memberId) {
        return ResponseEntity.ok(holdingService.aggregatePortfolioHoldings(memberId));
    }
}
