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
class StockDBDaoTest {

    @Autowired
    private PortfolioDao portfolioDao;

    @Autowired
    private HoldingDao holdingDao;

    @Autowired
    private ClosingDao closingDao;

    @Autowired
    private StockDao stockDao;

    /** generates randomized dtos
     * if you generate a portfolio for example, it generates holdings
     * which generate closings, which generate stocks
     * all are accessible through progressive get calls from the portfolio
     */
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
        Stock stock = mocker.nextStock();

        //when
        Stock createdStock = stockDao.addMember(stock);
        Stock fetchedStock = stockDao.getMemberByKey(stock.getTicker());

        //then
        assertEquals(stock, createdStock);
        assertEquals(stock, fetchedStock);
    }

    @Test
    public void getMembers() {
        //given
        final int LIST_LENGTH = 50 - mocker.nextInt(50);
        List<Stock> stocks = mocker.stocks().limit(LIST_LENGTH).collect(Collectors.toList());
        Set<String> tickers = new HashSet<>();
        for (int i = 0; i < stocks.size(); i++) {
            Stock stock = stocks.get(i);
            while (tickers.contains(stock.getTicker())) {
                stock = mocker.nextStock();
            }
            tickers.add(stock.getTicker());
            stocks.set(i, stock);

            stockDao.addMember(stock);
        }

        //when
        List<Stock> fetchedStocks = stockDao.getMembers();

        //then
        assertEquals(LIST_LENGTH, fetchedStocks.size());
        assertTrue(fetchedStocks.containsAll(stocks));
    }

    @Test
    public void deleteMemberByKey() {
        final int LIST_LENGTH = 50 - mocker.nextInt(50);
        List<Stock> stocks = mocker.stocks().limit(LIST_LENGTH).collect(Collectors.toList());
        Set<String> tickers = new HashSet<>();
        for (int i = 0; i < stocks.size(); i++) {
            Stock stock = stocks.get(i);
            while (tickers.contains(stock.getTicker())) {
                stock = mocker.nextStock();
            }
            tickers.add(stock.getTicker());
            stocks.set(i, stock);

            stockDao.addMember(stock);
        }
        Stock stockToDelete = stocks.get(0);
        List<Stock> preDeleteStocks = stockDao.getMembers();

        //when
        boolean deleteSucceeded = stockDao.deleteMemberByKey(stockToDelete.getTicker());
        List<Stock> postDeleteStocks = stockDao.getMembers();

        //then
        assertTrue(preDeleteStocks.contains(stockToDelete));
        if (deleteSucceeded) {
            assertEquals(LIST_LENGTH - 1, postDeleteStocks.size());
            assertFalse(postDeleteStocks.contains(stockToDelete));
        } else {
            assertEquals(LIST_LENGTH, postDeleteStocks.size());
            assertTrue(postDeleteStocks.contains(stockToDelete));
        }
        assertTrue(deleteSucceeded);
    }

    @Test
    public void updateMember() {
        //given
        Stock stockToBeUpdated = mocker.nextStock();
        stockToBeUpdated = stockDao.addMember(stockToBeUpdated);

        List<Stock> preUpdateStocks = stockDao.getMembers();

        LocalDate newIpo = mocker.nextDate();
        Stock stockWithUpdates = new Stock();
        stockWithUpdates.setTicker(stockToBeUpdated.getTicker());
        stockWithUpdates.setIpo(newIpo);

        //when
        boolean updateSucceeded = stockDao.updateMember(stockWithUpdates);
        Stock fetchWithOldKey = stockDao.getMemberByKey(stockToBeUpdated.getTicker());
        Stock fetchWithNewKey = stockDao.getMemberByKey(stockWithUpdates.getTicker());
        List<Stock> postUpdateStocks = stockDao.getMembers();


        //then
        // verify arrangement
        assertNotEquals(stockToBeUpdated, stockWithUpdates);
        assertTrue(preUpdateStocks.contains(stockToBeUpdated));
        assertFalse(preUpdateStocks.contains(stockWithUpdates));

        // verify behavior
        assertEquals(preUpdateStocks.size(), postUpdateStocks.size());
        if (updateSucceeded) {
            assertEquals(stockWithUpdates, fetchWithNewKey);
            assertEquals(fetchWithNewKey, fetchWithOldKey);

            assertTrue(postUpdateStocks.contains(stockWithUpdates));
            assertFalse(postUpdateStocks.contains(stockToBeUpdated));
        } else {
            assertEquals(stockToBeUpdated, fetchWithOldKey);
            assertEquals(stockToBeUpdated, fetchWithNewKey);

            assertFalse(postUpdateStocks.contains(stockWithUpdates));
            assertTrue(postUpdateStocks.contains(stockToBeUpdated));
        }
        assertTrue(updateSucceeded);
    }
}