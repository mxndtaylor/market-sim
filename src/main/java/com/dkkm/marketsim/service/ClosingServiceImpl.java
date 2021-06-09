package com.dkkm.marketsim.service;

import com.dkkm.marketsim.model.dao.ClosingDao;
import com.dkkm.marketsim.model.dto.Closing;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;

public class ClosingServiceImpl
        extends PassThruCrudServiceImpl<Closing, Closing>
        implements ClosingService {

    @Autowired
    public ClosingServiceImpl(ClosingDao closingDao) {
        dao = closingDao;
    }

    @Override
    public double getSharePrice(String ticker, LocalDate date) {
        Closing closing = new Closing();
        closing.setTicker(ticker);
        closing.setDate(date);
        closing = dao.getMemberByKey(closing);
        return closing.getPrice();
    }
}
