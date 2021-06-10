package com.dkkm.marketsim.model.dto;

import org.apache.tomcat.jni.Local;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Mocker {

    // rng generator
    private Random rng;

    // data type generators
    private Iterator<String> tickerIter;
    private Iterator<LocalDate> dateIter;
    private Iterator<Double> priceIter;
    private Iterator<Integer> idIter;
    private Iterator<Double> cashIter;

    // DTO generators
    private Iterator<Stock> stockIter;
    private Iterator<Closing> closingIter;
    private Iterator<Holding> holdingIter;
    private Iterator<Portfolio> portfolioIter;

    public Mocker(long seed) {
        rng = new Random(seed);

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
        Stream<Stock> stockStream = Stream.generate(() -> {
            Stock stock = new Stock();
            stock.setTicker(tickerIter.next());
            stock.setIpo(dateIter.next());

            return stock;
        });
        if (stockIter == null) {
            stockIter = stockStream.iterator();
        }
        return stockStream;
    }

    public Stock nextStock() {
        return stockIter.next();
    }

    public Stream<Closing> closings(Iterator<Stock> stockIter) {
        Stream<Closing> closingStream = Stream.generate(() -> {
            Closing closing = new Closing();
            closing.setDate(dateIter.next());
            closing.setPrice(priceIter.next());

            // make valid connection
            Stock stock = stockIter.next();
            closing.setTicker(stock.getTicker());
            closing.setStock(stock);

            return closing;
        });
        if (closingIter == null) {
            closingIter = closingStream.iterator();
        }
        return closingStream;
    }

    public Stream<Closing> closings() {
        return closings(stockIter);
    }

    public Closing nextClosing() {
        return closingIter.next();
    }

    public Stream<Holding> holdings(Iterator<Closing> closingIter, Iterator<Portfolio> portfolioIter) {

        Stream<Holding> holdingStream = Stream.generate(() -> {
            Holding holding = new Holding();
            holding.setPortfolioId(idIter.next());
            holding.setShareQuantity(idIter.next());

            // make valid connections
            Closing closing = closingIter.next();
            holding.setTicker(closing.getTicker());
            holding.setPurchaseDate(closing.getDate());
            holding.setClosing(closing);

            return holding;
        });
        if (holdingIter == null) {
            holdingIter = holdingStream.iterator();
        }
        return holdingStream;
    }

    public Stream<Holding> holdings() {
        return holdings(closingIter, portfolioIter);
    }

    public Holding nextHolding() {
        return holdingIter.next();
    }

    public Stream<Portfolio> portfolios() {
        Stream<Portfolio> portfolioStream = Stream.generate(() -> {
            Portfolio portfolio = new Portfolio();
            portfolio.setId(idIter.next());

            // make real valid holdings to insert into table
            List<Holding> holdings = new ArrayList<>();
            for (int i = 0; i < rng.nextInt(12); i++) {
                Holding holding = holdingIter.next();
                holding.setPortfolioId(portfolio.getId());
                holdings.add(holding);
            }
            portfolio.setHoldings(holdings);

            portfolio.setCash(cashIter.next());
            portfolio.setStartCash(cashIter.next());

            // verify start date comes first
            LocalDate startDate = dateIter.next();
            LocalDate currentDate = dateIter.next();
            if (currentDate.isBefore(startDate)) {
                portfolio.setStartDate(currentDate);
                portfolio.setDate(startDate);
            } else {
                portfolio.setStartDate(startDate);
                portfolio.setDate(currentDate);
            }

            return portfolio;
        });

        if (portfolioIter == null) {
            portfolioIter = portfolioStream.iterator();
        }

        return portfolioStream;
    }

    public Portfolio nextPortfolio() {
        return portfolioIter.next();
    }

    public IntStream ids() {
        IntStream idStream = rng.ints(0, Integer.MAX_VALUE);
        if (idIter == null) {
            idIter = idStream.iterator();
        }
        return idStream;
    }

    public DoubleStream prices() {
        DoubleStream doubleStream = rng.doubles(0, 400000);
        if (priceIter == null) {
            priceIter = doubleStream.iterator();
        }
        return doubleStream;
    }

    public DoubleStream cashes() {
        DoubleStream doubleStream = rng.doubles(0, Double.MAX_VALUE);
        if (cashIter == null) {
            cashIter = doubleStream.iterator();
        }
        return doubleStream;
    }

    public Stream<String> tickers() {
        int capA = 65; // ascii code for A
        int capZ = 90; // ascii code for Z
        PrimitiveIterator.OfInt chars = rng.ints(capA, capZ).iterator();
        Stream<String> tickerStream = Stream.generate(() -> {
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < 4 - rng.nextInt(4); i++) {
                builder.appendCodePoint(chars.nextInt());
            }
            return builder.toString();
        });
        if (tickerIter == null) {
            tickerIter = tickerStream.iterator();
        }
        return tickerStream;
    }

    public Stream<LocalDate> dates() {
        long epoch = LocalDate.EPOCH.toEpochDay();
        long now = LocalDate.now().toEpochDay();
        PrimitiveIterator.OfLong dayIter = rng.longs(epoch, now).iterator();
        Stream<LocalDate> dateStream = Stream.generate(() -> (LocalDate.ofEpochDay(dayIter.nextLong())));
        if (dateIter == null) {
            dateIter = dateStream.iterator();
        }
        return dateStream;
    }
}
