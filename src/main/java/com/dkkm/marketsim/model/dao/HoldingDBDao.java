package com.dkkm.marketsim.model.dao;

import com.dkkm.marketsim.model.dto.Closing;
import com.dkkm.marketsim.model.dto.Holding;
import org.hibernate.bytecode.spi.NotInstrumentedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class HoldingDBDao implements HoldingDao {

    @Autowired
    private JdbcTemplate jdbc;

    @Override
    public Holding addMember(Holding holding) {
        final String ADD_MEMBER = "INSERT INTO Holdings (PortfolioId, Ticker, PurchaseDate, Quantity) " +
                "VALUES (?,?,?,?);";
        int rowsAffected = jdbc.update(ADD_MEMBER,
                holding.getPortfolioId(),
                holding.getTicker(),
                holding.getPurchaseDate(),
                holding.getShareQuantity());

        if (rowsAffected == 0) {
            return null;
        }

        return holding;
    }

    @Override
    public Holding getMemberByKey(Holding key) {
        final String GET_MEMBER = "SELECT * FROM Holdings WHERE PortfolioId = ? AND " +
                "PurchaseDate = ? AND Ticker = ?;";
        Holding holding;
        holding = jdbc.queryForObject(GET_MEMBER, new HoldingMapper(),
                key.getPortfolioId(),
                key.getPurchaseDate(),
                key.getTicker());

        return holding;
    }

    @Override
    public List<Holding> getPortfolioHoldings(int portfolioId) {
        List<Holding> holdingsByPortfolio;

        final String GET_MEMBER_BY_PORTFOLIO = "SELECT * FROM Holdings WHERE PortfolioId = ?;";
        holdingsByPortfolio = jdbc.query(GET_MEMBER_BY_PORTFOLIO, new HoldingMapper(), portfolioId);

        return holdingsByPortfolio;
    }

    @Override
    public List<Holding> getPortfolioHoldingsByTicker(int portfolioId, String ticker) {
        List<Holding> tickerHoldingsByPortfolio;

        final String GET_MEMBER_BY_PORTFOLIO_TICKER = "SELECT * FROM Holdings WHERE PortfolioId = ? AND Ticker = ?;";
        tickerHoldingsByPortfolio = jdbc.query(GET_MEMBER_BY_PORTFOLIO_TICKER, new HoldingMapper(), portfolioId, ticker);

        return tickerHoldingsByPortfolio;

    }

    @Override
    public List<Holding> getMembers() {
        final String GET_ALL_MEMBERS = "SELECT * FROM Holdings;";
        List<Holding> holdings;

        holdings = jdbc.query(GET_ALL_MEMBERS, new HoldingMapper());

        return holdings;
    }

    @Override
    public boolean deleteMemberByKey(Holding key) {
        final String DELETE_MEMBER = "DELETE * FROM Holdings WHERE " +
                "PortfolioId = ? AND Ticker = ? AND PurchaseDate = ?;";
        int rowsAffected = jdbc.update(DELETE_MEMBER,
                key.getPortfolioId(),
                key.getTicker(),
                key.getPurchaseDate());
        return rowsAffected == 1;
    }

    @Override
    public boolean updateMember(Holding holding) {
        final String UPDATE_MEMBER = "UPDATE Holdings SET Quantity = ? " +
                "WHERE PortfolioId = ? AND Ticker = ? AND PurchaseDate = ?;";
        int rowsAffected = jdbc.update(UPDATE_MEMBER,
                holding.getShareQuantity(),
                holding.getPortfolioId(),
                holding.getTicker(),
                holding.getPurchaseDate());

        return rowsAffected == 1;
    }

    private static final class HoldingMapper implements RowMapper<Holding> {

        @Override
        public Holding mapRow(ResultSet resultSet, int i) throws SQLException {
            Holding holding = new Holding();

            holding.setPortfolioId(resultSet.getInt("PortfolioId"));
            holding.setTicker(resultSet.getString("Ticker"));
            holding.setPurchaseDate(resultSet.getDate("PurchaseDate").toLocalDate());
            holding.setShareQuantity(resultSet.getInt("Quantity"));

            return holding;
        }
    }

}
