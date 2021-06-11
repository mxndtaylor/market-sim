package com.dkkm.marketsim.model.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.math.RoundingMode;
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
    private BigDecimal cash;

    @NotNull
    private LocalDate startDate;
    @NotNull
    private BigDecimal startCash;

    private List<Holding> holdings; // Added by service layer

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

    public BigDecimal getStartCash() {
        return startCash;
    }

    public void setStartCash(BigDecimal startCash) {
        this.startCash = startCash.setScale(2, RoundingMode.HALF_UP);
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public BigDecimal getCash() {
        return cash;
    }

    public void setCash(BigDecimal cash) {
        this.cash = cash.setScale(2, RoundingMode.HALF_UP);
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

    // TODO: remove, only added for test debugging
    @Override
    public String toString() {
        return "Portfolio{" +
                "id=" + id +
                ", date=" + date +
                ", cash=" + cash +
                ", startDate=" + startDate +
                ", startCash=" + startCash +
                '}';
    }
}
