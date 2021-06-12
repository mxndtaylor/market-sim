package com.dkkm.marketsim.service;

import com.dkkm.marketsim.model.dto.Stock;

import java.util.List;

public interface StockService
        extends PassThruCrudService<Stock, String> {

    // TODO: List<Stock> getStocksByPortfolio(int portfolioId);
}
