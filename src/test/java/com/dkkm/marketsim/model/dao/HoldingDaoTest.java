package com.dkkm.marketsim.model.dao;

import com.dkkm.marketsim.TestApplicationConfiguration;
import com.dkkm.marketsim.model.dto.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = TestApplicationConfiguration.class)
class HoldingDaoTest {

    @Autowired
    private PortfolioDao portfolioDao;

    @Autowired
    private HoldingDao holdingDao;

    @Autowired
    private ClosingDao closingDao;

    @Autowired
    private StockDao stockDao;

    private Mocker mocker;

    @BeforeEach
    public void setUp() {
        List<Stock> stocks = stockDao.getMembers();
        for (Stock stock : stocks) {
            stockDao.deleteMemberByKey(stock.getTicker());
        }

        List<Closing> closings = closingDao.getMembers();
        for (Closing closing : closings) {
            closingDao.deleteMemberByKey(closing);
        }

        List<Holding> holdings = holdingDao.getMembers();
        for (Holding holding : holdings) {
            holdingDao.deleteMemberByKey(holding);
        }

        List<Portfolio> portfolios = portfolioDao.getMembers();
        for (Portfolio portfolio : portfolios) {
            portfolioDao.deleteMemberByKey(portfolio.getId());
        }

        mocker = new Mocker();
    }

    @Test
    public void addGetMember() {
        //given
        Portfolio portfolio = mocker.nextPortfolio();
        portfolioDao.addMember(portfolio);
        Holding holding = portfolio.getHoldings().get(0);
        Closing closing = holding.getClosing();
        Stock stock = closing.getStock();
        stockDao.addMember(stock);
        closingDao.addMember(closing);

        //when
        Holding createdHolding = holdingDao.addMember(holding);
        Holding fetchedByOldKey = holdingDao.getMemberByKey(holding);
        Holding fetchedByNewKey = holdingDao.getMemberByKey(createdHolding);

        //then
        assertEquals(holding, createdHolding);
        assertEquals(holding, fetchedByOldKey);
        assertEquals(holding, fetchedByNewKey);
    }

    @Test
    public void getPortfolioHoldings() {
        //given
        Portfolio portfolioToSearchBy = mocker.nextPortfolio();
        portfolioToSearchBy = portfolioDao.addMember(portfolioToSearchBy);
        int portfolioToSearchById = portfolioToSearchBy.getId();
        final int LIST_LENGTH = 30 - mocker.nextInt(30);
        List<Holding> holdings = mocker.holdings(portfolioToSearchById)
                                       .limit(LIST_LENGTH)
                                       .collect(Collectors.toList());
        holdings.forEach(holding -> {
            // TODO: avoid duplicate inserts
            Closing closing = holding.getClosing();
            Stock stock = closing.getStock();
            stockDao.addMember(stock);
            closingDao.addMember(closing);
            holdingDao.addMember(holding);
        });


        //when
        //List<Holding> fetched = holdingDao.getPortfolioHoldings(portfolioToSearchById);

        //then
        //assertEquals(LIST_LENGTH, fetched.size());

    }

    @Test
    public void getPortfolioHoldingsByTicker() {
    }

    @Test
    public void getMembers() {
    }

    @Test
    public void deleteMemberByKey() {
    }

    @Test
    public void updateMember() {
    }
}