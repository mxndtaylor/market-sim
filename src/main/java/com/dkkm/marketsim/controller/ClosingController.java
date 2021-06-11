package com.dkkm.marketsim.controller;

import com.dkkm.marketsim.model.dao.ClosingDao;
import com.dkkm.marketsim.model.dto.Closing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/closings")
public class ClosingController {

    @Autowired
    private ClosingDao closingDao;

    @GetMapping("/{date}")
    @ResponseStatus(HttpStatus.OK)
    public List<Closing> fetchClosingsByDate(@PathVariable String date) {
        LocalDate localDate = LocalDate.parse(date);
        return closingDao.getClosingsByDate(localDate);
    }

    @GetMapping("/{date}/{ticker}")
    public ResponseEntity<Closing> fetchClosingByKeys(
            @PathVariable String date, @PathVariable String ticker) {
        LocalDate localDate = LocalDate.parse(date);
        Closing closing = new Closing();
        closing.setDate(localDate);
        closing.setTicker(ticker);
        // TODO: add validator check
        return ResponseEntity.ok(closingDao.getMemberByKey(closing));
    }

    @GetMapping("/price/{date}/{ticker}")
    public ResponseEntity<BigDecimal> getClosingPriceByDate(
            @PathVariable String date, @PathVariable String ticker) {
        LocalDate localDate = LocalDate.parse(date);
        Closing closingKey = new Closing();
        Closing closing = new Closing();
        closingKey.setDate(localDate);
        closingKey.setTicker(ticker);
        // TODO: add validator check
        closing = closingDao.getMemberByKey(closingKey);
        return ResponseEntity.ok(closing.getPrice());
    }
}
