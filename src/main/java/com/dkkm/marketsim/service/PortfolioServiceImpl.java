package com.dkkm.marketsim.service;

import com.dkkm.marketsim.model.dao.PortfolioDao;
import com.dkkm.marketsim.model.dto.Holding;
import com.dkkm.marketsim.model.dto.Portfolio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
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
    public Portfolio addMember(Portfolio portfolio) {
        LocalDate currentDate = portfolio.getDate();
        LocalDate startDate = portfolio.getStartDate();
        if (startDate != null) {
            portfolio.setDate(startDate);
        } else if (currentDate != null) {
            portfolio.setStartDate(currentDate);
        }

        BigDecimal currentCash = portfolio.getCash();
        BigDecimal startCash = portfolio.getStartCash();
        if (startCash != null) {
            portfolio.setCash(startCash);
        } else if (currentCash != null) {
            portfolio.setStartCash(currentCash);
        }

        portfolio = super.addMember(portfolio);
        portfolio.setHoldings(new ArrayList<>());
        return portfolio;
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
    @Transactional
    public Holding sellTickerQuantityFromPortfolio(int portfolioId, String ticker, int sellQuantity) {
        Portfolio portfolio = dao.getMemberByKey(portfolioId);

        Holding receipt = holdingService.aggregatePortfolioHoldingsByTicker(portfolioId, ticker);

        // guarantee that 0 < sellQuantity < availableQuantity
        int availableQuantity = receipt.getShareQuantity();
        sellQuantity = Math.max(sellQuantity, 0);
        sellQuantity = Math.min(availableQuantity, sellQuantity);

        // save attempt to receipt
        receipt.setShareQuantity(sellQuantity);

        // attempt sale, save actual sale in receipt
        receipt.setShareQuantity(holdingService.sellHoldingsByAggHolding(receipt));

        // calculate profit
        BigDecimal soldQuantity = BigDecimal.valueOf(receipt.getShareQuantity());
        BigDecimal shareValue = closingService.getSharePrice(ticker, portfolio.getDate());
        BigDecimal profit = shareValue.multiply(soldQuantity);

        // save negated profit to receipt to represent money flowing into portfolio
        receipt.setInvested(profit.negate());

        // update portfolio with profit
        portfolio.setCash(portfolio.getCash().add(profit));
        dao.updateMember(portfolio);

        return receipt;
    }

    @Override
    @Transactional
    public Holding buyTickerQuantityForPortfolio(int portfolioId, String ticker, int buyQuantity) {
        Portfolio portfolio = dao.getMemberByKey(portfolioId);
        Holding receipt = makePurchasePreview(portfolio, ticker, buyQuantity);

        if (receipt.getShareQuantity() == 0) {
            return receipt;
        }

        // To avoid duplicate insertion
        Holding dup = holdingService.getMemberByKey(receipt);
        if (dup != null) { // update member when there is a duplicate
            int prevPurchased = dup.getShareQuantity();
            dup.setShareQuantity(prevPurchased + receipt.getShareQuantity());

            if (!holdingService.updateMember(dup)) {
                receipt.setShareQuantity(0);
            }
        } else { // add member when there's no duplicate
            receipt = holdingService.addMember(receipt);
            receipt.setShareQuantity(receipt.getShareQuantity());
        }

        // conditional b/c holdingService may mutate receipt's share quantity
        if (receipt.getShareQuantity() > 0) {
            BigDecimal remainderAfterPurchase = portfolio.getCash().subtract(receipt.getInvested());
            portfolio.setCash(remainderAfterPurchase);
            dao.updateMember(portfolio);
        }

        return receipt;
    }

    private Holding makePurchasePreview(Portfolio portfolio, String ticker, int purchasesDesired) {
        BigDecimal price = closingService.getSharePrice(ticker, portfolio.getDate());
        BigDecimal budget = portfolio.getCash();

        // start receipt
        Holding purchasePreview = new Holding();
        purchasePreview.setPortfolioId(portfolio.getId());
        purchasePreview.setPurchaseDate(portfolio.getDate());
        purchasePreview.setTicker(ticker);

        // make sure number of actual purchases does not result in negative funds
        // find most purchases allowed regardless of desired purchase quantity
        int purchasesAllowed = budget.divide(price, RoundingMode.DOWN).intValue();
        // find most purchases that are allowed and desired
        int purchasesAllowedAndDesired = Math.min(purchasesDesired, purchasesAllowed);

        // save to receipt
        purchasePreview.setShareQuantity(purchasesAllowedAndDesired);

        // save actual purchases total price to receipt
        purchasePreview.setInvested(price.multiply(BigDecimal.valueOf(purchasesAllowedAndDesired)));

        return purchasePreview;
    }

    private void setHoldings(Portfolio portfolio) {
        portfolio.setHoldings(holdingService.aggregatePortfolioHoldings(portfolio.getId()));
    }
}
