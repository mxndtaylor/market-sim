package com.dkkm.marketsim.model.dao;

import com.dkkm.marketsim.model.dto.Holding;

import java.time.LocalDate;
import java.util.List;

public interface HoldingDao extends CrudDao<Holding, Holding> {

    List<Holding> getPortfolioHoldings(int portfolioId);
    List<Holding> getPortfolioHoldingsByTicker(int portfolioId, String ticker);
    // TODO: List<Holding> getHoldingsByPurchaseDate(LocalDate date);
}
