package com.dkkm.marketsim.service;

import com.dkkm.marketsim.model.dao.ClosingDao;
import com.dkkm.marketsim.model.dto.Closing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class ClosingServiceImpl
        extends PassThruCrudServiceImpl<Closing, Closing>
        implements ClosingService {

    @Autowired
    public ClosingServiceImpl(ClosingDao closingDao) {
        dao = closingDao;
    }

    @Override
    public BigDecimal getSharePrice(String ticker, LocalDate date) {
        Closing closing = new Closing();
        closing.setTicker(ticker);
        closing.setDate(date);
        closing = dao.getMemberByKey(closing);
        return closing.getPrice();
    }

    @Override
    public List<Closing> getClosingsByDate(LocalDate date) {
        return ((ClosingDao) dao).getClosingsByDate(date);
    }
}
