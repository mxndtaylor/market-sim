package com.dkkm.marketsim.service;

import com.dkkm.marketsim.model.dto.Holding;
import com.dkkm.marketsim.model.dto.Portfolio;

public interface PortfolioService extends PassThruCrudService<Portfolio, Integer> {

    /**
     * attempt a sale of a certain number of shares from a portfolio
     * @param portfolioId the portfolio look up
     * @param ticker the look up key for a stock
     * @param sellQuantity the number of shares attempted to sell
     * @return int representing the number of shares successfully sold
     */
    Holding sellTickerQuantityFromPortfolio(int portfolioId, String ticker, int sellQuantity);

    /**
     * attempt to buy a certain number of shares for a portfolio
     * @param portfolioId the portfolio look up
     * @param ticker the look up key for a stock
     * @param buyQuantity the number of shares attempted to buy
     * @return int representing number of shares successfully bought
     */
    Holding buyTickerQuantityForPortfolio(int portfolioId, String ticker, int buyQuantity);
}
