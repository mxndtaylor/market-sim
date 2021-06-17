package com.dkkm.marketsim.service;

import com.dkkm.marketsim.model.dto.Holding;
import com.dkkm.marketsim.model.dto.Portfolio;

import java.time.LocalDate;

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
     * @param date date to attempt to purchase
     * @param buyQuantity the number of shares attempted to buy
     * @return int representing number of shares successfully bought
     */
    Holding buyTickerQuantityForPortfolio(int portfolioId, String ticker, LocalDate date, int buyQuantity);

    /**
     * checks that the portfolio is headed to the correct day,
     * fills null values with current values before calling update
     * @param portfolio the portfolio to update with a date in the future from the current one,
     *                  the date should correspond to a day the market is open
     * @return boolean representing the success of the operation
     */
    boolean nextMarketDay(Portfolio portfolio);
}
