package com.dkkm.marketsim.model.dao;

import com.dkkm.marketsim.model.dto.Closing;
import java.time.LocalDate;
import java.util.List;

public interface ClosingDao extends CrudDao<Closing, Closing> {

    List<Closing> getClosingsByDate(LocalDate date);
    List<Closing> getClosingsByTicker(String ticker);
    // TODO: List<Closing> getClosingsByTickerInDateRange(String ticker, LocalDate start, LocalDate stop);
}
