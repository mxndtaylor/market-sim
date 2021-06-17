package com.dkkm.marketsim.service;

import com.dkkm.marketsim.model.dto.Closing;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface ClosingService extends PassThruCrudService<Closing, Closing> {

    BigDecimal getSharePrice(String ticker, LocalDate date);
    List<Closing> getClosingsByDate(LocalDate date);
}
