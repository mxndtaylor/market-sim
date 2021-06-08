package com.dkkm.marketsim.model.dao;

import com.dkkm.marketsim.model.dto.Stock;

import java.time.LocalDate;
import java.util.List;

public interface StockDao extends CrudDao<Stock, String> {
    // TODO: List<Stock> getStocksByIpo(LocalDate date);
}
