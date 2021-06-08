package com.dkkm.marketsim.model.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Objects;

public class Holding {

    @NotNull
    private Integer portfolioId;
    @NotNull
    private String ticker;
    @NotNull
    private LocalDate purchaseDate;
    @NotNull
    @Min(0)
    private Integer shareQuantity;

    private Closing closing; // added in service layer

    public Integer getPortfolioId() {
        return portfolioId;
    }

    public void setPortfolioId(Integer portfolioId) {
        this.portfolioId = portfolioId;
    }

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public LocalDate getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(LocalDate purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public Integer getShareQuantity() {
        return shareQuantity;
    }

    public void setShareQuantity(Integer shareQuantity) {
        this.shareQuantity = shareQuantity;
    }

    public Closing getClosing() {
        return closing;
    }

    public void setClosing(Closing closing) {
        this.closing = closing;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Holding holding = (Holding) o;
        return portfolioId.equals(holding.portfolioId) && ticker.equals(holding.ticker) && purchaseDate.equals(holding.purchaseDate) && shareQuantity.equals(holding.shareQuantity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(portfolioId, ticker, purchaseDate, shareQuantity);
    }
}
