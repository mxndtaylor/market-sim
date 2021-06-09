package com.dkkm.marketsim.service;

import com.dkkm.marketsim.model.dto.Portfolio;

import java.util.List;

public interface PortfolioService {

    Portfolio addPortfolio(Portfolio portfolio);
    Portfolio getPortfolioById(int id);
    List<Portfolio> getPortfolios();
    boolean deletePortfolioById(int id);
    boolean updatePortfolio(Portfolio portfolio);

    int sellTickerQuantityFromPortfolio(int portfolioId, String ticker, int sellQuantity);
}
