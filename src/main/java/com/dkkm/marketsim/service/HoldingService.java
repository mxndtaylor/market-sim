package com.dkkm.marketsim.service;

import com.dkkm.marketsim.model.dto.Holding;

import java.util.List;

public interface HoldingService extends PassThruCrudService<Holding, Holding> {

    // TODO: int getPortfolioNetWorth(int portfolioId);
    List<Holding> aggregatePortfolioHoldings(int portfolioId);
    Holding aggregatePortfolioHoldingsByTicker(int portfolioId, String ticker);

    /**
     * sell the oldest shares of a stock from a portfolio
     * @param portfolioId the lookup for the portfolio
     * @param ticker the lookup for the stock
     * @return true on success, false otherwise
     */
    boolean updatePortfolioHoldingsByTicker(int portfolioId, String ticker, int sellQuantity);

}
