package com.dkkm.marketsim.model.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class Portfolio {

    @NotNull
    @Min(1)
    private Integer id;

    @NotNull
    private LocalDate date;
    @NotNull
    @Min(0)
    private Double cash;

    @NotNull
    private LocalDate startDate;
    @NotNull
    @Min(0)
    private Double startCash;

    @NotNull
    private List<Holding> holdings;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Double getCash() {
        return cash;
    }

    public void setCash(Double cash) {
        this.cash = cash;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public Double getStartCash() {
        return startCash;
    }

    public void setStartCash(Double startCash) {
        this.startCash = startCash;
    }

    public List<Holding> getHoldings() {
        return holdings;
    }

    public void setHoldings(List<Holding> holdings) {
        this.holdings = holdings;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Portfolio portfolio = (Portfolio) o;
        return date.equals(portfolio.date)
                && cash.equals(portfolio.cash)
                && startDate.equals(portfolio.startDate)
                && startCash.equals(portfolio.startCash);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, cash, startDate, startCash);
    }
}
