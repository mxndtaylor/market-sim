package com.dkkm.marketsim.service;

import com.dkkm.marketsim.model.dao.HoldingDao;
import com.dkkm.marketsim.model.dto.Closing;
import com.dkkm.marketsim.model.dto.Holding;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

@Service
public class HoldingServiceImpl
    extends PassThruCrudServiceImpl<Holding, Holding>
    implements HoldingService {

    private ClosingService closingService;

    @Autowired
    public HoldingServiceImpl(HoldingDao holdingDao, ClosingService closingService) {
        dao = holdingDao;
        this.closingService = closingService;
    }

    @Override
    public List<Holding> getPortfolioHoldings(int portfolioId) {
        List<Holding> holdings = ((HoldingDao) dao).getPortfolioHoldings(portfolioId);
        for (Holding holding : holdings) {
            setClosing(holding);
            setInvested(holding);
        }
        return holdings;
    }

    @Override
    public List<Holding> aggregatePortfolioHoldings(int portfolioId) {
        List<Holding> holdings = getPortfolioHoldings(portfolioId);
        Map<String, Holding> holdingsByTicker = new HashMap<>();

        // adds up all the quantities of a certain stock and removes purchase date field
        for (Holding holding : holdings) {
            String ticker = holding.getTicker();
            if (holdingsByTicker.containsKey(ticker)) {
                Holding aggHolding = holdingsByTicker.get(ticker);

                // total the quantity of shares of a stock
                int totalShareQuantity = aggHolding.getShareQuantity() + holding.getShareQuantity();
                aggHolding.setShareQuantity(totalShareQuantity);

                // total the invested money
                BigDecimal invested = aggHolding.getInvested();
                aggHolding.setInvested(invested.add(holding.getInvested()));

                // might not be necessary TODO: check
                holdingsByTicker.put(ticker, aggHolding);
            } else {
                holding.setClosing(null);
                holdingsByTicker.put(ticker, holding);
            }
        }

        return new ArrayList<>(holdingsByTicker.values());
    }

    @Override
    public Holding aggregatePortfolioHoldingsByTicker(int portfolioId, String ticker) {
        Holding aggHolding = new Holding();
        aggHolding.setTicker(ticker);
        aggHolding.setPortfolioId(portfolioId);

        List<Holding> holdings
                = ((HoldingDao) dao).getPortfolioHoldingsByTicker(portfolioId, ticker);
        int totalQuantity = 0;
        for (Holding holding : holdings) {
             totalQuantity +=  holding.getShareQuantity();
        }

        aggHolding.setShareQuantity(totalQuantity);

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

        // get sell orders
        int sellOrders = aggregateHolding.getShareQuantity();
        int sellOrdersRemaining = sellOrders;

        // reverse iteration to get oldest first
        for (int i = holdings.size() - 1; sellOrdersRemaining > 0 && i >= 0; i--) {
            Holding holding = holdings.get(i);
            int holdingQuantity = holding.getShareQuantity();

            // set holding quantity to 0 when we sell all of it
            //                   or to the remaining quantity when we run out of sell orders
            holding.setShareQuantity(Math.max(0, holdingQuantity - sellOrdersRemaining));

            // do nothing if update does not succeed
            if (dao.updateMember(holding)) {
                // 0 when above has remaining quantity
                // remaining orders when above is 0
                sellOrdersRemaining = Math.max(0, sellOrdersRemaining - holdingQuantity);

                // attempt delete of holdings with 0 remaining shares
                if (holding.getShareQuantity() == 0) {
                    dao.deleteMemberByKey(holding);
                }
            }
        }

        // calculate the number of successful sell orders
        return sellOrders - sellOrdersRemaining;
    }

    private void setClosing(Holding holding) {
        Closing key = new Closing();
        key.setTicker(holding.getTicker());
        key.setDate(holding.getPurchaseDate());
        key = closingService.getMemberByKey(key);

        holding.setClosing(key);
    }

    private void setInvested(Holding holding) {
        BigDecimal price;
        if (holding.getClosing() != null) {
            price = holding.getClosing().getPrice();
        } else {
            price = closingService.getSharePrice(holding.getTicker(), holding.getPurchaseDate());
        }
        BigDecimal invested = BigDecimal.valueOf(holding.getShareQuantity()).multiply(price);
        holding.setInvested(invested);
    }
}
