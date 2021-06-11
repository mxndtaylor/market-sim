package com.dkkm.marketsim.model.dao;

import com.dkkm.marketsim.TestApplicationConfiguration;
import com.dkkm.marketsim.model.dto.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = TestApplicationConfiguration.class)
class PortfolioDaoTest {

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

        //when
        Portfolio createdPortfolio = portfolioDao.addMember(portfolio);
        Portfolio fetchedPortfolio = portfolioDao.getMemberByKey(portfolio.getId());

        //then
        assertEquals(portfolio, createdPortfolio);
        assertEquals(portfolio, fetchedPortfolio);
    }

    @Test
    public void getMembers() {
        //given
        final int LIST_LENGTH = 50 - mocker.nextInt(50);
        List<Portfolio> portfolios = mocker.portfolios().limit(LIST_LENGTH).collect(Collectors.toList());
        Set<Integer> ids = new HashSet<>();
        for (int i = 0; i < portfolios.size(); i++) {
            Portfolio portfolio = portfolios.get(i);
            while (ids.contains(portfolio.getId())) {
                portfolio = mocker.nextPortfolio();
            }
            ids.add(portfolio.getId());
            portfolios.set(i, portfolio);

            portfolioDao.addMember(portfolio);
        }

        //when
        List<Portfolio> fetchedPortfolios = portfolioDao.getMembers();

        //then
        assertEquals(LIST_LENGTH, fetchedPortfolios.size());
        for (Portfolio portfolio : portfolios) {
            if (!fetchedPortfolios.contains(portfolio)) {
                System.out.println(portfolio);
            }
        }

        for (Portfolio portfolio : fetchedPortfolios) {
            if (!portfolios.contains(portfolio)) {
                System.out.println(portfolio);
            }
        }
        assertTrue(fetchedPortfolios.containsAll(portfolios));
    }

    @Test
    public void deleteMemberByKey() {
        //given
        final int LIST_LENGTH = 50 - mocker.nextInt(50);
        List<Portfolio> portfolios = mocker.portfolios().limit(LIST_LENGTH).collect(Collectors.toList());
        Set<Integer> ids = new HashSet<>();
        for (int i = 0; i < portfolios.size(); i++) {
            Portfolio portfolio = portfolios.get(i);
            while (ids.contains(portfolio.getId())) {
                portfolio = mocker.nextPortfolio();
            }
            ids.add(portfolio.getId());
            portfolios.set(i, portfolio);

            portfolioDao.addMember(portfolio);
        }
        Portfolio portfolioToDelete = portfolios.get(0);
        List<Portfolio> preDeletePortfolios = portfolioDao.getMembers();

        //when
        boolean deleteSucceeded = portfolioDao.deleteMemberByKey(portfolioToDelete.getId());
        List<Portfolio> postDeletePortfolios = portfolioDao.getMembers();

        //then
        assertTrue(preDeletePortfolios.contains(portfolioToDelete));
        if (deleteSucceeded) {
            assertEquals(LIST_LENGTH - 1, postDeletePortfolios.size());
            assertFalse(postDeletePortfolios.contains(portfolioToDelete));
        } else {
            assertEquals(LIST_LENGTH, postDeletePortfolios.size());
            assertTrue(postDeletePortfolios.contains(portfolioToDelete));
        }
        assertTrue(deleteSucceeded);
    }

    @Test
    public void updateMember() {
        //given
        Portfolio portfolioToBeUpdated = mocker.nextPortfolio();
        portfolioToBeUpdated = portfolioDao.addMember(portfolioToBeUpdated);

        List<Portfolio> preUpdatePortfolios = portfolioDao.getMembers();

        Portfolio portfolioWithUpdates = portfolioToBeUpdated;
        while (portfolioWithUpdates.equals(portfolioToBeUpdated)) {
            portfolioWithUpdates = mocker.nextPortfolio();
        }
        portfolioWithUpdates.setId(portfolioToBeUpdated.getId());

        //when
        boolean updateSucceeded = portfolioDao.updateMember(portfolioWithUpdates);
        Portfolio fetchWithOldKey = portfolioDao.getMemberByKey(portfolioToBeUpdated.getId());
        Portfolio fetchWithNewKey = portfolioDao.getMemberByKey(portfolioWithUpdates.getId());
        List<Portfolio> postUpdatePortfolios = portfolioDao.getMembers();


        //then
        // verify arrangement
        assertNotEquals(portfolioToBeUpdated, portfolioWithUpdates);
        assertTrue(preUpdatePortfolios.contains(portfolioToBeUpdated));
        assertFalse(preUpdatePortfolios.contains(portfolioWithUpdates));

        // verify behavior
        assertEquals(preUpdatePortfolios.size(), postUpdatePortfolios.size());
        if (updateSucceeded) {
            assertEquals(portfolioWithUpdates, fetchWithNewKey);
            assertEquals(fetchWithNewKey, fetchWithOldKey);

            assertTrue(postUpdatePortfolios.contains(portfolioWithUpdates));
            assertFalse(postUpdatePortfolios.contains(portfolioToBeUpdated));
        } else {
            assertEquals(portfolioToBeUpdated, fetchWithOldKey);
            assertEquals(portfolioToBeUpdated, fetchWithNewKey);

            assertFalse(postUpdatePortfolios.contains(portfolioWithUpdates));
            assertTrue(postUpdatePortfolios.contains(portfolioToBeUpdated));
        }
        assertTrue(updateSucceeded);
    }
}