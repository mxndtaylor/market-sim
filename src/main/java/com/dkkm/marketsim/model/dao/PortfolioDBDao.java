package com.dkkm.marketsim.model.dao;

import com.dkkm.marketsim.model.dto.Portfolio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class PortfolioDBDao implements PortfolioDao {

    @Autowired
    private JdbcTemplate jdbc;

    @Override
    @Transactional
    public Portfolio addMember(Portfolio portfolio) {
        final String ADD_MEMBER = "INSERT INTO Portfolios (Date, Cash, StartDate, StartCash) " +
                "VALUES (?,?,?,?);";
        int rowsAffected = jdbc.update(ADD_MEMBER,
                portfolio.getDate(),
                portfolio.getCash(),
                portfolio.getStartDate(),
                portfolio.getStartCash());

        if (rowsAffected == 1) {
            final String GET_LAST_ID = "SELECT LAST_INSERT_ID();";
            int id = jdbc.queryForObject(GET_LAST_ID, Integer.class);
            portfolio.setId(id);
        }

        return portfolio;
    }

    @Override
    public Portfolio getMemberByKey(Integer id) {
        final String GET_MEMBER = "SELECT * FROM Portfolios WHERE PortfolioId = ?;";
        Portfolio portfolio; // TODO: add error catching
        portfolio = jdbc.queryForObject(GET_MEMBER, new PortfolioMapper(), id);

        return portfolio;
    }

    @Override
    public List<Portfolio> getMembers() {
        final String GET_ALL_MEMBERS = "SELECT * FROM Portfolios;";
        List<Portfolio> portfolios; // TODO: add error catching

        portfolios = jdbc.query(GET_ALL_MEMBERS, new PortfolioMapper());

        return portfolios;
    }

    @Override
    @Transactional
    public boolean deleteMemberByKey(Integer id) {
        final String DELETE_HOLDINGS = "DELETE * FROM Holings WHERE PortfolioId = ?;";
        jdbc.update(DELETE_HOLDINGS, id);

        final String DELETE_MEMBER = "DELETE * FROM Portfolios WHERE PortfolioId = ?;";
        int rowsAffected = jdbc.update(DELETE_MEMBER, id);
        return rowsAffected == 1;
    }

    @Override
    public boolean updateMember(Portfolio portfolio) {
        final String UPDATE_MEMBER = "UPDATE Portfolios SET Date = ?, Cash = ?, StartDate = ?, StartCash = ? " +
                "WHERE PortfolioId = ?;";
        int rowsAffected = jdbc.update(UPDATE_MEMBER,
                portfolio.getDate(),
                portfolio.getCash(),
                portfolio.getStartDate(),
                portfolio.getStartCash(),
                portfolio.getId());

        return rowsAffected == 1;
    }

    private static final class PortfolioMapper implements RowMapper<Portfolio> {

        @Override
        public Portfolio mapRow(ResultSet resultSet, int i) throws SQLException {
            Portfolio portfolio = new Portfolio();

            portfolio.setId(resultSet.getInt("PortfolioId"));
            portfolio.setCash((double) resultSet.getFloat("Cash"));
            portfolio.setStartCash((double) resultSet.getFloat("StartCash"));
            portfolio.setDate(resultSet.getDate("Date").toLocalDate());
            portfolio.setStartDate(resultSet.getDate("StartDate").toLocalDate());

            return portfolio;
        }
    }

}
