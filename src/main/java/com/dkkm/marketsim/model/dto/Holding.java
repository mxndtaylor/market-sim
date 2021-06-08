package com.dkkm.marketsim.model.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Objects;

public class Holding {

    @NotNull
    private Stock stock;
    @NotNull
    @Min(0)
    private Integer quantity;

    public Stock getStock() {
        return stock;
    }

    public void setStock(Stock stock) {
        this.stock = stock;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Holding holding = (Holding) o;
        return stock.equals(holding.stock) && quantity.equals(holding.quantity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(stock, quantity);
    }
}
