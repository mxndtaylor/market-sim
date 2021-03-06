package com.dkkm.marketsim.model.dao;

import com.dkkm.marketsim.TestApplicationConfiguration;
import com.dkkm.marketsim.model.dto.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = TestApplicationConfiguration.class)
class ClosingDaoTest {

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
        Closing closing = mocker.nextClosing();
        Stock stock = closing.getStock();
        stock = stockDao.addMember(stock);
        closing.setTicker(stock.getTicker());

        //when
        Closing createdClosing = closingDao.addMember(closing);
        Closing fetchedClosing = closingDao.getMemberByKey(closing);

        //then
        assertEquals(closing, createdClosing);
        assertEquals(closing, fetchedClosing);
    }

    @Test
    public void getMembers() {
        //given
        final int LIST_LENGTH = 50 - mocker.nextInt(50);
        List<Closing> closings = mocker.closings().limit(LIST_LENGTH).collect(Collectors.toList());
        Set<String> tickers = new HashSet<>();
        for (Closing closing : closings) {
            Stock stock = closing.getStock();

            // avoid duplicate keys
            while (tickers.contains(stock.getTicker())) {
                stock = mocker.nextStock();
            }
            closing.setTicker(stock.getTicker());
            tickers.add(stock.getTicker());

            stockDao.addMember(stock);
            closingDao.addMember(closing);
        }

        //when
        List<Closing> fetchedClosings = closingDao.getMembers();

        //then
        assertEquals(LIST_LENGTH, fetchedClosings.size());
        assertTrue(fetchedClosings.containsAll(closings));
    }

    @Test
    public void deleteMemberByKey() {
        //given
        final int LIST_LENGTH = 50 - mocker.nextInt(50);
        List<Closing> closings = mocker.closings().limit(LIST_LENGTH).collect(Collectors.toList());
        Set<String> tickers = new HashSet<>();
        for (Closing closing : closings) {
            Stock stock = closing.getStock();

            // avoid duplicate keys
            while (tickers.contains(stock.getTicker())) {
                stock = mocker.nextStock();
            }
            closing.setTicker(stock.getTicker());
            tickers.add(stock.getTicker());

            stockDao.addMember(stock);
            closingDao.addMember(closing);
        }
        Closing closingToDelete = closings.get(0);
        List<Closing> preDeleteClosings = closingDao.getMembers();

        //when
        boolean deleteSucceeded = closingDao.deleteMemberByKey(closingToDelete);
        List<Closing> postDeleteClosings = closingDao.getMembers();

        //then
        assertTrue(preDeleteClosings.contains(closingToDelete));
        if (deleteSucceeded) {
            assertEquals(LIST_LENGTH - 1, postDeleteClosings.size());
            assertFalse(postDeleteClosings.contains(closingToDelete));
        } else {
            assertEquals(LIST_LENGTH, postDeleteClosings.size());
            assertTrue(postDeleteClosings.contains(closingToDelete));
        }
        assertTrue(deleteSucceeded);
    }

    @Test
    public void updateMember() {
        //given
        Closing closingToBeUpdated = mocker.nextClosing();
        Stock stock = closingToBeUpdated.getStock();
        stockDao.addMember(stock);
        closingToBeUpdated = closingDao.addMember(closingToBeUpdated);

        List<Closing> preUpdateClosings = closingDao.getMembers();

        BigDecimal priceDelta = BigDecimal.valueOf(mocker.nextDouble()).setScale(2, RoundingMode.FLOOR);
        Closing closingWithUpdates = new Closing(closingToBeUpdated.getDate(),
                stock.getTicker(), closingToBeUpdated.getPrice().subtract(priceDelta));


        //when
        boolean updateSucceeded = closingDao.updateMember(closingWithUpdates);
        Closing fetchWithOldKey = closingDao.getMemberByKey(closingToBeUpdated);
        Closing fetchWithNewKey = closingDao.getMemberByKey(closingWithUpdates);
        List<Closing> postUpdateClosings = closingDao.getMembers();


        //then
        // verify arrangement
        assertNotEquals(closingToBeUpdated, closingWithUpdates);
        assertTrue(preUpdateClosings.contains(closingToBeUpdated));
        assertFalse(preUpdateClosings.contains(closingWithUpdates));

        // verify behavior
        assertEquals(preUpdateClosings.size(), postUpdateClosings.size());
        if (updateSucceeded) {
            assertEquals(closingWithUpdates, fetchWithNewKey);
            assertEquals(fetchWithNewKey, fetchWithOldKey);

            assertTrue(postUpdateClosings.contains(closingWithUpdates));
            assertFalse(postUpdateClosings.contains(closingToBeUpdated));
        } else {
            assertEquals(closingToBeUpdated, fetchWithOldKey);
            assertEquals(closingToBeUpdated, fetchWithNewKey);

            assertFalse(postUpdateClosings.contains(closingWithUpdates));
            assertTrue(postUpdateClosings.contains(closingToBeUpdated));
        }
        assertTrue(updateSucceeded);
    }

    @Test
    public void getClosingsByDate() {
        //given
        final int LIST_LENGTH = 50 - mocker.nextInt(50);
        final LocalDate dateToSearchBy = mocker.nextDate();
        List<Closing> closings = mocker.closings().limit(LIST_LENGTH).collect(Collectors.toList());
        List<Closing> expectedClosingsByDate = new ArrayList<>();
        Set<String> tickers = new HashSet<>();
        for (Closing closing : closings) {
            Stock stock = closing.getStock();

            while (tickers.contains(stock.getTicker())) {
                stock = mocker.nextStock();
            }
            closing.setTicker(stock.getTicker());
            tickers.add(stock.getTicker());

            stockDao.addMember(stock);

            if (mocker.nextBoolean()) {
                closing.setDate(dateToSearchBy);
                closing = closingDao.addMember(closing);
                expectedClosingsByDate.add(closing);
            } else {
                closingDao.addMember(closing);
            }
        }

        //when
        List<Closing> fetchedClosings = closingDao.getMembers();
        List<Closing> actualClosingsByDate = closingDao.getClosingsByDate(dateToSearchBy);

        //then
        assertTrue(fetchedClosings.containsAll(actualClosingsByDate));
        assertEquals(expectedClosingsByDate.size(), actualClosingsByDate.size());
        assertTrue(actualClosingsByDate.containsAll(expectedClosingsByDate));
    }
}