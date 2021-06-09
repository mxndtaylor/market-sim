package com.dkkm.marketsim.service;

import com.dkkm.marketsim.model.dto.Portfolio;

public interface PortfolioService extends PassThruCrudService<Portfolio, Integer> {

    /**
     * attempt a sale of a certain number of shares from a portfolio
     * @param portfolioId the portfolio look up
     * @param ticker the look up key for a stock
     * @param sellQuantity the number of shares attempted to sell
     * @return int representing the number of shares successfully sold
     */
    int sellTickerQuantityFromPortfolio(int portfolioId, String ticker, int sellQuantity);
}
