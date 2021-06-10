package com.dkkm.marketsim.model.dao;

import com.dkkm.marketsim.model.dto.Closing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

@Repository
public class ClosingDBDao implements ClosingDao {

    @Autowired
    private JdbcTemplate jdbc;

    @Override
    @Transactional
    public Closing addMember(Closing closing) {
        final String ADD_MEMBER = "INSERT INTO Closings (Date, Ticker, Price) " +
                "VALUES (?,?,TRUNCATE(?,2));";
        int rowsAffected = jdbc.update(ADD_MEMBER,
                closing.getDate(),
                closing.getTicker(),
                closing.getPrice());

        if (rowsAffected == 0) {
            return null;
        }

        return closing;
    }

    @Override
    public Closing getMemberByKey(Closing key) {
        final String GET_MEMBER = "SELECT * FROM Closings WHERE Date = ? AND Ticker = ?;";
        Closing closing;
        closing = jdbc.queryForObject(GET_MEMBER, new ClosingMapper(),
                key.getDate(),
                key.getTicker());

        return closing;
    }

    @Override
    public List<Closing> getMembers() {
        final String GET_ALL_MEMBERS = "SELECT * FROM Closings;";
        List<Closing> closings;

        closings = jdbc.query(GET_ALL_MEMBERS, new ClosingMapper());

        return closings;
    }

    @Override
    @Transactional
    public boolean deleteMemberByKey(Closing key) {
        final String DELETE_HOLDINGS = "DELETE FROM Holdings WHERE Date = ? AND Ticker = ?;";
        jdbc.update(DELETE_HOLDINGS,
                key.getDate(),
                key.getTicker());

        final String DELETE_MEMBER = "DELETE FROM Closings WHERE Date = ? AND Ticker = ?;";
        int rowsAffected = jdbc.update(DELETE_MEMBER,
                key.getDate(),
                key.getTicker());

        return rowsAffected == 1;
    }

    @Override
    public boolean updateMember(Closing closing) {
        final String UPDATE_MEMBER = "UPDATE Closings SET Price = TRUNCATE(?,2) " +
                "WHERE Date = ? AND Ticker = ?;";
        int rowsAffected = jdbc.update(UPDATE_MEMBER,
                closing.getPrice(),
                closing.getDate(),
                closing.getTicker());

        return rowsAffected == 1;
    }

    @Override
    public List<Closing> getClosingsByDate(LocalDate date) {
        List<Closing> closings;

        final String CLOSINGS_BY_DATE = "SELECT * FROM closings WHERE Date = ? ORDER BY Price DESC;";
        closings = jdbc.query(CLOSINGS_BY_DATE, new ClosingMapper(), Date.valueOf(date));

        return closings;
    }

    private static final class ClosingMapper implements RowMapper<Closing> {

        @Override
        public Closing mapRow(ResultSet resultSet, int i) throws SQLException {
            Closing closing = new Closing();

            closing.setDate(resultSet.getDate("Date").toLocalDate());
            closing.setTicker(resultSet.getString("Ticker"));
            closing.setPrice((double) resultSet.getFloat("Price"));

            return closing;
        }
    }

}
