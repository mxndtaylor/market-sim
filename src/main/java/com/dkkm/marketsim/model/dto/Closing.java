package com.dkkm.marketsim.model.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Objects;

public class Closing {

    @NotNull
    private String ticker;
    @NotNull
    private LocalDate date;
    @NotNull
    @Min(0)
    private Double price;

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Closing closing = (Closing) o;
        return ticker.equals(closing.ticker)
                && date.equals(closing.date)
                && price.equals(closing.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ticker, date, price);
    }
}