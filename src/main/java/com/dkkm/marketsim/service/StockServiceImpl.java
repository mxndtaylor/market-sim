package com.dkkm.marketsim.service;

import com.dkkm.marketsim.model.dto.Stock;
import org.springframework.stereotype.Service;

@Service
public class StockServiceImpl
    extends PassThruCrudServiceImpl<Stock, String>
    implements StockService {

}
