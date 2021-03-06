package com.dkkm.marketsim.model.dto;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/** generates randomized dtos
 * if you generate a portfolio for example, it generates holdings
 * which generate closings, which generate stocks
 * all are accessible through progressive get calls from the portfolio
 */
public class Mocker extends Random {

    public Mocker(long seed) {
        super(seed);
    }

    public Mocker() {
        super();
    }

    private void reseed() {
        super.setSeed(nextLong());
    }

    public Stream<Stock> stocks() {
        return Stream.generate(this::nextStock);
    }

    public Stock nextStock() {
        reseed();
        Stock stock = new Stock();
        stock.setTicker(nextTicker());
        stock.setIpo(nextDate());
        return stock;
    }

    public Stream<Closing> closings() {
        return Stream.generate(this::nextClosing);
    }

    public Closing nextClosing() {
        reseed();
        Closing closing = new Closing();
        closing.setDate(nextDate());
        closing.setPrice(nextMoneyAmount());

        // make valid connection
        Stock stock = nextStock();
        closing.setTicker(stock.getTicker());
        closing.setStock(stock);

        return closing;
    }

    public Stream<Holding> holdings(int portfolioId) {
        return Stream.generate(() -> (nextHolding(portfolioId)));
    }

    public Holding nextHolding(int portfolioId) {
        reseed();
        Holding holding = new Holding();
        holding.setPortfolioId(portfolioId);
        holding.setShareQuantity(nextPositiveInt());

        // make valid connections
        Closing closing = nextClosing();
        holding.setTicker(closing.getTicker());
        holding.setPurchaseDate(closing.getDate());
        holding.setClosing(closing);

        // calculate invested
        BigDecimal price = holding.getClosing().getPrice();
        BigDecimal invested = BigDecimal.valueOf(holding.getShareQuantity()).multiply(price);
        holding.setInvested(invested);

        return holding;
    }

    public Stream<Portfolio> portfolios() {
        return Stream.generate(this::nextPortfolio);
    }

    public Portfolio nextPortfolio() {
        reseed();
        Portfolio portfolio = new Portfolio();
        portfolio.setId(nextPositiveInt());

        // make valid holdings to insert into table
        List<Holding> holdings = new ArrayList<>();
        for (int i = 0; i < super.nextInt(12); i++) {
            Holding holding = nextHolding(portfolio.getId());
            holding.setPortfolioId(portfolio.getId());
            holdings.add(holding);
        }
        portfolio.setHoldings(holdings);

        portfolio.setCash(nextMoneyAmount());
        portfolio.setStartCash(nextMoneyAmount());

        // verify start date comes first
        LocalDate startDate = nextDate();
        LocalDate currentDate = nextDate();
        if (currentDate.isBefore(startDate)) {
            portfolio.setStartDate(currentDate);
            portfolio.setDate(startDate);
        } else {
            portfolio.setStartDate(startDate);
            portfolio.setDate(currentDate);
        }

        return portfolio;
    }

    public IntStream positiveInts() {
        return super.ints(0, Integer.MAX_VALUE);
    }

    public Integer nextPositiveInt() {
        reseed();
        return super.ints(0, Integer.MAX_VALUE).iterator().nextInt();
    }

    public Stream<BigDecimal> moneyAmounts() {
        reseed();
        return Stream.generate(this::nextMoneyAmount);
    }

    public BigDecimal nextMoneyAmount() {
        reseed();
        return BigDecimal.valueOf(
                // $100 billion seems like a big enough amount that no one would
                // sit around for long enough to earn, so it makes a good testing maximum
                // plus numbers over that seem to have rounding errors when converting to
                // doubles for the sql table
                super.doubles(0, 1000000000)
                        .iterator().next()).setScale(2, RoundingMode.HALF_UP);
    }

    public Stream<String> tickers() {
        return Stream.generate(this::nextTicker);
    }

    public String nextTicker() {
        reseed();

        int asciiMin = 65; // ascii code for A
        int asciiMax = 90; // ascii code for Z
        int tickerLength = 4 - super.nextInt(4);

        // generates a stream of ints with ascii codepoints of capital letters, limits the length to (0,4]
        // uses StringBuilder to turn the stream into a string.
        return super.ints(asciiMin, asciiMax).limit(tickerLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

    public Stream<LocalDate> dates() {
        return Stream.generate(this::nextDate);
    }

    public LocalDate nextDate() {
        reseed();

        long epoch = LocalDate.now().toEpochDay();
        long now = LocalDate.now().toEpochDay();
        long epochDay = super.longs(epoch, now).limit(1).iterator().nextLong();
        return LocalDate.ofEpochDay(epochDay);
    }
}
