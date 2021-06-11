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
import java.util.*;
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
        portfolio = portfolioDao.addMember(portfolio);
        Holding holding = portfolio.getHoldings().get(0);
        holding.setPortfolioId(portfolio.getId());

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
        List<Holding> expectedResults = mocker.holdings(portfolioToSearchById)
                .limit(LIST_LENGTH)
                .collect(Collectors.toList());
        Set<String> tickers = new HashSet<>();
        Map<String, LocalDate> closingKeys = new HashMap<>();
        expectedResults.forEach(holding -> {
            Closing closing = holding.getClosing();
            Stock stock = closing.getStock();

            // avoid duplicate inserts
            while (tickers.contains(stock.getTicker())) {
                stock = mocker.nextStock();
            }
            tickers.add(stock.getTicker());
            closing.setTicker(stock.getTicker());
            closingKeys.put(closing.getTicker(), closing.getDate());
            closing.setStock(stock);
            holding.setTicker(stock.getTicker());

            // add to table
            stockDao.addMember(stock);
            closingDao.addMember(closing);
            holdingDao.addMember(holding);
        });
        portfolioToSearchBy.setHoldings(expectedResults);

        // arranged with an extra portfolio to differentiate from the getMembers method
        Portfolio portfolioNotToSearchBy = mocker.nextPortfolio();
        portfolioNotToSearchBy = portfolioDao.addMember(portfolioNotToSearchBy);
        int portfolioNotToSearchById = portfolioNotToSearchBy.getId();
        List<Holding> unexpectedResults = portfolioNotToSearchBy.getHoldings();
        unexpectedResults.forEach(holding -> {
            holding.setPortfolioId(portfolioNotToSearchById);
            Closing closing = holding.getClosing();

            // avoid duplicate inserts
            if (tickers.contains(holding.getTicker())) {
                while (closingKeys.get(closing.getTicker()).equals(closing.getDate())) {
                    closing.setDate(mocker.nextDate());
                }
            } else {
                stockDao.addMember(closing.getStock());
            }
            closingDao.addMember(closing);
            holdingDao.addMember(holding);
        });
        portfolioNotToSearchBy.setHoldings(unexpectedResults);

        //when
        List<Holding> fetched = holdingDao.getPortfolioHoldings(portfolioToSearchById);

        //then
        assertEquals(LIST_LENGTH, fetched.size());
        assertTrue(fetched.containsAll(expectedResults));
        assertFalse(unexpectedResults.containsAll(fetched));
    }

    @Test
    public void getPortfolioHoldingsByTicker() {
        //given
        Portfolio portfolioToSearchBy = mocker.nextPortfolio();
        portfolioToSearchBy = portfolioDao.addMember(portfolioToSearchBy);
        int portfolioToSearchById = portfolioToSearchBy.getId();

        final int LIST_LENGTH = 30 - mocker.nextInt(30);
        List<Holding> expectedResults = mocker.holdings(portfolioToSearchById)
                .limit(LIST_LENGTH)
                .collect(Collectors.toList());
        Set<String> tickers = new HashSet<>();
        Map<String, LocalDate> closingKeys = new HashMap<>();
        expectedResults.forEach(holding -> {
            Closing closing = holding.getClosing();
            Stock stock = closing.getStock();

            // avoid duplicate inserts
            while (tickers.contains(stock.getTicker())) {
                stock = mocker.nextStock();
            }
            tickers.add(stock.getTicker());
            closing.setTicker(stock.getTicker());
            closingKeys.put(closing.getTicker(), closing.getDate());
            closing.setStock(stock);
            holding.setTicker(stock.getTicker());

            // add to table
            stockDao.addMember(stock);
            closingDao.addMember(closing);
            holdingDao.addMember(holding);
        });
        portfolioToSearchBy.setHoldings(expectedResults);

        // arranged with an extra portfolio to differentiate from the getMembers method
        Portfolio portfolioNotToSearchBy = mocker.nextPortfolio();
        portfolioNotToSearchBy = portfolioDao.addMember(portfolioNotToSearchBy);
        int portfolioNotToSearchById = portfolioNotToSearchBy.getId();
        List<Holding> unexpectedResults = portfolioNotToSearchBy.getHoldings();
        unexpectedResults.forEach(holding -> {
            holding.setPortfolioId(portfolioNotToSearchById);
            Closing closing = holding.getClosing();

            // avoid duplicate inserts
            if (tickers.contains(holding.getTicker())) {
                while (closingKeys.get(closing.getTicker()).equals(closing.getDate())) {
                    closing.setDate(mocker.nextDate());
                }
            } else {
                stockDao.addMember(closing.getStock());
            }
            closingDao.addMember(closing);
            holdingDao.addMember(holding);
        });
        portfolioNotToSearchBy.setHoldings(unexpectedResults);

        //when
        List<Holding> fetched = holdingDao.getPortfolioHoldings(portfolioToSearchById);

        //then
        assertEquals(LIST_LENGTH, fetched.size());
        assertTrue(fetched.containsAll(expectedResults));
        assertFalse(unexpectedResults.containsAll(fetched));
    }

    @Test
    public void getMembers() {
        //given
        Portfolio portfolioOne = mocker.nextPortfolio();
        portfolioOne = portfolioDao.addMember(portfolioOne);
        int portfolioToSearchById = portfolioOne.getId();

        final int HOLDINGS_ONE_LENGTH = 30 - mocker.nextInt(30);
        List<Holding> holdingsOne = mocker.holdings(portfolioToSearchById)
                .limit(HOLDINGS_ONE_LENGTH)
                .collect(Collectors.toList());
        Set<String> tickers = new HashSet<>();
        Map<String, LocalDate> closingKeys = new HashMap<>();

        // add to table
        holdingsOne.forEach(holding -> {
            Closing closing = holding.getClosing();
            Stock stock = closing.getStock();

            // avoid duplicate inserts
            while (tickers.contains(stock.getTicker())) {
                stock = mocker.nextStock();
            }
            tickers.add(stock.getTicker());
            closing.setTicker(stock.getTicker());
            closingKeys.put(closing.getTicker(), closing.getDate());
            closing.setStock(stock);
            holding.setTicker(stock.getTicker());

            // add to table
            stockDao.addMember(stock);
            closingDao.addMember(closing);
            holdingDao.addMember(holding);
        });

        Portfolio portfolioTwo = mocker.nextPortfolio();
        portfolioTwo = portfolioDao.addMember(portfolioTwo);
        List<Holding> holdingsTwo = portfolioTwo.getHoldings();
        final int HOLDINGS_TWO_LENGTH = holdingsTwo.size();

        // add to table
        Portfolio finalPortfolioTwo = portfolioTwo;
        holdingsTwo.forEach(holding -> {
            holding.setPortfolioId(finalPortfolioTwo.getId());
            Closing closing = holding.getClosing();

            // avoid duplicate inserts
            if (tickers.contains(holding.getTicker())) {
                while (closingKeys.get(closing.getTicker()).equals(closing.getDate())) {
                    closing.setDate(mocker.nextDate());
                }
            } else {
                stockDao.addMember(closing.getStock());
            }
            closingDao.addMember(closing);
            holdingDao.addMember(holding);
        });
        holdingsOne.addAll(holdingsTwo);
        List<Holding> holdings = holdingsOne;
        final int HOLDINGS_LENGTH = HOLDINGS_ONE_LENGTH + HOLDINGS_TWO_LENGTH;

        //when
        List<Holding> fetched = holdingDao.getMembers();

        //then
        assertEquals(HOLDINGS_LENGTH, fetched.size());
        assertTrue(fetched.containsAll(holdings));
    }

    @Test
    public void deleteMemberByKey() {
        // given
        Portfolio portfolio = mocker.nextPortfolio();
        portfolio = portfolioDao.addMember(portfolio);
        int portfolioId = portfolio.getId();

        final int HOLDINGS_LENGTH = 30 - mocker.nextInt(30);
        List<Holding> holdings = mocker.holdings(portfolioId)
                .limit(HOLDINGS_LENGTH)
                .collect(Collectors.toList());
        Set<String> tickers = new HashSet<>();

        // add to table
        holdings.forEach(holding -> {
            Closing closing = holding.getClosing();
            Stock stock = closing.getStock();

            // avoid duplicate inserts
            while (tickers.contains(stock.getTicker())) {
                stock = mocker.nextStock();
            }
            tickers.add(stock.getTicker());
            closing.setTicker(stock.getTicker());
            closing.setStock(stock);
            holding.setTicker(stock.getTicker());

            // add to table
            stockDao.addMember(stock);
            closingDao.addMember(closing);
            holdingDao.addMember(holding);
        });
        Holding holdingToDelete = holdings.get(0);
        Closing closingToDelete = holdingToDelete.getClosing();
        Stock stockToDelete = closingToDelete.getStock();

        //when
        List<Holding> preDeleteHoldings = holdingDao.getMembers();
        boolean deleteSucceeded = holdingDao.deleteMemberByKey(holdingToDelete);
        List<Holding> postDeleteHoldings = holdingDao.getMembers();


        //then
        assertEquals(HOLDINGS_LENGTH, preDeleteHoldings.size());
        assertTrue(preDeleteHoldings.contains(holdingToDelete));
        if (deleteSucceeded) {
            assertEquals(HOLDINGS_LENGTH - 1, postDeleteHoldings.size());
            assertFalse(postDeleteHoldings.contains(holdingToDelete));
        } else {
            assertEquals(HOLDINGS_LENGTH, postDeleteHoldings.size());
            assertTrue(postDeleteHoldings.contains(holdingToDelete));
        }
        assertTrue(deleteSucceeded);
    }

    @Test
    public void updateMember() {
        //given
        Portfolio portfolio = mocker.nextPortfolio();
        portfolio = portfolioDao.addMember(portfolio);
        int portfolioId = portfolio.getId();
        Holding holdingToUpdate = mocker.nextHolding(portfolioId);

        // populate table
        Closing closing = holdingToUpdate.getClosing();
        Stock stock = closing.getStock();
        stockDao.addMember(stock);
        closingDao.addMember(closing);
        holdingDao.addMember(holdingToUpdate);

        Holding holdingWithUpdates = new Holding();
        holdingWithUpdates.setTicker(holdingToUpdate.getTicker());
        holdingWithUpdates.setPortfolioId(holdingToUpdate.getPortfolioId());
        holdingWithUpdates.setPurchaseDate(holdingToUpdate.getPurchaseDate());
        holdingWithUpdates.setShareQuantity(holdingToUpdate.getShareQuantity() + 1);

        //when
        List<Holding> preUpdateHoldings = holdingDao.getMembers();
        boolean updateSucceeded = holdingDao.updateMember(holdingWithUpdates);
        Holding fetchWithOldKey = holdingDao.getMemberByKey(holdingToUpdate);
        Holding fetchWithNewKey = holdingDao.getMemberByKey(holdingWithUpdates);
        List<Holding> postUpdateHoldings = holdingDao.getMembers();

        //then
        assertEquals(postUpdateHoldings.size(), preUpdateHoldings.size());
        assertTrue(preUpdateHoldings.contains(holdingToUpdate));
        if (updateSucceeded) {
            assertEquals(holdingWithUpdates, fetchWithNewKey);
            assertEquals(fetchWithNewKey, fetchWithOldKey);

            assertTrue(postUpdateHoldings.contains(holdingWithUpdates));
            assertFalse(postUpdateHoldings.contains(holdingToUpdate));
        } else {
            assertEquals(holdingToUpdate, fetchWithOldKey);
            assertEquals(holdingToUpdate, fetchWithNewKey);

            assertFalse(postUpdateHoldings.contains(holdingWithUpdates));
            assertTrue(postUpdateHoldings.contains(holdingToUpdate));
        }
        assertTrue(updateSucceeded);
    }
}