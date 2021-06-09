package com.dkkm.marketsim.service;

import com.dkkm.marketsim.model.dao.ClosingDao;
import com.dkkm.marketsim.model.dto.Closing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
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
