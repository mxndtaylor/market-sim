package com.dkkm.marketsim.service;

import com.dkkm.marketsim.model.dao.HoldingDao;
import com.dkkm.marketsim.model.dto.Holding;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class HoldingServiceImpl
    extends PassThruCrudServiceImpl<Holding, Holding>
    implements HoldingService {

    @Autowired
    public HoldingServiceImpl(HoldingDao holdingDao) {
        dao = holdingDao;
    }

    @Override
    public List<Holding> aggregatePortfolioHoldings(int portfolioId) {
        List<Holding> holdings = ((HoldingDao) dao).getPortfolioHoldings(portfolioId);
        Map<String, Holding> holdingsByTicker = new HashMap<>();

        // adds up all the quantities of a certain stock and removes purchase date field
        for (Holding holding : holdings) {
            String ticker = holding.getTicker();
            if (holdingsByTicker.containsKey(ticker)) {
                Holding aggHolding = holdingsByTicker.get(ticker);

                // total the quantity of shares of a stock
                int totalShareQuantity = aggHolding.getShareQuantity() + holding.getShareQuantity();
                aggHolding.setShareQuantity(totalShareQuantity);
            } else {
                holdingsByTicker.put(ticker, holding);
            }
        }

        // clunky? TODO: rework the conversion to a list
        return holdingsByTicker.values().stream().collect(Collectors.toList());
    }

    @Override
    public Holding aggregatePortfolioHoldingsByTicker(int portfolioId, String ticker) {
        Holding aggHolding = new Holding();
        aggHolding.setTicker(ticker);
        aggHolding.setShareQuantity(0);
        aggHolding.setPortfolioId(portfolioId);

        List<Holding> holdings
                = ((HoldingDao) dao).getPortfolioHoldingsByTicker(portfolioId, ticker);
        for (Holding holding : holdings) {
            int totalQuantity = aggHolding.getShareQuantity() + holding.getShareQuantity();
            aggHolding.setShareQuantity(totalQuantity);
        }

        return aggHolding;
    }

    @Override
    @Transactional
    public int sellHoldingsByAggHolding(Holding aggregateHolding) {
        int portfolioId = aggregateHolding.getPortfolioId();
        String ticker = aggregateHolding.getTicker();
        List<Holding> holdings
                = ((HoldingDao) dao).getPortfolioHoldingsByTicker(portfolioId, ticker);

        // sorted by newest first
        holdings.sort(Comparator.comparing(Holding::getPurchaseDate));

        int sellQuantity = aggregateHolding.getShareQuantity();

        // reverse iteration to get oldest first
        for (int i = holdings.size() - 1; sellQuantity > 0 && i > 0; i--) {
            Holding holding = holdings.get(i);
            int holdingQuantity = holding.getShareQuantity();
            if (sellQuantity > holdingQuantity && dao.deleteMemberByKey(holding)) {
                sellQuantity = sellQuantity - holdingQuantity;
            } else {
                holding.setShareQuantity(holdingQuantity - sellQuantity);
                if (dao.updateMember(holding)) {
                    sellQuantity = 0;
                }
            }
        }

        return aggregateHolding.getShareQuantity() - sellQuantity;
    }
}
