package com.dkkm.marketsim.service;

import com.dkkm.marketsim.model.dao.PortfolioDao;
import com.dkkm.marketsim.model.dto.Holding;
import com.dkkm.marketsim.model.dto.Portfolio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PortfolioServiceImpl
        extends PassThruCrudServiceImpl<Portfolio, Integer>
        implements PortfolioService {

    private HoldingService holdingService;
    private ClosingService closingService;

    @Autowired
    public PortfolioServiceImpl(PortfolioDao portfolioDao, HoldingService holdingService,
                                ClosingService closingService) {
        dao = portfolioDao;

        this.closingService = closingService;
        this.holdingService = holdingService;
    }

    @Override
    public Portfolio getMemberByKey(Integer portfolioId) {
        Portfolio portfolio = super.getMemberByKey(portfolioId);
        setHoldings(portfolio);
        return portfolio;
    }

    @Override
    public List<Portfolio> getMembers() {
        List<Portfolio> portfolios = super.getMembers();
        for (Portfolio portfolio : portfolios) {
            setHoldings(portfolio);
        }
        return portfolios;
    }

    @Override
    public boolean updateMember(Portfolio portfolio) {
        List<Holding> holdings = portfolio.getHoldings();
        if (holdings != null && holdings.size() != 0) {
            for (Holding holding : holdings) {
                holdingService.updateMember(holding);
            }
            portfolio.setHoldings(null);
        }
        return super.updateMember(portfolio);
    }

    @Override
    public int sellTickerQuantityFromPortfolio(int portfolioId, String ticker, int sellQuantity) {
        Portfolio portfolio = dao.getMemberByKey(portfolioId);

        Holding holding = holdingService.aggregatePortfolioHoldingsByTicker(portfolioId, ticker);
        int initialQuantity = holding.getShareQuantity();
        int remainingQuantity = Math.max(0, initialQuantity - sellQuantity);

        holding.setShareQuantity(remainingQuantity);
        int soldQuantity = holdingService.sellHoldingsByAggHolding(holding);

        double shareValue = closingService.getSharePrice(ticker, portfolio.getDate());
        portfolio.setCash(portfolio.getCash() + soldQuantity * shareValue);
        dao.updateMember(portfolio);
        return soldQuantity;
    }

    @Override
    public int buyTickerQuantityForPortfolio(int portfolioId, String ticker, int buyQuantity) {
        Portfolio portfolio = dao.getMemberByKey(portfolioId);

        double pricePerShare = closingService.getSharePrice(ticker, portfolio.getDate());
        int affordQuantity = (int) Math.floor(portfolio.getCash() / pricePerShare);
        int boughtQuantity = Math.min(buyQuantity, affordQuantity);

        Holding holding = new Holding();
        holding.setShareQuantity(boughtQuantity);

        if (holding.getShareQuantity() > 0) {
            Holding newHolding = new Holding();
            newHolding.setPortfolioId(portfolioId);
            newHolding.setTicker(ticker);
            newHolding.setPurchaseDate(portfolio.getDate());

            holding = holdingService.addMember(newHolding);
        }

        return holding.getShareQuantity();
    }

    private void setHoldings(Portfolio portfolio) {
        portfolio.setHoldings(holdingService.aggregatePortfolioHoldings(portfolio.getId()));
    }
}
