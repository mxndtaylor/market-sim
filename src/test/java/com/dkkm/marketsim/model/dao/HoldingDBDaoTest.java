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

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = TestApplicationConfiguration.class)
class HoldingDBDaoTest {

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
    public void addMember() {
    }

    @Test
    public void getPortfolioHoldings() {
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