package com.dkkm.marketsim.model.dao;

import com.dkkm.marketsim.model.dto.Stock;
import com.dkkm.marketsim.model.dto.Stock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class StockDBDao implements CrudDao<Stock, String> {

    @Autowired
    private JdbcTemplate jdbc;

    @Override
    public Stock addMember(Stock stock) {
        final String ADD_STOCK = "INSERT INTO Stocks (Ticker, IPODate) VALUES (?, ?);";

        int rowsAffected = jdbc.update(
                ADD_STOCK,
                stock.getTicker(),
                stock.getIpo()
        );

        if (rowsAffected == 1) { // TODO: add error catching
        }

        return stock;
    }

    @Override
    public Stock getMemberByKey(String ticker) {
        final String GET_MEMBER = "SELECT * FROM Stocks WHERE Ticker = ?;";
        Stock stock; // TODO: add error catching
        stock = jdbc.queryForObject(GET_MEMBER, new StockMapper(), ticker);

        return stock;
    }

    @Override
    public List<Stock> getMembers() {
        final String GET_ALL_MEMBERS = "SELECT * FROM Stocks;";
        List<Stock> stocks; // TODO: add error catching

        stocks = jdbc.query(GET_ALL_MEMBERS, new StockMapper());

        return stocks;
    }

    @Override
    @Transactional
    public boolean deleteMemberByKey(String ticker) {
        final String DELETE_HOLDINGS = "DELETE * FROM Holdings WHERE Ticker = ?;";
        jdbc.update(DELETE_HOLDINGS, ticker);

        final String DELETE_CLOSINGS = "DELETE * FROM Closings WHERE Ticker = ?;";
        jdbc.update(DELETE_CLOSINGS, ticker);

        final String DELETE_MEMBER = "DELETE * FROM Stocks WHERE Ticker = ?";
        int rowsAffected = jdbc.update(DELETE_MEMBER, ticker);
        return rowsAffected == 1;
    }

    @Override
    public boolean updateMember(Stock stock) {
        final String UPDATE_MEMBER = "UPDATE Stocks SET IPODate = ? " +
                "WHERE Ticker = ?;";
        int rowsAffected = jdbc.update(UPDATE_MEMBER,
                stock.getIpo(),
                stock.getTicker());

        return rowsAffected == 1;
    }

    private static final class StockMapper implements RowMapper<Stock> {

        @Override
        public Stock mapRow(ResultSet resultSet, int i) throws SQLException {
            Stock stock = new Stock();

            stock.setTicker(resultSet.getString("Ticker"));
            stock.setIpo(resultSet.getDate("IPODate").toLocalDate());

            return stock;
        }
    }

}
