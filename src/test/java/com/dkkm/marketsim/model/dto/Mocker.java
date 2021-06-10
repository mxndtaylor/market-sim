package com.dkkm.marketsim.model.dto;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Mocker extends Random {

    public Mocker(long seed) {
        super(seed);

        // init data type generators
        tickers();
        dates();
        prices();
        ids();
        cashes();

        // init DTO generators
        stocks();
        closings();
        holdings();
        portfolios();
    }

    public Mocker() {
        this(new Random().nextLong());
    }

    public Stream<Stock> stocks() {
        Stream<Stock> stockStream = Stream.generate(this::nextStock);
        return stockStream;
    }

    public Stock nextStock() {
        Stock stock = new Stock();
        stock.setTicker(nextTicker());
        stock.setIpo(nextDate());
        return stock;
    }

    public Stream<Closing> closings() {
        Stream<Closing> closingStream = Stream.generate(this::nextClosing);
        return closingStream;
    }

    public Closing nextClosing() {
        Closing closing = new Closing();
        closing.setDate(nextDate());
        closing.setPrice(nextPrice());

        // make valid connection
        Stock stock = nextStock();
        closing.setTicker(stock.getTicker());
        closing.setStock(stock);

        return closing;
    }

    public Stream<Holding> holdings() {
        Stream<Holding> holdingStream = Stream.generate(this::nextHolding);
        return holdingStream;
    }

    public Holding nextHolding() {
        Holding holding = new Holding();
        holding.setPortfolioId(nextPortfolio().getId());
        holding.setShareQuantity(nextId());

        // make valid connections
        Closing closing = nextClosing();
        holding.setTicker(closing.getTicker());
        holding.setPurchaseDate(closing.getDate());
        holding.setClosing(closing);

        return holding;
    }

    public Stream<Portfolio> portfolios() {
        Stream<Portfolio> portfolioStream = Stream.generate(this::nextPortfolio);
        return portfolioStream;
    }

    public Portfolio nextPortfolio() {
        Portfolio portfolio = new Portfolio();
        portfolio.setId(nextId());

        // make real valid holdings to insert into table
        List<Holding> holdings = new ArrayList<>();
        for (int i = 0; i < super.nextInt(12); i++) {
            Holding holding = nextHolding();
            holding.setPortfolioId(portfolio.getId());
            holdings.add(holding);
        }
        portfolio.setHoldings(holdings);

        portfolio.setCash(nextCash());
        portfolio.setStartCash(nextCash());

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

    public IntStream ids() {
        IntStream idStream = super.ints(0, Integer.MAX_VALUE);
        return idStream;
    }

    public Integer nextId() {
        return super.ints(0, Integer.MAX_VALUE).iterator().nextInt();
    }

    public DoubleStream prices() {
        DoubleStream doubleStream = super.doubles(0, 400000);
        return doubleStream;
    }

    public Double nextPrice() {
        return super.doubles(0, 400000).iterator().next();
    }

    public DoubleStream cashes() {
        DoubleStream doubleStream = super.doubles(0, Double.MAX_VALUE);
        return doubleStream;
    }

    public Double nextCash() {
        return super.doubles(0, Double.MAX_VALUE).iterator().next();
    }

    public Stream<String> tickers() {
        Stream<String> tickerStream = Stream.generate(this::nextTicker);
        return tickerStream;
    }

    public String nextTicker() {
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
        Stream<LocalDate> dateStream = Stream.generate(this::nextDate);
        return dateStream;
    }

    public LocalDate nextDate() {
        long epoch = LocalDate.EPOCH.toEpochDay();
        long now = LocalDate.now().toEpochDay();
        long epochDay = super.longs(epoch, now).limit(1).iterator().nextLong();
        return LocalDate.ofEpochDay(epochDay);
    }
}
