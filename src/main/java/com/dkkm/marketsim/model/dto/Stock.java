package com.dkkm.marketsim.model.dto;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Objects;

public class Stock {

    @NotNull
    private String ticker;
    private LocalDate ipo;

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public LocalDate getIpo() {
        return ipo;
    }

    public void setIpo(LocalDate ipo) {
        this.ipo = ipo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Stock stock = (Stock) o;
        return ticker.equals(stock.ticker)
                && Objects.equals(ipo, stock.ipo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ticker, ipo);
    }
}
