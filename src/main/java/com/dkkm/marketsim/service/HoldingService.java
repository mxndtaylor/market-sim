package com.dkkm.marketsim.service;

import com.dkkm.marketsim.model.dto.Holding;

import java.util.List;

public interface HoldingService extends PassThruCrudService<Holding, Holding> {

    // TODO: int getPortfolioNetWorth(int portfolioId);
    // TODO: consider moving these to dao, using a sql agg function
    // con: sql has good agg function, but other dao impls might not
    List<Holding> aggregatePortfolioHoldings(int portfolioId);
    Holding aggregatePortfolioHoldingsByTicker(int portfolioId, String ticker);

    /**
     * sell the oldest shares of a stock from a portfolio
     * @param aggregateHolding a Holding object containing:
     *                         portfolioId, ticker, shareQuantity
     *                         where the last value represents the
     *                         desired quantity
     * @return number of shares sold
     */
    int sellHoldingsByAggHolding(Holding aggregateHolding);
}
