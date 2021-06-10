package com.dkkm.marketsim.model.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Objects;

public class Closing {

    @NotNull
    private String ticker;
    @NotNull
    private LocalDate date;
    @NotNull
    @Min(0)
    private BigDecimal price;

    private Stock stock; // added by service layer

    public Closing(LocalDate date, String symbol, BigDecimal price) {
        this.date = date;
        this.ticker = symbol;
        this.price = price.setScale(2, RoundingMode.HALF_UP);
    }

    public Closing() {

    }

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

    public BigDecimal getPrice() {
        return price.setScale(2, RoundingMode.HALF_UP);
    }

    public void setPrice(BigDecimal price) {
        this.price = price.setScale(2, RoundingMode.HALF_UP);
    }

    public Stock getStock() {
        return stock;
    }

    public void setStock(Stock stock) {
        this.stock = stock;
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
