package com.dkkm.marketsim.service;

import com.dkkm.marketsim.model.dto.Closing;

import java.time.LocalDate;

public interface ClosingService extends PassThruCrudService<Closing, Closing> {

    double getSharePrice(String ticker, LocalDate date);
}
